package com.akhterrasool.collegelibrary;

import com.akhterrasool.collegelibrary.clientrequest.response.Author;
import com.akhterrasool.collegelibrary.clientrequest.response.Book;
import com.akhterrasool.collegelibrary.clientrequest.response.BookLocation;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hamcrest.core.Is;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class AuthorResponseTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void captureResponse() throws IOException {
        int numOfBooksAuthored = 2;
        int numOfLocations = 1;
        String expectedAuthor = "Andrew Tanenbaum";
        String expectedBookTitle = "Operating Systems";

        Author author = objectMapper.readValue(getResponse(), Author.class);

        String name = author.getName();
        assertThat(name, Is.is(expectedAuthor));
        List<Book> authoredBooks = author.getAuthoredBooks();
        assertFalse(authoredBooks.isEmpty());
        assertThat(authoredBooks.size(), Is.is(numOfBooksAuthored));
        Book book = authoredBooks.get(0);
        assertThat(book.getTitle(), Is.is(expectedBookTitle));
        List<BookLocation> bookLocations = book.getBookLocations();
        assertThat(bookLocations.size(), Is.is(numOfLocations));
        List<String> authors = book.getAuthors();
        assertFalse(authors.isEmpty());
        assertTrue(authors.contains(expectedAuthor));
    }

    private String getResponse() {
        return "{\n" +
                "\t\"name\": \"Andrew Tanenbaum\",\n" +
                "\t\"authoredBooks\": [{\n" +
                "\t\t\"title\": \"Operating Systems\",\n" +
                "\t\t\"bookLocations\": [{\n" +
                "\t\t\t\"rack\": 27,\n" +
                "\t\t\t\"row\": 4,\n" +
                "\t\t\t\"col\": 3\n" +
                "\t\t}],\n" +
                "\t\t\"authors\": [\n" +
                "\t\t\t\"Andrew Tanenbaum\"\n" +
                "\t\t]\n" +
                "\t}, {\n" +
                "\t\t\"title\": \"Computer Architecture\",\n" +
                "\t\t\"bookLocations\": [{\n" +
                "\t\t\t\"rack\": 18,\n" +
                "\t\t\t\"row\": 12,\n" +
                "\t\t\t\"col\": 19\n" +
                "\t\t}],\n" +
                "\t\t\"authors\": [\n" +
                "\t\t\t\"Andrew Tanenbaum\",\n" +
                "\t\t\t\"Henry Williams\"\n" +
                "\t\t]\n" +
                "\t}]\n" +
                "}";
    }
}


