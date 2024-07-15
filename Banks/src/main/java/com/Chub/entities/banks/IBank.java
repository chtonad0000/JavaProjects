package com.Chub.entities.banks;

import com.Chub.entities.accounts.AccountBase;
import com.Chub.entities.accounts.transactions.ITransaction;
import com.Chub.entities.clients.Client;

import java.util.ArrayList;

public interface IBank {
    public String getName();

    public void ClientRegistration(Client client);

    public void UndoTransaction(ITransaction transaction);

    public void ChargePercentages();

    public Client FindClient(String name, String surname);

    public ArrayList<AccountBase> ReturnClientAccounts(Client client);

    public void ChangeAccountStatus(Client client);

    public void SubscribeDebit(Client client);

    public void SubscribeDeposit(Client client);

    public void SubscribeCredit(Client client);

    public void UnsubscribeDebit(Client client);

    public void UnsubscribeDeposit(Client client);

    public void UnsubscribeCredit(Client client);

    public AccountBase FindAccountById(int id);

    public void CreateAccount(Client client, AccountBase account);

    public Integer get_operationLimit();

    public void ChangeDebitPercent(double newPercent);

    public void ChangeDepositPercent(double newCoefficient, double newAmountStep);

    public void ChangeCreditLimit(double newLimit);

    public void ChangeCreditCommission(double newCommission);

    public double get_debitPercent();

    public double get_commission();

    public double get_limit();

    public double get_coefficient();

    public double get_stepAmount();
}
