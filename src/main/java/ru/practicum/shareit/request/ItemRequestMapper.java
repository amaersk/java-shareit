package ru.practicum.shareit.request;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.dto.ItemAnswerDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

@UtilityClass
public class ItemRequestMapper {
    public ItemRequestDto toDto(ItemRequest request) {
        if (request == null) return null;
        return new ItemRequestDto(
                request.getId(),
                request.getDescription(),
                request.getRequestor() != null ? request.getRequestor().getId() : null,
                request.getCreated()
        );
    }

    public ItemRequestResponseDto toResponse(ItemRequest request, List<ItemAnswerDto> items) {
        if (request == null) return null;
        return ItemRequestResponseDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .items(items)
                .build();
    }
}



