package com.example.walletapp.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.profiles.active=test")
@Testcontainers // Эта аннотация управляет жизненным циклом контейнера
public class DockerIntegrationTest {

    // Контейнер будет автоматически запущен перед выполнением тестов
    @Container
    private static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15")
        .withDatabaseName("walletdb")
        .withUsername("wallet-user")
        .withPassword("password");

    // Динамически настраиваем параметры подключения к базе данных
    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Test
    void testPostgresContainerStartup() {
        assertThat(postgresContainer.isRunning()).isTrue();
        assertThat(postgresContainer.getJdbcUrl()).isNotNull();

        // Ваши проверки
    }
}
