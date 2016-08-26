package ru.cdfe.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import ru.cdfe.domain.Parent;

import java.util.stream.Stream;

public interface ParentRepository extends MongoRepository<Parent, ObjectId> {
	@Query("{}")
	Stream<Parent> streamAll();
}
