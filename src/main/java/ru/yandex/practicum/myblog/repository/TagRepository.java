package ru.yandex.practicum.myblog.repository;

import ru.yandex.practicum.myblog.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Set<Tag> findAllByPostId(Long id);
    boolean existsByPostIdAndName(Long postId, String name);
//    /**
//     * Добавление тега сообщению
//     *
//     * @param postId Идентификатор сообщения
//     * @param name   Название тега
//     */
//    void add(Long postId, String name);
//
//    /**
//     * Удаление тега
//     *
//     * @param id    Идентификатор тега
//     */
//    void del(Long id);
//
//    /**
//     * Получение всех тегов сообщения
//     *
//     * @param postId
//     * @return
//     */
//    Collection<Tag> findAllByPostId(Long postId);
}
