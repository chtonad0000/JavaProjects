package com.Chub.entities.accounts.transactions;

/**
 * Represents the status of a transaction.
 * This class implements the ITransaction interface.
 */
public enum TransactionStatus {
    /**
     * The transaction has been approved.
     */
    Approved,

    /**
     * The transaction has not been approved.
     */
    NoApproved,

    /**
     * The transaction has been cancelled.
     */
    Cancelled
}
