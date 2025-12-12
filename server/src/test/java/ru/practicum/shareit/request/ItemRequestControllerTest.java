package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private ItemRequestService itemRequestService;

	@Test
	void create_returnsCreated() throws Exception {
		given(itemRequestService.create(eq(1L), any())).willReturn(new ItemRequestDto(5L, "Need", 1L, null));
		mockMvc.perform(post("/requests").header("X-Sharer-User-Id", 1)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(new ItemRequestDto(null, "Need", 1L, null))))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(5));
	}

	@Test
	void getOwn_returnsList() throws Exception {
		given(itemRequestService.getOwn(1L)).willReturn(List.of(ItemRequestResponseDto.builder().id(5L).description("Need").build()));
		mockMvc.perform(get("/requests").header("X-Sharer-User-Id", 1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));
	}

	@Test
	void getAll_returnsList() throws Exception {
		given(itemRequestService.getAllExceptUser(2L, null, null)).willReturn(List.of(ItemRequestResponseDto.builder().id(6L).build()));
		mockMvc.perform(get("/requests/all").header("X-Sharer-User-Id", 2))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));
	}

	@Test
	void getById_returnsOne() throws Exception {
		given(itemRequestService.getById(1L, 5L)).willReturn(ItemRequestResponseDto.builder().id(5L).description("Need").build());
		mockMvc.perform(get("/requests/5").header("X-Sharer-User-Id", 1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(5));
	}
}


