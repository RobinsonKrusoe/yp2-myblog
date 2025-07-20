package ru.yandex.practicum.myblog.repository;

import ru.yandex.practicum.myblog.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

/**
 * Интерфейс репозитория для работы с тегами сообщения
 */
public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * Выборка всех тегов сообщения
     * @param postId Идентификатор сообщения
     * @return Набор тегов сообщения
     */
    Set<Tag> findAllByPostId(Long postId);

    /**
     * Проверка наличия определённого тега у сообщения
     * @param postId Идентификатор сообщения
     * @param name   Имя тега
     * @return флаг наличия
     */
    boolean existsByPostIdAndName(Long postId, String name);
}
