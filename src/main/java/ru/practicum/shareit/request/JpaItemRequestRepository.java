package ru.practicum.shareit.request;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByRequestorIdOrderByCreatedDesc(Long requestorId);

    List<ItemRequest> findByRequestorIdNotOrderByCreatedDesc(Long requestorId, Pageable pageable);

    List<ItemRequest> findByRequestorIdNotOrderByCreatedDesc(Long requestorId);
}



