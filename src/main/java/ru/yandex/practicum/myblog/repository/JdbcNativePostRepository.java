package ru.yandex.practicum.myblog.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.myblog.model.Post;

import java.util.Collection;

@Repository
public class JdbcNativePostRepository implements PostRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcNativePostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Добавить новое Сообщение
     *
     * @param post  Сообщение
     * @return      Идентификатор нового Сообщения
     */
    @Override
    public Long add(Post post) {
        return jdbcTemplate.queryForObject(
                "insert into posts (title, text, image) values (?, ?, ?) returning id",
                new Object[]{post.getTitle(), post.getText(), post.getImage()},
                (rs, rowNum) ->
                        rs.getLong("id"));

    }

    /**
     * Редактирование существующего Сообщения
     *
     * @param post  Сообщение
     * @return      Идентификатор Сообщения
     */
    @Override
    public Long edit(Post post) {
        if (post.getImage() == null || post.getImage().length == 0){
            jdbcTemplate.update("""
                update posts
                set title = coalesce(?, title),
                    text  = coalesce(?, text)
                where id = ?
                """,
                    post.getTitle(),
                    post.getText(),
                    post.getId());
            return post.getId();
        } else {
            jdbcTemplate.update("""
                update posts
                set title = coalesce(?, title),
                    text  = coalesce(?, text),
                    image = coalesce(?, image)
                where id = ?
                """,
                    post.getTitle(),
                    post.getText(),
                    post.getImage(),
                    post.getId());
            return post.getId();
        }
    }

    /**
     * Удаление Сообщения
     *
     * @param id    Идентификатор Сообщения
     */
    @Override
    public void del(Long id){
        jdbcTemplate.update("delete from posts where id = ?", id);
    }

    /**
     * Добавить лайк Сообщению
     *
     * @param id    Идентификатор Сообщения
     */
    @Override
    public void incLike(Long id) {
        jdbcTemplate.update("update posts set likes_count = likes_count + 1 WHERE id = ?",
                id);
    }

    /**
     * Отнять лайк у Сообщения
     *
     * @param id    Идентификатор Сообщения
     */
    @Override
    public void decLike(Long id) {
        jdbcTemplate.update("update posts set likes_count = likes_count - 1 WHERE id = ?",
                id);
    }

    /**
     * Получение Сообщения по идентификатору
     *
     * @param id    Идентификатор Сообщения
     * @return      Сообщение
     */
    @Override
    public Post findById(Long id) {
        return jdbcTemplate.queryForObject(
                "select id, title, text, image, likes_count from posts where id = ?",
                new Object[]{id},
                (rs, rowNum) -> new Post(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("text"),
                        rs.getBytes("image"),
                        rs.getInt("likes_count")
                ));
    }

    /**
     * Получение всех сообщений
     *
     * @return Список Сообщений
     */

    @Override
    public Collection<Post> findAll() {
        return jdbcTemplate.query(
                "select id, title, text, image, likes_count, created_at from posts order by created_at",
                (rs, rowNum) ->
                        new Post(
                                rs.getLong("id"),
                                rs.getString("title"),
                                rs.getString("text"),
                                rs.getBytes("image"),
                                rs.getInt("likes_count")
                        )
        );
    }

    /**
     * Получение постов с заданным тагом
     * @param tag Наименование тага
     * @return  Набор постов
     */
    @Override
    public Collection<Post> findAllByTag(String tag) {
        return jdbcTemplate.query(
                """
                select
                    id, title, text, image, likes_count
                from
                    posts
                where
                    id in (select
                               post_id
                           from
                               tags
                           where name = ?)
                order by created_at
                """,
                new Object[]{tag},
                (rs, rowNum) ->
                        new Post(
                                rs.getLong("id"),
                                rs.getString("title"),
                                rs.getString("text"),
                                rs.getBytes("image"),
                                rs.getInt("likes_count")
                        )
        );
    }
}
