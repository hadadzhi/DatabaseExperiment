package ru.cdfe.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
public final class Child {
	@Indexed
	private final String name;

	@Indexed
	private final int age;
}
