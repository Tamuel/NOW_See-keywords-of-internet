package controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import domain.Word;
import domain.WordList;
import repository.WordRepository;

@RestController
public class Controller {
	
	@Autowired
	WordRepository wordRepository;
	
	
	@RequestMapping("/")
	public WordList list() {

		List<Word> words = wordRepository.findAllOrderByNumberOfWord();
		WordList wordList = new WordList();
		
		for (Word word: words){
			wordList.getWordList().add(word);
		}
		
		return wordList;
	}
	
	@RequestMapping("/number")
	public long getNumberOfKeyword() {

		return wordRepository.count();
	}
	
	public void saveData(String[] data) {
	    for(String temp : data) {
	    	if(wordRepository.findByKeyword(temp) != null) {
	    		wordRepository.findByKeyword(temp).addNumberOfWord(1);
	    	}
	    	else
	    		wordRepository.save(new Word(temp));
	    }		
	}
	
}
