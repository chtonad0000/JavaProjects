package com.Chub.services.scenarios.account;

import com.Chub.entities.accounts.AccountBase;
import com.Chub.entities.accounts.transactions.WithdrawMoneyTransaction;
import com.Chub.services.scenarios.IScenario;

import java.util.Scanner;

/**
 * Represents a scenario for withdrawing money from an account, implementing the {@link IScenario} interface.
 */
public class WithdrawMoneyScenario implements IScenario {

    /**
     * The account from which money will be withdrawn.
     */
    private final AccountBase _account;

    /**
     * Constructs a new WithdrawMoneyScenario with the specified account.
     *
     * @param account the account from which money will be withdrawn
     */
    public WithdrawMoneyScenario(AccountBase account) {
        _account = account;
    }

    /**
     * Runs the scenario, allowing the user to input the amount of money to withdraw.
     *
     * @throws Exception if an error occurs while running the scenario
     */
    @Override
    public void Run() throws Exception {
        System.out.println("Enter amount of money:");
        Scanner input = new Scanner(System.in);
        double amount = input.nextDouble();
        WithdrawMoneyTransaction transaction = new WithdrawMoneyTransaction(_account, amount);
        transaction.Do();
        _account.SaveTransaction(transaction);
    }
}
