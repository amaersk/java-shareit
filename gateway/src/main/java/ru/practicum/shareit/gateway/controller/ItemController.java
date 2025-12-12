package ru.practicum.shareit.gateway.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.client.ItemClient;
import ru.practicum.shareit.gateway.dto.item.ItemDto;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
	private final ItemClient itemClient;

	@PostMapping
	public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId,
	                                     @RequestBody @Valid ItemDto itemDto) {
		log.info("GW POST /items - userId={}, body={}", userId, itemDto);
		return itemClient.create(userId, itemDto);
	}

	@PatchMapping("/{itemId}")
	public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") Long userId,
	                                     @PathVariable Long itemId,
	                                     @RequestBody ItemDto patch) {
		log.info("GW PATCH /items/{} - userId={}, patch={}", itemId, userId, patch);
		return itemClient.update(userId, itemId, patch);
	}

	@GetMapping("/{itemId}")
	public ResponseEntity<Object> getById(@PathVariable Long itemId) {
		log.info("GW GET /items/{}", itemId);
		return itemClient.getById(itemId);
	}

	@GetMapping
	public ResponseEntity<Object> getByOwner(@RequestHeader("X-Sharer-User-Id") Long userId) {
		log.info("GW GET /items - ownerId={}", userId);
		return itemClient.getByOwner(userId);
	}

	@GetMapping("/search")
	public ResponseEntity<Object> search(@RequestParam(name = "text") String text) {
		log.info("GW GET /items/search?text={}", text);
		return itemClient.search(text);
	}

	@PostMapping("/{itemId}/comment")
	public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
	                                         @PathVariable Long itemId,
	                                         @RequestBody Object request) {
		log.info("GW POST /items/{}/comment - userId={}, body={}", itemId, userId, request);
		return itemClient.addComment(userId, itemId, request);
	}
}


