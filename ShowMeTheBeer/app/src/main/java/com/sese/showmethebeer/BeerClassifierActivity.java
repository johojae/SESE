package com.sese.showmethebeer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class BeerClassifierActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_classifier);

        scanBeerBarcode();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if(requestCode == IntentIntegrator.REQUEST_CODE) {
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if (result != null) {
                    if (result.getContents() != null) {
                        Toast.makeText(this, "Scanned: " + result.getContents() + "\nFormat:" + result.getFormatName(), Toast.LENGTH_LONG).show();
                        String barcode = result.getContents();

                        Intent intent = new Intent(getApplicationContext(), DetailBeerInfoActivity.class);
                        intent.putExtra(Constants.KEY_BARCODE, barcode);
                        startActivity(intent);
                    }
                }
            }
        } else { //error dialog ǥ��
            showErrorDialog();
        }
    }


    private void showErrorDialog() { //���� �߻�
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.dialog_error_title).setMessage(R.string.dialog_error_try_again);

        //����ڰ� ��õ��ϰ��� �ϴ� ���
        builder.setPositiveButton(R.string.text_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scanBeerBarcode();
            }
        });
        //����ڰ� ��õ��ϰ��� ���� �ʴ� ��� 
        builder.setNegativeButton(R.string.text_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish(); //activity �����Ŵ
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void scanBeerBarcode() {
        new IntentIntegrator(BeerClassifierActivity.this)
                .setBeepEnabled(false)
                .initiateScan();
    }
}