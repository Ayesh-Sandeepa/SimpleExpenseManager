package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.MainActivity;

/**
 * Created by Ayesh Sandeepa on 12/19/2019.
 */

public class ConsistentAccountDAO implements AccountDAO {

    private final Map<String, Account> accounts;
    private final DatabaseHelper databaseHelper;

    public ConsistentAccountDAO() {
        databaseHelper=new DatabaseHelper();
        this.accounts = new HashMap<>();

        Cursor res = databaseHelper.getAllAccount();
        if(res.getCount() == 0) {
            // show message
            //showMessage("Error","Nothing found");
            return;
        }


        while (res.moveToNext()) {
            Account acct = new Account(res.getString(0), res.getString(1), res.getString(2), res.getDouble(3));
            accounts.put(acct.getAccountNo(),acct);
        }

    }



    @Override
    public List<String> getAccountNumbersList() {
        return new ArrayList<>(accounts.keySet());
    }

    @Override
    public List<Account> getAccountsList() {
        return new ArrayList<>(accounts.values());
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        if (accounts.containsKey(accountNo)) {
            return accounts.get(accountNo);
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        accounts.put(account.getAccountNo(), account);
        databaseHelper.addAccount(account.getAccountNo(),account.getBankName(),account.getAccountHolderName(),account.getBalance());
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        if (!accounts.containsKey(accountNo)) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        accounts.remove(accountNo);
        databaseHelper.removeAccount(accountNo);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        if (!accounts.containsKey(accountNo)) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        Account account = accounts.get(accountNo);
        // specific implementation based on the transaction type
        switch (expenseType) {
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                databaseHelper.updateAccount(accountNo,account.getBankName(),account.getAccountHolderName(),account.getBalance() - amount);
                break;
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                databaseHelper.updateAccount(accountNo,account.getBankName(),account.getAccountHolderName(),account.getBalance() + amount);
                break;
        }
        accounts.put(accountNo, account);


    }
}
