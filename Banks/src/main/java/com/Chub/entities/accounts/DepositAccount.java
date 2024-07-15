package com.Chub.entities.accounts;

import lombok.Setter;

import java.time.LocalDate;

/**
 * Represents a deposit account.
 * This class extends the {@link AccountBase} abstract class.
 */
public class DepositAccount extends AccountBase {

    private double _percent;
    private double _monthBalance;

    @Setter
    private double _coefficient;

    @Setter
    private double _amountStep;

    /**
     * Initializes a new instance of the DepositAccount class with specified parameters.
     *
     * @param balance the initial balance of the account
     * @param date the start date of the account
     * @param suspicious indicates if the account is suspicious
     * @param limit the operation limit for suspicious accounts
     * @param id the unique identifier of the account
     */
    public DepositAccount(int balance, LocalDate date, boolean suspicious, Integer limit, int id) {
        super(balance, date, suspicious, limit, id);
    }

    /**
     * Calculates the percentage for the account.
     */
    public void CountPercent() {
        _percent = (_balance / _amountStep * _coefficient + _coefficient) % 1;
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