package ru.yandex.practicum.myblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.myblog.model.Post;

import java.util.Set;

/**
 * Интерфейс репозитория для работы с сообщениями
 */
public interface PostRepository extends JpaRepository<Post, Long> {
    /**
     * Выборка всех сообщений, имеющих заданный тег
     * @param tag Тег для поиска
     * @return Набор сообщений
     */
    Set<Post> findAllByTags_Name(String tag);
}
