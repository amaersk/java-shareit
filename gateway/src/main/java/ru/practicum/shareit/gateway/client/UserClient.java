package ru.practicum.shareit.gateway.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.gateway.dto.user.UserDto;

@Component
public class UserClient extends BaseClient {
	public UserClient(@Value("${shareit-server.url}") String serverUrl) {
		super(serverUrl, "/users");
	}

	public ResponseEntity<Object> create(UserDto dto) {
		return post(null, "", dto);
	}

	public ResponseEntity<Object> update(Long id, UserDto patch) {
		return patch(null, "/" + id, patch);
	}

	public ResponseEntity<Object> getById(Long id) {
		return get(null, "/" + id, null);
	}

	public ResponseEntity<Object> getAll() {
		return get(null, "", null);
	}

	public ResponseEntity<Void> delete(Long id) {
		return (ResponseEntity<Void>) (ResponseEntity<?>) delete(null, "/" + id);
	}
}


