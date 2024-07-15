package com.Chub.services.scenarios;

import com.Chub.entities.accounts.AccountBase;
import com.Chub.entities.banks.IBank;
import com.Chub.entities.clients.Client;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents a scenario for choosing an account to manage, implementing the {@link IScenario} interface.
 */
public class ChooseAccountScenario implements IScenario {

    /**
     * The list of accounts to choose from.
     */
    private final ArrayList<AccountBase> _accounts;

    /**
     * The client associated with the accounts.
     */
    private final Client _client;

    /**
     * The bank associated with the accounts.
     */
    private final IBank _bank;

    /**
     * Constructs a new ChooseAccountScenario with the specified list of accounts, client, and bank.
     *
     * @param accounts the list of accounts to choose from
     * @param client the client associated with the accounts
     * @param bank the bank associated with the accounts
     */
    public ChooseAccountScenario(ArrayList<AccountBase> accounts, Client client, IBank bank) {
        _accounts = accounts;
        _client = client;
        _bank = bank;
    }

    /**
     * Runs the scenario, allowing the user to choose an account to manage.
     *
     * @throws Exception if an error occurs while running the scenario
     */
    @Override
    public void Run() throws Exception {
        System.out.println("Choose one from these accounts");
        System.out.println("-------------------------");
        for (int i = 1; i <= _accounts.size(); ++i) {
            System.out.println(i + ": " + _accounts.get(i - 1).Id);
        }
        Scanner input = new Scanner(System.in);
        int selection = input.nextInt();
        AccountBase resultAccount = _accounts.get(selection - 1);
        new AccountScenario(resultAccount, _bank, _client).Run();
    }
}
