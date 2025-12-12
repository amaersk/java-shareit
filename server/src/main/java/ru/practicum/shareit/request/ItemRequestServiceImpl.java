package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemAnswerDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
	private final ItemRequestRepository itemRequestRepository;
	private final UserRepository userRepository;
	private final ItemRepository itemRepository;

	@Override
	public ItemRequestDto create(Long userId, ItemRequestDto request) {
		User requestor = userRepository.findById(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		if (request == null || request.getDescription() == null || request.getDescription().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description must not be blank");
		}
		ItemRequest toSave = new ItemRequest();
		toSave.setId(null);
		toSave.setDescription(request.getDescription());
		toSave.setRequestor(requestor);
		toSave.setCreated(LocalDateTime.now());
		ItemRequest saved = itemRequestRepository.save(toSave);
		return ItemRequestMapper.toDto(saved);
	}

	@Override
	public List<ItemRequestResponseDto> getOwn(Long userId) {
		// ensure user exists
		userRepository.findById(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		List<ItemRequest> requests = itemRequestRepository.findByRequestorIdOrderByCreatedDesc(userId);
		return attachItems(requests);
	}

	@Override
	public List<ItemRequestResponseDto> getAllExceptUser(Long userId, Integer from, Integer size) {
		userRepository.findById(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		List<ItemRequest> requests;
		if (from != null && size != null) {
			requests = itemRequestRepository.findByRequestorIdNotOrderByCreatedDesc(userId, from, size);
		} else {
			requests = itemRequestRepository.findByRequestorIdNotOrderByCreatedDesc(userId);
		}
		// Ensure newest first if repository without pageable returns unsorted
		requests.sort(Comparator.comparing(ItemRequest::getCreated).reversed());
		return attachItems(requests);
	}

	@Override
	public ItemRequestResponseDto getById(Long userId, Long requestId) {
		// ensure user exists
		userRepository.findById(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		ItemRequest request = itemRequestRepository.findById(requestId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));
		List<Item> items = itemRepository.findByRequestId(request.getId());
		List<ItemAnswerDto> answers = items.stream()
				.map(i -> ItemAnswerDto.builder().id(i.getId()).name(i.getName()).ownerId(i.getOwnerId()).build())
				.collect(Collectors.toList());
		return ItemRequestMapper.toResponse(request, answers);
	}

	private List<ItemRequestResponseDto> attachItems(List<ItemRequest> requests) {
		if (requests.isEmpty()) {
			return List.of();
		}
		List<Long> requestIds = requests.stream().map(ItemRequest::getId).collect(Collectors.toList());
		List<Item> items = itemRepository.findByRequestIdIn(requestIds);
		Map<Long, List<ItemAnswerDto>> itemsByRequestId = items.stream()
				.collect(Collectors.groupingBy(Item::getRequestId,
						Collectors.mapping(i -> ItemAnswerDto.builder()
								.id(i.getId())
								.name(i.getName())
								.ownerId(i.getOwnerId())
								.build(), Collectors.toList())));
		return requests.stream()
				.map(r -> ItemRequestMapper.toResponse(r, itemsByRequestId.getOrDefault(r.getId(), List.of())))
				.collect(Collectors.toList());
	}
}



