package ru.yandex.practicum.myblog.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.myblog.dto.CommentDTO;
import ru.yandex.practicum.myblog.dto.InPostDTO;
import ru.yandex.practicum.myblog.dto.OutPostDTO;
import ru.yandex.practicum.myblog.mapper.CommentMapper;
import ru.yandex.practicum.myblog.mapper.PostMapper;
import ru.yandex.practicum.myblog.mapper.TagMapper;
import ru.yandex.practicum.myblog.model.Comment;
import ru.yandex.practicum.myblog.model.Post;
import ru.yandex.practicum.myblog.model.Tag;
import ru.yandex.practicum.myblog.repository.CommentRepository;
import ru.yandex.practicum.myblog.repository.PostRepository;
import ru.yandex.practicum.myblog.repository.TagRepository;
import ru.yandex.practicum.myblog.service.PostService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Сервис для работы с сообщениями
 */

@Slf4j
@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepo;
    private final CommentRepository commRepo;
    private final TagRepository tagRepo;

    public PostServiceImpl(PostRepository postRepo, CommentRepository commRepo, TagRepository tagRepo) {
        this.postRepo = postRepo;
        this.commRepo = commRepo;
        this.tagRepo = tagRepo;
    }

    /**
     * Добавление нового Сообщения
     *
     * @param newPost  Сообщения для вставки в базу
     * @return      Идентификатор нового сообщения
     */

    @Override
    @Transactional
    public OutPostDTO add(InPostDTO newPost) throws IOException {
        Post post = PostMapper.toPost(newPost);
        post = postRepo.save(post);

        for(String tag : newPost.getTags().split(" "))
            tagRepo.save(TagMapper.toTag(post, tag));

        OutPostDTO ret = PostMapper.toPostDTO(post,
                commRepo.findAllByPostId(post.getId()).stream().map(CommentMapper::toCommentDTO).toList(),
                tagRepo.findAllByPostId(post.getId()).stream().map(Tag::getName).toList());

        return ret;

    }

    /**
     * Редактирование сообщения
     *
     * @param inPost Сообщения для редактирования
     */
    @Override
    @Transactional
    public void edit(InPostDTO inPost) throws IOException {
        Post post = PostMapper.toPost(inPost);
        post = postRepo.save(post);

        for(String tag : inPost.getTags().split(" ")) {
            if (!tagRepo.existsByPostIdAndName(post.getId(), tag)){
                tagRepo.save(TagMapper.toTag(post, tag));
            }
        }
    }

    /**
     * Удаление сообщения
     */
    @Override
    @Transactional
    public void del(Long id) {
        postRepo.deleteById(id);
    }

    /**
     * Получение сообщения по его идентификатору
     *
     * @param id Идентификатор сообщения
     */
    @Override
    public OutPostDTO getById(Long id) {
        Post post = postRepo.getReferenceById(id);

        return PostMapper.toPostDTO(post,
                commRepo.findAllByPostId(post.getId())
                        .stream()
                        .map(CommentMapper::toCommentDTO)
                        .toList(),
                tagRepo.findAllByPostId(post.getId())
                       .stream()
                       .map(Tag::getName)
                       .toList());
    }

    /**
     * Получение всех сообщений
     *
     * @return Набор постов
     */
    @Override
    public Collection<OutPostDTO> getAll() {
        List<OutPostDTO> ret = new ArrayList<>();

        for(var post : postRepo.findAll())
            ret.add(PostMapper.toPostDTO(post,
                        commRepo.findAllByPostId(post.getId()).stream().map(CommentMapper::toCommentDTO).toList(),
                        tagRepo.findAllByPostId(post.getId()).stream().map(Tag::getName).toList()
                    )
            );
        return ret;
    }

    /**
     * Получение постов с заданным тагом
     * @param tag Наименование тага
     * @return  Набор постов
     */
    @Override
    public Collection<OutPostDTO> findAllByTag(String tag) {
        List<OutPostDTO> ret = new ArrayList<>();
        Collection<Post> posts;

        if (tag == null || tag.isBlank() || tag.isEmpty())
            posts = postRepo.findAll();
        else
            posts = postRepo.findAllByTags_Name(tag);

        for(var post : posts)
            ret.add(PostMapper.toPostDTO(post,
                            commRepo.findAllByPostId(post.getId()).stream().map(CommentMapper::toCommentDTO).toList(),
                            tagRepo.findAllByPostId(post.getId()).stream().map(Tag::getName).toList()
                    )
            );
        return ret;
    }

    /**
     * Получение сообщений с фильтрацией по тагу и пагинацией
     *
     * @param search    Строка поиска
     * @param pageable  Пагинация
     * @return  Страница сообщений
     */
    @Override
    public Page<OutPostDTO> findAllByTagPaginated(String search, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<OutPostDTO> list;
        List<OutPostDTO> posts = findAllByTag(search).stream().toList();


        if (posts.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, posts.size());
            list = posts.subList(startItem, toIndex);
        }

        Page<OutPostDTO> postsPage
                = new PageImpl<OutPostDTO>(list, PageRequest.of(currentPage, pageSize), posts.size());

        return postsPage;
    }

    /**
     * Добавление лайка сообщению
     *
     * @param id Идентификатор сообщения
     */
    @Override
    @Transactional
    public void incLike(Long id) {
        Post post = postRepo.getReferenceById(id);

        post.setLikesCount(post.getLikesCount() + 1);

        postRepo.save(post);
    }

    /**
     * Дизлайк Сообщению
     */
    @Override
    @Transactional
    public void decLike(Long id) {
        Post post = postRepo.getReferenceById(id);

        post.setLikesCount(post.getLikesCount() - 1);

        postRepo.save(post);
    }

    /**
     * Добавление нового комментария к сообщению
     * @param id  Идентификатор сообщения
     * @param text    Текст комментария
     */
    @Override
    public void addComment(Long id, String text) {
        Post post = postRepo.getReferenceById(id);

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setText(text);

        commRepo.save(comment);
    }

    /**
     * Изменение существующего комментария
     * @param commentId Идентификатор комментария
     * @param text      Новый текст сообщения
     */
    @Override
    public void editComment(Long commentId, String text) {
        Comment comment =commRepo.getReferenceById(commentId);
        comment.setText(text);

        commRepo.save(comment);
    }

    /**
     * Удаление комментария
     * @param commentId Идентификатор комментария
     */
    @Override
    public void delComment(Long commentId) {
        commRepo.deleteById(commentId);
    }

    @Override
    public Collection<Comment> getAllCommentsByPostId(Long postId) {
        return commRepo.findAllByPostId(postId);
    }

    /**
     * Получение картинки Сообщения
     *
     * @param id  Идентификатор сообщения
     * @return
     */
    @Override
    public byte[] getImage(Long id) {
        Post post = postRepo.getReferenceById(id);
        return post.getImage();
    }

}
