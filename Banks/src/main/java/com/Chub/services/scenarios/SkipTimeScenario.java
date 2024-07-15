package com.Chub.services.scenarios;

import com.Chub.entities.accounts.AccountBase;

import java.time.LocalDate;
import java.util.Scanner;

/**
 * Represents a scenario for skipping time in an account, implementing the {@link IScenario} interface.
 */
public class SkipTimeScenario implements IScenario {

    /**
     * The account associated with this scenario.
     */
    private final AccountBase _account;

    /**
     * Constructs a new SkipTimeScenario with the specified account.
     *
     * @param account the account associated with this scenario
     */
    public SkipTimeScenario(AccountBase account) {
        _account = account;
    }

    /**
     * Runs the scenario, allowing the user to skip time by specifying a new date.
     */
    @Override
    public void Run() {
        System.out.println("Enter year:");
        Scanner input = new Scanner(System.in);
        int year = input.nextInt();
        System.out.println("Enter month:");
        int month = input.nextInt();
        System.out.println("Enter day:");
        int day = input.nextInt();
        _account.GoToDate(LocalDate.of(year, month, day));
    }
}
