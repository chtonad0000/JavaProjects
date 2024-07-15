package com.Chub;

import com.Chub.entities.banks.Bank;
import com.Chub.entities.banks.CentralBank;
import com.Chub.entities.banks.IBank;
import com.Chub.services.scenarios.IScenario;
import com.Chub.services.scenarios.StartMenuScenario;

public class Main {
    public static void main(String[] args) throws Exception {
        CentralBank centralBank = new CentralBank();
        IBank sber = new Bank("Sber", 50000);
        IBank tinkoff = new Bank("Tinkoff", 10000);
        sber.ChangeCreditLimit(50000);
        sber.ChangeDebitPercent(0.1);
        centralBank.CreateBank(sber);
        centralBank.CreateBank(tinkoff);
        IScenario scenario = new StartMenuScenario(centralBank);
        scenario.Run();
    }
}