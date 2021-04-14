package pl.michol.budget.service;

import pl.michol.budget.controller.model.Balance;
import pl.michol.budget.controller.model.TransferBalance;

import java.util.List;

public interface RegisterInterface {

    Balance recharge(Balance balance);

    List<Balance> transfer(TransferBalance transferBalance);

    List<Balance> getAll();

}
