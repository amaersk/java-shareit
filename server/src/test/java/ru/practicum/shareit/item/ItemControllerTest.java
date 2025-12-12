package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
class ItemControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private ItemService itemService;

	@Test
	void create_returnsCreatedItem() throws Exception {
		given(itemService.create(eq(1L), any())).willReturn(ItemDto.builder().id(1L).name("Drill").description("power").available(true).build());
		mockMvc.perform(post("/items").header("X-Sharer-User-Id", 1)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(ItemDto.builder().name("Drill").description("power").available(true).build())))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1));
	}

	@Test
	void update_returnsUpdatedItem() throws Exception {
		given(itemService.update(eq(1L), eq(2L), any())).willReturn(ItemDto.builder().id(2L).name("New name").description("power").available(true).build());
		mockMvc.perform(patch("/items/2").header("X-Sharer-User-Id", 1)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(ItemDto.builder().name("New name").build())))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("New name"));
	}

	@Test
	void getById_returnsItem() throws Exception {
		given(itemService.getById(2L)).willReturn(ItemDto.builder().id(2L).name("Drill").description("power").available(true).build());
		mockMvc.perform(get("/items/2"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Drill"));
	}

	@Test
	void getByOwner_returnsList() throws Exception {
		given(itemService.getByOwner(1L)).willReturn(List.of(ItemDto.builder().id(2L).name("Drill").description("power").available(true).build()));
		mockMvc.perform(get("/items").header("X-Sharer-User-Id", 1))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));
	}

	@Test
	void search_returnsList() throws Exception {
		given(itemService.search("dr")).willReturn(List.of(ItemDto.builder().id(2L).name("Drill").description("power").available(true).build()));
		mockMvc.perform(get("/items/search").param("text", "dr"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));
	}
}


