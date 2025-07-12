package ru.yandex.practicum.myblog.repository;

import ru.yandex.practicum.myblog.model.Post;

import java.util.Collection;

public interface PostRepository {

    /**
     * Добавить новое Сообщение
     *
     * @param post  Сообщение
     * @return      Идентификатор нового Сообщения
     */
    Long add(Post post);

    /**
     * Редактирование существующего Сообщения
     *
     * @param post  Сообщение
     * @return      Идентификатор Сообщения
     */
    Long edit(Post post);

    /**
     * Удаление Сообщения
     *
     * @param id    Идентификатор Сообщения
     */
    void del(Long id);

    /**
     * Добавить лайк Сообщению
     *
     * @param id    Идентификатор Сообщения
     */
    void incLike(Long id);

    /**
     * Отнять лайк у Сообщения
     *
     * @param id    Идентификатор Сообщения
     */
    void decLike(Long id);

    /**
     * Получение Сообщения по идентификатору
     *
     * @param id    Идентификатор Сообщения
     * @return      Сообщение
     */
    Post findById(Long id);

    /**
     * Получение всех сообщений
     *
     * @return Список Сообщений
     */
    Collection<Post> findAll();

    /**
     * Получение постов с заданным тагом
     * @param tag Наименование тага
     * @return  Набор постов
     */
    Collection<Post> findAllByTag(String tag);
}
