package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.comment.CommentDto;

import java.util.List;

/**
 * DTO основной информации о вещи. Для представления расширенной информации
 * (комментарии, предыдущая/следующая аренда) поля могут быть не заполнены.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDto {
	private Long id;
	private String name;
	private String description;
	private Boolean available;
	// идентификатор связанного запроса, если есть
	private Long requestId;

	// Дополнительно для представления
	private BookingDto lastBooking;
	private BookingDto nextBooking;
	private List<CommentDto> comments;
}


