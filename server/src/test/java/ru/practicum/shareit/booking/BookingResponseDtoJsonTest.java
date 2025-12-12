package ru.practicum.shareit.booking;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.booking.dto.BookerShortDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.ItemShortDto;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingResponseDtoJsonTest {
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void serialize_containsIsoDatesAndNestedObjects() throws Exception {
		LocalDateTime start = LocalDateTime.of(2025, 1, 2, 3, 4, 5);
		LocalDateTime end = start.plusDays(1);
		BookingResponseDto dto = new BookingResponseDto(
				10L, start, end, BookingStatus.APPROVED,
				new ItemShortDto(2L, "Drill"),
				new BookerShortDto(3L)
		);
		String json = objectMapper.writeValueAsString(dto);
		Map<String, Object> asMap = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
		assertThat(asMap.get("id")).isEqualTo(10);
		assertThat(asMap.get("status")).isEqualTo("APPROVED");
		assertThat(((Map<?, ?>) asMap.get("item")).get("id")).isEqualTo(2);
		assertThat(((Map<?, ?>) asMap.get("item")).get("name")).isEqualTo("Drill");
		assertThat(((Map<?, ?>) asMap.get("booker")).get("id")).isEqualTo(3);
		assertThat(asMap.get("start").toString()).contains("2025-01-02T03:04:05");
		assertThat(asMap.get("end").toString()).contains("2025-01-03T03:04:05");
	}
}


