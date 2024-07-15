package com.Chub.entities.accounts.transactions;

import com.Chub.entities.accounts.AccountBase;
import com.Chub.entities.accounts.DepositAccount;
import lombok.RequiredArgsConstructor;

/**
 * Represents a transaction for transferring money between two accounts.
 * This class implements the ITransaction interface.
 */
@RequiredArgsConstructor
public class TransferTransaction implements ITransaction {
    private final AccountBase _accountFirst;
    private final AccountBase _accountSecond;
    private final double _amount;

    /**
     * The status of the transaction.
     */
    public TransactionStatus Status = TransactionStatus.NoApproved;

    /**
     * Executes the transaction by transferring money from one account to another.
     *
     * @throws Exception if the first account is a DepositAccount
     * @throws IllegalArgumentException if the amount is negative
     */
    public void Do() throws Exception {
        if (_accountFirst instanceof DepositAccount) {
            throw new Exception("You can't transfer money from a deposit account");
        }
        _accountFirst.WithdrawMoney(_amount);
        _accountSecond.PutMoney(_amount);
        Status = TransactionStatus.Approved;
    }

    /**
     * Reverts the transaction by transferring money back from the second account to the first.
     *
     * @throws IllegalArgumentException if the amount is negative
     */
    public void Undo() {
        _accountSecond.WithdrawMoney(_amount);
        _accountFirst.PutMoney(_amount);
        Status = TransactionStatus.Cancelled;
    }
}
