package ru.practicum.shareit.request;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class DbItemRequestRepository implements ItemRequestRepository {
    private final JpaItemRequestRepository jpa;

    public DbItemRequestRepository(JpaItemRequestRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public ItemRequest save(ItemRequest request) {
        return jpa.save(request);
    }

    @Override
    public Optional<ItemRequest> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public List<ItemRequest> findByRequestorIdOrderByCreatedDesc(Long requestorId) {
        return jpa.findByRequestorIdOrderByCreatedDesc(requestorId);
    }

    @Override
    public List<ItemRequest> findByRequestorIdNotOrderByCreatedDesc(Long requestorId) {
        return jpa.findByRequestorIdNotOrderByCreatedDesc(requestorId);
    }

    @Override
    public List<ItemRequest> findByRequestorIdNotOrderByCreatedDesc(Long requestorId, int from, int size) {
        if (size <= 0) {
            return List.of();
        }
        int page = from / size;
        return jpa.findByRequestorIdNotOrderByCreatedDesc(requestorId, PageRequest.of(page, size));
    }
}



