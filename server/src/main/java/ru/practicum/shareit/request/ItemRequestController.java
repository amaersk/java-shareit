package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

/**
 * TODO Спринт: добавить функциональность запросов вещей.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
	private final ItemRequestService itemRequestService;

	@PostMapping
	public ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
	                             @RequestBody ItemRequestDto request) {
		log.info("POST /requests - userId={}, body={}", userId, request);
		return itemRequestService.create(userId, request);
	}

	@GetMapping
	public List<ItemRequestResponseDto> getOwn(@RequestHeader("X-Sharer-User-Id") Long userId) {
		log.info("GET /requests - userId={}", userId);
		return itemRequestService.getOwn(userId);
	}

	@GetMapping("/all")
	public List<ItemRequestResponseDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
	                                           @RequestParam(value = "from", required = false) Integer from,
	                                           @RequestParam(value = "size", required = false) Integer size) {
		log.info("GET /requests/all - userId={}, from={}, size={}", userId, from, size);
		return itemRequestService.getAllExceptUser(userId, from, size);
	}

	@GetMapping("/{requestId}")
	public ItemRequestResponseDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
	                                      @PathVariable Long requestId) {
		log.info("GET /requests/{} - userId={}", requestId, userId);
		return itemRequestService.getById(userId, requestId);
	}
}

