package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.JpaItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.JpaUserRepository;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class BookingServiceImplIT {
	@Autowired
	private BookingService bookingService;
	@Autowired
	private JpaUserRepository userRepository;
	@Autowired
	private JpaItemRepository itemRepository;

	@Test
	void createBooking_persistsWithWaitingStatus() {
		User owner = userRepository.save(User.builder().name("Owner").email("owner@example.com").build());
		User booker = userRepository.save(User.builder().name("Booker").email("booker@example.com").build());
		Item item = itemRepository.save(Item.builder()
				.name("Drill").description("Power drill").available(true).ownerId(owner.getId()).build());

		LocalDateTime start = LocalDateTime.now().plusDays(1);
		LocalDateTime end = start.plusDays(1);
		BookingDto created = bookingService.create(booker.getId(),
				BookingDto.builder().itemId(item.getId()).start(start).end(end).build());

		assertThat(created.getId()).isNotNull();
		assertThat(created.getStatus()).isEqualTo(BookingStatus.WAITING);
	}
}


