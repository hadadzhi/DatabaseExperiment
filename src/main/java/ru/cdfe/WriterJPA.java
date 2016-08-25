package ru.cdfe;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.cdfe.domain.ChildJPA;
import ru.cdfe.domain.ParentJPA;
import ru.cdfe.domain.repositories.ParentJPARepository;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static ru.cdfe.DatabaseExperimentApplication.DEFAULT_AGE;
import static ru.cdfe.DatabaseExperimentApplication.MAX_CHILDREN;

@Service
public class WriterJPA {
	private final ParentJPARepository parents;
//	private final ChildJPARepository children;
	
	public WriterJPA(ParentJPARepository parents/*, ChildJPARepository children*/) {
		this.parents = parents;
//		this.children = children;
	}
	
	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void doWriting() {
		for (ParentJPA p : parents.listAll()) {
			writeParentJPA(p);
			parents.save(p);
		}
	}
	
	public void writeParentJPA(ParentJPA p) {
		final List<ChildJPA> c = Stream
			.generate(() -> UUID.randomUUID().toString())
			.limit(MAX_CHILDREN)
			.map(name -> new ChildJPA(name, ThreadLocalRandom.current().nextInt(100)))
			.collect(toList());
		
		// throws a JPA exception about child entity with orphan removal not being referenced by parent, bla bla bla, orphans are not removed anyway!111
//		p.setChildren(c);
		
		if (p.getChildren() != null) {
			p.getChildren().clear();
			p.getChildren().addAll(c);
		} else {
			p.setChildren(c);
		}

		p.setMaxChildAge(c.stream().mapToInt(ChildJPA::getAge).max().orElse(DEFAULT_AGE));
		
//		children.save(c);
		parents.save(p);
	}
}
