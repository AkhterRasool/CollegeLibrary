package com.akhterrasool.collegelibrary.clientrequest.response;


import com.akhterrasool.collegelibrary.app.Constants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.stream.Stream;

import static com.akhterrasool.collegelibrary.app.Constants.NEW_LINE;
import static com.akhterrasool.collegelibrary.app.Constants.ROW_SEPARATOR_ON_NEW_LINE;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Book implements ResultActivityFormattable {

    private String title;
    private List<String> authors;
    private List<BookLocation> bookLocations;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        return this.authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public List<BookLocation> getBookLocations() {
        return this.bookLocations;
    }

    public void setBookLocations(List<BookLocation> bookLocations) {
        this.bookLocations = bookLocations;
    }

    public StringBuilder formatForResultActivity() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.title);
        builder.append(Constants.ROW_SEPARATOR_ON_NEW_LINE);
        StringBuilder allBookLocations =
                bookLocations
                        .stream()
                        .map(BookLocation::formatForResultActivity)
                        .reduce(new StringBuilder(), StringBuilder::append);
        builder.append(allBookLocations);
        builder.append(NEW_LINE);
        builder.append(getFormattedAuthors(authors));
        return builder;
    }


    private String getFormattedAuthors(List<String> authorList) {
        String authorTitle = "Authored By: " + ROW_SEPARATOR_ON_NEW_LINE;
        StringBuilder authorListText = new StringBuilder();
        if (authorList.size() > 1) {
            authorListText = Stream.iterate(1, n -> n + 1)
                    .map(i -> new StringBuilder(i + ") " + authorList.get(i - 1)))
                    .map(row -> row.append(NEW_LINE))
                    .limit(authorList.size())
                    .reduce(new StringBuilder(), StringBuilder::append);
        } else {
            authorListText.append(authorList.get(0));
        }
        return authorTitle + authorListText;
    }
}
