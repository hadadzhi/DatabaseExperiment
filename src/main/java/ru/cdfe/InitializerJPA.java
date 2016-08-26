package ru.cdfe;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cdfe.domain.ParentJPA;
import ru.cdfe.repositories.ParentJPARepository;

import java.util.stream.IntStream;

@Slf4j
@Service
public class InitializerJPA {
	private final WriterJPA writer;
	private final ParentJPARepository parents;
	
	public InitializerJPA(WriterJPA writer, ParentJPARepository parents) {
		this.writer = writer;
		this.parents = parents;
	}
	
	@Transactional
	public void doInit() {
		parents.deleteAll();
		IntStream.range(0, 1).parallel().forEach(value -> {
			final ParentJPA p = new ParentJPA();
			writer.writeParentJPA(p);
			log.info("Saving: " + parents.save(p).toString());
		});
	}
}
