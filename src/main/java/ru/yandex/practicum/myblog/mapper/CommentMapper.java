package ru.yandex.practicum.myblog.mapper;

import ru.yandex.practicum.myblog.dto.CommentDTO;
import ru.yandex.practicum.myblog.model.Comment;
import ru.yandex.practicum.myblog.model.Post;

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

    public static Comment toComment(CommentDTO commentDTO, Post post){
        if (commentDTO != null){
            Comment comment = new Comment();
            comment.setId(commentDTO.getId());
            comment.setPost(post);
            comment.setText(commentDTO.getText());
            return comment;
        } else {
            return null;
        }
    }
}
