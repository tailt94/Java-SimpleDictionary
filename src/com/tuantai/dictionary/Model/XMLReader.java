package com.tuantai.dictionary.Model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shine on 22-Mar-17.
 */
public class XMLReader {
    public static final String ENG_VIE_PATH = "Anh_Viet.xml";
    public static final String VIE_ENG_PATH = "Viet_Anh.xml";

    /**
     * Đọc danh sách các record từ file xml
     * @param filePath Đường dẫn đến file
     * @return Danh sách các record
     */
    public static HashMap<String, Record> getRecordsFromXML(String filePath) {
        HashMap<String, Record> wordMap = new HashMap<>();
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbFactory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            NodeList list = doc.getElementsByTagName("record");
            for(int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    Record record = new Record(element.getElementsByTagName("word").item(0).getTextContent(),
                            element.getElementsByTagName("meaning").item(0).getTextContent());
                    wordMap.put(record.getWord(), record);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wordMap;
    }
}
