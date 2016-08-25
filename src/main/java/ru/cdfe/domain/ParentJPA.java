package ru.cdfe.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class ParentJPA {
	@Id
	@GeneratedValue
	private long id;

	@Version
	private long version;

	private int maxChildAge;
	
//	@ManyToMany // No orphan removal
//	@OneToMany(orphanRemoval = true) // Yes orphan removal, but doesn't work as expected
	@ElementCollection // Can't do embedded collection inside embedded collection
	private List<ChildJPA> children;
}
