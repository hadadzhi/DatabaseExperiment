package ru.cdfe.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

//@Entity // Can't have both entity and embedded
@Embeddable
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChildJPA {
//	@Id
//	@GeneratedValue
//	private long id;
	
	private String name;
	private int age;
}
