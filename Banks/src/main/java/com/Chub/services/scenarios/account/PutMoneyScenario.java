package com.Chub.services.scenarios.account;

import com.Chub.entities.accounts.AccountBase;
import com.Chub.entities.accounts.transactions.PutMoneyTransaction;
import com.Chub.services.scenarios.IScenario;

import java.util.Scanner;

/**
 * Represents a scenario for putting money into an account, implementing the {@link IScenario} interface.
 */
public class PutMoneyScenario implements IScenario {

    /**
     * The account to put money into.
     */
    private final AccountBase _account;

    /**
     * Constructs a new PutMoneyScenario with the specified account.
     *
     * @param account the account to put money into
     */
    public PutMoneyScenario(AccountBase account) {
        _account = account;
    }

    /**
     * Runs the scenario, allowing the user to input the amount of money to put into the account.
     *
     * @throws Exception if an error occurs while running the scenario
     */
    @Override
    public void Run() throws Exception {
        System.out.println("Enter amount of money:");
        Scanner input = new Scanner(System.in);
        double amount = input.nextDouble();
        PutMoneyTransaction transaction = new PutMoneyTransaction(_account, amount);
        transaction.Do();
        _account.SaveTransaction(transaction);
    }
}
