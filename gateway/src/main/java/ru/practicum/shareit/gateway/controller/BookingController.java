package ru.practicum.shareit.gateway.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.client.BookingClient;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
	private final BookingClient bookingClient;

	@PostMapping
	public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId,
	                                     @RequestBody Object body) {
		log.info("GW POST /bookings - userId={}, body={}", userId, body);
		return bookingClient.create(userId, body);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> approve(@RequestHeader("X-Sharer-User-Id") Long ownerId,
	                                      @PathVariable Long bookingId,
	                                      @RequestParam("approved") boolean approved) {
		log.info("GW PATCH /bookings/{}?approved={} - ownerId={}", bookingId, approved, ownerId);
		return bookingClient.approve(ownerId, bookingId, approved);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") Long userId,
	                                      @PathVariable Long bookingId) {
		log.info("GW GET /bookings/{} - userId={}", bookingId, userId);
		return bookingClient.getById(userId, bookingId);
	}

	@GetMapping
	public ResponseEntity<Object> getByBooker(@RequestHeader("X-Sharer-User-Id") Long userId,
	                                          @RequestParam(name = "state", defaultValue = "ALL") String state) {
		log.info("GW GET /bookings?state={} - userId={}", state, userId);
		return bookingClient.getByBooker(userId, state);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getByOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId,
	                                         @RequestParam(name = "state", defaultValue = "ALL") String state) {
		log.info("GW GET /bookings/owner?state={} - ownerId={}", state, ownerId);
		return bookingClient.getByOwner(ownerId, state);
	}
}

