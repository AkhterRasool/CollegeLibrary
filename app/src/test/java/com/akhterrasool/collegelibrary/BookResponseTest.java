package com.akhterrasool.collegelibrary;

import com.akhterrasool.collegelibrary.clientrequest.response.BookLocation;
import com.akhterrasool.collegelibrary.clientrequest.response.Book;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hamcrest.core.Is;
import org.hamcrest.core.IsCollectionContaining;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import static com.akhterrasool.collegelibrary.app.Constants.NEW_LINE;
import static com.akhterrasool.collegelibrary.app.Constants.ROW_SEPARATOR_ON_NEW_LINE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

public class BookResponseTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void captureResponse() throws IOException {
        Book response = getBookResponse();
        String expectedTitle = "Computer Architecture";
        String author = "Henry Williams";
        int numOfLocations = 2;
        assertThat(response.getTitle(), Is.is(expectedTitle));
        List<String> authors = response.getAuthors();
        assertFalse(authors.isEmpty());
        assertThat(authors, IsCollectionContaining.hasItem(author));
        List<BookLocation> bookLocations = response.getBookLocations();
        assertFalse(bookLocations.isEmpty());
        assertThat(bookLocations.size(), Is.is(numOfLocations));
    }

    @Test
    public void getFormattedTextForMultipleAuthors() throws IOException {
        Book book = getBookResponse();
        int numOfLocations = 2;
        List<BookLocation> bookLocation = book.getBookLocations();
        assertFalse(bookLocation.isEmpty());
        assertThat(bookLocation.size(), Is.is(numOfLocations));
        List<String> authors = book.getAuthors();
        assertFalse(authors.isEmpty());
        StringBuffer expectedFormatText = new StringBuffer();
        expectedFormatText.append(book.getTitle());
        expectedFormatText.append(ROW_SEPARATOR_ON_NEW_LINE);
        String allBookLocationsFormattedText = bookLocation.stream().map(this::getFormattedBookLocation).reduce("", String::concat);
        expectedFormatText.append(allBookLocationsFormattedText);
        expectedFormatText.append(NEW_LINE);
        expectedFormatText.append(getFormattedAuthors(authors));

        String actualFormatText = book.formatForResultActivity().toString();

        assertEquals(expectedFormatText.toString(), actualFormatText);
    }

    @Test
    public void getFormattedTextForSingleAuthor() throws IOException {
        Book book = getBookResponse();
        int numOfLocations = 2;
        List<BookLocation> bookLocation = book.getBookLocations();
        assertFalse(bookLocation.isEmpty());
        assertThat(bookLocation.size(), Is.is(numOfLocations));
        List<String> authors = book.getAuthors();
        assertFalse(authors.isEmpty());
        //We remove everything except the first the author
        if (authors.size() > 1) {
            String firstAuthor = authors.get(0);
            authors.clear();
            authors.add(firstAuthor);
        }
        StringBuffer expectedFormatText = new StringBuffer();
        expectedFormatText.append(book.getTitle());
        expectedFormatText.append(ROW_SEPARATOR_ON_NEW_LINE);
        String allBookLocationsFormattedText = bookLocation.stream().map(this::getFormattedBookLocation).reduce("", String::concat);
        expectedFormatText.append(allBookLocationsFormattedText);
        expectedFormatText.append(NEW_LINE);
        expectedFormatText.append(getFormattedAuthors(authors));

        String actualFormatText = book.formatForResultActivity().toString();

        assertEquals(expectedFormatText.toString(), actualFormatText);
    }

    private Book getBookResponse() throws IOException {
        String json = expectedResponse();
        return objectMapper.readValue(json, Book.class);
    }

    private String getFormattedBookLocation(BookLocation bookLocation) {
        StringBuffer formattedText = new StringBuffer();
        formattedText.append("Rack: " + bookLocation.getRack());
        formattedText.append("\n");
        formattedText.append("Row: " + bookLocation.getRow());
        formattedText.append("\n");
        formattedText.append("Col: " + bookLocation.getCol());
        formattedText.append("\n");
        formattedText.append("\n");
        return formattedText.toString();
    }

    private String getFormattedAuthors(List<String> authorList) {

        String authorTitle = "Authored By: " + ROW_SEPARATOR_ON_NEW_LINE;
        String authorListText = "";
        if (authorList.size() > 1) {
            authorListText = Stream.iterate(1, n -> n + 1)
                    .map(i -> i + ") " + authorList.get(i - 1))
                    .map(row -> row.concat(NEW_LINE))
                    .limit(authorList.size())
                    .reduce("", String::concat);
        } else if (authorList.size() == 1) {
            authorListText = authorList.get(0);
        }
        return authorTitle + authorListText;
    }

    private String expectedResponse() {
        return "{\n" +
                "    \"id\": 404,\n" +
                "    \"title\": \"Computer Architecture\",\n" +
                "    \"bookLocations\": [\n" +
                "        {\n" +
                "            \"id\": 421,\n" +
                "            \"rack\": 7,\n" +
                "            \"row\": 5,\n" +
                "            \"col\": 1\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 422,\n" +
                "            \"rack\": 8,\n" +
                "            \"row\": 2,\n" +
                "            \"col\": 1\n" +
                "        }\n" +
                "    ],\n" +
                "    \"authors\": [\n" +
                "        \"Henry Williams\",\n" +
                "        \"Andrew Tanenbaum\"\n" +
                "    ]\n" +
                "}";
    }
}

