package com.tuantai.dictionary.Model;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shine on 23-Mar-17.
 */
public class HistoryFileManager {
    public static final String PATH_NAME = "history.ser";

    /**
     * Đọc file lịch sử duyệt từ
     * @param filePath Đường dẫn đến file
     * @return  Danh sách các record
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static HashMap<String, HistoryRecord> readHistoryRecords(String filePath) throws IOException, ClassNotFoundException{
        HashMap<String, HistoryRecord> historyMap = new HashMap<>();
        FileInputStream f = new FileInputStream(new File(filePath));
        ObjectInputStream o = new ObjectInputStream(f);
        try {
            while(true) {
                HistoryRecord historyRecord = (HistoryRecord) o.readObject();
                historyMap.put(historyRecord.getWord(), historyRecord);
            }
        } catch (EOFException e) {
            return historyMap;
        } finally {
            o.close();
            f.close();
        }
    }

    /**
     * Ghi lịch sử duyệt từ vào file
     * @param filePath Đường dẫn đến file
     * @param historyMap Danh sách các record cần ghi vào file
     */
    public static void writeHistoryRecords(String filePath, HashMap<String, HistoryRecord> historyMap) {
        try {
            FileOutputStream f = new FileOutputStream(filePath);
            ObjectOutputStream o = new ObjectOutputStream(f);

            for(Map.Entry<String, HistoryRecord> entry : historyMap.entrySet()) {
                HistoryRecord historyRecord = entry.getValue();
                o.writeObject(historyRecord);
            }
            o.close();
            f.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }
    }
}
