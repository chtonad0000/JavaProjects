package com.Chub.services.scenarios.account;

import com.Chub.entities.accounts.AccountBase;
import com.Chub.entities.banks.IBank;
import com.Chub.entities.accounts.transactions.TransferTransaction;
import com.Chub.services.scenarios.IScenario;

import java.util.Scanner;

/**
 * Represents a scenario for transferring money between accounts, implementing the {@link IScenario} interface.
 */
public class TransferMoneyScenario implements IScenario {

    /**
     * The account from which money will be transferred.
     */
    private final AccountBase _account;

    /**
     * The bank where the transfer will occur.
     */
    private final IBank _bank;

    /**
     * Constructs a new TransferMoneyScenario with the specified account and bank.
     *
     * @param account the account from which money will be transferred
     * @param bank the bank where the transfer will occur
     */
    public TransferMoneyScenario(AccountBase account, IBank bank) {
        _account = account;
        _bank = bank;
    }

    /**
     * Runs the scenario, allowing the user to input the recipient account number and the amount of money to transfer.
     *
     * @throws Exception if an error occurs while running the scenario
     */
    @Override
    public void Run() throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter account number:");
        int number = input.nextInt();
        System.out.println("Enter amount of money:");
        double amount = input.nextDouble();
        TransferTransaction transaction = new TransferTransaction(_account, _bank.FindAccountById(number), amount);
        transaction.Do();
        _account.SaveTransaction(transaction);
    }
}
