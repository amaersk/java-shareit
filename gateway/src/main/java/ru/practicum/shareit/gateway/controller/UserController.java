package ru.practicum.shareit.gateway.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.client.UserClient;
import ru.practicum.shareit.gateway.dto.user.UserDto;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
	private final UserClient userClient;

	@PostMapping
	public ResponseEntity<Object> create(@RequestBody @Valid UserDto dto) {
		log.info("GW POST /users - body={}", dto);
		return userClient.create(dto);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody UserDto patch) {
		log.info("GW PATCH /users/{} - patch={}", id, patch);
		return userClient.update(id, patch);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> getById(@PathVariable Long id) {
		log.info("GW GET /users/{}", id);
		return userClient.getById(id);
	}

	@GetMapping
	public ResponseEntity<Object> getAll() {
		log.info("GW GET /users");
		return userClient.getAll();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		log.info("GW DELETE /users/{}", id);
		return userClient.delete(id);
	}
}


