package ru.cdfe.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

//@Entity // Can't have both entity and embedded
@Embeddable
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChildJPA {
//	@Id
//	@GeneratedValue
//	private long id;
	
	private String name;
	private int age;
	
	public ChildJPA(String name, int age) {
		this.name = name;
		this.age = age;
	}
}
