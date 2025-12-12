package ru.practicum.shareit.gateway.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.gateway.dto.item.ItemDto;

@Component
public class ItemClient extends BaseClient {
	public ItemClient(@Value("${shareit-server.url}") String serverUrl) {
		super(serverUrl, "/items");
	}

	public ResponseEntity<Object> create(Long userId, ItemDto dto) {
		return post(userId, "", dto);
	}

	public ResponseEntity<Object> update(Long userId, Long itemId, ItemDto patch) {
		return patch(userId, "/" + itemId, patch);
	}

	public ResponseEntity<Object> getById(Long itemId) {
		return get(null, "/" + itemId, null);
	}

	public ResponseEntity<Object> getByOwner(Long userId) {
		return get(userId, "", null);
	}

	public ResponseEntity<Object> search(String text) {
		return get(null, "/search?text=" + text, null);
	}

	public ResponseEntity<Object> addComment(Long userId, Long itemId, Object body) {
		return post(userId, "/" + itemId + "/comment", body);
	}
}


