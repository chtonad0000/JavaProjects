package com.Chub.entities.accounts;

import com.Chub.entities.accounts.transactions.ITransaction;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Represents a base class for different types of accounts.
 */
public abstract class AccountBase {
    protected double _balance;
    protected Integer _operationLimit;
    protected boolean _suspicious;
    private LocalDate _currentDate;

    /**
     * The start date of the account.
     */
    public LocalDate StartDate;

    /**
     * The unique identifier of the account.
     */
    public final int Id;

    /**
     * List of transactions associated with this account.
     */
    public ArrayList<ITransaction> TransactionHistory = new ArrayList<>();

    /**
     * Initializes a new instance of the AccountBase class with specified parameters.
     *
     * @param startBalance the initial balance of the account
     * @param startDate the start date of the account
     * @param suspicious indicates if the account is suspicious
     * @param operationLimit the operation limit for suspicious accounts
     * @param id the unique identifier of the account
     */
    public AccountBase(int startBalance, LocalDate startDate, boolean suspicious, Integer operationLimit, int id) {
        _balance = startBalance;
        StartDate = startDate;
        _suspicious = suspicious;
        _operationLimit = operationLimit;
        Id = id;
        _currentDate = startDate;
    }

    /**
     * Removes suspicious status and operation limit from the account.
     */
    public void RemoveSuspicious() {
        _suspicious = false;
        _operationLimit = null;
    }

    /**
     * Calculates the day's percentage count.
     */
    public void DayPercentCount() {
        // Implementation specific to account type
    }

    /**
     * Charges percentages for the day.
     */
    public void ChargePercentages() {
        // Implementation specific to account type
    }

    /**
     * Saves a transaction to the transaction history.
     *
     * @param transaction the transaction to be saved
     */
    public void SaveTransaction(ITransaction transaction) {
        TransactionHistory.add(transaction);
    }

    /**
     * Gets the current balance of the account.
     *
     * @return the current balance
     */
    public double GetBalance() {
        return _balance;
    }

    /**
     * Adds money to the account balance.
     *
     * @param amount the amount to be added
     */
    public void PutMoney(double amount) {
        if (_suspicious && amount > _operationLimit) {
            System.out.println("Amount is bigger than limit");
        } else {
            _balance += amount;
        }
    }

    /**
     * Withdraws money from the account balance.
     *
     * @param amount the amount to be withdrawn
     */
    public void WithdrawMoney(double amount) {
        if (_suspicious && amount > _operationLimit) {
            System.out.println("Amount is bigger than limit");
        } else {
            if (amount > _balance) {
                System.out.println("You don't have enough money");
            } else {
                _balance -= amount;
            }
        }
    }

    /**
     * Moves the simulation to a specific date.
     *
     * @param date the date to which the simulation will move
     */
    public void GoToDate(LocalDate date) {
        LocalDate simulationDate = _currentDate;
        while (!simulationDate.isAfter(date)) {
            if (simulationDate.equals(StartDate) || simulationDate.getDayOfMonth() == StartDate.getDayOfMonth()) {
                ChargePercentages();
            }
            DayPercentCount();
            simulationDate = simulationDate.plusDays(1);
        }
        _currentDate = simulationDate;
    }
}
