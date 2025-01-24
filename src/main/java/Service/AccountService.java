package Service;

import java.util.ArrayList;
import java.util.List;

import DAO.AccountDAO;
import Model.Account;
import Model.Message;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public Account addAccount(Account account) {
        if (account.getUsername().isBlank()) return null;
        if (account.getPassword().length() < 4) return null;
        if (accountDAO.getAccountByUsername(account.getUsername()) != null) return null;
        return accountDAO.insertAccount(account);
    }

    public Account verifyLogin(Account account) {
        Account accountDB = accountDAO.getAccountByUsername(account.getUsername());
        if (accountDB != null && accountDB.password.equals(account.password)) return accountDB;
        return null;
    }

    public List<Message> getAccountMessages(int accountId) {
        Account account = accountDAO.getAccountById(accountId);
        if (account == null) return new ArrayList<>();

        return accountDAO.getAccountMessages(accountId);
    }
}
