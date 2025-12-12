package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.JpaUserRepository;
import ru.practicum.shareit.user.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ItemRequestServiceImplIT {
	@Autowired
	private ItemRequestService itemRequestService;
	@Autowired
	private JpaUserRepository userRepository;

	@Test
	void createAndGetOwn_returnsCreated() {
		User requestor = userRepository.save(User.builder().name("Req").email("req@example.com").build());
		ItemRequestDto created = itemRequestService.create(requestor.getId(),
				new ItemRequestDto(null, "Need ladder", requestor.getId(), null));
		assertThat(created.getId()).isNotNull();

		List<ItemRequestResponseDto> own = itemRequestService.getOwn(requestor.getId());
		assertThat(own).hasSize(1);
		assertThat(own.get(0).getDescription()).isEqualTo("Need ladder");
	}
}


