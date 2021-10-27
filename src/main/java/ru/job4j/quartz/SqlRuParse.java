package ru.job4j.quartz;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements row = doc.select("tr");
        for (int i = 4; i < row.size(); i++) {
            Element element = row.get(i);
            if (element.childrenSize() > 2) {
                System.out.println(element.child(5).text());
            }

        }
    }
}
