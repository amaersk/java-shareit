package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.JpaItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.JpaUserRepository;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
	private final BookingRepository bookingRepository;
	private final JpaItemRepository itemRepository;
	private final JpaUserRepository userRepository;

	@Override
	public BookingDto create(Long userId, BookingDto request) {
		if (request.getStart() == null || request.getEnd() == null || !request.getEnd().isAfter(request.getStart())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date range");
		}
		User booker = userRepository.findById(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		Item item = itemRepository.findById(request.getItemId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
		if (Boolean.FALSE.equals(item.getAvailable())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item not available");
		}
		if (item.getOwnerId().equals(userId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner cannot book own item");
		}
		Booking booking = BookingMapper.fromDto(request, item, booker);
		booking.setId(null);
		booking.setStatus(BookingStatus.WAITING);
		Booking saved = bookingRepository.save(booking);
		return BookingMapper.toDto(saved);
	}

	@Override
	public BookingDto approve(Long ownerId, Long bookingId, boolean approved) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
		Item item = booking.getItem();
		if (!item.getOwnerId().equals(ownerId)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only owner can approve booking");
		}
		if (booking.getStatus() != BookingStatus.WAITING) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking already decided");
		}
		booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
		return BookingMapper.toDto(bookingRepository.save(booking));
	}

	@Override
	public BookingDto getById(Long userId, Long bookingId) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
		Long ownerId = booking.getItem().getOwnerId();
		Long bookerId = booking.getBooker().getId();
		if (!ownerId.equals(userId) && !bookerId.equals(userId)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
		}
		return BookingMapper.toDto(booking);
	}

	@Override
	public List<BookingDto> getByBooker(Long userId, BookingState state) {
		// ensure user exists
		userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		LocalDateTime now = LocalDateTime.now();
		List<Booking> bookings;
		switch (state) {
			case CURRENT -> bookings = bookingRepository.findCurrentByBooker(userId, now);
			case PAST -> bookings = bookingRepository.findByBookerIdAndEndBeforeOrderByStartDesc(userId, now);
			case FUTURE -> bookings = bookingRepository.findByBookerIdAndStartAfterOrderByStartDesc(userId, now);
			case WAITING ->
					bookings = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.WAITING);
			case REJECTED ->
					bookings = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.REJECTED);
			case ALL -> bookings = bookingRepository.findByBookerIdOrderByStartDesc(userId);
			default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown state: " + state);
		}
		return bookings.stream().map(BookingMapper::toDto).collect(Collectors.toList());
	}

	@Override
	public List<BookingDto> getByOwner(Long ownerId, BookingState state) {
		// ensure owner exists
		userRepository.findById(ownerId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		LocalDateTime now = LocalDateTime.now();
		List<Booking> bookings;
		switch (state) {
			case CURRENT -> bookings = bookingRepository.findCurrentByOwner(ownerId, now);
			case PAST -> bookings = bookingRepository.findPastByOwner(ownerId, now);
			case FUTURE -> bookings = bookingRepository.findFutureByOwner(ownerId, now);
			case WAITING -> bookings = bookingRepository.findByOwnerAndStatus(ownerId, BookingStatus.WAITING);
			case REJECTED -> bookings = bookingRepository.findByOwnerAndStatus(ownerId, BookingStatus.REJECTED);
			case ALL -> bookings = bookingRepository.findAllByOwnerOrderByStartDesc(ownerId);
			default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown state: " + state);
		}
		return bookings.stream().map(BookingMapper::toDto).collect(Collectors.toList());
	}
}



