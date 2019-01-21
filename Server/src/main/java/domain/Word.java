package domain;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

@Entity
public class Word {
	
//	@OrderBy("numberOfWord Desc")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String keyword;
	
	private int numberOfWord;
	private int relationScore;

	@OneToMany( mappedBy = "Word")
	private List<Word> relatedWords;
	
	
//	private TreeMap<String, Integer> sortedRelatedWords;
	
	public Word(String keyword) {
		this.keyword = keyword;

		numberOfWord = 0;
		relationScore = 0;
		
//		relatedWords = new HashMap<String, Integer>();
//		ValueComparator bvc = new ValueComparator(relatedWords);
//		sortedRelatedWords = new TreeMap<String, Integer>(bvc);
	}
	
	public void addRelatedWord(String relationWords[]) {
//    	for(String temp : relationWords) {
//    		if(!temp.equals(keyword)) {
//		    	if(relatedWords.containsKey(temp)) {
//		    		relatedWords.put(temp, relatedWords.get(temp) + 1);
//		    	} else {
//		    		relatedWords.put(temp, 1);
//		    	}
//    		}
//    	}
//    	
    	calculateRelationScore();
	}
    
//    public void getData() {
//    	sortedRelatedWords.clear();
//		sortedRelatedWords.putAll(relatedWords);
//		int i = 0;
//		for(Map.Entry<String, Integer> entry : sortedRelatedWords.entrySet()) {
//			i++;
//			System.out.println("\t" + entry.getKey() + " " + relatedWords.get(entry.getKey()));
//			if(i == 7)
//				break;
//		}
//    }
    
    public void calculateRelationScore() {
//    	sortedRelatedWords.clear();
//		sortedRelatedWords.putAll(relatedWords);
		
		relationScore = 0;
    	int i = 0;
		for(Word entry : relatedWords) {
			i++;
			relationScore += entry.numberOfWord;
			if(i == 7)
				break;
		}
    }
    

//    private class ValueComparator implements Comparator<String> {
//    	Map<String, Integer> base;
//
//    	public ValueComparator(Map<String, Integer> base) {
//    		this.base = base;
//		}
//    	
//		@Override
//		public int compare(String o1, String o2) {
//			if(base.get(o1) >= base.get(o2)) {
//				return -1;
//			} else
//				return 1;
//		}
//    }
    
    
	public int getRelationScore() {
		return relationScore;
	}

	public void setRelationScore(int relationScore) {
		this.relationScore = relationScore;
	}

	public int getNumberOfWord() {
		return numberOfWord;
	}

	public void setNumberOfWord(int numberOfWord) {
		this.numberOfWord = numberOfWord;
	}
	
	public void addNumberOfWord(int num) {
		this.numberOfWord += num;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public List<Word> getRelatedWords() {
		return relatedWords;
	}

	public void setRelatedWords(List<Word> relatedWords) {
		this.relatedWords = relatedWords;
	}

//	public Date getDate() {
//		return date;
//	}
//
//	public void setDate(Date date) {
//		this.date = date;
//	}
	
	
}
