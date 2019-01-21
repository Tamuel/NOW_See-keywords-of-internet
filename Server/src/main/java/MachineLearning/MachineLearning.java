package MachineLearning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import controller.Controller;
import controller.WordController;
import domain.Word;
import kr.ac.kaist.swrc.jhannanum.hannanum.Workflow;
import kr.ac.kaist.swrc.jhannanum.hannanum.WorkflowFactory;
import repository.WordRepository;


public class MachineLearning {
	public static MachineLearning instance = null;
	
	private Workflow workflow;
	
	private String replaceSet[] = {
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
		"들 ", "로 ",
		".", ",", "!", "?",
		"Ⅲ", "-", "[", "]", "(",
		")", "‘", "’", "”", "'",
		"\"", "|", "“", "”", "=",
		"…", "`", "`", "】", "【"
    };
	
	private MachineLearning() {
//		wordManager = new WordController();
		
        workflow = WorkflowFactory.getPredefinedWorkflow(WorkflowFactory.WORKFLOW_POS_SIMPLE_22);
        try {
			workflow.activateWorkflow(true);
        } catch(Exception e) {
        	e.printStackTrace();
        }
	}
	
	public String[] getParsedData(String data) {
		System.out.println("Data : " + data);
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

        return result.split(" ");
	}

//	public void printData() {
//		wordManager.getData(wordManager.getSortedScoredWords());
//	}
	
	public static MachineLearning getInstance() {
		if(instance == null)
			instance = new MachineLearning();
		return instance;
	}
}
