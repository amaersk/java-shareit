package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@UtilityClass
public class ItemMapper {
    public ItemDto toDto(Item item) {
        if (item == null) {
            return null;
        }
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequestId())
                .build();
    }

    public Item fromDto(ItemDto dto, Long ownerId) {
        if (dto == null) {
            return null;
        }
        return Item.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .available(dto.getAvailable())
                .ownerId(ownerId)
                .requestId(dto.getRequestId())
                .build();
    }

    public ItemDto toDtoWithView(Item item,
                                 ItemDto base,
                                 ItemDto bookingsPart,
                                 List<CommentDto> comments) {
        ItemDto result = base != null ? base : toDto(item);
        if (bookingsPart != null) {
            result.setLastBooking(bookingsPart.getLastBooking());
            result.setNextBooking(bookingsPart.getNextBooking());
        }
        result.setComments(comments);
        return result;
    }
}


