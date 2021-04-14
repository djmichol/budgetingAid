package pl.michol.budget.unit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.michol.budget.controller.model.Balance;
import pl.michol.budget.controller.model.TransferBalance;
import pl.michol.budget.converter.RegisterEntityToRegisterConverter;
import pl.michol.budget.repository.RegisterRepository;
import pl.michol.budget.repository.entity.RegisterEntity;
import pl.michol.budget.service.RegisterService;
import pl.michol.budget.service.exception.RegisterNotFoundException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RegisterServiceTest {

    private RegisterRepository registerRepository;
    private RegisterService registerService;

    private final static String WALLET = "wallet";

    @BeforeEach
    public void setUp() {
        RegisterEntity registerEntity = getRegisterEntity(WALLET, BigDecimal.TEN);
        registerRepository = mock(RegisterRepository.class);
        when(registerRepository.findByRegisterName(WALLET)).thenReturn(registerEntity);
        registerService = new RegisterService(registerRepository, new RegisterEntityToRegisterConverter());
    }

    private static RegisterEntity getRegisterEntity(String name, BigDecimal value) {
        RegisterEntity registerEntity = new RegisterEntity();
        registerEntity.setValue(value);
        registerEntity.setRegisterName(name);
        registerEntity.setId(1L);
        return registerEntity;
    }

    @Test
    public void rechargeSuccessfulTest() {
        Balance balance = new Balance(WALLET, BigDecimal.TEN);
        when(registerRepository.save(any())).thenReturn(getRegisterEntity(WALLET, new BigDecimal(20)));

        Balance recharge = registerService.recharge(balance);

        assertNotNull(recharge);
        assertEquals(WALLET, recharge.getKey());
        assertEquals(new BigDecimal(20), recharge.getValue());

        verify(registerRepository, times(1)).save(any());
    }

    @Test
    public void rechargeRegisterNotFoundTest() {
        Balance balance = new Balance("test", BigDecimal.TEN);

        assertThrows(RegisterNotFoundException.class, () -> {
            registerService.recharge(balance);
        });

        verify(registerRepository, times(0)).save(any());
    }

    @Test
    public void transferBalanceNotFoundFromTest() {
        TransferBalance transfer = new TransferBalance();
        transfer.setFrom("test");

        assertThrows(RegisterNotFoundException.class, () -> {
            registerService.transfer(transfer);
        });

        verify(registerRepository, times(0)).save(any());
        verify(registerRepository, times(1)).findByRegisterName(any());
    }

    @Test
    public void transferBalanceNotFoundToTest() {
        TransferBalance transfer = new TransferBalance();
        transfer.setFrom(WALLET);
        transfer.setTo("test");

        assertThrows(RegisterNotFoundException.class, () -> {
            registerService.transfer(transfer);
        });

        verify(registerRepository, times(0)).save(any());
        verify(registerRepository, times(2)).findByRegisterName(any());
    }

    @Test
    public void transferBalanceSuccess() {
        TransferBalance transfer = new TransferBalance();
        transfer.setFrom("from");
        transfer.setTo("to");
        transfer.setValue(BigDecimal.TEN);

        when(registerRepository.findByRegisterName("from")).thenReturn(getRegisterEntity("from", BigDecimal.TEN));
        when(registerRepository.findByRegisterName("to")).thenReturn(getRegisterEntity("to",BigDecimal.TEN));

        List<Balance> transferResult = registerService.transfer(transfer);

        verify(registerRepository, times(2)).save(any());

        assertEquals(2, transferResult.size());
        assertEquals(new BigDecimal(20), transferResult.stream().filter(e -> e.getKey().equals("to")).map(Balance::getValue).findFirst().get());
    }

    @Test
    public void getAllTest() {
        when(registerRepository.findAll())
                .thenReturn(Arrays.asList(getRegisterEntity(WALLET, BigDecimal.ONE), getRegisterEntity(WALLET, BigDecimal.TEN)));

        List<Balance> all = registerService.getAll();

        assertEquals(2, all.size());
        assertEquals(BigDecimal.ONE, all.get(0).getValue());
        assertEquals(BigDecimal.TEN, all.get(1).getValue());
    }
}
