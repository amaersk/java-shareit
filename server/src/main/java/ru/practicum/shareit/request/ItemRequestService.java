package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

public interface ItemRequestService {
	ItemRequestDto create(Long userId, ItemRequestDto request);

	List<ItemRequestResponseDto> getOwn(Long userId);

	List<ItemRequestResponseDto> getAllExceptUser(Long userId, Integer from, Integer size);

	ItemRequestResponseDto getById(Long userId, Long requestId);
}



