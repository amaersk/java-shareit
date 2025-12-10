package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Спринт: добавить функциональность бронирований.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;
    private final BookingRepository bookingRepository;

    @PostMapping
    public BookingResponseDto create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @RequestBody BookingDto request) {
        log.info("POST /bookings - userId={}, body={}", userId, request);
        BookingDto created = bookingService.create(userId, request);
        return bookingRepository.findById(created.getId())
                .map(BookingResponseMapper::toResponse)
                .orElseGet(() -> {
                    // fallback minimal response
                    var fallback = new Booking();
                    fallback.setId(created.getId());
                    fallback.setStart(created.getStart());
                    fallback.setEnd(created.getEnd());
                    fallback.setStatus(created.getStatus());
                    return BookingResponseMapper.toResponse(fallback);
                });
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approve(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                      @PathVariable Long bookingId,
                                      @RequestParam("approved") boolean approved) {
        log.info("PATCH /bookings/{}?approved={} - ownerId={}", bookingId, approved, ownerId);
        BookingDto dto = bookingService.approve(ownerId, bookingId, approved);
        return bookingRepository.findById(dto.getId())
                .map(BookingResponseMapper::toResponse)
                .orElseThrow();
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                      @PathVariable Long bookingId) {
        log.info("GET /bookings/{} - userId={}", bookingId, userId);
        BookingDto dto = bookingService.getById(userId, bookingId);
        return bookingRepository.findById(dto.getId())
                .map(BookingResponseMapper::toResponse)
                .orElseThrow();
    }

    @GetMapping
    public List<BookingResponseDto> getByBooker(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestParam(name = "state", defaultValue = "ALL") BookingState state) {
        log.info("GET /bookings?state={} - userId={}", state, userId);
        return bookingService.getByBooker(userId, state).stream()
                .map(dto -> bookingRepository.findById(dto.getId()).map(BookingResponseMapper::toResponse).orElse(null))
                .filter(r -> r != null)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getByOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                               @RequestParam(name = "state", defaultValue = "ALL") BookingState state) {
        log.info("GET /bookings/owner?state={} - ownerId={}", state, ownerId);
        return bookingService.getByOwner(ownerId, state).stream()
                .map(dto -> bookingRepository.findById(dto.getId()).map(BookingResponseMapper::toResponse).orElse(null))
                .filter(r -> r != null)
                .collect(Collectors.toList());
    }
}
