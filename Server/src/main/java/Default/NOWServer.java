package Default;
import java.io.*;

import java.net.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import MachineLearning.MachineLearning;
import domain.Word;
import kr.ac.kaist.swrc.jhannanum.plugin.MajorPlugin.MorphAnalyzer.ChartMorphAnalyzer.ChartMorphAnalyzer;
import repository.WordRepository;

import java.lang.String;

@ComponentScan
@EnableAutoConfiguration
@SpringBootApplication
public class NOWServer {
	private static boolean mask = false;
	
	public static void main(String[] args) {

//		SpringApplication.run(NOWServer.class, args);
		System.out.println("aaa");
		
		String naverAPI_ID = "PK89YnYKC5soKYkpYhNe";
		String naverAPI_PW = "kJXv7yGScc";
		
		
		URL url = null;
		HttpURLConnection connection = null;

		int numberOfdata = 100;
		for(int i = 0; i < 9; i++) {
			StringBuilder urlBuilder = new StringBuilder("https://openapi.naver.com/v1/search/news.xml"); /*URL*/
			System.out.println(urlBuilder.toString());
			try {
				urlBuilder.append(
						"?" + URLEncoder.encode("query","UTF-8") + "=" +
						URLEncoder.encode("은 | 는 | 이 | 가", "UTF-8")
				); /*검색 키워드*/
		        urlBuilder.append(
		        		"&" + URLEncoder.encode("display","UTF-8") + "=" +
    					URLEncoder.encode(numberOfdata + "", "UTF-8")
		        ); /*표시할 갯수*/
		        urlBuilder.append(
		        		"&" + URLEncoder.encode("start","UTF-8") + "=" +
    					URLEncoder.encode((i * numberOfdata + 1) + "", "UTF-8")
        		); /*시작 번호*/
		        urlBuilder.append(
		        		"&" + URLEncoder.encode("sort","UTF-8") + "=" +
        				URLEncoder.encode("date", "UTF-8")
        		); /*정렬 기준 : sim(정확도 기준), date(날짜 기준)*/
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			
			try {
				url = new URL(urlBuilder.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				connection = (HttpURLConnection)url.openConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			connection.setRequestProperty("Content-Type", "application.xml");
			connection.setRequestProperty("X-Naver-Client-Id", naverAPI_ID);
			connection.setRequestProperty("X-Naver-Client-Secret", naverAPI_PW);
			
			BufferedReader rd = null;
			try {
				rd = new BufferedReader(
						new InputStreamReader(connection.getInputStream()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				factory.setNamespaceAware(true);
				XmlPullParser xpp = factory.newPullParser();
				
				xpp.setInput(rd);
				NOWServer myConnection = new NOWServer();
				myConnection.processDocument(xpp);
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		
	}
	
	public void processDocument(XmlPullParser xpp)
			throws XmlPullParserException, IOException{
		int eventType = xpp.getEventType();
		String tab = "";
		do {
			if(eventType == xpp.START_DOCUMENT) {
			} else if(eventType == xpp.END_DOCUMENT) {
			} else if(eventType == xpp.START_TAG) {
				processStartElement(xpp);
			} else if(eventType == xpp.END_TAG) {
				processEndElement(xpp);
			} else if(eventType == xpp.TEXT) {
				processText(xpp);
			}
			
			eventType = xpp.next();
		} while (eventType != xpp.END_DOCUMENT);
	}
	
	public void processStartElement(XmlPullParser xpp) {
		String name = xpp.getName();
		String uri = xpp.getNamespace();
		if(name.equals("title") || name.equals("description")) {
			mask = true;
		}
	}
	
	public void processEndElement(XmlPullParser xpp) {
		String name = xpp.getName();
		String uri = xpp.getNamespace();
		if(name.equals("title") || name.equals("description")) {
			mask = false;
		}
	}
	

    public void processText (XmlPullParser xpp) throws XmlPullParserException {
        int holderForStartAndLength[] = new int[2];
        char ch[] = xpp.getTextCharacters(holderForStartAndLength);
        int start = holderForStartAndLength[0];
        int length = holderForStartAndLength[1];
        String content = "";
        for (int i = start; i < start + length; i++) {
            switch (ch[i]) {
                case '\\':
                    content += "\\\\";
                    break;
                case '"':
                	content += "\\\"";
                    break;
                case '\n':
                    content += "\\n";
                    break;
                case '\r':
                	content += "\\r";
                    break;
                case '\t':
                	content += "\\t";
                    break;
                default:
                	content += ch[i];
                    break;
            }
        }
        content = content.replace("&quot;", "");
        content = content.replace("&lt;", "");
        content = content.replace("&gt;", "");
        content = content.replace("<b>", "");
        content = content.replace("</b>", "");
        content = content.replace("&amp", "");
//        System.out.print(content + "\n");
        
        
        if(mask)
        	MachineLearning.getInstance().getParsedData(content);
    }    
}
