package com.Chub.services.scenarios.fabric;

import com.Chub.entities.banks.IBank;
import com.Chub.services.scenarios.ClientLoginScenario;
import com.Chub.services.scenarios.ClientRegistrationScenario;
import com.Chub.services.scenarios.IScenario;

import java.util.ArrayList;

/**
 * Represents a fabric for creating bank-related scenarios.
 */
public class BankOptionFabric {

    /**
     * The list of scenarios available in this fabric.
     */
    private final ArrayList<IScenario> _scenarios = new ArrayList<>();

    /**
     * Constructs a new BankOptionFabric with scenarios related to the specified bank.
     *
     * @param bank the bank for which scenarios are created
     */
    public BankOptionFabric(IBank bank) {
        _scenarios.add(new ClientLoginScenario(bank));
        _scenarios.add(new ClientRegistrationScenario(bank));
    }

    /**
     * Gets the scenario corresponding to the specified selection.
     *
     * @param selection the selection index of the scenario
     * @return the scenario corresponding to the selection, or null if the selection is invalid
     */
    public IScenario GetScenario(int selection) {
        if (selection > _scenarios.size()) {
            return null;
        }
        return _scenarios.get(selection - 1);
    }
}
