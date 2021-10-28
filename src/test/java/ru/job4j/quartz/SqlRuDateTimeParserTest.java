package ru.job4j.quartz;

import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.time.DateTimeException;
import java.time.LocalDateTime;

public class SqlRuDateTimeParserTest {
    @Test
    public void parsingDateBase() {
        SqlRuDateTimeParser date = new SqlRuDateTimeParser();
        LocalDateTime dateour = date.parse("25 апр 15, 13:27");
        assertEquals(dateour.getYear(), 2015);
        assertEquals(dateour.getMonthValue(), 4);
        assertEquals(dateour.getDayOfMonth(), 25);
        assertEquals(dateour.getHour(), 13);
        assertEquals(dateour.getMinute(), 27);
    }

    @Test@Ignore
    public void parsingDateToday() {
        SqlRuDateTimeParser date = new SqlRuDateTimeParser();
        LocalDateTime dateour = date.parse("сегодня, 05:02");
        assertEquals(dateour.getYear(), 2021);
        assertEquals(dateour.getMonthValue(), 10);
        assertEquals(dateour.getDayOfMonth(), 28);
        assertEquals(dateour.getHour(), 5);
        assertEquals(dateour.getMinute(), 2);
    }

    @Test@Ignore
    public void parsingDateYesterday() {
        SqlRuDateTimeParser date = new SqlRuDateTimeParser();
        LocalDateTime dateour = date.parse("вчера, 11:05");
        assertEquals(dateour.getYear(), 2021);
        assertEquals(dateour.getMonthValue(), 10);
        assertEquals(dateour.getDayOfMonth(), 27);
        assertEquals(dateour.getHour(), 11);
        assertEquals(dateour.getMinute(), 5);
    }

    @Test
    public void parsingDateDateOther() {
        SqlRuDateTimeParser date = new SqlRuDateTimeParser();
        LocalDateTime dateour = date.parse("31 дек 19, 15:41");
        assertEquals(dateour.getYear(),2019);
        assertEquals(dateour.getMonthValue(), 12);
        assertEquals(dateour.getDayOfMonth(), 31);
        assertEquals(dateour.getHour(), 15);
        assertEquals(dateour.getMinute(), 41);
    }

    @Test(expected = DateTimeException.class)
    public void incorrectData() {
        SqlRuDateTimeParser date = new SqlRuDateTimeParser();
        LocalDateTime dateour = date.parse("45 дек 19, 25:41");
        System.out.println(dateour);
    }
}