package ru.job4j.quartz;

import org.jsoup.nodes.Element;

import java.util.List;

public interface Parse {
    List<Post> parse(String link);

    Post detail(String link);
}
