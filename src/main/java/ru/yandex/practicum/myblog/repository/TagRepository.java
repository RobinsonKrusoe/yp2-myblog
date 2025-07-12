package ru.yandex.practicum.myblog.repository;

import ru.yandex.practicum.myblog.model.Tag;

import java.util.Collection;

public interface TagRepository {
    /**
     * Добавление тега сообщению
     *
     * @param postId Идентификатор сообщения
     * @param name   Название тега
     */
    void add(Long postId, String name);

    /**
     * Удаление тега
     *
     * @param id    Идентификатор тега
     */
    void del(Long id);

    /**
     * Получение всех тегов сообщения
     *
     * @param postId
     * @return
     */
    Collection<Tag> findAllByPostId(Long postId);
}
