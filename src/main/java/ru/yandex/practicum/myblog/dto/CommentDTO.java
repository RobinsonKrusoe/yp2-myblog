package ru.yandex.practicum.myblog.dto;

import lombok.*;

@Getter
@Setter
@Builder
public class CommentDTO {
    private long    id;                    //Идентификатор записи
    private String  text;                //Текст коментария
}
