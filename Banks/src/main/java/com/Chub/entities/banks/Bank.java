package com.Chub.entities.banks;

import com.Chub.entities.accounts.AccountBase;
import com.Chub.entities.accounts.CreditAccount;
import com.Chub.entities.accounts.DebitAccount;
import com.Chub.entities.accounts.DepositAccount;
import com.Chub.entities.accounts.transactions.ITransaction;
import com.Chub.entities.clients.Client;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Represents a bank entity implementing the IBank interface.
 */
public class Bank implements IBank {

    private final Map<Client, ArrayList<AccountBase>> _accounts = new HashMap<>();

    /**
     * The debit percentage used for accounts.
     */
    @Getter
    @Setter
    private double _debitPercent;

    /**
     * The commission rate used for credit accounts.
     */
    @Getter
    @Setter
    private double _commission;

    /**
     * The credit limit used for credit accounts.
     */
    @Getter
    @Setter
    private double _limit;

    /**
     * The coefficient used for calculating deposit percentages.
     */
    @Getter
    @Setter
    private double _coefficient;

    /**
     * The step amount used for calculating deposit percentages.
     */
    @Getter
    @Setter
    private double _stepAmount;

    /**
     * The operation limit for transactions.
     */
    @Getter
    private final Integer _operationLimit;

    private final ArrayList<Client> notificationDebitSubscribers = new ArrayList<>();
    private final ArrayList<Client> notificationDepositSubscribers = new ArrayList<>();
    private final ArrayList<Client> notificationCreditSubscribers = new ArrayList<>();

    /**
     * Initializes a new instance of the Bank class with specified parameters.
     *
     * @param name the name of the bank
     * @param operationLimit the operation limit for transactions
     */
    public Bank(String name, Integer operationLimit) {
        Name = name;
        _operationLimit = operationLimit;
    }

    /**
     * The name of the bank.
     */
    @Getter
    public String Name;

    /**
     * Registers a new client in the bank.
     *
     * @param client the client to be registered
     */
    public void ClientRegistration(Client client) {
        _accounts.put(client, new ArrayList<>());
    }

    /**
     * Creates a new account for the specified client.
     *
     * @param client the client for whom the account is created
     * @param account the account to be created
     */
    public void CreateAccount(Client client, AccountBase account) {
        if (FindAccountById(account.Id) == null) {
            _accounts.get(client).add(account);
            if (account instanceof DebitAccount) {
                ChangeDebitPercent(_debitPercent);
            }
            if (account instanceof DepositAccount acc) {
                ChangeDepositPercent(_coefficient, _stepAmount);
                acc.CountPercent();
            }
            if (account instanceof CreditAccount) {
                ChangeCreditCommission(_commission);
                ChangeCreditLimit(_limit);
            }
        }
    }

    /**
     * Subscribes a client to receive debit notifications.
     *
     * @param client the client to be subscribed
     */
    public void SubscribeDebit(Client client) {
        notificationDebitSubscribers.add(client);
    }

    /**
     * Subscribes a client to receive deposit notifications.
     *
     * @param client the client to be subscribed
     */
    public void SubscribeDeposit(Client client) {
        notificationDepositSubscribers.add(client);
    }

    /**
     * Subscribes a client to receive credit notifications.
     *
     * @param client the client to be subscribed
     */
    public void SubscribeCredit(Client client) {
        notificationCreditSubscribers.add(client);
    }

    /**
     * Unsubscribes a client from debit notifications.
     *
     * @param client the client to be unsubscribed
     */
    public void UnsubscribeDebit(Client client) {
        notificationDebitSubscribers.remove(client);
    }

    /**
     * Unsubscribes a client from deposit notifications.
     *
     * @param client the client to be unsubscribed
     */
    public void UnsubscribeDeposit(Client client) {
        notificationDepositSubscribers.remove(client);
    }

    /**
     * Unsubscribes a client from credit notifications.
     *
     * @param client the client to be unsubscribed
     */
    public void UnsubscribeCredit(Client client) {
        notificationCreditSubscribers.remove(client);
    }

    /**
     * Reverts a transaction.
     *
     * @param transaction the transaction to be reverted
     */
    public void UndoTransaction(ITransaction transaction) {
        transaction.Undo();
    }

    /**
     * Charges percentages for all accounts.
     */
    public void ChargePercentages() {
        for (Client client : _accounts.keySet()) {
            ArrayList<AccountBase> accounts = _accounts.get(client);
            accounts.forEach(AccountBase::ChargePercentages);
        }
    }

    /**
     * Changes the debit percentage for all debit accounts.
     *
     * @param newPercent the new debit percentage
     */
    public void ChangeDebitPercent(double newPercent) {
        _debitPercent = newPercent;
        for (Client client : _accounts.keySet()) {
            ArrayList<AccountBase> accounts = _accounts.get(client);
            for (AccountBase account : accounts) {
                if (account instanceof DebitAccount acc) {
                    acc.set_percent(_debitPercent);
                }
            }
        }
        PercentNotifyClients();
    }

    /**
     * Changes the deposit percentage for all deposit accounts.
     *
     * @param newCoefficient the new coefficient for calculating deposit percentages
     * @param newAmountStep the new step amount for calculating deposit percentages
     */
    public void ChangeDepositPercent(double newCoefficient, double newAmountStep) {
        _coefficient = newCoefficient;
        _stepAmount = newAmountStep;
        for (Client client : _accounts.keySet()) {
            ArrayList<AccountBase> accounts = _accounts.get(client);
            for (AccountBase account : accounts) {
                if (account instanceof DepositAccount acc) {
                    acc.set_coefficient(newCoefficient);
                    acc.set_amountStep(newAmountStep);
                    acc.CountPercent();
                }
            }
        }
        DepositNotifyClients();
    }

    /**
     * Changes the credit limit for all credit accounts.
     *
     * @param newLimit the new credit limit
     */
    public void ChangeCreditLimit(double newLimit) {
        _limit = newLimit;
        for (Client client : _accounts.keySet()) {
            ArrayList<AccountBase> accounts = _accounts.get(client);
            for (AccountBase account : accounts) {
                if (account instanceof CreditAccount acc) {
                    acc.set_limit(_limit);
                }
            }
        }
        LimitNotifyClients();
    }

    /**
     * Changes the commission rate for all credit accounts.
     *
     * @param newCommission the new commission rate
     */
    public void ChangeCreditCommission(double newCommission) {
        _commission = newCommission;
        for (Client client : _accounts.keySet()) {
            ArrayList<AccountBase> accounts = _accounts.get(client);
            for (AccountBase account : accounts) {
                if (account instanceof CreditAccount acc) {
                    acc.set_commission(_commission);
                }
            }
        }
        LimitNotifyClients();
    }

    /**
     * Notifies clients about changes in debit percentage.
     */
    public void PercentNotifyClients() {
        for (Client subscriber : notificationDebitSubscribers) {
            subscriber.Notify("percent change: " + _debitPercent);
        }
    }

    /**
     * Notifies clients about changes in deposit conditions.
     */
    public void DepositNotifyClients() {
        for (Client subscriber : notificationDepositSubscribers) {
            subscriber.Notify("deposit conditions change");
        }
    }

    /**
     * Notifies clients about changes in credit limit.
     */
    public void LimitNotifyClients() {
        for (Client subscriber : notificationCreditSubscribers) {
            subscriber.Notify("limit change: " + _limit);
        }
    }

    /**
     * Finds an account by its unique identifier.
     *
     * @param id the unique identifier of the account to find
     * @return the account with the specified identifier, or null if not found
     */
    public AccountBase FindAccountById(int id) {
        for (Client client : _accounts.keySet()) {
            ArrayList<AccountBase> accounts = _accounts.get(client);
            Optional<AccountBase> result = accounts.stream().filter(x -> x.Id == id).findFirst();
            if (result.isPresent()) {
                return result.get();
            }
        }
        return null;
    }

    /**
     * Finds a client by their name and surname.
     *
     * @param name the name of the client
     * @param surname the surname of the client
     * @return the client with the specified name and surname, or null if not found
     */
    public Client FindClient(String name, String surname) {
        for (Client client : _accounts.keySet()) {
            if (client.Name.equals(name) && client.Surname.equals(surname)) {
                return client;
            }
        }
        return null;
    }

    /**
     * Returns a list of accounts belonging to the specified client.
     *
     * @param client the client whose accounts to retrieve
     * @return the list of accounts belonging to the client
     */
    public ArrayList<AccountBase> ReturnClientAccounts(Client client) {
        return _accounts.get(client);
    }

    /**
     * Changes the status of all accounts belonging to the specified client to non-suspicious.
     *
     * @param client the client whose account status to change
     */
    public void ChangeAccountStatus(Client client) {
        if (!client.Status()) {
            for (int i = 0; i < _accounts.get(client).size(); ++i) {
                _accounts.get(client).get(i).RemoveSuspicious();
            }
        }
    }
}
