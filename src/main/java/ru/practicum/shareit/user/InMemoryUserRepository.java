package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryUserRepository implements UserRepository {
    private final ConcurrentMap<Long, User> storage = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(0L);

    @Override
    public User save(User user) {
        long id = idSequence.incrementAndGet();
        user.setId(id);
        storage.put(id, user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }

    @Override
    public User update(User user) {
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        return storage.values().stream()
                .filter(u -> email.equalsIgnoreCase(u.getEmail()))
                .findFirst();
    }
}


