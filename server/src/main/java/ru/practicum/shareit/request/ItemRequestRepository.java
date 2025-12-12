package ru.practicum.shareit.request;

import java.util.List;
import java.util.Optional;

public interface ItemRequestRepository {
	ItemRequest save(ItemRequest request);

	Optional<ItemRequest> findById(Long id);

	List<ItemRequest> findByRequestorIdOrderByCreatedDesc(Long requestorId);

	List<ItemRequest> findByRequestorIdNotOrderByCreatedDesc(Long requestorId);

	List<ItemRequest> findByRequestorIdNotOrderByCreatedDesc(Long requestorId, int from, int size);
}



