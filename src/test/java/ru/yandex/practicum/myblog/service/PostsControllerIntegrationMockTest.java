package ru.yandex.practicum.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.myblog.dto.InPostDTO;
import ru.yandex.practicum.myblog.dto.OutPostDTO;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest         //The @SpringBootTest annotation is used to create a test environment by loading a FULL application context
@ActiveProfiles("test") //Без указания профиля тесты пишут/читают основную базу
@AutoConfigureMockMvc
public class PostsControllerIntegrationMockTest {
    @Autowired
    private MockMvc mockMvc;

    private final InPostDTO inPostDTO = InPostDTO.builder()
            .title("Test title 1")
            .text("Test text 1")
            .image(new MockMultipartFile("image", "test1".getBytes()))
            .tags("tag1 mem1")
            .build();

    @Autowired
    private PostService postServ;

    @Test
    void testGetRootPath() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
    }

    @Test
    void testGetAddPage() throws Exception {
        mockMvc.perform(get("/posts/add"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("add-post"));
    }

    @Test
    void testGetPostsPage() throws Exception {
        //Сохранение тестового сообщения в базу
        OutPostDTO outPostDTO = postServ.add(inPostDTO);

        //Вызов сохранённой сущности из базы через контроллер
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("posts"))
                .andExpect(xpath("//table/tr/td").nodeCount(2))
                .andExpect(xpath("//table/tr[2]/td/h2").string(inPostDTO.getTitle()))
                .andExpect(xpath("//table/tr[2]/td/p[2]").string(inPostDTO.getText()))
                .andExpect(xpath("//table/tr[2]/td/p[4]").string(containsString(inPostDTO.getTags().split(" ")[0])))
                .andExpect(xpath("//table/tr[2]/td/p[4]").string(containsString(inPostDTO.getTags().split(" ")[1])));
    }

    @Test
    void testGetPostPageById() throws Exception {
        //Сохранение тестового сообщения в базу
        OutPostDTO outPostDTO = postServ.add(inPostDTO);

        //Вызов сохранённой сущности из базы через контроллер
        mockMvc.perform(get("/posts/"+ outPostDTO.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"))
                .andExpect(xpath("//table/tr[2]/td/h2").string(inPostDTO.getTitle()))
                .andExpect(xpath("//table/tr[3]/td").string(inPostDTO.getText()));

    }

    @Test
    void testDeletePost() throws Exception {
        //Сохранение тестового сообщения в базу
        OutPostDTO outPostDTO = postServ.add(inPostDTO);

        mockMvc.perform(post("/posts/" + outPostDTO.getId() + "/delete"))
                .andExpect(view().name("redirect:/posts"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        //Попытка обратиться к удалённой записи должна вызывать исключение
        assertThrows(Exception.class, () ->postServ.getById(outPostDTO.getId()));
    }
}
