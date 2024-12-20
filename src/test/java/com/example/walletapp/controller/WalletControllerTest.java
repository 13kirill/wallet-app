package com.example.walletapp.controller;

import com.example.walletapp.entity.Wallet;
import com.example.walletapp.repository.WalletRepository;
import com.example.walletapp.service.WalletService;
import com.example.walletapp.exception.InsufficientFundsException;
import com.example.walletapp.exception.WalletNotFoundException;
import com.example.walletapp.exception.UnsupportedOperationTypeException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
@Testcontainers
class WalletControllerTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
        .withDatabaseName("walletdb")
        .withUsername("wallet-user")
        .withPassword("password");

    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WalletRepository walletRepository;

    @Mock
    private WalletService walletService;

    @InjectMocks
    private WalletController walletController;

    @Test
    void createWallet_success() throws Exception {
        mockMvc.perform(post("/api/v1/wallets/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\": 1000.0}"))
            .andExpect(status().isCreated())  // Ожидаем 201 Created
            .andExpect(jsonPath("$.balance").value(1000.0));
    }

    @Test
    void getWalletBalance_success() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setBalance(500.0);
        wallet = walletRepository.save(wallet);

        mockMvc.perform(get("/api/v1/wallets/" + wallet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().string("500.0"));
    }

    @Test
    void updateWallet_withdraw_success() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setBalance(1000.0);
        wallet = walletRepository.save(wallet);

        String walletId = wallet.getId().toString();

        mockMvc.perform(post("/api/v1/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"walletId\": \"" + walletId + "\", \"operationType\": \"WITHDRAW\", \"amount\": 500.0}"))
            .andExpect(status().isOk())
            .andExpect(content().string("Withdrawal successful"));
    }

    @Test
    void updateWallet_walletNotFound() throws Exception {
        String walletId = UUID.randomUUID().toString();

        mockMvc.perform(post("/api/v1/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"walletId\": \"" + walletId + "\", \"operationType\": \"WITHDRAW\", \"amount\": 500.0}"))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Wallet not found: " + walletId));
    }

    @Test
    void updateWallet_insufficientFunds() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setBalance(100.0);  // Баланс меньше 500
        wallet = walletRepository.save(wallet);

        String walletId = wallet.getId().toString();

        mockMvc.perform(post("/api/v1/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"walletId\": \"" + walletId + "\", \"operationType\": \"WITHDRAW\", \"amount\": 500.0}"))
            .andExpect(status().isBadRequest())
            .andExpect(content().string(org.hamcrest.Matchers.containsString("Insufficient funds")));
    }

    @Test
    void updateWallet_noOperationType() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setBalance(1000.0);
        wallet = walletRepository.save(wallet);

        String walletId = wallet.getId().toString();

        mockMvc.perform(post("/api/v1/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"walletId\": \"" + walletId + "\", \"amount\": 500.0}"))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Operation type must be provided"));
    }

    @Test
    void updateWallet_deposit_success() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setBalance(1000.0);
        wallet = walletRepository.save(wallet);

        String walletId = wallet.getId().toString();

        mockMvc.perform(post("/api/v1/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"walletId\": \"" + walletId + "\", \"operationType\": \"DEPOSIT\", \"amount\": 500.0}"))
            .andExpect(status().isOk())
            .andExpect(content().string("Deposit successful"));
    }

    @Test
    void getWalletBalance_walletNotFound() throws Exception {
        String walletId = UUID.randomUUID().toString();

        mockMvc.perform(get("/api/v1/wallets/" + walletId))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Wallet not found: " + walletId));
    }

    @Test
    void updateWallet_unsupportedOperationType() throws Exception {
        Wallet wallet = new Wallet();
        wallet.setBalance(1000.0);
        wallet = walletRepository.save(wallet);

        String walletId = wallet.getId().toString();

        // Настроим мока для выбрасывания UnsupportedOperationTypeException
        doThrow(new UnsupportedOperationTypeException("Unsupported operation type: TRANSFER"))
            .when(walletService).processWalletOperation(any());

        // Отправляем запрос с неподдерживаемым типом операции (например, "TRANSFER")
        mockMvc.perform(post("/api/v1/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"walletId\": \"" + walletId + "\", \"operationType\": \"TRANSFER\", \"amount\": 500.0}"))
            .andExpect(status().isBadRequest())  // Ожидаем ошибку 400
            .andExpect(content().string("Unsupported operation type: TRANSFER"));  // Проверка, что сообщение о неподдерживаемой операции
    }
}
