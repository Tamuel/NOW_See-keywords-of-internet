package com.softart.controller;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.softart.domain.Word;
import com.softart.domain.WordList;
import com.softart.repository.WordRepository;

@RestController
@RequestMapping("/")
public class Controller {
	
	@Autowired
	WordRepository wordRepository;
	
	
	@RequestMapping(value = "/", produces="application/json; charset=UTF-8")
	public List<Word> list() {

		List<Word> words = wordRepository.findAll();
		WordList wordList = new WordList();
		
		for (Word word : words){
			wordList.getWordList().add(word);
		}
		
		return words;
	}
	
	@RequestMapping(value = "/number", produces="application/json; charset=UTF-8")
	public Long numberOfWords() {
		System.out.println("NUMBER!!");
		return wordRepository.count();
	}
	
	@RequestMapping(value = "/sortbynumber", produces="application/json; charset=UTF-8")
	public WordList sortedList() {

		List<Word> words = wordRepository.findAllByOrderByNumberOfWordDesc();
		WordList wordList = new WordList();
		
		for (Word word : words){
			wordList.getWordList().add(word);
		}
		
		return wordList;
	}
	
	@RequestMapping(value = "/sortbyscore", produces="application/json; charset=UTF-8")
	public String sorted100List() {

		List<Word> words = wordRepository.findFirst100ByOrderByRelationScoreDesc();
		WordList wordList = new WordList();

		String result = "";
		for (Word word : words){
			result += word.getKeyword() + " " + word.getNumberOfWord() +  " " + word.getRelationScore() + "\n";
			

	    	TreeMap<String, Integer> sortedRelatedWords;
			ValueComparator bvc = new ValueComparator(word.getRelatedWords());
			sortedRelatedWords = new TreeMap<String, Integer>(bvc);
	    	sortedRelatedWords.clear();
			sortedRelatedWords.putAll(word.getRelatedWords());
			int i = 0;
			for(String key : sortedRelatedWords.keySet()) {
				result += key + " " + word.getRelatedWords().get(key) + "\n";
				i++;
				if(i == 7) break;
			}
		}
		
		return result;
	}
	
	@RequestMapping(value = "/keyword/{keyword}", produces="application/json; charset=UTF-8")
	public Word getKeyword( @PathVariable("keyword") String keyword ) {
		Word word = wordRepository.findByKeyword(keyword);
		
		return word;
	}
	
	@RequestMapping(value = "/keyword/{keyword}/{score}", produces="application/json; charset=UTF-8")
	public Word addKeyword(
			@PathVariable("keyword") String keyword,
			@PathVariable("score") int score
			) {
		Word word = wordRepository.findByKeyword(keyword);
		word.setNumberOfWord(word.getNumberOfWord() + score);
		wordRepository.save(word);
		
		return word;
	}
	

	private class ValueComparator implements Comparator<String> {
		Map<String, Integer> base;
		
		public ValueComparator(Map<String, Integer> base) {
		this.base = base;
		}
		
		@Override
		public int compare(String o1, String o2) {
			if(base.get(o1) >= base.get(o2)) {
				return -1;
			} else
				return 1;
		}
	}
	
}
