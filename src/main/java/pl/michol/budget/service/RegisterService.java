package pl.michol.budget.service;

import org.springframework.stereotype.Service;
import pl.michol.budget.controller.model.Balance;
import pl.michol.budget.controller.model.TransferBalance;
import pl.michol.budget.converter.RegisterEntityToRegisterConverter;
import pl.michol.budget.repository.RegisterRepository;
import pl.michol.budget.repository.entity.RegisterEntity;
import pl.michol.budget.service.exception.RegisterNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegisterService implements RegisterInterface {

    private final RegisterRepository registerRepository;
    private final RegisterEntityToRegisterConverter registerEntityToRegisterConverter;

    public RegisterService(final RegisterRepository registerRepository, final RegisterEntityToRegisterConverter registerEntityToRegisterConverter) {
        this.registerRepository = registerRepository;
        this.registerEntityToRegisterConverter = registerEntityToRegisterConverter;
    }

    @Override
    public Balance recharge(Balance balance) {
        RegisterEntity byRegisterName = registerRepository.findByRegisterName(balance.getKey());
        validateRegister(byRegisterName, balance.getKey());
        byRegisterName.setValue(byRegisterName.getValue().add(balance.getValue()));
        return registerEntityToRegisterConverter.convert(registerRepository.save(byRegisterName));
    }

    @Override
    public List<Balance> transfer(TransferBalance transferBalance) {
        RegisterEntity from = registerRepository.findByRegisterName(transferBalance.getFrom());
        validateRegister(from, transferBalance.getFrom());
        RegisterEntity to = registerRepository.findByRegisterName(transferBalance.getTo());
        validateRegister(to, transferBalance.getTo());
        from.setValue(from.getValue().subtract(transferBalance.getValue()));
        to.setValue(to.getValue().add(transferBalance.getValue()));
        registerRepository.save(from);
        registerRepository.save(to);
        return Arrays.asList(
                registerEntityToRegisterConverter.convert(from),
                registerEntityToRegisterConverter.convert(to)
        );
    }

    private void validateRegister(RegisterEntity registerEntity, String registerName) {
        if(registerEntity == null){
            throw new RegisterNotFoundException(registerName + " register not found");
        }
    }

    @Override
    public List<Balance> getAll() {
        return registerRepository.findAll().stream()
                .map(registerEntityToRegisterConverter::convert)
                .collect(Collectors.toList());
    }
}
