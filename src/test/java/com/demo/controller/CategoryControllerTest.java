package com.demo.controller;

import com.demo.model.Category;
import com.demo.repository.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

public class CategoryControllerTest {

    WebTestClient webTestClient;
    CategoryRepository categoryRepository;
    CategoryController categoryController;

    @Before
    public void setUp() throws Exception {
        categoryRepository = Mockito.mock(CategoryRepository.class);
        categoryController = new CategoryController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    public void list() {
        BDDMockito.given(categoryRepository.findAll())
                .willReturn(Flux.just(
                        Category.builder().description("cate1").build(),
                        Category.builder().description("cate2").build()
                        ));
        webTestClient.get()
                .uri("/api/v1/categories")
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);

    }

    @Test
    public void getById() {

        BDDMockito.given(categoryRepository.findById("someid"))
                .willReturn(
                        Mono.just(
                                Category.builder().description("Cat").build()
                        ));
        webTestClient.get()
                .uri("/api/v1/categories/someid")
                .exchange()
                .expectBody(Category.class);

    }

    @Test
    public void createCategoryTest() {

        BDDMockito.given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(
                        Category.builder().build()
                ));
        Mono<Category> categoryMono = Mono.just(
          Category.builder().description("cat").build()
        );

        webTestClient.post()
                .uri("/api/v1/categories")
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus()
                .isCreated();

    }

    @Test
    public void update() {
        BDDMockito.given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));
        Mono<Category> categoryMono = Mono.just(Category.builder().description("somecat").build());

        webTestClient.put()
                .uri("/api/v1/categories/asdasd")
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void patchWithChange() {
        BDDMockito.given(categoryRepository.findById(anyString())).willReturn(
                Mono.just(Category.builder().build())
        );

        BDDMockito.given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));
        Mono<Category> categoryMono = Mono.just(Category.builder().description("somecat").build());

        webTestClient.patch()
                .uri("/api/v1/categories/asdasd")
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        Mockito.verify(categoryRepository).save(any());
    }

    @Test
    public void patchWithNoChanges() {
        BDDMockito.given(categoryRepository.findById(anyString())).willReturn(
                Mono.just(Category.builder().build())
        );

        BDDMockito.given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));
        Mono<Category> categoryMono = Mono.just(Category.builder().build());

        webTestClient.patch()
                .uri("/api/v1/categories/asdasd")
                .body(categoryMono, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        Mockito.verify(categoryRepository, Mockito.never()).save(any());

    }
}