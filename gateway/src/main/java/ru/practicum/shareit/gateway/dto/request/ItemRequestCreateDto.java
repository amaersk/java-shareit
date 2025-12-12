package ru.practicum.shareit.gateway.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ItemRequestCreateDto {
	@NotBlank
	private String description;
}


