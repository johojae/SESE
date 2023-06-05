package com.sese.showmethebeer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.sese.showmethebeer.sqlite.SQLiteManager;

public class HiddenMenuActivity extends AppCompatActivity {

    private static final String IPADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\:" +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_hidden_menu);

        App app = (App)getApplication();
        SQLiteManager sqliteMngr = app.getSQLiteManager();

        EditText editText = findViewById(R.id.ipAddrEditText);
        Button saveButton = findViewById(R.id.saveIpAddrbutton);
        CheckBox checkBox = findViewById(R.id.userGuideCheckBox);

        String serverIPAddr = sqliteMngr.getServerIpAddress();
        if (serverIPAddr != null)
            editText.setText(serverIPAddr);


        saveButton.setOnClickListener(new View.OnClickListener () {
            public void onClick(View var1) {
                String text = editText.getText().toString();
                /*if (!text.matches(IPADDRESS_PATTERN)) {
                    Toast.makeText(context, "192.168.0.10:1234 형식으로 넣어주세요", Toast.LENGTH_SHORT).show();
                    return;
                }*/

                boolean result = sqliteMngr.updateServerIpsAddress(text);
                if (result) {
                    Toast.makeText(context, "저장했습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "저장에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View var1) {
                boolean checked = checkBox.isChecked();
                if (checked) {
                    sqliteMngr.updateUserGuideRead(false); //user guide보이지 않은 것처럼 reset
                    sqliteMngr.dropRatingTalbes();
                } else {
                    sqliteMngr.updateUserGuideRead(true); //user guide보였던 것처럼 설정
                }
            }
        });

    }
}