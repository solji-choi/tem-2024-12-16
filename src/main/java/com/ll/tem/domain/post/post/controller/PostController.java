package com.ll.tem.domain.post.post.controller;

import com.ll.tem.domain.post.post.entity.Post;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/posts")
public class PostController {
    private List<Post> posts = new ArrayList<>() {{
        add(
                Post.builder()
                        .title("제목1")
                        .content("내용1")
                        .build()
        );

        add(
                Post.builder()
                        .title("제목2")
                        .content("내용2")
                        .build()
        );

        add(
                Post.builder()
                        .title("제목3")
                        .content("내용3")
                        .build()
        );
    }};

    @GetMapping
    public String showList(Model model) {
        model.addAttribute("posts", posts.reversed());

        return "domain/post/post/list";
    }


    @GetMapping("/{id}")
    public String showDetail(Model model, @PathVariable long id) {
        Post post = posts
                .stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow();

        model.addAttribute("post", post);

        return "domain/post/post/detail";
    }


    @GetMapping("/write")
    public String showWrite(
            PostWriteForm form
    ) {
        return "domain/post/post/write";
    }

    private record PostWriteForm(
            @NotBlank(message = "01-제목을 입력해주세요.")
            @Length(min = 5, message = "02-제목을 5자 이상 입력해주세요.")
            String title,
            @NotBlank(message = "03-내용을 입력해주세요.")
            @Length(min = 10, message = "04-내용을 10자 이상 입력해주세요.")
            String content
    ) {
    }

    @PostMapping("/write")
    public String write(
            @Valid PostWriteForm form,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "domain/post/post/write";
        }

        posts.add(
                Post.builder()
                        .title(form.title)
                        .content(form.content)
                        .build()
        );

        return "redirect:/posts";
    }
}
