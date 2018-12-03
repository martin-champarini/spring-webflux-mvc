package com.demo.controller;

import com.demo.model.Vendor;
import com.demo.repository.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public class VendorControllerTest {

    private VendorRepository vendorRepository;
    private VendorController vendorController;
    WebTestClient webTestClient;


    @Before
    public void setUp() throws Exception {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();

    }

    @Test
    public void list() {
        BDDMockito.given(vendorRepository.findAll())
                .willReturn(Flux.just(
                        Vendor.builder().firstName("firstname1").lastName("lastname1").build(),
                        Vendor.builder().firstName("firstname2").lastName("lastname2").build(),
                        Vendor.builder().firstName("firstname3").lastName("lastname3").build()
                ));
        webTestClient.get()
                .uri("/api/v1/vendors")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(3);
    }

    @Test
    public void findVendorById() {
        BDDMockito.given(vendorRepository.findById("someid"))
                .willReturn(Mono.just(
                        Vendor.builder().firstName("firstname").lastName("lastName").id("someid").build()
                ));

        webTestClient.get()
                .uri("/api/v1/vendors/someid")
                .exchange()
                .expectBodyList(Vendor.class);

    }
}