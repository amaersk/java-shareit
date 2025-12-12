package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private UserService userService;

	@Test
	void create_returnsCreatedUser() throws Exception {
		given(userService.create(any())).willReturn(UserDto.builder().id(1L).name("Bob").email("b@e.com").build());
		mockMvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(UserDto.builder().name("Bob").email("b@e.com").build())))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1));
	}

	@Test
	void update_returnsUpdatedUser() throws Exception {
		given(userService.update(eq(1L), any())).willReturn(UserDto.builder().id(1L).name("Bobby").email("b@e.com").build());
		mockMvc.perform(patch("/users/1").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(UserDto.builder().name("Bobby").build())))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Bobby"));
	}

	@Test
	void getById_returnsUser() throws Exception {
		given(userService.getById(1L)).willReturn(UserDto.builder().id(1L).name("Bob").email("b@e.com").build());
		mockMvc.perform(get("/users/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("b@e.com"));
	}

	@Test
	void getAll_returnsList() throws Exception {
		given(userService.getAll()).willReturn(List.of(UserDto.builder().id(1L).name("Bob").email("b@e.com").build()));
		mockMvc.perform(get("/users"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));
	}

	@Test
	void delete_returnsOk() throws Exception {
		mockMvc.perform(delete("/users/1"))
				.andExpect(status().isOk());
	}
}


