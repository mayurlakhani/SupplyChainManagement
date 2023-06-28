package com.microservices.inventoryservice;

import com.microservices.inventoryservice.dto.InventoryResponse;
import com.microservices.inventoryservice.model.Inventory;
import com.microservices.inventoryservice.repository.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
@Slf4j
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner  commandLineRunner(InventoryRepository inventoryRepository){
        return args -> {
            Inventory inventory = new Inventory();
            inventory.setSkuCode("iphone-13");
            inventory.setQuantity(10);

            Inventory inventory1 = new Inventory();
            inventory1.setSkuCode("iphone-13-red");
            inventory1.setQuantity(0);
          /*  List<String> l = new ArrayList<>();
            l.add("iphone-13-red");*/
            inventoryRepository.save(inventory);
            inventoryRepository.save(inventory1);
            log.info("_____find all record of inventory______");
           /* List<Inventory> inv = inventoryRepository.findBySkuCodeIn(l);
            log.info(String.valueOf(inv));*/

        };
    }
}
