package ru.cdfe.domain.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.cdfe.domain.Parent;

import java.util.stream.Stream;

public interface ParentRepository extends CrudRepository<Parent, ObjectId> {
	@Query("{}")
	Stream<Parent> streamAll();
}
