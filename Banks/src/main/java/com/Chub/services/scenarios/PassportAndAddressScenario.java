package com.Chub.services.scenarios;

import com.Chub.entities.banks.IBank;
import com.Chub.entities.clients.Client;

import java.util.Scanner;

/**
 * Represents a scenario for adding passport and address to a client, implementing the {@link IScenario} interface.
 */
public class PassportAndAddressScenario implements IScenario {

    /**
     * The client associated with this scenario.
     */
    private final Client _client;

    /**
     * The bank where the client is performing actions.
     */
    private final IBank _bank;

    /**
     * Constructs a new PassportAndAddressScenario with the specified client and bank.
     *
     * @param client the client associated with this scenario
     * @param bank   the bank where the client is performing actions
     */
    public PassportAndAddressScenario(Client client, IBank bank) {
        _client = client;
        _bank = bank;
    }

    /**
     * Runs the scenario, allowing the client to add passport and address.
     */
    @Override
    public void Run() {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter passport:");
        String passport = input.nextLine();
        System.out.println("Enter address:");
        String address = input.nextLine();
        _client.setPassportNumber(passport);
        _client.setAddress(address);
        _bank.ChangeAccountStatus(_client);
    }
}
