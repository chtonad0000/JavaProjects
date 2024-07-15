package com.Chub.entities.accounts;

import lombok.Setter;

import java.time.LocalDate;

/**
 * Represents a credit account.
 * This class extends the {@link AccountBase} abstract class.
 */
public class CreditAccount extends AccountBase {

    @Setter
    private double _limit;

    @Setter
    private double _commission;

    /**
     * Initializes a new instance of the CreditAccount class with specified parameters.
     *
     * @param balance the initial balance of the account
     * @param date the start date of the account
     * @param suspicious indicates if the account is suspicious
     * @param limit the credit limit of the account
     * @param id the unique identifier of the account
     */
    public CreditAccount(int balance, LocalDate date, boolean suspicious, Integer limit, int id) {
        super(balance, date, suspicious, limit, id);
    }

    /**
     * Withdraws money from the credit account balance.
     *
     * @param amount the amount to be withdrawn
     */
    @Override
    public void WithdrawMoney(double amount) {
        if (_balance - amount - _commission < -_limit) {
            System.out.println("You can't break the limit");
        } else {
            _balance -= amount;
            if (_balance < 0) {
                _balance -= _commission;
            }
        }
    }
}