package com.akhterrasool.collegelibrary.clientrequest.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import static com.akhterrasool.collegelibrary.app.Constants.NEW_LINE;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookLocation implements ResultActivityFormattable {

    private int rack;
    private int row;
    private int col;

    public int getRack() {
        return rack;
    }

    public void setRack(int rack) {
        this.rack = rack;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    @Override
    public StringBuilder formatForResultActivity() {
        StringBuilder formattedText = new StringBuilder();
        formattedText.append("Rack: " + rack);
        formattedText.append(NEW_LINE);
        formattedText.append("Row: " + row);
        formattedText.append(NEW_LINE);
        formattedText.append("Col: " + col);
        formattedText.append(NEW_LINE);
        formattedText.append(NEW_LINE);
        return formattedText;
    }
}
