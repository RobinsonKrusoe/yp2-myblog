package ru.yandex.practicum.myblog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OutPostDTO {
    private long id;                    //Идентификатор записи
    private String title;
    private String text;
    private String imagePath;
    private int likesCount;
    private List<CommentDTO> comments;
    private List<String> tags;

    public String getTagsAsText(){
        return String.join(" ", tags);
    }
}

