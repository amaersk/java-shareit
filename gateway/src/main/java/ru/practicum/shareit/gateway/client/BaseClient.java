package ru.practicum.shareit.gateway.client;

import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Map;

public class BaseClient {
	private static final String USER_HEADER = "X-Sharer-User-Id";
	protected final RestTemplate rest;
	protected final String basePath;

	public BaseClient(String serverUrl, String basePath) {
		this.basePath = basePath;
		this.rest = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
		this.rest.setUriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + basePath));
	}

	public ResponseEntity<Object> get(Long userId, String path, Map<String, Object> params) {
		HttpHeaders headers = defaultHeaders(userId);
		HttpEntity<Void> request = new HttpEntity<>(headers);
		String url = expand(path, params);
		try {
			return rest.exchange(url, HttpMethod.GET, request, Object.class);
		} catch (HttpStatusCodeException e) {
			return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
		} catch (RestClientException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	public ResponseEntity<Object> post(Long userId, String path, Object body) {
		HttpHeaders headers = defaultHeaders(userId);
		HttpEntity<Object> request = new HttpEntity<>(body, headers);
		try {
			return rest.exchange(path, HttpMethod.POST, request, Object.class);
		} catch (HttpStatusCodeException e) {
			return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
		} catch (RestClientException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	public ResponseEntity<Object> patch(Long userId, String path, Object body) {
		HttpHeaders headers = defaultHeaders(userId);
		HttpEntity<Object> request = new HttpEntity<>(body, headers);
		try {
			return rest.exchange(path, HttpMethod.PATCH, request, Object.class);
		} catch (HttpStatusCodeException e) {
			return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
		} catch (RestClientException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	public ResponseEntity<Object> delete(Long userId, String path) {
		HttpHeaders headers = defaultHeaders(userId);
		HttpEntity<Void> request = new HttpEntity<>(headers);
		try {
			return rest.exchange(path, HttpMethod.DELETE, request, Object.class);
		} catch (HttpStatusCodeException e) {
			return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
		} catch (RestClientException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	private HttpHeaders defaultHeaders(Long userId) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		if (userId != null) {
			headers.add(USER_HEADER, String.valueOf(userId));
		}
		return headers;
	}

	private String expand(String path, Map<String, Object> params) {
		if (params == null || params.isEmpty()) {
			return path;
		}
		String expanded = path;
		for (Map.Entry<String, Object> e : params.entrySet()) {
			expanded = expanded.replace("{" + e.getKey() + "}", String.valueOf(e.getValue()));
		}
		return expanded;
	}
}


