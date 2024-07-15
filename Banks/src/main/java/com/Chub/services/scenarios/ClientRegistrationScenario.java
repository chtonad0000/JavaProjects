package com.Chub.services.scenarios;

import com.Chub.entities.banks.IBank;
import com.Chub.entities.clients.Client;

import java.util.Scanner;

/**
 * Represents a scenario for client registration, implementing the {@link IScenario} interface.
 */
public class ClientRegistrationScenario implements IScenario {

    /**
     * The bank where the client is registering.
     */
    private final IBank _bank;

    /**
     * Constructs a new ClientRegistrationScenario with the specified bank.
     *
     * @param bank the bank where the client is registering
     */
    public ClientRegistrationScenario(IBank bank) {
        _bank = bank;
    }

    /**
     * Runs the scenario, allowing the client to register.
     */
    @Override
    public void Run() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter name:");
        String name = input.nextLine();
        System.out.println("Enter surname:");
        String surname = input.nextLine();
        Client.ClientBuilder builder = new Client.ClientBuilder();
        builder.AddName(name);
        builder.AddSurname(surname);
        System.out.println("Do you want set passport and address? (Yes/No)");
        String answer = input.nextLine();
        if (answer.equals("Yes")) {
            System.out.println("Enter passport:");
            String passport = input.nextLine();
            System.out.println("Enter address:");
            String address = input.nextLine();
            builder.AddPassport(passport);
            builder.AddAddress(address);
        }
        _bank.ClientRegistration(builder.Build());
    }
}
