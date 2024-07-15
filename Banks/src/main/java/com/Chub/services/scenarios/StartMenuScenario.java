package com.Chub.services.scenarios;

import com.Chub.entities.banks.IBank;
import com.Chub.entities.banks.CentralBank;

import java.util.Scanner;

/**
 * Represents a scenario for displaying the start menu of the application, implementing the {@link IScenario} interface.
 */
public class StartMenuScenario implements IScenario {

    /**
     * The central bank associated with this scenario.
     */
    private final CentralBank _centralBank;

    /**
     * Constructs a new StartMenuScenario with the specified central bank.
     *
     * @param centralBank the central bank associated with this scenario
     */
    public StartMenuScenario(CentralBank centralBank) {
        _centralBank = centralBank;
    }

    /**
     * Runs the scenario, displaying the start menu and allowing the user to choose a bank.
     */
    @Override
    public void Run() throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.println("Choose one from these banks");
        System.out.println("-------------------------");
        for (int i = 1; i <= _centralBank.Banks.size(); ++i) {
            System.out.println(i + ": " + _centralBank.Banks.get(i - 1).getName());
        }
        int selection = input.nextInt();
        IBank result = _centralBank.Banks.get(selection - 1);
        new BankOptionScenario(result).Run();
    }
}
