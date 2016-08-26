package ru.cdfe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.cdfe.domain.ParentJPA;

import java.util.List;

public interface ParentJPARepository extends JpaRepository<ParentJPA, Long> {
	// Can't stream -- opens OVER9000 connections and never closes them, even if I close the stream after use
	@Query("select p from ParentJPA p")
	List<ParentJPA> listAll();
}
