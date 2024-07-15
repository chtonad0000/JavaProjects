package com.Chub.entities.accounts.transactions;

import com.Chub.entities.accounts.AccountBase;
import com.Chub.entities.accounts.DepositAccount;
import lombok.RequiredArgsConstructor;

/**
 * Represents a transaction for withdrawing money from an account.
 * This class implements the ITransaction interface.
 */
@RequiredArgsConstructor
public class WithdrawMoneyTransaction implements ITransaction {
    private final AccountBase _account;
    private final double _amount;

    /**
     * The status of the transaction.
     */
    public TransactionStatus Status = TransactionStatus.NoApproved;

    /**
     * Executes the transaction by withdrawing money from the account.
     *
     * @throws Exception if the account is a DepositAccount
     * @throws IllegalArgumentException if the amount is negative
     */
    public void Do() throws Exception {
        if (_account instanceof DepositAccount) {
            throw new Exception("You can't withdraw money from a deposit account");
        }
        _account.WithdrawMoney(_amount);
        Status = TransactionStatus.Approved;
    }

    /**
     * Reverts the transaction by depositing the previously withdrawn money back into the account.
     *
     * @throws IllegalArgumentException if the amount is negative
     */
    public void Undo() {
        _account.PutMoney(_amount);
        Status = TransactionStatus.Cancelled;
    }
}