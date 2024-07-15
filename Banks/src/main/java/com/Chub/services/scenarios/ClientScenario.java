package com.Chub.services.scenarios;

import com.Chub.entities.accounts.AccountBase;
import com.Chub.entities.banks.IBank;
import com.Chub.entities.clients.Client;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents a scenario for client actions, implementing the {@link IScenario} interface.
 */
public class ClientScenario implements IScenario {

    /**
     * The client associated with this scenario.
     */
    private final Client _client;

    /**
     * The bank where the client is performing actions.
     */
    private final IBank _bank;

    /**
     * Constructs a new ClientScenario with the specified client and bank.
     *
     * @param client the client associated with this scenario
     * @param bank   the bank where the client is performing actions
     */
    public ClientScenario(Client client, IBank bank) {
        _client = client;
        _bank = bank;
    }

    /**
     * Runs the scenario, allowing the client to perform various actions.
     */
    @Override
    public void Run() throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.println("Choose one from these actions");
        System.out.println("1: Choose account");
        System.out.println("2: AccountRegistration");
        System.out.println("3: Exit");
        ArrayList<AccountBase> accounts = _bank.ReturnClientAccounts(_client);
        if (_client.PassportNumber == null && _client.Address == null) {
            System.out.println("-------------------------");
            System.out.println("4: Add passport and address");
        }
        int selection = input.nextInt();
        if (selection == 4 && _client.PassportNumber == null && _client.Address == null) {
            new PassportAndAddressScenario(_client, _bank).Run();
            Run();
        }
        if (selection == 1) {
            new ChooseAccountScenario(accounts, _client, _bank).Run();
            Run();
        }
        if (selection == 2) {
            new AccountRegistrationScenario(_client, _bank).Run();
            Run();
        }
    }
}
