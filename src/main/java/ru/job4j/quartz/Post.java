package ru.job4j.quartz;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@ToString
public class Post {
    private int id;
    private String name;
    private String description;
}
