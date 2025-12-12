package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class UserServiceImplIT {
	@Autowired
	private UserService userService;

	@Test
	void createAndGetUser_persistsAndReturns() {
		UserDto created = userService.create(UserDto.builder().name("Alice").email("alice@example.com").build());
		assertThat(created.getId()).isNotNull();
		UserDto fetched = userService.getById(created.getId());
		assertThat(fetched.getEmail()).isEqualTo("alice@example.com");
	}
}


