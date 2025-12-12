package ru.practicum.shareit.item;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class DbItemRepository implements ItemRepository {
    private final JpaItemRepository jpa;

    public DbItemRepository(JpaItemRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Item save(Item item) {
        return jpa.save(item);
    }

    @Override
    public Optional<Item> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public Item update(Item item) {
        return jpa.save(item);
    }

    @Override
    public List<Item> findByOwnerId(Long ownerId) {
        return jpa.findByOwnerId(ownerId);
    }

    @Override
    public List<Item> search(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        return jpa.searchAvailableByText(text);
    }

    @Override
    public List<Item> findByRequestId(Long requestId) {
        if (requestId == null) {
            return List.of();
        }
        return jpa.findByRequestId(requestId);
    }

    @Override
    public List<Item> findByRequestIdIn(List<Long> requestIds) {
        if (requestIds == null || requestIds.isEmpty()) {
            return List.of();
        }
        return jpa.findByRequestIdIn(requestIds);
    }
}


