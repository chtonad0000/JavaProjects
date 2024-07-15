package com.Chub.entities.accounts.transactions;

/**
 * Represents a transaction.
 */
public interface ITransaction {

    /**
     * Undoes the transaction.
     */
    void Undo();

    /**
     * Executes the transaction.
     *
     * @throws Exception if an error occurs during execution
     */
    void Do() throws Exception;
}
