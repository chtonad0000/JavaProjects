import com.Chub.entities.banks.Bank;
import com.Chub.entities.banks.CentralBank;
import com.Chub.entities.banks.IBank;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BankTest {
    @Test
    public void CreateBank() {
        CentralBank mainBank = new CentralBank();
        IBank sber = new Bank("Sber", 50000);
        mainBank.CreateBank(sber);
        assertEquals(sber, mainBank.Banks.get(0));
    }

    @Test
    public void SetLimits() {
        CentralBank mainBank = new CentralBank();
        IBank sber = new Bank("Sber", 50000);
        sber.ChangeDebitPercent(0.1);
        sber.ChangeCreditLimit(50000);
        sber.ChangeCreditCommission(100);
        mainBank.CreateBank(sber);
        assertEquals(0.1, mainBank.Banks.get(0).get_debitPercent());
        assertEquals(50000, mainBank.Banks.get(0).get_limit());
        assertEquals(100, mainBank.Banks.get(0).get_commission());
    }
}
