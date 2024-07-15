package com.Chub.services.scenarios;

import com.Chub.entities.accounts.AccountBase;
import com.Chub.entities.banks.IBank;
import com.Chub.entities.accounts.CreditAccount;
import com.Chub.entities.accounts.DebitAccount;
import com.Chub.entities.accounts.DepositAccount;
import com.Chub.entities.clients.Client;
import com.Chub.services.scenarios.account.PutMoneyScenario;
import com.Chub.services.scenarios.account.TransferMoneyScenario;
import com.Chub.services.scenarios.account.WithdrawMoneyScenario;

import java.util.Scanner;

/**
 * Represents a scenario for managing an account, implementing the {@link IScenario} interface.
 */
public class AccountScenario implements IScenario {

    /**
     * The account to manage.
     */
    private final AccountBase _account;

    /**
     * The bank associated with the account.
     */
    private final IBank _bank;

    /**
     * The client associated with the account.
     */
    private final Client _client;

    /**
     * Constructs a new AccountScenario with the specified account, bank, and client.
     *
     * @param account the account to manage
     * @param bank the bank associated with the account
     * @param client the client associated with the account
     */
    public AccountScenario(AccountBase account, IBank bank, Client client) {
        _account = account;
        _bank = bank;
        _client = client;
    }

    /**
     * Runs the scenario, allowing the user to choose actions to perform on the account.
     *
     * @throws Exception if an error occurs while running the scenario
     */
    @Override
    public void Run() throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.println("Choose one from these actions");
        System.out.println("-------------------------");
        System.out.println("Account balance:" + _account.GetBalance());
        System.out.println("1: Put money");
        System.out.println("2: Withdraw money");
        System.out.println("3: Transfer money");
        System.out.println("4: Subscribe for notify");
        System.out.println("5: Unsubscribe");
        System.out.println("6: Skip time");
        System.out.println("7: Exit");
        int selection = input.nextInt();
        switch (selection) {
            case 1:
                new PutMoneyScenario(_account).Run();
                break;
            case 2:
                new WithdrawMoneyScenario(_account).Run();
                break;
            case 3:
                new TransferMoneyScenario(_account, _bank).Run();
                break;
            case 4:
                if (_account instanceof DebitAccount) {
                    _bank.SubscribeDebit(_client);
                }
                if (_account instanceof DepositAccount) {
                    _bank.SubscribeDeposit(_client);
                }
                if (_account instanceof CreditAccount) {
                    _bank.SubscribeCredit(_client);
                }
                break;
            case 5:
                if (_account instanceof DebitAccount) {
                    _bank.UnsubscribeDebit(_client);
                }
                if (_account instanceof DepositAccount) {
                    _bank.UnsubscribeDeposit(_client);
                }
                if (_account instanceof CreditAccount) {
                    _bank.UnsubscribeCredit(_client);
                }
                break;
            case 6:
                new SkipTimeScenario(_account).Run();
                break;
        }
        if (selection != 7) {
            Run();
        }
    }
}
