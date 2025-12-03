package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto create(Long ownerId, ItemDto itemDto) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Owner not found"));
        Item toSave = itemMapper.fromDto(itemDto, ownerId);
        toSave.setId(null);
        Item saved = itemRepository.save(toSave);
        return itemMapper.toDto(saved);
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
        return itemMapper.toDto(updated);
    }

    @Override
    public ItemDto getById(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
        return itemMapper.toDto(item);
    }

    @Override
    public List<ItemDto> getByOwner(Long ownerId) {
        return itemRepository.findByOwnerId(ownerId).stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text) {
        return itemRepository.search(text).stream()
                .map(itemMapper::toDto)
                .collect(Collectors.toList());
    }
}


