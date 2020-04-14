package com.akhterrasool.collegelibrary.clientrequest.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.stream.Stream;

import static com.akhterrasool.collegelibrary.app.Constants.NEW_LINE;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentBookIssues implements ResultActivityFormattable {

    private String rollNumber;
    private double fineAmount;
    private List<Book> booksIssued;

    public List<Book> getBooksIssued() {
        return booksIssued;
    }

    public void setBooksIssued(List<Book> booksIssued) {
        this.booksIssued = booksIssued;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    @Override
    public StringBuilder formatForResultActivity() {
        List<Book> booksIssued = getBooksIssued();
        StringBuilder formattedText = new StringBuilder();
        String individualBookListItemFormat = "%d) %s";
        StringBuilder listOfBooks = Stream.iterate(1, n -> n + 1)
                .map(i -> new StringBuilder(String.format(individualBookListItemFormat, i, booksIssued.get(i - 1).getTitle())))
                .map(stringBuilder -> stringBuilder.append(NEW_LINE))
                .map(stringBuilder -> stringBuilder.append(NEW_LINE))
                .limit(booksIssued.size())
                .reduce(new StringBuilder(), StringBuilder::append);

        formattedText.append(listOfBooks);
        formattedText.append(NEW_LINE);
        formattedText.append("Total Fine: " + fineAmount + " Rs/-");
        formattedText.append(NEW_LINE);
        formattedText.append(NEW_LINE);
        return formattedText;
    }
}
