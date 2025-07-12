package ru.yandex.practicum.myblog.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.myblog.model.Comment;

import java.util.Collection;

@Repository
public class JdbcNativeCommentRepository implements CommentRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcNativeCommentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Добавление нового комментария к сообщению
     * @param postId  Идентификатор сообщения
     * @param text    Текст комментария
     */
    @Override
    public void add(Long postId, String text) {
        jdbcTemplate.update("insert into comments (post_id, text) values (?, ?)", postId, text);
    }

    /**
     * Изменение существующего комментария
     * @param commentId Идентификатор комментария
     * @param text      Новый текст сообщения
     */
    @Override
    public void edit(Long commentId, String text) {
        jdbcTemplate.update("update comments set text = ? WHERE id = ?", text, commentId);
    }

    /**
     * Удаление комментария
     * @param commentId Идентификатор комментария
     */
    @Override
    public void del(Long commentId) {
        jdbcTemplate.update("delete from comments where id = ?", commentId);

    }

    /**
     * Получение всех комментариев на сообщение
     * @param postId    Идентификатор сообщения
     * @return
     */
    @Override
    public Collection<Comment> findAllByPostId(Long postId) {
        return jdbcTemplate.query(
                "select id, text, created_at from comments where post_id = ?",
                new Object[]{postId},
                (rs, rowNum) ->
                        new Comment(
                                rs.getLong("id"),
                                postId,
                                rs.getString("text"),
                                rs.getTimestamp("created_at").toLocalDateTime()
                        )
        );
    }
}
