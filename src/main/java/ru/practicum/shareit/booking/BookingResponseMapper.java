package ru.practicum.shareit.booking;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookerShortDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.ItemShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

@UtilityClass
public class BookingResponseMapper {
    public BookingResponseDto toResponse(Booking booking) {
        if (booking == null) {
            return null;
        }
        Item item = booking.getItem();
        User booker = booking.getBooker();
        return new BookingResponseDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                item != null ? new ItemShortDto(item.getId(), item.getName()) : null,
                booker != null ? new BookerShortDto(booker.getId()) : null
        );
    }
}


