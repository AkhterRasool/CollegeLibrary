package com.akhterrasool.collegelibrary.clientrequest.response;

import java.util.List;

import static com.akhterrasool.collegelibrary.app.Constants.NEW_LINE;

public class Author implements ResultActivityFormattable {

    private String name;
    private List<Book> authoredBooks;

    public String getName() {
        return this.name;
    }

    public List<Book> getAuthoredBooks() {
        return this.authoredBooks;
    }

    @Override
    public StringBuilder formatForResultActivity() {
        return getAuthoredBooks()
                .stream()
                .map(ResultActivityFormattable::formatForResultActivity)
                .map(s -> s.append(NEW_LINE))
                .map(s -> s.append(NEW_LINE))
                .map(s -> s.append(NEW_LINE))
                .reduce(new StringBuilder(), StringBuilder::append);
    }
}
