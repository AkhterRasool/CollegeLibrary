package com.akhterrasool.collegelibrary;

import com.akhterrasool.collegelibrary.clientrequest.response.Book;
import com.akhterrasool.collegelibrary.clientrequest.response.StudentBookIssues;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hamcrest.core.Is;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class BookIssuesResponseTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void captureResponse() throws IOException {
        String expectedRoll = "160413733002";
        String expectedBookIssued = "Computer Architecture";
        double expectedFineAmount = 123.45;

        StudentBookIssues bookIssues = objectMapper.readValue(getResponse(), StudentBookIssues.class);

        assertThat(bookIssues.getRollNumber(), Is.is(expectedRoll));
        boolean containsExpectedBookIssued = bookIssues.getBooksIssued()
                .stream()
                .map(Book::getTitle)
                .anyMatch(title -> title.equals(expectedBookIssued));
        assertTrue(containsExpectedBookIssued);
        assertThat(bookIssues.getFineAmount(), Is.is(expectedFineAmount));

        System.out.println(bookIssues.formatForResultActivity());
    }

    private String getResponse() {
        return "{\n" +
                "    \"id\": 2,\n" +
                "    \"rollNumber\": \"160413733002\",\n" +
                "    \"fineAmount\": 123.45,\n" +
                "    \"booksIssued\": [\n" +
                "        {\n" +
                "            \"title\": \"Computer Architecture\",\n" +
                "            \"bookLocations\": [\n" +
                "                {\n" +
                "                    \"rack\": 7,\n" +
                "                    \"row\": 5,\n" +
                "                    \"col\": 1\n" +
                "                },\n" +
                "                {\n" +
                "                    \"rack\": 8,\n" +
                "                    \"row\": 2,\n" +
                "                    \"col\": 1\n" +
                "                }\n" +
                "            ],\n" +
                "            \"authors\": [\n" +
                "                \"Henry Williams\"\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }
}
