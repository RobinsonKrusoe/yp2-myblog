package ru.yandex.practicum.myblog.model;

import lombok.*;

/**
 * Класс сообщения
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    private long id;                    //Идентификатор записи

    private String title;
    private String text;
    private byte[] image;
    private int likesCount;
}

