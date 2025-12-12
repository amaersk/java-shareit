package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository repository;

	@Override
	public UserDto create(UserDto userDto) {
		User toSave = UserMapper.fromDto(userDto);
		toSave.setId(null);
		// уникальность адреса электронной почты
		if (toSave.getEmail() != null && repository.findByEmail(toSave.getEmail()).isPresent()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
		}
		User saved = repository.save(toSave);
		return UserMapper.toDto(saved);
	}

	@Override
	public UserDto update(Long id, UserDto patch) {
		User existing = repository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		if (patch.getName() != null) {
			existing.setName(patch.getName());
		}
		if (patch.getEmail() != null) {
			// конфликт, если email принадлежит другому пользователю
			repository.findByEmail(patch.getEmail()).ifPresent(found -> {
				if (!found.getId().equals(existing.getId())) {
					throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
				}
			});
			existing.setEmail(patch.getEmail());
		}
		User updated = repository.update(existing);
		return UserMapper.toDto(updated);
	}

	@Override
	public UserDto getById(Long id) {
		User user = repository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		return UserMapper.toDto(user);
	}

	@Override
	public List<UserDto> getAll() {
		return repository.findAll().stream()
				.map(UserMapper::toDto)
				.collect(Collectors.toList());
	}

	@Override
	public void delete(Long id) {
		repository.deleteById(id);
	}
}



