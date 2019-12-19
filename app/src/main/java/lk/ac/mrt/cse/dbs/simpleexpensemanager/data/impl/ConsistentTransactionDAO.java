package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Ayesh Sandeepa on 12/19/2019.
 */

public class ConsistentTransactionDAO implements TransactionDAO{

    private final List<Transaction> transactions;
    private final DatabaseHelper databaseHelper;
    private SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
    private ExpenseType extype;
    private ConsistentAccountDAO account;

    public ConsistentTransactionDAO() {

        transactions = new LinkedList<>();
        databaseHelper=new DatabaseHelper();
        account=new ConsistentAccountDAO();

        Cursor res = databaseHelper.getAllTransaction();
        if(res.getCount() == 0) {
            // show message
            //showMessage("Error","Nothing found");
            return;
        }


        while (res.moveToNext()) {
            Date dt=new Date();
            try {
                dt=dateFormat.parse(res.getString(0));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(res.getString(3).equalsIgnoreCase("EXPENSE")){
                extype=ExpenseType.EXPENSE;
            }else{
                extype=ExpenseType.INCOME;
            }
            Transaction trans= new Transaction(dt, res.getString(1), extype, res.getDouble(3));
            transactions.add(trans);
        }
    }


    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        transactions.add(transaction);
        databaseHelper.addTransaction(date,accountNo,expenseType,amount);
        try {
            account.updateBalance(accountNo,expenseType,amount);
        } catch (InvalidAccountException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return transactions;

    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {

        int size = transactions.size();
        if (size <= limit) {
            return transactions;
        }
        // return the last <code>limit</code> number of transaction logs
        return transactions.subList(size - limit, size);
    }
}
