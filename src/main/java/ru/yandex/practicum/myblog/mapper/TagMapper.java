package ru.yandex.practicum.myblog.mapper;

import ru.yandex.practicum.myblog.model.Post;
import ru.yandex.practicum.myblog.model.Tag;

public class TagMapper {
    public static Tag toTag(Post post, String tagName){
        if (tagName != null) {
            Tag tag = new Tag();
            tag.setPost(post);
            tag.setName(tagName);
            return tag;
        } else {
            return null;
        }
    }
}
