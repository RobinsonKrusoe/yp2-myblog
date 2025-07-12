package ru.yandex.practicum.myblog.mapper;

import ru.yandex.practicum.myblog.dto.CommentDTO;
import ru.yandex.practicum.myblog.dto.InPostDTO;
import ru.yandex.practicum.myblog.dto.OutPostDTO;
import ru.yandex.practicum.myblog.model.Post;

import java.io.IOException;
import java.util.List;

public class PostMapper {

    public static OutPostDTO toPostDTO(Post post, List<CommentDTO> comments, List<String> tags) {
        if (post != null) {
            return OutPostDTO.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .text(post.getText())
                    .imagePath(String.valueOf(post.getId()))
                    .likesCount(post.getLikesCount())
                    .comments(comments)
                    .tags(tags)
                    .build();
        } else
            return null;
    }

    public static Post toPost(InPostDTO inPostDTO) throws IOException {
        if(inPostDTO != null){
            Post post = new Post();
            post.setId(inPostDTO.getId());
            post.setTitle(inPostDTO.getTitle());
            post.setText(inPostDTO.getText());
            post.setImage(inPostDTO.getImage().getBytes());

            return post;
        } else {
            return null;
        }
    }
}
