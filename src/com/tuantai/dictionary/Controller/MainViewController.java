package com.tuantai.dictionary.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import com.tuantai.dictionary.Model.HistoryFileManager;
import com.tuantai.dictionary.Model.HistoryRecord;
import com.tuantai.dictionary.Model.Record;
import com.tuantai.dictionary.Model.XMLReader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainViewController implements Initializable{
    private HashMap<String, Record> recordsEngVie;
    private HashMap<String, Record> recordsVieEng;
    private HashMap<String, Record> records;
    private HashMap<String, HistoryRecord> historyMap;
    private String currentWord;
    private LocalDate fromDate, toDate;

    @FXML
    private JFXButton btnEngVie;

    @FXML
    private JFXButton btnVieEng;

    @FXML
    private TextArea taMeaning;

    @FXML
    private TextArea taHistory;

    @FXML
    private TextArea taFavorite;

    @FXML
    private JFXTextField tfSearch;

    @FXML
    private JFXToggleButton btnFavorite;

    @FXML
    private JFXDatePicker fromDatePicker;

    @FXML
    private JFXDatePicker toDatePicker;

    @FXML
    private JFXButton btnShowHistory;

    public MainViewController() {
        recordsEngVie = XMLReader.getRecordsFromXML(XMLReader.ENG_VIE_PATH);
        recordsVieEng = XMLReader.getRecordsFromXML(XMLReader.VIE_ENG_PATH);
        records = recordsEngVie;
        try {
            historyMap = HistoryFileManager.readHistoryRecords(HistoryFileManager.PATH_NAME);
        } catch (Exception e) {
            historyMap = new HashMap<>();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showFavorite();
    }

    @FXML
    void onClickButtonEngVie(ActionEvent event) {
        records = recordsEngVie;
        btnEngVie.setDisable(true);
        btnVieEng.setDisable(false);
    }

    @FXML
    void onClickButtonVieEng(ActionEvent event) {
        records = recordsVieEng;
        btnVieEng.setDisable(true);
        btnEngVie.setDisable(false);
    }

    @FXML
    void onSearchEntered(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            currentWord = normalizeSearchText(tfSearch.getText());
            //currentDisplayRecord = records.get(currentWord);
            String meaning = searchMeaningByWord(records, currentWord);

            //Không tìm thấy từ
            if(meaning.equals("NULL")) {
                taMeaning.setText("Không có dữ liệu phù hợp");
            } else { //Tìm thấy từ phù hợp
                //Lịch sử chưa có từ khóa tìm kiếm
                if(!historyMap.containsKey(currentWord)) {
                    HashMap<LocalDate, Integer> searchHistory = new HashMap<>();
                    searchHistory.put(LocalDate.now(), 1);
                    historyMap.put(currentWord, new HistoryRecord(currentWord, false, searchHistory));
                } else { //Lịch sử đã có từ khóa tìm kiếm
                    HistoryRecord historyRecord = historyMap.get(currentWord);
                    HashMap<LocalDate, Integer> searchHistory = historyRecord.getSearchHistory();
                    LocalDate toDay = LocalDate.now();
                    //Trong ngày hôm nay chưa tìm kiếm từ khóa này
                    if(!searchHistory.containsKey(toDay)) {
                        searchHistory.put(toDay, 1);
                    } else { //Trong ngày hôm nay đã tìm kiếm từ này
                        searchHistory.put(toDay, searchHistory.get(toDay) + 1);
                    }
                }
                HistoryRecord historyRecord = historyMap.get(currentWord);
                if(historyRecord.isFavorite()) {
                    btnFavorite.setSelected(true);
                } else {
                    btnFavorite.setSelected(false);
                }
                taMeaning.setText(meaning);
            }
        }
    }

    @FXML
    void onToggleButtonFavorite(ActionEvent event) {
        HistoryRecord historyRecord = historyMap.get(currentWord);
        if(btnFavorite.isSelected()) {
            historyRecord.setFavorite(true);
        } else if (!btnFavorite.isSelected()){
            historyRecord.setFavorite(false);
        }
        showFavorite();
    }

    @FXML
    void onClickButtonShowHistory(ActionEvent event) {
        fromDate = fromDatePicker.getValue();
        toDate = toDatePicker.getValue();
        showHistory(fromDate, toDate);
    }

    /**
     * Hiển thị lịch sử tra từ
     * @param fromDate Ngày bắt đầu
     * @param toDate Ngày kết thúc
     */
    private void showHistory(LocalDate fromDate, LocalDate toDate) {
        if(fromDate == null || toDate == null || fromDate.isAfter(toDate)) {
            taHistory.setText("Dữ liệu đầu vào không hợp lệ");
            return;
        }
        StringBuilder word = new StringBuilder();
        for(Map.Entry<String, HistoryRecord> entry : historyMap.entrySet()) {
            int time = 0;
            HashMap<LocalDate, Integer> searchHistory = entry.getValue().getSearchHistory();
            for(Map.Entry<LocalDate, Integer> item : searchHistory.entrySet()) {
                LocalDate date = item.getKey();
                if((date.isAfter(fromDate) || date.isEqual(fromDate)) && (date.isBefore(toDate) || date.isEqual(toDate))) {
                    time += item.getValue();
                }
            }
            word.append(entry.getKey()).append("  -  ").append(time).append(" lần").append("\n");
        }
        taHistory.setText(word.toString());
    }

    /**
     * Hiển thị danh sách các từ yêu thích lên TextArea
     */
    private void showFavorite() {
        StringBuilder word = new StringBuilder();
        for(Map.Entry<String, HistoryRecord> entry : historyMap.entrySet()) {
            if(entry.getValue().isFavorite()) {
                word.append(entry.getKey()).append("\n");
            }
        }
        taFavorite.setText(word.toString());
    }

    /**
     * Xóa khoảng trắng đầu, cuối và chuyển toàn bộ text thành chữ thường
     * @param text Chuỗi ký tự cần normalize
     *
     */
    private String normalizeSearchText(String text) {
        return text.trim().toLowerCase();
    }

    /**
     * Tìm nghĩa của từ cần tìm
     * @param records Danh sách chứa các records
     * @param word Từ khóa cần tìm nghĩa
     * @return Nghĩa của từ
     */
    private String searchMeaningByWord(HashMap<String, Record> records, String word) {
        word = word.trim().toLowerCase();
        String result ;
        try {
            result = records.get(word).getMeaning();
        } catch (NullPointerException e) {
            result = "NULL";
        }
        return result;
    }

    /**
     * Method chạy khi đóng chương trình
     */
    public void onApplicationClose() {
        HistoryFileManager.writeHistoryRecords(HistoryFileManager.PATH_NAME, historyMap);
    }
}
