package ru.yandex.practicum.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.myblog.configuration.TestConfig;
import ru.yandex.practicum.myblog.dto.CommentDTO;
import ru.yandex.practicum.myblog.dto.InPostDTO;
import ru.yandex.practicum.myblog.dto.OutPostDTO;
import ru.yandex.practicum.myblog.model.Comment;
import ru.yandex.practicum.myblog.model.Post;
import ru.yandex.practicum.myblog.model.Tag;
import ru.yandex.practicum.myblog.repository.CommentRepository;
import ru.yandex.practicum.myblog.repository.PostRepository;
import ru.yandex.practicum.myblog.repository.TagRepository;

import org.mockito.Mockito;
import ru.yandex.practicum.myblog.service.impl.PostServiceImpl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class PostServiceImplTest {

    private TagRepository tagRepository;

    private CommentRepository commentRepository;

    private PostRepository postRepository;

    private PostService postService;

    private final Post postOne = new Post(1L, "Test title 1", "Test text 1", "test1".getBytes(), 100500);
    private final Post postTwo = new Post(2L, "Test title 2", "Test text 2", "test2".getBytes(), 200500);
    private final Post postThree = new Post(3L, "Test title 3", "Test text 3", "test3".getBytes(), 300500);

    private final InPostDTO inPostDTO = InPostDTO.builder()
            .title("Test title 1")
            .text("Test text 1")
            .image(new MockMultipartFile("image", "test1".getBytes()))
            .tags("tag1 mem1")
            .build();

    private final OutPostDTO outPostDTO = OutPostDTO.builder()
            .id(1)
            .title("Test title 1")
            .text("Test text 1")
            .likesCount(100500)
            .comments(List.of(CommentDTO.builder().id(1).text("Test comment 1").build()))
            .tags(List.of("tag1", "mem11"))
            .build();

    private final Tag tag1 = new Tag(1, 1, "tag1");
    private final Tag tag2 = new Tag(2, 1, "mem1");

    private final Comment comment1 = new Comment(1, 1, "Test comment 1", LocalDateTime.now());

    @BeforeEach
    void setUp() {
        commentRepository = mock(CommentRepository.class);
        tagRepository = mock(TagRepository.class);
        postRepository = mock(PostRepository.class);

        Mockito.when(postRepository.add(any())).thenReturn(10L);
        Mockito.when(postRepository.findAll()).thenReturn(List.of(postOne, postTwo, postThree));
        Mockito.when(postRepository.findById(1L)).thenReturn(postOne);
        Mockito.when(postRepository.findById(99L)).thenReturn(null);
        Mockito.when(postRepository.findAllByTag("mem1")).thenReturn(List.of(postOne, postTwo));

        Mockito.when(tagRepository.findAllByPostId(any())).thenReturn(List.of(tag1, tag2));

        Mockito.when(commentRepository.findAllByPostId(1L)).thenReturn(List.of(comment1));

        postService = new PostServiceImpl(postRepository, commentRepository, tagRepository);
    }

    @Test
    void add() throws IOException {
        //Идентификатор нового сообщения
        assertEquals(10, postService.add(inPostDTO));
    }

    @Test
    void getById() {
        OutPostDTO post = postService.getById(1L);

        //Должно вернуться сообщение
        assertNotNull(post);

        //Проверка списка тегов
        assertEquals("tag1 mem1", post.getTagsAsText());

        //Проверка, что при выборке присоединились комментарии
        assertEquals(1, post.getComments().size());

        //Проверка заголовка
        assertEquals(outPostDTO.getTitle(), post.getTitle());

        //Проверка текста сообщения
        assertEquals(outPostDTO.getText(), post.getText());
    }

    @Test
    void getAll() {
        Collection<OutPostDTO> posts = postService.getAll();

        //Должны быть возвращены все три предполагаемые записи
        assertEquals(3, posts.size());
    }

    @Test
    void findAllByTag() {
        Collection<OutPostDTO> posts1 = postService.findAllByTag("mem1");
        Collection<OutPostDTO> posts2 = postService.findAllByTag("");

        //Предполагается возвращение 2-х записей
        assertEquals(2, posts1.size());

        //При пустом фильтре должны возвращаться все строки (3)
        assertEquals(3, posts2.size());
    }

    @Test
    void getAllCommentsByPostId() {
        Collection<Comment> posts = postService.getAllCommentsByPostId(1L);
        //Предполагается, что у сообщения только один комментарий
        assertEquals(1, posts.size());
    }
}