import com.Chub.entities.accounts.AccountBase;
import com.Chub.entities.accounts.DebitAccount;
import com.Chub.entities.banks.Bank;
import com.Chub.entities.banks.CentralBank;
import com.Chub.entities.banks.IBank;
import com.Chub.entities.clients.Client;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ClientTests {
    private final CentralBank _centralBank = new CentralBank();

    public ClientTests() {
        IBank sber = new Bank("Sber", 50000);
        sber.ChangeDebitPercent(0.1);
        sber.ChangeCreditLimit(50000);
        sber.ChangeCreditCommission(100);
        _centralBank.CreateBank(sber);
        Client.ClientBuilder builder = new Client.ClientBuilder();
        builder.AddName("Daniil");
        builder.AddSurname("Chub");
        Client client = builder.Build();
        IBank bank = _centralBank.Banks.get(0);
        bank.ClientRegistration(client);
    }

    @Test
    public void RegistrationTest() {
        IBank bank = _centralBank.Banks.get(0);
        assertEquals("Daniil", bank.FindClient("Daniil", "Chub").Name);
        assertEquals("Chub", bank.FindClient("Daniil", "Chub").Surname);
    }

    @Test
    public void SetAddressAndPassportTest() {
        IBank bank = _centralBank.Banks.get(0);
        bank.FindClient("Daniil", "Chub").setPassportNumber("12345 67890");
        bank.FindClient("Daniil", "Chub").setAddress("Pushkina str. 17");
        assertEquals("12345 67890", bank.FindClient("Daniil", "Chub").PassportNumber);
        assertEquals("Pushkina str. 17", bank.FindClient("Daniil", "Chub").Address);
    }

    @Test
    public void CreateAccountTest() {
        IBank bank = _centralBank.Banks.get(0);
        Client client = bank.FindClient("Daniil", "Chub");
        Integer limit = null;
        if (client.Status()) {
            limit = bank.get_operationLimit();
        }
        AccountBase account = new DebitAccount(0, LocalDate.now(), client.Status(), limit, 1234);
        bank.CreateAccount(client, account);

        assertNotNull(bank.FindAccountById(1234));
    }
}
