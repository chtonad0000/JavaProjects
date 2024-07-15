package com.Chub.services.scenarios;

import com.Chub.entities.banks.IBank;
import com.Chub.entities.clients.Client;

import java.util.Scanner;

/**
 * Represents a scenario for client login, implementing the {@link IScenario} interface.
 */
public class ClientLoginScenario implements IScenario {

    /**
     * The bank where the client is logging in.
     */
    private final IBank _bank;

    /**
     * Constructs a new ClientLoginScenario with the specified bank.
     *
     * @param bank the bank where the client is logging in
     */
    public ClientLoginScenario(IBank bank) {
        _bank = bank;
    }

    /**
     * Runs the scenario, allowing the client to log in.
     *
     * @throws Exception if an error occurs while running the scenario
     */
    @Override
    public void Run() throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter name:");
        String name = input.nextLine();
        System.out.println("Enter surname:");
        String surname = input.nextLine();
        Client resultClient = _bank.FindClient(name, surname);
        if (resultClient != null) {
            new ClientScenario(resultClient, _bank).Run();
        } else {
            System.out.println("No client with that name and surname");
        }
    }
}
