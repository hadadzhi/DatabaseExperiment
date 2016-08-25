package ru.cdfe;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.cdfe.domain.ChildJPA;
import ru.cdfe.domain.ParentJPA;
import ru.cdfe.domain.repositories.ParentJPARepository;

import static ru.cdfe.DatabaseExperimentApplication.DEFAULT_AGE;
import static ru.cdfe.DatabaseExperimentApplication.INCONSISTENT_DATA_MESSAGE;

@Slf4j
@Service
public class ReaderJPA {
	private final ParentJPARepository parents;
	
	public ReaderJPA(ParentJPARepository parents) {
		this.parents = parents;
	}
	
	@Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
	public void doReading() {
		for (ParentJPA p : parents.listAll()) {
			final int maxChildAge = p.getMaxChildAge();
			final int maxChildAgeActual = p.getChildren().stream().mapToInt(ChildJPA::getAge).max().orElse(DEFAULT_AGE);
			
			if (maxChildAge != maxChildAgeActual) {
				throw new RuntimeException(INCONSISTENT_DATA_MESSAGE);
			}
		}
	}
}
