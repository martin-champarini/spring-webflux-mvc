package com.demo.bootstrap;

import com.demo.model.Category;
import com.demo.model.Vendor;
import com.demo.repository.CategoryRepository;
import com.demo.repository.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    private final VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (categoryRepository.count().block() == 0) {
            //load data
            System.out.println("## LOADING DATA ON BOOTSTRAP ##");

            categoryRepository.save(Category.builder().description("Fruits").build()).block();
            categoryRepository.save(Category.builder().description("Eggs").build()).block();
            categoryRepository.save(Category.builder().description("Breads").build()).block();
            categoryRepository.save(Category.builder().description("Meats").build()).block();

            System.out.println("Loaded Categories: " + categoryRepository.count().block());

            vendorRepository.save(Vendor.builder().firstName("Joe").lastName("Buck").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Michael").lastName("Buck").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Kile").lastName("Blank").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Mia").lastName("Loudvek").build()).block();
            vendorRepository.save(Vendor.builder().firstName("Hanna").lastName("Simpson").build()).block();

            System.out.println("Loaded Vendors: " + vendorRepository.count().block());


        }

    }
}
