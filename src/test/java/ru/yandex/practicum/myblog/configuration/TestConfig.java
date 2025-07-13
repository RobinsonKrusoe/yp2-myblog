package ru.yandex.practicum.myblog.configuration;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.yandex.practicum.myblog.repository.TagRepository;
import ru.yandex.practicum.myblog.repository.CommentRepository;
import ru.yandex.practicum.myblog.repository.PostRepository;


@Configuration
@ComponentScan("ru.yandex.practicum.myblog.service")
public class TestConfig {

    @Bean
    @Primary
    public PostRepository mockOrderRepository() {
        return Mockito.mock(PostRepository.class);
    }

    @Bean
    @Primary
    public CommentRepository mockCommentRepository() {
        return Mockito.mock(CommentRepository.class);
    }

    @Bean
    @Primary
    public TagRepository mockTagRepository() {
        return Mockito.mock(TagRepository.class);
    }
}
