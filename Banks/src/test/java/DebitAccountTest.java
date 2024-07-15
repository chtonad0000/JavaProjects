import com.Chub.entities.accounts.AccountBase;
import com.Chub.entities.accounts.DebitAccount;
import com.Chub.entities.accounts.transactions.ITransaction;
import com.Chub.entities.accounts.transactions.PutMoneyTransaction;
import com.Chub.entities.banks.Bank;
import com.Chub.entities.banks.CentralBank;
import com.Chub.entities.banks.IBank;
import com.Chub.entities.clients.Client;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DebitAccountTest {
    private final CentralBank _centralBank = new CentralBank();

    public DebitAccountTest() {
        IBank sber = new Bank("Sber", 50000);
        sber.ChangeDebitPercent(0.0365);
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
        AccountBase account = new DebitAccount(0, LocalDate.of(2024, 3, 8), client.Status(), limit, 1234);
        bank.CreateAccount(client, account);
    }

    @Test
    public void PercentTest() throws Exception {
        IBank bank = _centralBank.Banks.get(0);
        ITransaction transaction = new PutMoneyTransaction(bank.FindAccountById(1234), 10000);
        transaction.Do();
        bank.FindAccountById(1234).GoToDate(LocalDate.of(2024, 4, 10));

        assertEquals(10031, bank.FindAccountById(1234).GetBalance());
    }

    @Test
    public void PercentTest2() throws Exception {
        IBank bank = _centralBank.Banks.get(0);
        ITransaction transaction = new PutMoneyTransaction(bank.FindAccountById(1234), 10000);
        transaction.Do();
        bank.FindAccountById(1234).GoToDate(LocalDate.of(2024, 3, 10));
        transaction = new PutMoneyTransaction(bank.FindAccountById(1234), 10000);
        transaction.Do();
        bank.FindAccountById(1234).GoToDate(LocalDate.of(2024, 4, 10));
        assertEquals(20059, bank.FindAccountById(1234).GetBalance());
    }
}
