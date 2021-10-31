package ru.job4j.quartz;

import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse implements Parse {

   @SneakyThrows(value = {IOException.class})
   public List<Post> parse(String link) {
       List<Post> posts = new ArrayList<>();
       Document doc = Jsoup.connect(link).get();
       Elements row = doc.select("tr");
       for (int i = 7; i < row.size(); i++) {
           Element element = row.get(i);
           if (element.childrenSize() > 2) {
               Elements elements = element.child(1).getElementsByTag("a");
               String link1 = elements.attr("href");
               Post post = detail(link1);
               posts.add(post);
           }
       }
   }

    @Override
    public Post detail(String link) {
       Post post = new Post();
       post.setCreated(new SqlRuDateTimeParser());

        return
    }


   public LocalDateTime date(Element row) {
       SqlRuDateTimeParser timeParser = new SqlRuDateTimeParser();
       String date = row.child(5).text();
       System.out.println(timeParser.parse(date));
       return timeParser.parse(date);
   }

   private String title(Element row) {
       System.out.println(row.child(1).text());
       return row.child(1).text();
   }

    @SneakyThrows(value = {IOException.class})
   private String details(String link) {
       Document doc = Jsoup.connect(link).get();
        Elements row = doc.select(".msgBody");
        return row.get(1).text();
   }
}
