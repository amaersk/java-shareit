package ru.practicum.shareit.gateway.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.client.RequestClient;
import ru.practicum.shareit.gateway.dto.request.ItemRequestCreateDto;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
	private final RequestClient requestClient;

	@PostMapping
	public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId,
	                                     @RequestBody @Valid ItemRequestCreateDto body) {
		log.info("GW POST /requests - userId={}, body={}", userId, body);
		return requestClient.create(userId, body);
	}

	@GetMapping
	public ResponseEntity<Object> getOwn(@RequestHeader("X-Sharer-User-Id") Long userId) {
		log.info("GW GET /requests - userId={}", userId);
		return requestClient.getOwn(userId);
	}

	@GetMapping("/all")
	public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
	                                     @RequestParam(value = "from", required = false) Integer from,
	                                     @RequestParam(value = "size", required = false) Integer size) {
		log.info("GW GET /requests/all - userId={}, from={}, size={}", userId, from, size);
		return requestClient.getAll(userId, from, size);
	}

	@GetMapping("/{requestId}")
	public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") Long userId,
	                                      @PathVariable Long requestId) {
		log.info("GW GET /requests/{} - userId={}", requestId, userId);
		return requestClient.getById(userId, requestId);
	}
}


