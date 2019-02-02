package com.softart.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.softart.domain.Word;

@RepositoryRestResource(collectionResourceRel = "words", path = "words")
public interface WordRepository extends PagingAndSortingRepository<Word, String>{

	List<Word> findAll();
	List<Word> findAllByOrderByNumberOfWordDesc();
	List<Word> findFirst100ByOrderByRelationScoreDesc();
	
	void deleteAll();
	
	Word findByKeyword(String keyword);
	
}

