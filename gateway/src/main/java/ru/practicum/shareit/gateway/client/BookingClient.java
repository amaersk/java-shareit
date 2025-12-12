package ru.practicum.shareit.gateway.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class BookingClient extends BaseClient {
	public BookingClient(@Value("${shareit-server.url}") String serverUrl) {
		super(serverUrl, "/bookings");
	}

	public ResponseEntity<Object> create(Long userId, Object body) {
		return post(userId, "", body);
	}

	public ResponseEntity<Object> approve(Long ownerId, Long bookingId, boolean approved) {
		return patch(ownerId, "/" + bookingId + "?approved=" + approved, null);
	}

	public ResponseEntity<Object> getById(Long userId, Long bookingId) {
		return get(userId, "/" + bookingId, null);
	}

	public ResponseEntity<Object> getByBooker(Long userId, String state) {
		return get(userId, "?state=" + state, null);
	}

	public ResponseEntity<Object> getByOwner(Long ownerId, String state) {
		return get(ownerId, "/owner?state=" + state, null);
	}
}

