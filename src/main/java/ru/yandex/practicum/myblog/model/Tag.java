package ru.yandex.practicum.myblog.model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
/**
 * Класс тегов сообщения
 */

@Entity
@Table(name = "tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;                    //Идентификатор записи
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    @NotBlank
    private String name;                //Имя тега
}
