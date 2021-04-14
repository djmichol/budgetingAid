package pl.michol.budget.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.michol.budget.controller.BudgetController;
import pl.michol.budget.controller.model.Balance;
import pl.michol.budget.controller.model.TransferBalance;
import pl.michol.budget.repository.RegisterRepository;
import pl.michol.budget.repository.entity.RegisterEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
class BudgetApplicationTests {

    @Autowired
    BudgetController budgetController;
    @Autowired
    RegisterRepository registerRepository;

    @Test
    void contextLoads() {
        assertNotNull(budgetController);
    }

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setUp(){
        registerRepository.deleteAll();
    }

    @Test
    public void getBalancesShouldReturnList() throws Exception {
        registerRepository.save(new RegisterEntity("Wallet", new BigDecimal(1000)));

        Balance[] forObject = this.restTemplate.getForObject("http://localhost:" + port + "/api/budget/balances",
                Balance[].class);

        assertNotNull(forObject);
        assertEquals(1, forObject.length);
        assertEquals("Wallet", forObject[0].getKey());
        assertEquals(new BigDecimal(1000).setScale(2), forObject[0].getValue());
    }

    @Test
    public void rechargeRegisterNotFound() {
        Balance bal = new Balance("test", BigDecimal.TEN);

        ResponseEntity<Balance> responseEntity = this.restTemplate.postForEntity("http://localhost:" + port + "/api/budget/recharge",
                bal,
                Balance.class);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void transferRegisterNotFound() {
        TransferBalance transferBalance = new TransferBalance();
        transferBalance.setFrom("test");
        transferBalance.setTo("test");
        transferBalance.setValue(BigDecimal.TEN);

        ResponseEntity<Object> responseEntity = this.restTemplate.postForEntity("http://localhost:" + port + "/api/budget/transfer",
                transferBalance,
                Object.class);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void transferRegisterValidationException() {
        TransferBalance transferBalance = new TransferBalance();


        ResponseEntity<Object> responseEntity = this.restTemplate.postForEntity("http://localhost:" + port + "/api/budget/transfer",
                transferBalance,
                Object.class);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}
