import com.Chub.entities.accounts.DepositAccount;
import com.Chub.entities.accounts.transactions.PutMoneyTransaction;
import com.Chub.entities.banks.Bank;
import com.Chub.entities.banks.CentralBank;
import com.Chub.entities.banks.IBank;
import com.Chub.entities.clients.Client;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DepositAccountTests {
    private final CentralBank _centralBank = new CentralBank();

    public DepositAccountTests() {
        IBank sber = new Bank("Sber", 50000);
        sber.ChangeDebitPercent(0.0365);
        sber.ChangeDepositPercent(0.0365, 10000);
        sber.ChangeCreditLimit(50000);
        sber.ChangeCreditCommission(100);
        _centralBank.CreateBank(sber);
        Client.ClientBuilder builder = new Client.ClientBuilder();
        builder.AddName("Daniil");
        builder.AddSurname("Chub");
        Client client = builder.Build();
        IBank bank = _centralBank.Banks.get(0);
        bank.ClientRegistration(client);
        client = bank.FindClient("Daniil", "Chub");
        Integer limit = null;
        if (client.Status()) {
            limit = bank.get_operationLimit();
        }
        DepositAccount account = new DepositAccount(10000, LocalDate.of(2024, 3, 8), client.Status(), limit, 1234);
        bank.CreateAccount(client, account);
    }

    @Test
    public void PercentTest() {
        IBank bank = _centralBank.Banks.get(0);
        bank.FindAccountById(1234).GoToDate(LocalDate.of(2024, 4, 10));

        assertEquals(10062, bank.FindAccountById(1234).GetBalance());
    }

    @Test
    public void PercentTest2() {
        IBank bank = _centralBank.Banks.get(0);
        PutMoneyTransaction transaction = new PutMoneyTransaction(bank.FindAccountById(1234), 10000);
        transaction.Do();
        bank.FindAccountById(1234).GoToDate(LocalDate.of(2024, 4, 10));

        assertEquals(20124, bank.FindAccountById(1234).GetBalance());
    }

}
