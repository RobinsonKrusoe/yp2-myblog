package ru.yandex.practicum.myblog.model;

import lombok.*;
import java.time.LocalDateTime;

/**
 * Класс комментария на сообщение
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private long id;                    //Идентификатор записи

    private long postId;                //Идентификатор сообщения

    private String text;                //Текст коментария

    private LocalDateTime createdAt;    //Дата и время создания комментария
}
