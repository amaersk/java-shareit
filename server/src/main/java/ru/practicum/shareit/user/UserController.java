package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
	private final UserService userService;

	@PostMapping
	public UserDto create(@RequestBody UserDto userDto) {
		log.info("POST /users - body={}", userDto);
		return userService.create(userDto);
	}

	@PatchMapping("/{id}")
	public UserDto update(@PathVariable Long id, @RequestBody UserDto patch) {
		log.info("PATCH /users/{} - patch={}", id, patch);
		return userService.update(id, patch);
	}

	@GetMapping("/{id}")
	public UserDto getById(@PathVariable Long id) {
		log.info("GET /users/{}", id);
		return userService.getById(id);
	}

	@GetMapping
	public List<UserDto> getAll() {
		log.info("GET /users");
		return userService.getAll();
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		log.info("DELETE /users/{}", id);
		userService.delete(id);
	}
}


