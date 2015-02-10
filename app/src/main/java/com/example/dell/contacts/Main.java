package com.example.dell.contacts;

import android.app.Activity;
//import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Main extends Activity {

    EditText nameTxt, phoneTxt, emailTxt, addressTxt;
    List<Contacts> Contact=new ArrayList<>();
    ListView contactListView;
    DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTxt=(EditText) findViewById(R.id.txtname);
        phoneTxt=(EditText) findViewById(R.id.txtPhone);
        emailTxt=(EditText) findViewById(R.id.txtEmail);
        addressTxt=(EditText) findViewById(R.id.txtAddress);
        contactListView=(ListView) findViewById(R.id.listView);
        dbHandler=new DatabaseHandler(getApplicationContext() );
        TabHost tabHost=(TabHost) findViewById(R.id.tabHost);
        final Button addBtn=(Button) findViewById(R.id.btnAdd);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contacts contacts=new Contacts(dbHandler.getContactsCount(),String.valueOf(nameTxt.getText()),String.valueOf(phoneTxt.getText()),String.valueOf(emailTxt.getText()),String.valueOf(addressTxt.getText()));
                dbHandler.createContact(contacts);
                Contact.add(contacts);
                populateList();
                Toast.makeText(getApplicationContext(),nameTxt.getText().toString()+" has been added to your contacts",Toast.LENGTH_SHORT).show();

            }
        });

        tabHost.setup();

        TabHost.TabSpec tabSpec=tabHost.newTabSpec("creator");
        tabSpec.setContent(R.id.tabCreator);
        tabSpec.setIndicator("Creator");
        tabHost.addTab(tabSpec);

        tabSpec=tabHost.newTabSpec("list");
        tabSpec.setContent(R.id.tabContactList);
        tabSpec.setIndicator("List");
        tabHost.addTab(tabSpec);



        nameTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                addBtn.setEnabled(!nameTxt.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        List<Contacts> addableContacts=dbHandler.getAllContacts();
        int contactCount=dbHandler.getContactsCount();
        for(int i=0;i<contactCount;i++){
            Contact.add(addableContacts.get(i));
        }
        if(!addableContacts.isEmpty())
            populateList();

    }

    private void populateList(){
        ArrayAdapter<Contacts> adapter=new ContactListAdapter();
        contactListView.setAdapter(adapter);
    }

   /* private void addContact(String name,String phone,String email,String address){
        Contact.add(new Contacts(0,name, phone, email, address));

    }*/

    private class ContactListAdapter extends ArrayAdapter<Contacts>{
        public ContactListAdapter(){
            super(Main.this,R.layout.listview_item,Contact);

        }

        @Override
        public View getView(int position,View view,ViewGroup parent){
            if (view==null)
                view=getLayoutInflater().inflate(R.layout.listview_item,parent,false);
            Contacts currentContact=Contact.get(position);

            TextView name=(TextView) view.findViewById(R.id.contactName);
            name.setText(currentContact.getName());

            TextView phone=(TextView) view.findViewById(R.id.phoneNumber);
            phone.setText(currentContact.getPhone());

            TextView email=(TextView) view.findViewById(R.id.emailAddress);
            email.setText(currentContact.getEmail());

            TextView address=(TextView) view.findViewById(R.id.cAddress);
            address.setText(currentContact.getAddress());

            return view;
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
