package com.Chub.entities.accounts;

import lombok.Setter;

import java.time.LocalDate;

/**
 * Represents a debit account.
 * This class extends  the {@link AccountBase} abstract class.
 */
public class DebitAccount extends AccountBase {

    @Setter
    private double _percent;

    private double _monthBalance;

    /**
     * Initializes a new instance of the DebitAccount class with specified parameters.
     *
     * @param balance the initial balance of the account
     * @param date the start date of the account
     * @param suspicious indicates if the account is suspicious
     * @param limit the operation limit for suspicious accounts
     * @param id the unique identifier of the account
     */
    public DebitAccount(int balance, LocalDate date, boolean suspicious, Integer limit, int id) {
        super(balance, date, suspicious, limit, id);
    }

    /**
     * Calculates the day's percentage count for the account.
     */
    @Override
    public void DayPercentCount() {
        _monthBalance += _balance * _percent / 365;
    }

    /**
     * Charges the accumulated percentages for the day to the account.
     */
    @Override
    public void ChargePercentages() {
        _balance += _monthBalance;
        _monthBalance = 0;
    }
}
