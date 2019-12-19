package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.Date;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.MainActivity;

/**
 * Created by Ayesh Sandeepa on 12/19/2019.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(){
        super(null,"170203P",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Account(Account_No TEXT, Bank TEXT, Account_Holder TEXT, Balance DECIMAL(25,2), PRIMARY KEY(Account_No))");
        db.execSQL("create table Transaction(Date TEXT, Account_No TEXT, Expense_Type TEXT, Amount DECIMAL(25,2)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addAccount(String accNo, String bank, String accountHolder, double balance){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("Account_No",accNo);
        contentValues.put("Bank",bank);
        contentValues.put("Account_Holder",accountHolder);
        contentValues.put("Balance",balance);
        long result=db.insert("Account",null,contentValues);

        if(result==-1){

        }else{
            //Toast.makeText(MainActivity.this,"Account Added",Toast.)
        }

    }


    public void updateAccount(String accountNo,String bank,String accountHolder,double balance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Account_No",accountNo);
        contentValues.put("Bank",bank);
        contentValues.put("Account_Holder",accountHolder);
        contentValues.put("Balance",balance);
        db.update("Account", contentValues, "Account_No = ?",new String[] { accountNo });

    }


    public void removeAccount (String accountNo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Account", "Account_No = ?",new String[] {accountNo});
    }

    public Cursor getAllAccount() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from Account",null);
        return res;
    }


    public void addTransaction(Date date, String accNo, ExpenseType expenseType, double amount){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("Date",date.toString());
        contentValues.put("Account_No",accNo);
        contentValues.put("Expense_Type",expenseType.toString());
        contentValues.put("Amount",amount);
        long result=db.insert("Transaction",null,contentValues);

        if(result==-1){

        }else{
            //Toast.makeText(MainActivity.this,"Account Added",Toast.)
        }

    }




    public Cursor getAllTransaction() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from Transaction",null);
        return res;
    }


}
