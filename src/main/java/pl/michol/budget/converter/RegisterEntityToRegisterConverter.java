package pl.michol.budget.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import pl.michol.budget.controller.model.Balance;
import pl.michol.budget.repository.entity.RegisterEntity;

@Component
public class RegisterEntityToRegisterConverter implements Converter<RegisterEntity, Balance> {

    @Override
    public Balance convert(RegisterEntity source) {
        return new Balance(source.getRegisterName(), source.getValue());
    }
}
