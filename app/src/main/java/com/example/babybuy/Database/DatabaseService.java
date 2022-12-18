package com.example.babybuy.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.babybuy.Model.Item;
import com.example.babybuy.Model.User;

import java.util.ArrayList;

public class DatabaseService extends SQLiteOpenHelper {
    public static final String DBName = "BabyBuy_Database";
    public static final String TName = "User";
    public static final String TName2 = "Item";

    Context context;

    public static final String Col1 = "Quantity";
    public static final String Col2 = "Name";
    public static final String Col3 = "Price";
    public static final String Col4 = "Image";
    public static final String Col5 = "UserId";
    public static final String Col6 = "State";
    public static final String Col7 = "ItemId";





    public static final String Col_1 = "User_Name";
    public static final String Col_2 = "User_Email";
    public static final String Col_3 = "User_Password";
    public static final String Col_4 = "User_Id";
    public DatabaseService(Context context) {

        super(context, DBName, null, 1);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // create table with its types of data that store record
        db.execSQL(" create table " + TName + "(" + Col_1 + " TEXT," + Col_2 + " TEXT," + Col_3 + " TEXT,"
                + Col_4 + " INTEGER PRIMARY KEY AUTOINCREMENT)");
        db.execSQL(" create table " + TName2 + "(" + Col1 + " TEXT," + Col2 + " TEXT," + Col3 + " TEXT," + Col4 + " BLOB," + Col5 + " TEXT,"
                + Col6 + " TEXT,"
                + Col7 + " INTEGER PRIMARY KEY AUTOINCREMENT)");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TName);
        db.execSQL("DROP TABLE IF EXISTS " + TName2);
        onCreate(db);
    }
    public void addUser(User user) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(Col_1,user.getName());
        cv.put(Col_2,user.getEmail());
        cv.put(Col_3,user.getPassword());
        long result=  db.insert(TName,null,cv);
        if(result!=-1){
            Toast.makeText(context, "user add sucessfully", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(context, "user not add sucessfully", Toast.LENGTH_LONG).show();

        }
        db.close();
    }
    public User getUserData(String email ,String password){
        User user=null;
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor =  db.rawQuery("SELECT * FROM User WHERE User_Email ='" + email + "' AND User_Password ='" + password + "'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            user=new User(cursor.getString(0),String.valueOf(cursor.getInt(3)));
            cursor.moveToNext();
        }
        return user;
    }
    public void addItem(Item item, byte[] image) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(Col1,item.getQuantity());
        cv.put(Col2,item.getName());
        cv.put(Col3,item.getPrice());
        cv.put(Col4,image);
        cv.put(Col5,item.getUserId());
        cv.put(Col6,"-1");
        long result=  db.insert(TName2,null,cv);
        if(result!=-1){
            Toast.makeText(context, "item add successfully", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(context, "item not add successfully", Toast.LENGTH_LONG).show();

        }
        db.close();
    }
    public void updateItem(Item item, byte[] image) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(Col1,item.getQuantity());
        cv.put(Col2,item.getName());
        cv.put(Col3,item.getPrice());
        cv.put(Col4,image);
        cv.put(Col5,item.getUserId());
        db.update(TName2,cv,"ItemId=?",new String[]{item.getUserId()});
    }
    public void updateItem(Item item) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(Col1,item.getQuantity());
        cv.put(Col2,item.getName());
        cv.put(Col3,item.getPrice());
        cv.put(Col5,item.getUserId());
        db.update(TName2,cv,"ItemId=?",new String[]{item.getUserId()});
    }
    public ArrayList<Item> getItemData(String UserId){
        ArrayList<Item> itemArrayList=new ArrayList<Item>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM Item WHERE UserId ='" + UserId + "'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            itemArrayList.add(new Item(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getBlob(3),cursor.getString(4),cursor.getString(5),String.valueOf(cursor.getInt(6))));
            cursor.moveToNext();
        }
        return itemArrayList;
    }
    public ArrayList<Item> getPurchaseItemData(String UserId){
        ArrayList<Item> itemArrayList=new ArrayList<Item>();
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM Item WHERE UserId ='" + UserId + "'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            if(cursor.getString(5).equals("1")){
                itemArrayList.add(new Item(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getBlob(3),cursor.getString(4),cursor.getString(5),String.valueOf(cursor.getInt(6))));
            }
            cursor.moveToNext();
        }
        return itemArrayList;
    }
    public int Del(String a){
        SQLiteDatabase db=this.getReadableDatabase();
        int b=  db.delete(TName2,"ItemId=?",new String[]{a});
        return b;
    }
    public void update(String itemId){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(Col6,"1");
        db.update(TName2,cv,"ItemId=?",new String[]{itemId});
    }
}
