package ru.yandex.practicum.myblog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class InPostDTO {
    private long id;                    //Идентификатор записи
    private String title;
    private String text;
    private MultipartFile image;
    private String tags;
}

