package com.Chub.services.scenarios;

import com.Chub.entities.banks.IBank;
import com.Chub.entities.accounts.CreditAccount;
import com.Chub.entities.accounts.DebitAccount;
import com.Chub.entities.accounts.DepositAccount;
import com.Chub.entities.clients.Client;

import java.time.LocalDate;
import java.util.Scanner;

/**
 * Represents a scenario for registering a new account for a client, implementing the {@link IScenario} interface.
 */
public class AccountRegistrationScenario implements IScenario {

    /**
     * The client for whom the account is being registered.
     */
    private final Client _client;

    /**
     * The bank where the account will be registered.
     */
    private final IBank _bank;

    /**
     * Constructs a new AccountRegistrationScenario with the specified client and bank.
     *
     * @param client the client for whom the account is being registered
     * @param bank the bank where the account will be registered
     */
    public AccountRegistrationScenario(Client client, IBank bank) {
        _client = client;
        _bank = bank;
    }

    /**
     * Runs the scenario, allowing the user to select the type of account and enter the account ID.
     */
    @Override
    public void Run() {
        System.out.println("Enter type of account:");
        System.out.println("1: Debit");
        System.out.println("2: Deposit");
        System.out.println("3: Credit");
        Scanner input = new Scanner(System.in);
        int selection = input.nextInt();
        System.out.println("Enter id");
        int id = input.nextInt();
        if (_bank.FindAccountById(id) != null) {
            System.out.println("Try another id, this is busy");
            Run();
        } else {
            switch (selection) {
                case 1:
                    boolean suspicious = false;
                    Integer limit = null;
                    if (_client.Status()) {
                        suspicious = true;
                        limit = _bank.get_operationLimit();
                    }
                    _bank.CreateAccount(_client, new DebitAccount(0, LocalDate.now(), suspicious, limit, id));
                    break;
                case 2:
                    suspicious = false;
                    limit = null;
                    if (_client.Status()) {
                        suspicious = true;
                        limit = _bank.get_operationLimit();
                    }
                    _bank.CreateAccount(_client, new DepositAccount(0, LocalDate.now(), suspicious, limit, id));
                    break;
                case 3:
                    suspicious = false;
                    limit = null;
                    if (_client.Status()) {
                        suspicious = true;
                        limit = _bank.get_operationLimit();
                    }
                    _bank.CreateAccount(_client, new CreditAccount(0, LocalDate.now(), suspicious, limit, id));
                    break;
            }
        }
    }
}
