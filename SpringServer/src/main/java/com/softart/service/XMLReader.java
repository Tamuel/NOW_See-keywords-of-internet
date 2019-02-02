package com.softart.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@Service
public class XMLReader {
	private boolean mask = false;
	@Autowired
	private MachineLearning machineLearning;

	public void processDocument(XmlPullParser xpp)
			throws XmlPullParserException, IOException{
		int eventType = xpp.getEventType();
		do {
			if(eventType == XmlPullParser.START_DOCUMENT) {
			} else if(eventType == XmlPullParser.END_DOCUMENT) {
			} else if(eventType == XmlPullParser.START_TAG) {
				processStartElement(xpp);
			} else if(eventType == XmlPullParser.END_TAG) {
				processEndElement(xpp);
			} else if(eventType == XmlPullParser.TEXT) {
				processText(xpp);
			}
			
			eventType = xpp.next();
		} while (eventType != XmlPullParser.END_DOCUMENT);
	}
	
	public void processStartElement(XmlPullParser xpp) {
		String name = xpp.getName();
		if(name.equals("title") || name.equals("description")) {
			mask = true;
		}
	}
	
	public void processEndElement(XmlPullParser xpp) {
		String name = xpp.getName();
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
        
        
        if(mask)
        	machineLearning.getParsedData(content);
    }    
}
