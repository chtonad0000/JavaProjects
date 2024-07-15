import com.Chub.entities.accounts.AccountBase;
import com.Chub.entities.accounts.DebitAccount;
import com.Chub.entities.accounts.transactions.ITransaction;
import com.Chub.entities.accounts.transactions.PutMoneyTransaction;
import com.Chub.entities.accounts.transactions.TransferTransaction;
import com.Chub.entities.accounts.transactions.WithdrawMoneyTransaction;
import com.Chub.entities.banks.Bank;
import com.Chub.entities.banks.CentralBank;
import com.Chub.entities.banks.IBank;
import com.Chub.entities.clients.Client;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountTransactionTests {
    private final CentralBank _centralBank = new CentralBank();

    public AccountTransactionTests() {
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
        client = bank.FindClient("Daniil", "Chub");
        Integer limit = null;
        if (client.Status()) {
            limit = bank.get_operationLimit();
        }
        AccountBase account = new DebitAccount(0, LocalDate.now(), client.Status(), limit, 1234);
        AccountBase account2 = new DebitAccount(0, LocalDate.now(), client.Status(), limit, 5678);
        bank.CreateAccount(client, account);
        bank.CreateAccount(client, account2);
    }

    @Test
    public void PutMoneySuspiciousTest() throws Exception {
        IBank bank = _centralBank.Banks.get(0);
        ITransaction transaction = new PutMoneyTransaction(bank.FindAccountById(1234), 10000);
        transaction.Do();
        assertEquals(10000, bank.FindAccountById(1234).GetBalance());

        transaction = new PutMoneyTransaction(bank.FindAccountById(1234), 60000);
        transaction.Do();
        assertEquals(10000, bank.FindAccountById(1234).GetBalance());
    }

    @Test
    public void PutMoneyTest() throws Exception {
        IBank bank = _centralBank.Banks.get(0);
        bank.FindClient("Daniil", "Chub").setPassportNumber("1234 567890");
        bank.FindClient("Daniil", "Chub").setAddress("Pushkina str. 17");
        bank.ChangeAccountStatus(bank.FindClient("Daniil", "Chub"));
        ITransaction transaction = new PutMoneyTransaction(bank.FindAccountById(1234), 60000);
        transaction.Do();
        assertEquals(60000, bank.FindAccountById(1234).GetBalance());
    }

    @Test
    public void WithdrawMoneySuspiciousTest() throws Exception {
        IBank bank = _centralBank.Banks.get(0);
        ITransaction transaction = new PutMoneyTransaction(bank.FindAccountById(1234), 50000);
        transaction.Do();
        transaction = new PutMoneyTransaction(bank.FindAccountById(1234), 50000);
        transaction.Do();
        transaction = new WithdrawMoneyTransaction(bank.FindAccountById(1234), 30000);
        transaction.Do();
        assertEquals(70000, bank.FindAccountById(1234).GetBalance());

        transaction = new WithdrawMoneyTransaction(bank.FindAccountById(1234), 60000);
        transaction.Do();
        assertEquals(70000, bank.FindAccountById(1234).GetBalance());
    }

    @Test
    public void WithdrawMoneyTest() throws Exception {
        IBank bank = _centralBank.Banks.get(0);
        bank.FindClient("Daniil", "Chub").setPassportNumber("1234 567890");
        bank.FindClient("Daniil", "Chub").setAddress("Pushkina str. 17");
        bank.ChangeAccountStatus(bank.FindClient("Daniil", "Chub"));
        ITransaction transaction = new PutMoneyTransaction(bank.FindAccountById(1234), 70000);
        transaction.Do();
        transaction = new WithdrawMoneyTransaction(bank.FindAccountById(1234), 60000);
        transaction.Do();
        assertEquals(10000, bank.FindAccountById(1234).GetBalance());
    }

    @Test
    public void TransferMoneySuspiciousTest() throws Exception {
        IBank bank = _centralBank.Banks.get(0);
        ITransaction transaction = new PutMoneyTransaction(bank.FindAccountById(1234), 50000);
        transaction.Do();
        transaction = new PutMoneyTransaction(bank.FindAccountById(1234), 50000);
        transaction.Do();
        transaction = new TransferTransaction(bank.FindAccountById(1234), bank.FindAccountById(5678), 30000);
        transaction.Do();
        assertEquals(70000, bank.FindAccountById(1234).GetBalance());
        assertEquals(30000, bank.FindAccountById(5678).GetBalance());

        transaction = new TransferTransaction(bank.FindAccountById(1234), bank.FindAccountById(5678), 60000);
        transaction.Do();
        assertEquals(70000, bank.FindAccountById(1234).GetBalance());
        assertEquals(30000, bank.FindAccountById(5678).GetBalance());
    }

    @Test
    public void TransferMoneyTest() throws Exception {
        IBank bank = _centralBank.Banks.get(0);
        bank.FindClient("Daniil", "Chub").setPassportNumber("1234 567890");
        bank.FindClient("Daniil", "Chub").setAddress("Pushkina str. 17");
        bank.ChangeAccountStatus(bank.FindClient("Daniil", "Chub"));
        ITransaction transaction = new PutMoneyTransaction(bank.FindAccountById(1234), 100000);
        transaction.Do();
        transaction = new TransferTransaction(bank.FindAccountById(1234), bank.FindAccountById(5678), 70000);
        transaction.Do();
        assertEquals(30000, bank.FindAccountById(1234).GetBalance());
        assertEquals(70000, bank.FindAccountById(5678).GetBalance());
    }

    @Test
    public void UndoTransactionTest() throws Exception {
        IBank bank = _centralBank.Banks.get(0);
        ITransaction transaction = new PutMoneyTransaction(bank.FindAccountById(1234), 10000);
        transaction.Do();
        transaction = new WithdrawMoneyTransaction(bank.FindAccountById(1234), 5000);
        transaction.Do();
        bank.UndoTransaction(transaction);
        assertEquals(10000, bank.FindAccountById(1234).GetBalance());
    }

    @Test
    public void LowBalanceTest() throws Exception {
        IBank bank = _centralBank.Banks.get(0);
        ITransaction transaction = new PutMoneyTransaction(bank.FindAccountById(1234), 10000);
        transaction.Do();
        transaction = new WithdrawMoneyTransaction(bank.FindAccountById(1234), 15000);
        transaction.Do();
        assertEquals(10000, bank.FindAccountById(1234).GetBalance());
    }
}
