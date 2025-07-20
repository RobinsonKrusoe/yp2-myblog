package ru.yandex.practicum.myblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.myblog.model.Comment;

import java.util.Set;

/**
 * Интерфейс репозитория для работы с комментариями сообщения
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Выбрать все комментарии сообщения
     * @param postId Идентификатор сообщения
     * @return Набор комментариев
     */
    Set<Comment> findAllByPostId(Long postId);
}
