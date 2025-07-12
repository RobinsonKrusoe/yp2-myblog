package ru.yandex.practicum.myblog.mapper;

import ru.yandex.practicum.myblog.dto.CommentDTO;
import ru.yandex.practicum.myblog.model.Comment;

public class CommentMapper {
    public static CommentDTO toCommentDTO(Comment comment) {
        if(comment != null){
            CommentDTO ret = CommentDTO.builder()
                    .id(comment.getId())
                    .text(comment.getText())
                    .build();

            return ret;
        } else {
            return null;
        }
    }
}
