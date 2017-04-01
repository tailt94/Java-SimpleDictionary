package com.tuantai.dictionary.Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shine on 23-Mar-17.
 */
public class HistoryRecord implements Serializable {
    private String word;
    private boolean isFavorite;
    private HashMap<LocalDate, Integer> searchHistory;

    public HistoryRecord(String word, boolean isFavorite, HashMap<LocalDate, Integer> searchHistory) {
        this.word = word;
        this.isFavorite = isFavorite;
        this.searchHistory = searchHistory;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public HashMap<LocalDate, Integer> getSearchHistory() {
        return searchHistory;
    }

    public void setSearchHistory(HashMap<LocalDate, Integer> searchHistory) {
        this.searchHistory = searchHistory;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
