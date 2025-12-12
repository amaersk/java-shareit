package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemDto create(Long ownerId, ItemDto itemDto) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found"));
        if (itemDto.getRequestId() != null) {
            itemRequestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));
        }
        Item toSave = ItemMapper.fromDto(itemDto, ownerId);
        toSave.setId(null);
        Item saved = itemRepository.save(toSave);
        return ItemMapper.toDto(saved);
    }

    @Override
    public ItemDto update(Long ownerId, Long itemId, ItemDto patch) {
        Item existing = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
        if (!existing.getOwnerId().equals(ownerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only owner can update item");
        }
        if (patch.getName() != null) {
            existing.setName(patch.getName());
        }
        if (patch.getDescription() != null) {
            existing.setDescription(patch.getDescription());
        }
        if (patch.getAvailable() != null) {
            existing.setAvailable(patch.getAvailable());
        }
        Item updated = itemRepository.update(existing);
        return ItemMapper.toDto(updated);
    }

    @Override
    public ItemDto getById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
        List<CommentDto> comments = commentRepository.findByItemIdOrderByCreatedDesc(itemId).stream()
                .map(c -> CommentDto.builder()
                        .id(c.getId())
                        .text(c.getText())
                        .authorName(c.getAuthor().getName())
                        .created(c.getCreated())
                        .build())
                .collect(Collectors.toList());
        ItemDto base = ItemMapper.toDto(item);
        base.setComments(comments);
        return base;
    }

    @Override
    public List<ItemDto> getByOwner(Long ownerId) {
        List<Item> items = itemRepository.findByOwnerId(ownerId);
        if (items.isEmpty()) {
            return List.of();
        }

        LocalDateTime now = LocalDateTime.now();
        List<Long> itemIds = items.stream().map(Item::getId).collect(Collectors.toList());

        // Bulk load last/next bookings for all items to avoid N+1
        var lastBookingsOrdered = bookingRepository.findLastForItems(itemIds, BookingStatus.APPROVED, now);
        var nextBookingsOrdered = bookingRepository.findNextForItems(itemIds, BookingStatus.APPROVED, now);

        // Pick first row per item id from ordered lists
        var lastByItemId = lastBookingsOrdered.stream()
                .collect(Collectors.toMap(b -> b.getItem().getId(), b -> b, (existing, ignore) -> existing));
        var nextByItemId = nextBookingsOrdered.stream()
                .collect(Collectors.toMap(b -> b.getItem().getId(), b -> b, (existing, ignore) -> existing));

        // Bulk load comments for all items
        var commentsByItemId = commentRepository.findByItemIds(itemIds).stream()
                .collect(Collectors.groupingBy(c -> c.getItem().getId(),
                        Collectors.mapping(c -> CommentDto.builder()
                                .id(c.getId())
                                .text(c.getText())
                                .authorName(c.getAuthor().getName())
                                .created(c.getCreated())
                                .build(), Collectors.toList())));

        return items.stream().map(item -> {
            ItemDto base = ItemMapper.toDto(item);
            ItemDto bookingsPart = ItemDto.builder()
                    .lastBooking(BookingMapper.toDto(lastByItemId.get(item.getId())))
                    .nextBooking(BookingMapper.toDto(nextByItemId.get(item.getId())))
                    .build();
            List<CommentDto> comments = commentsByItemId.getOrDefault(item.getId(), List.of());
            return ItemMapper.toDtoWithView(item, base, bookingsPart, comments);
        }).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text) {
        return itemRepository.search(text).stream()
                .map(ItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentDto request) {
        // author and item exist
        var author = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        var item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
        // user must have at least one approved booking that ended before now
        LocalDateTime now = LocalDateTime.now();
        boolean hasPastApproved = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.APPROVED)
                .stream()
                .anyMatch(b -> b.getItem().getId().equals(itemId) && b.getEnd().isBefore(now));
        if (!hasPastApproved) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User has not completed booking for this item");
        }
        var comment = new Comment();
        comment.setId(null);
        comment.setText(request.getText());
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(now);
        var saved = commentRepository.save(comment);
        return CommentDto.builder()
                .id(saved.getId())
                .text(saved.getText())
                .authorName(saved.getAuthor().getName())
                .created(saved.getCreated())
                .build();
    }
}


