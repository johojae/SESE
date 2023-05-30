package com.sese.showmethebeer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sese.showmethebeer.data.DetailBeerInfo;
import com.sese.showmethebeer.manager.DetailBeerInfoHelper;
import com.sese.showmethebeer.serverConn.ServerManager;
import com.sese.showmethebeer.sqlite.SQLiteManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DetailBeerInfoActivity extends AppCompatActivity {

    private SQLiteManager sqLiteManager;

    //private ActivityMainBinding binding;
    String beerId = "test";
    DetailBeerInfo objDetailBeerInfo;

    //flag data
    boolean markingState_marked = false;

    //view
    ImageView beerImageView = null;
    TextView beerNameText = null;
    RatingBar ratingBar = null;
    ImageView bookMark = null;

    TextView categoryTextView = null;

    TextView alcoholicityTextView = null;

    TextView countryTextView = null;

    TextView refreshMentTextView = null;
    ImageView refreshMentImageView = null;

    TextView manufactureTextView = null;
    TextView beerDescriptionTextView = null;


    FloatingActionButton scanFloatingBt = null;
    Toast toast = null;
    Context context = null;

    private static final int MESSAGE_ID_UPDATE_DETAIL_BEER_INFO = 0;

    final Handler handler = new Handler(){
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg){
            int messageId = msg.what;

            switch (messageId) {
                case MESSAGE_ID_UPDATE_DETAIL_BEER_INFO:
                    showDetailBeerInfo();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_beer_info);

        context = this;
        App app = (App)getApplication();
        sqLiteManager = app.getSQLiteManager();

        Intent intent = getIntent();
        String barcode = "8801021213217"; //intent.getStringExtra(Constants.KEY_BARCODE); //8712000010249

        //sendData(barcode);


        Toast.makeText(this, "barcode :: " + barcode, 60*60).show();

        beerImageView = findViewById(R.id.beerImageView);
        beerNameText = findViewById(R.id.beerNameText);
        ratingBar = findViewById(R.id.ratingBar);
        bookMark = findViewById(R.id.markingStateView);
        categoryTextView = findViewById(R.id.categoryTextView);
        alcoholicityTextView = findViewById(R.id.alcoholicityTextView);
        countryTextView = findViewById(R.id.countryTextView);
        manufactureTextView = findViewById(R.id.manufactureTextView);
        refreshMentTextView = findViewById(R.id.refreshMentTextView);
        refreshMentImageView = findViewById(R.id.refreshMentImageView);

        beerDescriptionTextView = findViewById(R.id.beerDescriptionTextView);
        scanFloatingBt = findViewById(R.id.scanFloatingBt);

        JSONObject testJson = new DetailBeerInfo().getTestData();
        objDetailBeerInfo = new DetailBeerInfo(testJson);


        handleBookMarkView();
        scanFloatingBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BeerClassifierActivity.class);
                startActivity(intent);
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                int rating = (int) (v*2); //int값으로 저장
                sqLiteManager.saveRating(objDetailBeerInfo.getBeerId(), rating);

                showToast(R.string.text_save_rating);

                if (!markingState_marked) { //rating 저장하면 자동으로 book mark되도록 함
                    bookMark.setImageResource(R.drawable.book_marked);
                    markingState_marked = true;
                }
            }
        });

        
        //Server에서 DetailBeerInfo를 get해 온 후 , 맞게 UPDATE함.
        showDetailBeerInfo();

    }

    private void showDetailBeerInfo() {
        if (objDetailBeerInfo != null) {
            String thumbnailUrl = objDetailBeerInfo.getThumbnail();

            if (thumbnailUrl != null && thumbnailUrl.length() > 0) {
                ImageLoadTask task = new ImageLoadTask(objDetailBeerInfo.getThumbnail(), beerImageView);
                task.execute();
            }
            beerNameText.setText(objDetailBeerInfo.getName());
            categoryTextView.setText(objDetailBeerInfo.getCategory());
            alcoholicityTextView.setText(objDetailBeerInfo.getAlcoholicity());
            countryTextView.setText(objDetailBeerInfo.getCountry());
            manufactureTextView.setText(objDetailBeerInfo.getManufacturer());
            beerDescriptionTextView.setText(objDetailBeerInfo.getDescription());

            showRefrementInfo(objDetailBeerInfo.getRefreshmentLevel());
        }
    }

    private void showRefrementInfo(int refreshMentLevel) {
        int drawableId = 1;
        int levelTextId = 1;
        switch (refreshMentLevel) {
            case 1:
                drawableId = R.drawable.freshness1;
                levelTextId = R.string.refreshness1_info;
                break;
            case 2:
                drawableId = R.drawable.freshness2;
                levelTextId = R.string.refreshness2_info;
                break;
            case 3:
                drawableId = R.drawable.freshness3;
                levelTextId = R.string.refreshness3_info;
                break;
            case 4:
                drawableId = R.drawable.freshness4;
                levelTextId = R.string.refreshness4_info;
                break;
            case 5:
                drawableId = R.drawable.freshness5;
                levelTextId = R.string.refreshness5_info;
                break;

        }

        refreshMentTextView.setText(levelTextId);
        refreshMentImageView.setImageResource(drawableId);
    }

    private void updateSaveMarkState(int rating) {
        if (markingState_marked) {
            bookMark.setImageResource(R.drawable.book_unmarked);
            sqLiteManager.deleteBeer(objDetailBeerInfo.getBeerId());
        } else {
            bookMark.setImageResource(R.drawable.book_marked);
            sqLiteManager.saveRating(objDetailBeerInfo.getBeerId(), rating);
        }

        markingState_marked = (!markingState_marked);

        int textId = (markingState_marked)? R.string.text_save_beer : R.string.text_delete_beer;

        if (markingState_marked) {
            showToast(textId);
        } else {
            if (toast != null)
                toast.cancel();
        }
    }

    private void handleBookMarkView () {
        bookMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int rating = (int) (ratingBar.getRating() * 2);

                if (markingState_marked) {
                    if (rating != 0) { //rating이 있는 경우는 삭제하기 전에 다시 확인
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(R.string.dialog_info_title).setMessage(R.string.dialog_delete_confirm);
                        //사용자가 삭제하고자 하는 경우
                        builder.setPositiveButton(R.string.text_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ratingBar.setRating(0);

                                bookMark.setImageResource(R.drawable.book_unmarked);
                                sqLiteManager.deleteBeer(objDetailBeerInfo.getBeerId());
                                updateSaveMarkState(rating);
                            }
                        });
                        //사용자가 삭제 하지 않는 경우
                        builder.setNegativeButton(R.string.text_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    } else {
                        bookMark.setImageResource(R.drawable.book_unmarked);
                        sqLiteManager.deleteBeer(objDetailBeerInfo.getBeerId());
                        updateSaveMarkState(rating);
                    }
                } else {
                    bookMark.setImageResource(R.drawable.book_marked);
                    sqLiteManager.saveRating(objDetailBeerInfo.getBeerId(), rating);
                    updateSaveMarkState(rating);
                }
            }
        });
    }

    private void showToast(int textId) {
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(this, textId, Toast.LENGTH_SHORT);
        toast.show();
    }

    /** 웹 서버로 데이터 전송 */
    private void sendData(String barcode) {
        new Thread() {
            public void run() {
                //파라미터 2개와 미리정의해논 콜백함수를 매개변수로 전달하여 호출
                ((App)getApplication()).getServerMngr()
                        .send(ServerManager.SUB_API_INFO_BY_BARCODE + barcode, getCallback());
            }
        }.start();
    }

    private Callback getCallback() {
        return new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                System.out.println("DetailBeerInfoActivity::onResponse:" + response);
                if (response.code() == 200) {
                    // Get response
                    String jsonData = response.body().string();
                    System.out.println("serverUrl::" + jsonData);
                    try {
                        objDetailBeerInfo
                                = new DetailBeerInfoHelper()
                                .getDetailBeerInfo(new JSONObject(jsonData));
                        handler.sendEmptyMessage(MESSAGE_ID_UPDATE_DETAIL_BEER_INFO);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {

                }

            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("DetailBeerInfoActivity::onFailure:" + e.getLocalizedMessage());
            }
        };
    }
}