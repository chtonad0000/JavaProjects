import com.Chub.entities.accounts.AccountBase;
import com.Chub.entities.accounts.CreditAccount;
import com.Chub.entities.accounts.transactions.ITransaction;
import com.Chub.entities.accounts.transactions.WithdrawMoneyTransaction;
import com.Chub.entities.banks.Bank;
import com.Chub.entities.banks.CentralBank;
import com.Chub.entities.banks.IBank;
import com.Chub.entities.clients.Client;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditAccountTests {
    private final CentralBank _centralBank = new CentralBank();

    public CreditAccountTests() {
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
        AccountBase account = new CreditAccount(0, LocalDate.of(2024, 3, 8), client.Status(), limit, 1234);
        bank.CreateAccount(client, account);
    }

    @Test
    public void CommissionTest() throws Exception {
        IBank bank = _centralBank.Banks.get(0);
        ITransaction transaction = new WithdrawMoneyTransaction(bank.FindAccountById(1234), 3000);
        transaction.Do();
        assertEquals(-3100, bank.FindAccountById(1234).GetBalance());
    }

    @Test
    public void LimitCommissionTest() throws Exception {
        IBank bank = _centralBank.Banks.get(0);
        ITransaction transaction = new WithdrawMoneyTransaction(bank.FindAccountById(1234), 300000);
        transaction.Do();
        assertEquals(0, bank.FindAccountById(1234).GetBalance());
        transaction = new WithdrawMoneyTransaction(bank.FindAccountById(1234), 50000);
        transaction.Do();
        assertEquals(0, bank.FindAccountById(1234).GetBalance());
        transaction = new WithdrawMoneyTransaction(bank.FindAccountById(1234), 49900);
        transaction.Do();
        assertEquals(-50000, bank.FindAccountById(1234).GetBalance());
    }
}
