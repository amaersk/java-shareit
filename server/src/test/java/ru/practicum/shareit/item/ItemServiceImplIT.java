package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.JpaUserRepository;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ItemServiceImplIT {
	@Autowired
	private ItemService itemService;
	@Autowired
	private BookingRepository bookingRepository;
	@Autowired
	private JpaUserRepository userRepository;
	@Autowired
	private ItemRepository itemRepository;

	@Test
	void getByOwner_returnsItemsWithLastAndNextBookings() {
		User owner = userRepository.save(User.builder().name("Owner").email("owner2@example.com").build());
		User booker = userRepository.save(User.builder().name("Booker").email("booker2@example.com").build());
		Item item = itemRepository.save(Item.builder()
				.name("Saw").description("Hand saw").available(true).ownerId(owner.getId()).build());

		LocalDateTime now = LocalDateTime.now();
		// past approved booking
		bookingRepository.save(Booking.builder()
				.item(item)
				.booker(booker)
				.start(now.minusDays(5))
				.end(now.minusDays(3))
				.status(BookingStatus.APPROVED)
				.build());
		// future approved booking
		bookingRepository.save(Booking.builder()
				.item(item)
				.booker(booker)
				.start(now.plusDays(3))
				.end(now.plusDays(5))
				.status(BookingStatus.APPROVED)
				.build());

		List<ItemDto> result = itemService.getByOwner(owner.getId());
		assertThat(result).hasSize(1);
		ItemDto dto = result.get(0);
		assertThat(dto.getLastBooking()).isNotNull();
		assertThat(dto.getNextBooking()).isNotNull();
	}
}


