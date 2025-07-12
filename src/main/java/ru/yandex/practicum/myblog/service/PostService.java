package ru.yandex.practicum.myblog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.myblog.dto.InPostDTO;
import ru.yandex.practicum.myblog.dto.OutPostDTO;
import ru.yandex.practicum.myblog.model.Comment;

import java.io.IOException;
import java.util.Collection;

/**
 * Интерфейс для работы с сообщениями
 */
public interface PostService {

    /**
     * Добавление нового Сообщения
     *
     * @param newPost  Сообщения для вставки в базу
     * @return          Идентификатор нового сообщения
     */
    long add(InPostDTO newPost) throws IOException;

    /**
     * Редактирование сообщения
     *
     * @param post Сообщения для редактирования
     */
    void edit(InPostDTO post) throws IOException;

    /**
     * Удаление сообщения
     */
    void del(Long id);

    /**
     * Получение сообщения
     */
    OutPostDTO getById(Long id);

    /**
     * Получение всех сообщений
     * @return  Набор постов
     */
    Collection<OutPostDTO> getAll();

    /**
     * Получение постов с заданным тагом
     * @param tag Наименование тага
     * @return  Набор постов
     */
     Collection<OutPostDTO> findAllByTag(String tag);


    /**
     * Получение сообщений с фильтрацией по тагу и пагинацией
     *
     * @param search    Строка поиска
     * @param pageable  Пагинация
     * @return  Страница сообщений
     */
    Page<OutPostDTO> findAllByTagPaginated(String search, Pageable pageable);

    /**
     * Добавление лайка сообщению
     */
    void incLike(Long id);

    /**
     * Дизлайк Сообщению
     */
    void decLike(Long id);

    /**
     * Добавление нового комментария к сообщению
     * @param id  Идентификатор сообщения
     * @param text    Текст комментария
     */
    void addComment(Long id, String text);

    /**
     * Изменение существующего комментария
     * @param commentId Идентификатор комментария
     * @param text      Новый текст сообщения
     */
    void editComment(Long commentId, String text);

    /**
     * Удаление комментария
     * @param commentId Идентификатор комментария
     */
    void delComment(Long commentId);

    /**
     * Получение всех комментариев на сообщение
     * @param postId    Идентификатор сообщения
     * @return
     */
    Collection<Comment> getAllCommentsByPostId(Long postId);

    /**
     * Получение картинки Сообщения
     *
     * @param id  Идентификатор сообщения
     * @return
     */
    byte[] getImage(Long id);

}
