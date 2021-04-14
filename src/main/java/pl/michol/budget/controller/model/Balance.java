package pl.michol.budget.controller.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class Balance {

    @NotEmpty
    private String key;
    @NotNull
    private BigDecimal value;

    public Balance() {
    }

    public Balance(final String key, final BigDecimal value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
