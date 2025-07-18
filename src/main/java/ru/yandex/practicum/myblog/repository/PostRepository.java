package ru.yandex.practicum.myblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.myblog.model.Post;

import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long> {

    Set<Post> findAllByTags_Name(String tag);

    //    /**
//     * Добавить новое Сообщение
//     *
//     * @param post  Сообщение
//     * @return      Идентификатор нового Сообщения
//     */
//    Long save(Post post);
//
//    /**
//     * Редактирование существующего Сообщения
//     *
//     * @param post  Сообщение
//     * @return      Идентификатор Сообщения
//     */
//    Long edit(Post post);
//
//    /**
//     * Удаление Сообщения
//     *
//     * @param id    Идентификатор Сообщения
//     */
//    void del(Long id);
//
//    /**
//     * Добавить лайк Сообщению
//     *
//     * @param id    Идентификатор Сообщения
//     */
//    void incLike(Long id);
//
//    /**
//     * Отнять лайк у Сообщения
//     *
//     * @param id    Идентификатор Сообщения
//     */
//    void decLike(Long id);
//
//    /**
//     * Получение Сообщения по идентификатору
//     *
//     * @param id    Идентификатор Сообщения
//     * @return      Сообщение
//     */
//    Post findById(Long id);
//
//    /**
//     * Получение всех сообщений
//     *
//     * @return Список Сообщений
//     */
//    Collection<Post> findAll();
//
//    /**
//     * Получение постов с заданным тагом
//     * @param tag Наименование тага
//     * @return  Набор постов
//     */
//    Collection<Post> findAllByTag(String tag);
}
