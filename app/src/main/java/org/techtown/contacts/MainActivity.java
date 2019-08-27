package org.techtown.contacts;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseContacts();
            }
        });
    }

    private void chooseContacts() {
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        //액션정보, 연락처 정보를 조회하는데 사용되는 URI

        startActivityForResult(contactPickerIntent, 101);
        //연락처를 선택할 수 있는 화면 표시
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){ //연락처를 하나 선택하면 호출
        if(resultCode == RESULT_OK){
            if(requestCode == 101){
                try{
                    Uri contactsUri = data.getData();
                    String id = contactsUri.getLastPathSegment(); //선택한 연락처의 id값 확인하기

                    getContacts(id);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    private void println(String data){ //파라미터로 전달받은 글자를 스크롤뷰 안에 들어있는 텍스트뷰 출력
        textView.append(data+"\n");
    }

    private void getContacts(String id) {

        Cursor cursor = null;
        String name = "";

        try{
            cursor = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                    null,ContactsContract.Data.CONTACT_ID+"=?",new String[] {id}, null);

            if (cursor.moveToNext()){
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                println("name:" + name);

                String columns[] = cursor.getColumnNames();
                for (String column : columns){
                    int index = cursor.getColumnIndex(column);
                    String columnOutput = ("#" + index+ "->["+column+"]" +cursor.getString(index));
                    println(columnOutput);
                }
                cursor.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
