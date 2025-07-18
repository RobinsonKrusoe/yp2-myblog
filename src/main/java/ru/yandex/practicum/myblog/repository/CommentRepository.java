package ru.yandex.practicum.myblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.myblog.model.Comment;

import java.util.Set;
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Set<Comment> findAllByPostId(Long id);
//
//    /**
//     * Добавление нового комментария к сообщению
//     * @param postId  Идентификатор сообщения
//     * @param text    Текст комментария
//     */
//    void add(Long postId, String text);
//
//    /**
//     * Изменение существующего комментария
//     * @param commentId Идентификатор комментария
//     * @param text      Новый текст сообщения
//     */
//    void edit(Long commentId, String text);
//
//    /**
//     * Удаление комментария
//     * @param commentId Идентификатор комментария
//     */
//    void del(Long commentId);
//
//    /**
//     * Получение всех комментариев на сообщение
//     * @param postId    Идентификатор сообщения
//     * @return          Список комментарии
//     */
//    Collection<Comment> findAllByPostId(Long postId);
}
