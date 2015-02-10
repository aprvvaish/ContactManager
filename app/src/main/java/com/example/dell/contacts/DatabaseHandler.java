package com.example.dell.contacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 21-12-2014.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="contactManager",
    TABLE_CONTACTS="contacts",
    KEY_ID="id",KEY_NAME="name",KEY_PHONE="phone",KEY_EMAIL="email",KEY_ADDRESS="address";

    public DatabaseHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE "+ TABLE_CONTACTS +" ( " + KEY_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+ KEY_NAME +" TEXT,"+KEY_PHONE+" TEXT,"+ KEY_EMAIL +" TEXT,"+ KEY_ADDRESS +" TEXT )");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int NewVersion){
        db.execSQL("DROP TABLE IF EXISTS"+TABLE_CONTACTS);
        onCreate(db);

    }
    public void createContact(Contacts contacts){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_NAME,contacts.getName());
        values.put(KEY_PHONE,contacts.getPhone());
        values.put(KEY_EMAIL, contacts.getEmail());
        values.put(KEY_ADDRESS,contacts.getAddress());
        db.insert(TABLE_CONTACTS,null,values);
        db.close();

    }
    public Contacts getContact(int id){
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.query(TABLE_CONTACTS,new String[]{KEY_ID,KEY_NAME,KEY_PHONE,KEY_EMAIL,KEY_ADDRESS},KEY_ID+"=?",new String[] {String.valueOf(id)},null,null,null,null);
        if(cursor!=null)
            cursor.moveToFirst();
        Contacts contacts=new Contacts(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
        db.close();
        return contacts;
    }

    public void deleteContact(Contacts contacts){
        SQLiteDatabase db=getWritableDatabase();
        db.delete(TABLE_CONTACTS,KEY_ID+"=?",new String[]{ String.valueOf(contacts.getId()) });
        db.close();
    }
    public int getContactsCount(){
        SQLiteDatabase db=getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_CONTACTS,null);
        int count=cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
    public int updateContact(Contacts contacts){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(KEY_NAME,contacts.getName());
        values.put(KEY_PHONE,contacts.getPhone());
        values.put(KEY_EMAIL, contacts.getEmail());
        values.put(KEY_ADDRESS,contacts.getAddress());

        return  db.update(TABLE_CONTACTS,values,KEY_ID+"=?",new String[] {String.valueOf(contacts.getId())});
    }
    public List<Contacts> getAllContacts(){
        List<Contacts> contact =new ArrayList<>();
        SQLiteDatabase db =getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_CONTACTS,null);
        if(cursor.moveToFirst()){
            do{
                Contacts contacts=new Contacts(Integer.parseInt(cursor.getString(0)),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));
                contact.add(contacts);
            }
            while (cursor.moveToNext());
        }
        return contact;

    }
}
