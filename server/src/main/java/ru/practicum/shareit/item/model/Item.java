package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "items")
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 255)
	private String name;

	@Column(nullable = false, length = 1000)
	private String description;

	@Column(nullable = false)
	private Boolean available;

	@Column(name = "owner_id", nullable = false)
	private Long ownerId;
	// необязательная ссылка на запрос по его id (оставляем id для простоты в этом спринте)
	@Column(name = "request_id")
	private Long requestId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Item item = (Item) o;
		return id != null && Objects.equals(id, item.id);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}


