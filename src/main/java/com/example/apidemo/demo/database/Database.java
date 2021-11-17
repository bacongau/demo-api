package com.example.apidemo.demo.database;

import com.example.apidemo.demo.models.Product;
import com.example.apidemo.demo.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.crypto.Data;


/*
docker run -d --rm --name mysql-spring-boot-tutorial -e MYSQL_ROOT_PASSWORD=123456 -e MYSQL_USER=cuongtt -e MYSQL_PASSWORD=123456 -e MYSQL_DATABASE=test_db -p 3309:3306 --volume mysql-spring-boot-tutorial-volume:/var/lib/mysql mysql:latest

mysql -h localhost -P 3309 --protocol=tcp -u cuongtt -p

 */

@Configuration
public class Database {
    //logger
    private static final Logger logger = LoggerFactory.getLogger(Database.class);

    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Product p1 = new Product("iporn", 2020, 200000.0, "");
                Product p2 = new Product("ibass", 2021, 400000.0, "");
                Product p3 = new Product("ipot", 2019, 40000.0, "");
                logger.info("Insert data: " + productRepository.save(p1));
                logger.info("Insert data: " + productRepository.save(p2));
                logger.info("Insert data: " + productRepository.save(p3));
            }
        };
    }
}
