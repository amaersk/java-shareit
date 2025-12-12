package ru.practicum.shareit.gateway.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.gateway.dto.request.ItemRequestCreateDto;

@Component
public class RequestClient extends BaseClient {
	public RequestClient(@Value("${shareit-server.url}") String serverUrl) {
		super(serverUrl, "/requests");
	}

	public ResponseEntity<Object> create(Long userId, ItemRequestCreateDto dto) {
		return post(userId, "", dto);
	}

	public ResponseEntity<Object> getOwn(Long userId) {
		return get(userId, "", null);
	}

	public ResponseEntity<Object> getAll(Long userId, Integer from, Integer size) {
		String path = "/all";
		if (from != null && size != null) {
			path += "?from=" + from + "&size=" + size;
		}
		return get(userId, path, null);
	}

	public ResponseEntity<Object> getById(Long userId, Long requestId) {
		return get(userId, "/" + requestId, null);
	}
}


