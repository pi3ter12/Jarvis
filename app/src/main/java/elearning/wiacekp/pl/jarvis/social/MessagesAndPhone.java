package elearning.wiacekp.pl.jarvis.social;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.ArrayList;

public class MessagesAndPhone {
    private Context ctx;
    private Activity act;
    private ArrayList<ContactList> contactLists = new ArrayList<>();

    public ArrayList<MessagesList> getMessagesLists() {
        return messagesLists;
    }
    public ArrayList<MessagesList> getDistinctMessagesLists() {
        return distinctMessagesLists;
    }

    private ArrayList<MessagesList> messagesLists = new ArrayList<>();
    private ArrayList<MessagesList> distinctMessagesLists = new ArrayList<>();
    private ArrayList<MessagesList> threadMessagesLists = new ArrayList<>();

    public MessagesAndPhone(Context ctx, Activity act){
        this.act = act;
        this.ctx = ctx;
        contactLists=fetchContacts();
        readSms();
    }

    public void call(String number){
        try {
            String uri = "tel:"+number;
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));

            ctx.startActivity(callIntent);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }


    public ArrayList<ContactList> getContactLists(){
        return contactLists;
    }

    public void readSms(){
        messagesLists.clear();
        Uri uriSMSURI = Uri.parse("content://sms");
        String[] selectionArgs = {"address", "date", "body", "type", "thread_id"};
        Cursor cur = ctx.getContentResolver().query(uriSMSURI, selectionArgs,null, null,"date");

        if (cur.moveToFirst()) {
            do {
                boolean inbox = (cur.getInt(3)==1)? true: false;
                messagesLists.add(new MessagesList(deleteSpace(cur.getString(0)), cur.getString(1), cur.getString(2), inbox, cur.getInt(4)));
            } while (cur.moveToNext());
        }

        distinctMessagesLists.clear();
        boolean isDistinct = false;
        for(int i=messagesLists.size()-1; i>=0; i--){
            isDistinct=true;
            for(int j=0; j<distinctMessagesLists.size(); j++){
                if(messagesLists.get(i).getThreadId()==distinctMessagesLists.get(j).getThreadId()){
                    isDistinct=false;
                }
            }
            if(isDistinct){
                distinctMessagesLists.add(messagesLists.get(i));
            }
        }
    }

    public ArrayList<MessagesList> getThreadMessagesLists(){
        return threadMessagesLists;
    }

    public void genThreadMessages(int threadID){
        threadMessagesLists.clear();
        for(int i=messagesLists.size()-1; i>=0; i--){
            if(messagesLists.get(i).getThreadId()==threadID){
                threadMessagesLists.add(messagesLists.get(i));
            }
        }
    }

    private ArrayList<ContactList> fetchContacts() {
        ArrayList<ContactList> contactLists = new ArrayList<>();

        String phoneNumber = null;
        String email = null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        Uri EmailCONTENT_URI =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;

        ContentResolver contentResolver = ctx.getContentResolver();

        Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);

        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {
                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));
                email=phoneNumber="";
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));

                if (hasPhoneNumber > 0) {
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);

                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                    }

                    phoneCursor.close();

                    Cursor emailCursor = contentResolver.query(EmailCONTENT_URI,	null, EmailCONTACT_ID+ " = ?", new String[] { contact_id }, null);

                    while (emailCursor.moveToNext()) {

                        email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                    }

                    emailCursor.close();
                    contactLists.add(new ContactList(0, name, "", deleteSpace(phoneNumber), email));
                }
            }
        }

        return contactLists;
    }

    public boolean sendSMSMessage(MessagesList messagesList) {
        String phoneNo = messagesList.getPhone();
        String message = messagesList.getMessage();

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            readSms();
            genThreadMessages(messagesList.getThreadId());
            return true;
        }

        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String deleteSpace(String input){
        return input.replaceAll(" ", "");
    }
}
