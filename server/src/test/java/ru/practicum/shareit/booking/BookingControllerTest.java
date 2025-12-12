package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.ItemShortDto;
import ru.practicum.shareit.booking.dto.BookerShortDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private BookingService bookingService;
	@MockBean
	private BookingRepository bookingRepository;

	@Test
	void create_returnsResponse() throws Exception {
		LocalDateTime start = LocalDateTime.now().plusDays(1);
		LocalDateTime end = start.plusDays(1);
		given(bookingService.create(eq(1L), any())).willReturn(BookingDto.builder().id(10L).start(start).end(end).status(BookingStatus.WAITING).build());
		given(bookingRepository.findById(10L)).willReturn(Optional.of(Booking.builder().id(10L).start(start).end(end).status(BookingStatus.WAITING).build()));
		mockMvc.perform(post("/bookings").header("X-Sharer-User-Id", 1)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(BookingDto.builder().itemId(2L).start(start).end(end).build())))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(10));
	}

	@Test
	void approve_returnsResponse() throws Exception {
		given(bookingService.approve(1L, 10L, true)).willReturn(BookingDto.builder().id(10L).status(BookingStatus.APPROVED).build());
		given(bookingRepository.findById(10L)).willReturn(Optional.of(Booking.builder().id(10L).status(BookingStatus.APPROVED).build()));
		mockMvc.perform(patch("/bookings/10").header("X-Sharer-User-Id", 1).param("approved", "true"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(10));
	}

	@Test
	void getById_returnsResponse() throws Exception {
		given(bookingService.getById(1L, 10L)).willReturn(BookingDto.builder().id(10L).build());
		given(bookingRepository.findById(10L)).willReturn(Optional.of(Booking.builder().id(10L).build()));
		mockMvc.perform(get("/bookings/10").header("X-Sharer-User-Id", 1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(10));
	}

	@Test
	void getByBooker_returnsList() throws Exception {
		given(bookingService.getByBooker(1L, BookingState.ALL)).willReturn(List.of(BookingDto.builder().id(10L).build()));
		given(bookingRepository.findById(10L)).willReturn(Optional.of(Booking.builder().id(10L).build()));
		mockMvc.perform(get("/bookings").header("X-Sharer-User-Id", 1).param("state", "ALL"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));
	}

	@Test
	void getByOwner_returnsList() throws Exception {
		given(bookingService.getByOwner(1L, BookingState.ALL)).willReturn(List.of(BookingDto.builder().id(10L).build()));
		given(bookingRepository.findById(10L)).willReturn(Optional.of(Booking.builder().id(10L).build()));
		mockMvc.perform(get("/bookings/owner").header("X-Sharer-User-Id", 1).param("state", "ALL"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));
	}
}


