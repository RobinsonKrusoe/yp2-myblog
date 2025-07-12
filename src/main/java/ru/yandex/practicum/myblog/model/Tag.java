package ru.yandex.practicum.myblog.model;

import lombok.*;

/**
 * Класс тегов сообщения
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    private long id;                    //Идентификатор записи
    private long postId;                //Идентификатор сообщения
    private String name;                //тег
}
