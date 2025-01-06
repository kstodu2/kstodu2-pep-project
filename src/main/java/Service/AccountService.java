package Service;
import Model.Account;

import java.sql.SQLException;

import DAO.AccountDAO;

public class AccountService {
    
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account addAccount(Account account){
        return accountDAO.insertAccount(account);
    }

}
