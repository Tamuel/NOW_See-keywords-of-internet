package controller;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import domain.Word;
import repository.WordRepository;

public class WordController {

//	@Autowired
//	private WordRepository wordRepository;
//	
//	public void saveWord(String[] data) {
//	    for(String temp : data) {
//	    	if(wordRepository.findByKeyword(temp) != null) {
//	    		wordRepository.findByKeyword(temp).addNumberOfWord(1);
//	    	}
//	    	else
//	    		wordRepository.save(new Word(temp));
//	    }		
//	}
//	private Map<String, Word> words;
//	private TreeMap<String, Word> sortedWords;
//	private TreeMap<String, Word> sortedScoredWords;
//	
//
//	public WordController() {
//		words = new HashMap<String, Word>();
//		NumberComparator bvc = new NumberComparator(words);
//		sortedWords = new TreeMap<String, Word>(bvc);
//		
//		ScoreComparator rvc = new ScoreComparator(words);
//		sortedScoredWords = new TreeMap<String, Word>(rvc);
//	}
//	
//	public void addWord(Word newWord, String relatedWords[]) {
//    	if(words.containsKey(newWord.getKeyword())) {
//    		words.get(newWord.getKeyword()).addNumberOfWord(1);
//    		words.get(newWord.getKeyword()).addRelatedWord(relatedWords);
//    	} else {
//    		newWord.setNumberOfWord(1);
//    		newWord.addRelatedWord(relatedWords);
//    		words.put(newWord.getKeyword(), newWord);
//    	}
//	}
//
//
//	public void getData(TreeMap<String, Word> criteria) {
//		criteria.clear();
//		criteria.putAll(words);
//		int i = 0;
//		if(criteria == sortedWords)
//			System.out.println("Print List By Number of word");
//		else
//			System.out.println("Print List By Score");
//		
//		for(Map.Entry<String, Word> entry : criteria.entrySet()) {
//			i++;
//			if(i == 100)
//				break;
////			System.out.println(
////					entry.getKey() + " : " + words.get(entry.getKey()).getNumberOfWord() +
////					", Score = " + words.get(entry.getKey()).getRelationScore() +
////					", SCORE = " + words.get(entry.getKey()).getRelationScore() * words.get(entry.getKey()).getNumberOfWord()
////					);
//			System.out.println(
//					entry.getKey() + " " + words.get(entry.getKey()).getNumberOfWord() + " " +
//					words.get(entry.getKey()).getRelationScore() * words.get(entry.getKey()).getNumberOfWord()
//					);
////			words.get(entry.getKey()).getData();
//		}
//	}
//	
//	private class NumberComparator implements Comparator<String> {
//		Map<String, Word> base;
//		
//		public NumberComparator(Map<String, Word> base) {
//			this.base = base;
//		}
//		
//		@Override
//		public int compare(String o1, String o2) {
//			if(base.get(o1).getNumberOfWord() >= base.get(o2).getNumberOfWord()) {
//				return -1;
//			} else
//				return 1;
//		}
//	}
//	
//	private class ScoreComparator implements Comparator<String> {
//		Map<String, Word> base;
//		
//		public ScoreComparator(Map<String, Word> base) {
//			this.base = base;
//		}
//		
//		@Override
//		public int compare(String o1, String o2) {
//			if(base.get(o1).getNumberOfWord() * base.get(o1).getRelationScore() >=
//			   base.get(o2).getNumberOfWord() * base.get(o2).getRelationScore()) {
//				return -1;
//			} else
//				return 1;
//		}
//	}
//
//	public TreeMap<String, Word> getSortedWords() {
//		return sortedWords;
//	}
//
//	public TreeMap<String, Word> getSortedScoredWords() {
//		return sortedScoredWords;
//	}
}
