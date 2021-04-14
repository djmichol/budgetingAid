package pl.michol.budget.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.michol.budget.controller.model.Balance;
import pl.michol.budget.controller.model.TransferBalance;
import pl.michol.budget.service.RegisterService;

import javax.validation.Valid;
import java.util.List;


@RequestMapping("/api/budget")
@RestController
@CrossOrigin(origins = "*")
public class BudgetController implements BudgetApi{

    private final RegisterService registerService;

    public BudgetController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @Override
    @PostMapping(value = "/recharge")
    public ResponseEntity<Balance> recharge(@RequestBody @Valid Balance balance) {
        return ResponseEntity.ok(registerService.recharge(balance));
    }

    @Override
    @PostMapping(value = "/transfer")
    public ResponseEntity<List<Balance>> transfer(@RequestBody @Valid TransferBalance transferBalance) {
        return ResponseEntity.ok(registerService.transfer(transferBalance));
    }

    @Override
    @GetMapping(value = "/balances", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Balance>> balances() {
        return ResponseEntity.ok(registerService.getAll());
    }
}
