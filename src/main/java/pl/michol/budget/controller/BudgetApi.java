package pl.michol.budget.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import pl.michol.budget.controller.model.Balance;
import pl.michol.budget.controller.model.TransferBalance;

import java.math.BigDecimal;
import java.util.List;

@Api(value = "Budet management", tags = {"Budet management"})
public interface BudgetApi {

    @ApiOperation(value = "Recharge", notes = "Recharge an existing register with given amount")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful update of register."),
            @ApiResponse(code = 404, message = "Register not found.")
    })
    ResponseEntity<Balance> recharge(Balance balance);

    @ApiOperation(value = "Transfer", notes = "Transfer given amount between two existing registers")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful transfer."),
            @ApiResponse(code = 404, message = "Register not found.")
    })
    ResponseEntity<List<Balance>> transfer(TransferBalance transferBalance);

    @ApiOperation(value = "balances", notes = "Get current balance of all registers")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrival of balances list.")
    })
    ResponseEntity<List<Balance>> balances();

}