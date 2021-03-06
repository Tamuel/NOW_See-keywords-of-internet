package com.softart.service;

import java.sql.Date;
import java.util.Calendar;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softart.domain.Word;
import com.softart.repository.WordRepository;

import kr.ac.kaist.swrc.jhannanum.hannanum.Workflow;
import kr.ac.kaist.swrc.jhannanum.hannanum.WorkflowFactory;

@Service
public class MachineLearning {
	@Resource
	private WordRepository wordRepository;
	
	private Workflow workflow;
	
	private String replaceSet[] = {
		" 기자",
		"’ 은", "” 은", "] 은", ") 은",
		"’ 는", "” 는", "] 는", ") 는",
		"’ 이", "” 이", "] 이", ") 이",
		"’ 가", "” 가", "] 가", ") 가",
		"’ 의", "” 의", "] 의", ") 의",
		"’은", "”은", "]은", ")은",
		"’는", "”는", "]는", ")는",
		"’이", "”이", "]이", ")이",
		"’가", "”가", "]가", ")가",
		"’의", "”의", "]의", ")의",
		"'은", "'는", "'이", "'가",
		" 것으로 ", " 기 ", "라는 ",
		"들 ", "로 ", " 를 ", "을 ",
		" 때 ", " 가운데 ", " 이 ",
		".", ",", "!", "?",
		"Ⅲ", "-", "[", "]", "(",
		")", "‘", "’", "”", "'",
		"\"", "|", "“", "”", "=",
		"…", "`", "`", "】", "【"
    };
	
	public MachineLearning() {
		
        workflow = WorkflowFactory.getPredefinedWorkflow(WorkflowFactory.WORKFLOW_POS_SIMPLE_22);
        try {
			workflow.activateWorkflow(true);
        } catch(Exception e) {
        	e.printStackTrace();
        }
	}
	
	public String[] getParsedData(String data) {
		System.out.println("Origianl : " + data);
        for(String temp : replaceSet) {
        	data = data.replace(temp, " ");
        }
        
        String result = "";
        // Get Keyword from news
		try {
			workflow.analyze(data);
			result = workflow.getResultOfSentence();
			result = result.replace("\t", "");
			result = result.replace("\n", " ");
			result = result.replace("  ", " ");
			String resultSetWhole[] = result.split(" ");
			result = "";
			for(int i = 0; i < resultSetWhole.length / 2; i++) {
				String temp = resultSetWhole[i * 2 + 1];
				if(temp.contains("/NC")) {
					if(temp.contains("+")) {
						for(String t : temp.split("\\+")) {
							if(t.contains("/NC")) {
								t = t.replace("/NC", "");
								result += t + " ";
							}
						}
					} else {
						temp = temp.replace("/NC", "");
						result += temp + " ";
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

		System.out.print("Data : ");
		for(String temp : result.split(" ")) {
			System.out.print(temp + " ");
			if(wordRepository.findByKeyword(temp) == null) {
				Word word = new Word(temp);
				word.addRelatedWord(result.split(" "));
				wordRepository.save(word);
			} else {
				Word word = wordRepository.findByKeyword(temp);
				word.addNumberOfWord(
						Date.valueOf(
								Calendar.getInstance().get(Calendar.YEAR) + "-" +
								Calendar.getInstance().get(Calendar.MONTH) + "-" +
								Calendar.getInstance().get(Calendar.DATE)
						),
						1
				);
				word.addRelatedWord(result.split(" "));
				word.setNumberOfWord(word.getNumberOfWord() + 1);
				wordRepository.save(
						word
				);
			}
		}
		System.out.println("");
        return result.split(" ");
	}

//	public void printData() {
//		wordManager.getData(wordManager.getSortedScoredWords());
//	}
	
}
