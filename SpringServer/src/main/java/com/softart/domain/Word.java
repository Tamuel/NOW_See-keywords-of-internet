package com.softart.domain;

import java.sql.Date;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

@Entity
@Table(name = "KEYWORDS")
public class Word {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
	private Long id;
	
	private String keyword;
	
	@Column(name="RELATION_SCORE")
	private int relationScore;
	
	@Column(name="NUMBER_OF_WORD")
	private int numberOfWord;
	

	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name="NUMBER_BY_TIME", joinColumns=@JoinColumn(name="ID"))
	@MapKeyColumn (name="TIME")
	@Column(name="NUMBER_OF_WORDS")
	private Map<Date, Integer> numberOfWordByTime = new HashMap<Date, Integer>();

	
	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name="RELATED_WORDS", joinColumns=@JoinColumn(name="ID"))
	@MapKeyColumn (name="WORD")
	@Column(name="NUMBER_OF_WORDS")
	private Map<String, Integer> relatedWords = new HashMap<String, Integer>();

	public Word() {
	}
	
	public Word(String keyword) {
		this.keyword = keyword;

		relationScore = 0;
		numberOfWord = 1;
		Date time = Date.valueOf(
				Calendar.getInstance().get(Calendar.YEAR) + "-" +
				Calendar.getInstance().get(Calendar.MONTH) + "-" +
				Calendar.getInstance().get(Calendar.DATE)
				);
		numberOfWordByTime.put(time, 1);
	}
	
	public void addNumberOfWord(Date date, int number) {
		if(numberOfWordByTime.containsKey(date))
			numberOfWordByTime.put(date, numberOfWordByTime.get(date) + number);
		else
			numberOfWordByTime.put(date, 1);
	}
	
	public void addRelatedWord(String relationWords[]) {
    	for(String temp : relationWords) {
    		if(!temp.equals(keyword)) {
		    	if(relatedWords.containsKey(temp)) {
		    		relatedWords.put(temp, relatedWords.get(temp) + 1);
		    	} else {
		    		relatedWords.put(temp, 1);
		    	}
    		}
    	}
	}
    
    public int getNumberOfWord() {
		return numberOfWord;
	}

	public void setNumberOfWord(int numberOfWord) {
		this.numberOfWord = numberOfWord;
	}

	public void calculateRelationScore() {
    	TreeMap<String, Integer> sortedRelatedWords;
		ValueComparator bvc = new ValueComparator(relatedWords);
		sortedRelatedWords = new TreeMap<String, Integer>(bvc);
    	sortedRelatedWords.clear();
		sortedRelatedWords.putAll(relatedWords);
		
		relationScore = 0;
    	int i = 0;
		for(Map.Entry<String, Integer> entry : sortedRelatedWords.entrySet()) {
			i++;
			relationScore += relatedWords.get(entry.getKey());
			if(i == 7)
				break;
		}
		relationScore *= numberOfWord;
		if(numberOfWordByTime.size() >= 2) {
			Date timeCur = Date.valueOf(
					Calendar.getInstance().get(Calendar.YEAR) + "-" +
					Calendar.getInstance().get(Calendar.MONTH) + "-" +
					Calendar.getInstance().get(Calendar.DATE)
					);

			Date timeLast = Date.valueOf(
					Calendar.getInstance().get(Calendar.YEAR) + "-" +
					Calendar.getInstance().get(Calendar.MONTH) + "-" +
					Calendar.getInstance().get(Calendar.DATE - 1)
					);
			relationScore *= numberOfWordByTime.get(timeCur) - numberOfWordByTime.get(timeLast);
		}
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
	
  
    
	public int getRelationScore() {
		return relationScore;
	}

	public void setRelationScore(int relationScore) {
		this.relationScore = relationScore;
	}
	
	public Map<Date, Integer> getNumberOfWordByTime() {
		return numberOfWordByTime;
	}

	public void setNumberOfWordByTime(Map<Date, Integer> numberOfWordByTime) {
		this.numberOfWordByTime = numberOfWordByTime;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Map<String, Integer> getRelatedWords() {
		return relatedWords;
	}

	public void setRelatedWords(Map<String, Integer> relatedWords) {
		this.relatedWords = relatedWords;
	}

	
}
