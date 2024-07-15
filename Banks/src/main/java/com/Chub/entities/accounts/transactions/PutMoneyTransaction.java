package com.Chub.entities.accounts.transactions;

import com.Chub.entities.accounts.AccountBase;
import lombok.RequiredArgsConstructor;

/**
 * Represents a transaction for putting money into an account.
 * This class implements the ITransaction interface.
 */
@RequiredArgsConstructor
public class PutMoneyTransaction implements ITransaction {
    private final AccountBase _account;
    private final double _amount;

    /**
     * The status of the transaction.
     */
    public TransactionStatus Status = TransactionStatus.NoApproved;

    /**
     * Executes the transaction by putting money into the account.
     *
     * @throws IllegalArgumentException if the amount is negative
     */
    public void Do() {
        _account.PutMoney(_amount);
        Status = TransactionStatus.Approved;
    }

    /**
     * Reverts the transaction by withdrawing the previously added money from the account.
     *
     * @throws IllegalArgumentException if the amount is negative
     */
    public void Undo() {
        _account.WithdrawMoney(_amount);
        Status = TransactionStatus.Cancelled;
    }
}

