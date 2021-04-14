package pl.michol.budget.data;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pl.michol.budget.repository.RegisterRepository;
import pl.michol.budget.repository.entity.RegisterEntity;

import java.math.BigDecimal;

@Component
public class DataLoader implements ApplicationRunner {

    private final RegisterRepository registerRepository;

    public DataLoader(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    public void run(ApplicationArguments args) {
        registerRepository.save(new RegisterEntity("Wallet", new BigDecimal(1000)));
        registerRepository.save(new RegisterEntity("Savings", new BigDecimal(5000)));
        registerRepository.save(new RegisterEntity("Insurance policy",  BigDecimal.ZERO));
        registerRepository.save(new RegisterEntity("Food expenses", BigDecimal.ZERO));
    }
}
