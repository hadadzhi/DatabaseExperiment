package ru.cdfe.domain;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
public class Parent {
	@Id
	private ObjectId id;
	
	@Version
	private long version;
	
	@Indexed
	private int maxChildAge;
	
	private List<Child> children;
}
