package com.Chub.services.scenarios;

import com.Chub.entities.banks.IBank;
import com.Chub.services.scenarios.fabric.BankOptionFabric;

import java.util.Scanner;

/**
 * Represents a scenario for selecting options related to a bank, implementing the {@link IScenario} interface.
 */
public class BankOptionScenario implements IScenario {

    /**
     * The bank for which options are selected.
     */
    private final IBank _bank;

    /**
     * Constructs a new BankOptionScenario with the specified bank.
     *
     * @param bank the bank for which options are selected
     */
    public BankOptionScenario(IBank bank) {
        _bank = bank;
    }

    /**
     * Runs the scenario, allowing the user to choose actions related to the bank.
     *
     * @throws Exception if an error occurs while running the scenario
     */
    @Override
    public void Run() throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.println("Choose one from these actions");
        System.out.println("-------------------------");
        System.out.println("1: Log in");
        System.out.println("2: Registration");
        System.out.println("3: Exit");
        int selection = input.nextInt();
        IScenario nextScenario = new BankOptionFabric(_bank).GetScenario(selection);
        if (nextScenario != null) {
            nextScenario.Run();
            Run();
        }
    }
}