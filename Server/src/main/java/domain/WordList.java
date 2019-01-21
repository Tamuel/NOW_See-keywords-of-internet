package domain;

import java.util.ArrayList;
import java.util.List;

public class WordList {

	private List<Word> wordList;

	public WordList () {
		wordList = new ArrayList<Word>();
	}
	
	public List<Word> getWordList() {
		return wordList;
	}

	public void setWordList(List<Word> wordList) {
		this.wordList = wordList;
	}
}
