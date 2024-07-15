package com.Chub.entities.banks;

import java.util.ArrayList;

/**
 * Represents a central bank managing multiple banks.
 */
public class CentralBank {

    /**
     * The list of banks managed by the central bank.
     */
    public final ArrayList<IBank> Banks = new ArrayList<>();

    /**
     * Creates a new bank and adds it to the list of managed banks.
     *
     * @param bank the bank to be created and added
     */
    public void CreateBank(IBank bank) {
        Banks.add(bank);
    }

    /**
     * Notifies all managed banks to charge percentages on accounts.
     */
    public void NotifyBanks() {
        for (IBank bank : Banks) {
            bank.ChargePercentages();
        }
    }
}
