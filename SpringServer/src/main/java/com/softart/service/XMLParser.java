package com.softart.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.softart.domain.Word;
import com.softart.repository.WordRepository;

@Service
public class XMLParser {
	@Autowired
	private XMLReader xmlreader;
	@Resource
	private WordRepository wordRepository;
	
	
	private String naverAPI_ID = "";
	private String naverAPI_PW = "";
	
	private URL url = null;
	private HttpURLConnection connection = null;

	
	public void parseData() {
		wordRepository.deleteAll();
		int numberOfdata = 100;
		for(int i = 0; i < 2; i++) {
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
				xmlreader.processDocument(xpp);
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for(Word word : wordRepository.findAll()) {
			word.calculateRelationScore();
			wordRepository.save(word);
		}
		
		System.out.println("Parse complete!");
		
	}
}
