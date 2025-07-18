package ru.yandex.practicum.myblog.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.yandex.practicum.myblog.dto.InPostDTO;
import ru.yandex.practicum.myblog.dto.OutPostDTO;
import ru.yandex.practicum.myblog.model.Paging;
import ru.yandex.practicum.myblog.service.PostService;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/")
public class PostsController {

    private final PostService postServ;

    public PostsController(PostService postServ) {
        this.postServ = postServ;
    }

    /**
     * а) GET "/" - редирект на "/posts"
     * @return - Шаблон posts
     */
    @GetMapping("/")
    public ModelAndView posts() {
        return new ModelAndView("redirect:/posts");
    }

    /**
     * 	б) GET "posts" - список постов на странице ленты постов
     * 		Параметры:
     * @param search - строка с поиском по тегу поста (по умолчанию, пустая строка - все посты)
     * @param pageSize - максимальное число постов на странице (по умолчанию, 10)
     * @param pageNumber - номер текущей страницы (по умолчанию, 1)
     *             	Возвращает:
     * @return шаблон "posts.html"
     *             		используется модель для заполнения шаблона:
     *             			"posts" - List<Post> - список постов (id, title, text, imagePath, likesCount, comments)
     *             			"search" - строка поиска (по умолчанию, пустая строка - все посты)
     *             			"paging":
     *             				"pageNumber" - номер текущей страницы (по умолчанию, 1)
     *             				"pageSize" - максимальное число постов на странице (по умолчанию, 10)
     *             				"hasNext" - можно ли пролистнуть вперед
     *             				"hasPrevious" - можно ли пролистнуть назад
     *
     *
     */
    @GetMapping("/posts")
    public ModelAndView getPostsPage(@RequestParam(name = "search", required = false) String search,
                                     @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                     @RequestParam(name = "pageNumber", required = false, defaultValue = "1") Integer pageNumber) {
        log.info("Get getPostsPage.");

        Page<OutPostDTO> page = postServ.findAllByTagPaginated(search,
                PageRequest.of(pageNumber - 1, pageSize));

        ModelAndView mv = new ModelAndView("posts");

        mv.addObject ("posts", page.getContent());
        mv.addObject ("search", search);
        mv.addObject ("paging", new Paging(page.getPageable().getPageNumber() + 1,
                page.getPageable().getPageSize(),
                page.hasNext(),
                page.hasPrevious()));
        return mv;
    }

    /**
     *   в) GET "/posts/{id}" - страница с постом
     * @param id Идентификатор Сообщения
     *   	Возвращает:
     * @return 		шаблон "post.html"
     *  			используется модель для заполнения шаблона:
     *  				"post" - модель поста (id, title, text, imagePath, likesCount, comments)
     */
    @GetMapping("/posts/{id}")
    public ModelAndView getPostPageById(@PathVariable("id") Long id) {
        log.info("Get getPostPageById id={}", id);

        OutPostDTO post = postServ.getById(id);

        ModelAndView mv = new ModelAndView("post");
        mv.addObject ("post", post);
        return mv;
    }

    /**
     * г) GET "/posts/add" - страница добавления поста
     * 	Возвращает:
     * @return шаблон "add-post.html"
     */
    @GetMapping("/posts/add")
    public ModelAndView getAddPage() {
        log.info("Get getAddPage");

        return new ModelAndView("add-post");
    }

    /**
     * д) POST "/posts" - добавление поста
     * 	Принимает:
     * 		"multipart/form-data"
     * 	Параметры:
     * @param title	 - название поста
     * @param text	 - текст поста
     * @param image  - файл картинки поста (класс MultipartFile)
     * @param tags	 - список тегов поста (по умолчанию, пустая строка)
     * 	Возвращает:
     * @return редирект на созданный "/posts/{id}"
     * @throws IOException
     */
    @PostMapping(path = "/posts", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE })
    public ModelAndView addPost(@RequestParam("title") String title,
                                @RequestParam("text") String text,
                                @RequestParam("image") MultipartFile image,
                                @RequestParam(name = "tags", required = false) String tags
    ) throws IOException {
        log.info("Post addPost title={}, text={}, tags={}", title, text, tags);

        OutPostDTO newPost = postServ.add(InPostDTO.builder()
                .title(title)
                .text(text)
                .tags(tags)
                .image(image)
                .build());

        return new ModelAndView("redirect:/posts/"+ newPost.getId());
    }

    /**
     * е) GET "/images/{id}" -эндпоинт, возвращающий набор байт картинки поста
     * 	Параметры:
     * @param id - идентификатор поста
     * @return Массив байт картинки
     */
    @GetMapping("/images/{id}")
    public void getImage(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        log.info("Get getImage id={}", id);

        byte[] imgBytes = postServ.getImage(id);
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
        response.getOutputStream().write(imgBytes);
        response.getOutputStream().close();
    }

    /**
     * ж) POST "/posts/{id}/like" - увеличение/уменьшение числа лайков поста
     * 	Параметры:
     * @param id    - идентификатор поста
     * @param like  - если true, то +1 лайк, если "false", то -1 лайк
     * 	Возвращает:
     * @return		редирект на "/posts/{id}"
     */
    @PostMapping("/posts/{id}/like")
    public ModelAndView likePost(@PathVariable("id") Long id,
                                 @RequestParam("like") String like) {
        log.info("Post likePost id={}, like={}", id, like);
        if (Boolean.parseBoolean(like)) {
            postServ.incLike(id);
        } else {
            postServ.decLike(id);
        }

        return new ModelAndView("redirect:/posts/" + id);
    }

    /**
     * з) POST "/posts/{id}/edit" - страница редактирования поста
     * 	Параметры:
     * @param id - идентификатор поста
     * 	Возвращает:
     * @return		редирект на форму редактирования поста "add-post.html"
     * 		        используется модель для заполнения шаблона:
     * 			    "post" - модель поста (id, title, text, imagePath, likesCount, comments)
     */
//    @PostMapping("/posts/{id}/edit")
    @GetMapping("/posts/{id}/edit")
    public ModelAndView editPostPage(@PathVariable("id") Long id) {
        log.info("Post editPostPage id={}", id);

        OutPostDTO post = postServ.getById(id);

//        ModelAndView mv = new ModelAndView("redirect:/add-post");
        ModelAndView mv = new ModelAndView("add-post");
        mv.addObject ("post", post);
        return mv;
    }

    /**
     *  и) POST "/posts/{id}" - редактирование поста
     *  	Принимает:
     *  		"multipart/form-data"
     *  	Параметры:
     * @param id - идентификатор поста
     * @param title - название поста
     * @param text - текст поста
     * @param image - файл картинки поста (класс MultipartFile, может быть null - значит, остается прежним)
     * @param tags - список тегов поста (по умолчанию, пустая строка)
     *  	Возвращает:
     * @return редирект на отредактированный "/posts/{id}"
     */
    @PostMapping(path = "/posts/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE })
    public ModelAndView editPost(@PathVariable("id") Long id,
                                 @RequestParam("title") String title,
                                 @RequestParam("text") String text,
                                 @RequestParam("image") MultipartFile image,
                                 @RequestParam("tags") String tags) throws IOException {

        log.info("Post editPost title={}, text={}, tags={}", title, text, tags);

        postServ.edit(InPostDTO.builder()
                .id(id)
                .title(title)
                .text(text)
                .image(image)
                .tags(tags)
                .build());

        return new ModelAndView("redirect:/posts/" + id);
    }

    /**
     *  к) POST "/posts/{id}/comments" - эндпоинт добавления комментария к посту
     *  	Параметры:
     * @param id - идентификатор поста
     * @param text - текст комментария
     *  	Возвращает:
     * @return редирект на "/posts/{id}"
     */
    @PostMapping("/posts/{id}/comments")
    public ModelAndView addComment(@PathVariable("id") Long id,
                                   @RequestParam("text") String text) {
        log.info("Post addComment ={}", text);

        postServ.addComment(id, text);

        return new ModelAndView("redirect:/posts/" + id);
    }

    /**
     * л) POST "/posts/{id}/comments/{commentId}" - эндпоинт редактирования комментария
     * 	Параметры:
     * @param id - идентификатор поста
     * @param commentId - идентификатор комментария
     * @param text - текст комментария
     * 	Возвращает:
     * @return редирект на "/posts/{id}"
     */
    @PostMapping("/posts/{id}/comments/{commentId}")
    public ModelAndView editComment(@PathVariable("id") Long id,
                                    @PathVariable("commentId") Long commentId,
                                    @RequestParam("text") String text) {
        log.info("Post editComment id={}, text={}", id, text);

        postServ.editComment(commentId, text);

        return new ModelAndView("redirect:/posts/" + id);
    }

    /**
     * м) POST "/posts/{id}/comments/{commentId}/delete" - эндпоинт удаления комментария
     * 	Параметры:
     * @param id - идентификатор поста
     * @param commentId - идентификатор комментария
     * 	Возвращает:
     * @return редирект на "/posts/{id}"
     */
    @PostMapping("/posts/{id}/comments/{commentId}/delete")
    public ModelAndView deleteComment(@PathVariable("id") Long id,
                                      @PathVariable("commentId") Long commentId) {
        log.info("Post deleteComment id={}, commentId={} ", id, commentId);

        postServ.delComment(commentId);

        return new ModelAndView("redirect:/posts/" + id);
    }

    /**
     * н) POST "/posts/{id}/delete" - эндпоинт удаления поста
     * 	Параметры:
     * @param id - идентификатор поста
     * 	Возвращает:
     * @return редирект на "/posts"
     */
    @PostMapping("/posts/{id}/delete")
    public ModelAndView deletePost(@PathVariable("id") Long id) {
        log.info("Post deletePost id={}", id);

        postServ.del(id);

        return new ModelAndView("redirect:/posts");
    }
}
