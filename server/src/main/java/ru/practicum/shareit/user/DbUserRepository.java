package ru.practicum.shareit.user;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class DbUserRepository implements UserRepository {
	private final JpaUserRepository jpa;

	public DbUserRepository(JpaUserRepository jpa) {
		this.jpa = jpa;
	}

	@Override
	public User save(User user) {
		return jpa.save(user);
	}

	@Override
	public Optional<User> findById(Long id) {
		return jpa.findById(id);
	}

	@Override
	public List<User> findAll() {
		return jpa.findAll();
	}

	@Override
	public void deleteById(Long id) {
		jpa.deleteById(id);
	}

	@Override
	public User update(User user) {
		return jpa.save(user);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		if (email == null) {
			return Optional.empty();
		}
		return jpa.findByEmailIgnoreCase(email);
	}
}



