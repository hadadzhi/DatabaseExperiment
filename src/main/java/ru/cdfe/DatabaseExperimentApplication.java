package ru.cdfe;

import com.mongodb.MongoClientOptions;
import com.mongodb.ReadConcern;
import com.mongodb.WriteConcern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import ru.cdfe.domain.Child;
import ru.cdfe.domain.Parent;
import ru.cdfe.domain.repositories.ParentRepository;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Slf4j
@SpringBootApplication
public class DatabaseExperimentApplication {
	public static final long MAX_CHILDREN = 10;
	public static final int DEFAULT_AGE = 0;
	public static final Random rnd = new Random();
	public static final String INCONSISTENT_DATA_MESSAGE = "OMG, INCONSISTENT DATA!!!11";
		
	public static void main(String[] args) {
		SpringApplication.run(DatabaseExperimentApplication.class, args);
	}
	
	@Bean
	public MongoClientOptions mongoClientOptions() {
		return MongoClientOptions.builder()
			.readConcern(ReadConcern.MAJORITY)
			.writeConcern(WriteConcern.MAJORITY)
			.build();
	}
	
	@Bean
	@Profile("writer")
	public ApplicationRunner writer(ParentRepository parents) {
		return args -> {
			while (!Thread.currentThread().isInterrupted()) {
				try (final Stream<Parent> stream = parents.streamAll()) {
					stream.forEach(p -> {
						writeParent(p);
						parents.save(p);
					});
				}
			}
		};
	}
	
	@Bean
	@Profile("reader")
	public ApplicationRunner reader(ParentRepository parents) {
		return args -> {
			while (!Thread.currentThread().isInterrupted()) {
				try (final Stream<Parent> stream = parents.streamAll()) {
					stream.forEach(p -> {
						if (p.getChildren().stream().mapToInt(Child::getAge).max().orElse(DEFAULT_AGE) != p.getMaxChildAge()) {
							throw new RuntimeException(INCONSISTENT_DATA_MESSAGE);
						}
					});
				}
			}
		};
	}
	
	@Bean
	@Profile("init")
	public ApplicationRunner initRunner(ParentRepository parents) {
		return args -> {
			parents.deleteAll();
			IntStream.range(0, 1).parallel().forEach(value -> {
				final Parent p = new Parent();
				writeParent(p);
				log.info("Saving: " + parents.save(p).toString());
			});
		};
	}
	
	private static void writeParent(Parent p) {
		final List<Child> children = Stream
			.generate(() -> UUID.randomUUID().toString())
			.limit(MAX_CHILDREN)
			.map(name -> new Child(name, rnd.nextInt(100)))
			.collect(toList());
		
		p.setChildren(children);
		p.setMaxChildAge(children.stream().mapToInt(Child::getAge).max().orElse(DEFAULT_AGE));
	}
	
	@Bean
	@Profile("writer_jpa")
	public ApplicationRunner writerJPARunner(WriterJPA writer) {
		return args -> {
			while (!Thread.currentThread().isInterrupted()) {
				writer.doWriting();
			}
		};
	}
	
	@Bean
	@Profile("reader_jpa")
	public ApplicationRunner readerJPARunner(ReaderJPA reader) {
		return args -> {
			while (!Thread.currentThread().isInterrupted()) {
				reader.doReading();
			}
		};
	}
	
	@Bean
	@Profile("init_jpa")
	public ApplicationRunner initJPARunner(InitializerJPA init) {
		return args -> init.doInit();
	}
}
