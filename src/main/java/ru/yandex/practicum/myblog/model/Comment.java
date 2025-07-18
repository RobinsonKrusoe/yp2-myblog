package ru.yandex.practicum.myblog.model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * Класс комментария на сообщение
 */

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;                    //Идентификатор записи
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    @NotBlank
    private String text;                //Текст коментария
    private LocalDateTime createdAt;    //Дата и время создания комментария
}
