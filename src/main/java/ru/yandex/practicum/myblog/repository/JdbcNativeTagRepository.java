package ru.yandex.practicum.myblog.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.myblog.model.Comment;
import ru.yandex.practicum.myblog.model.Tag;

import java.util.Collection;
import java.util.List;

@Repository
public class JdbcNativeTagRepository implements TagRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcNativeTagRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Добавление тега сообщению
     *
     * @param postId Идентификатор сообщения
     * @param name   Название тега
     */
    @Override
    public void add(Long postId, String name) {
        jdbcTemplate.update("""
                                    merge into tags m
                                    using (select ? post_id, ? name) u
                                    on 	  (m.post_id = u.post_id and m.name = u.name)
                                    when not matched then
                                    insert (post_id, name)
                                    values (u.post_id, u.name);
                                    """, postId, name);
    }

    /**
     * Удаление тега
     *
     * @param id Идентификатор тега
     */
    @Override
    public void del(Long id) {
        jdbcTemplate.update("delete from tags where id = ?", id);
    }

    /**
     * Получение всех тегов сообщения
     *
     * @param postId
     * @return
     */
    @Override
    public Collection<Tag> findAllByPostId(Long postId) {
        return jdbcTemplate.query(
                "select id, name from tags where post_id = ?",
                new Object[]{postId},
                (rs, rowNum) ->
                        new Tag(
                                rs.getLong("id"),
                                postId,
                                rs.getString("name")
                        )
        );
    }
}
