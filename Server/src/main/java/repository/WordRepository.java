package repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import domain.Word;

public interface WordRepository extends CrudRepository<Word, String>{

	List<Word> findAllOrderByNumberOfWord();
	
	Word findByKeyword(String keyword);
	
}
