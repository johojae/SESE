package com.sese.showmethebeer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sese.showmethebeer.data.DetailBeerInfo;
import com.sese.showmethebeer.manager.DetailBeerInfoHelper;
import com.sese.showmethebeer.manager.ImageLoadTask;
import com.sese.showmethebeer.manager.ImageLoadTaskManager;
import com.sese.showmethebeer.manager.NetworkConnectionUtil;
import com.sese.showmethebeer.serverConn.ServerManager;
import com.sese.showmethebeer.sqlite.SQLiteManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DetailBeerInfoActivity extends AppCompatActivity {

    private SQLiteManager sqLiteManager;

    DetailBeerInfo objDetailBeerInfo;
    ImageLoadTaskManager imgLoadTaskMngr;

    //flag data
    boolean markingState_marked = false;

    View detailInfoNoNetworkLayout = null;
    RelativeLayout detailInfoFullLayout = null;

    //view
    ImageView beerImageView = null;
    TextView beerNameText = null;
    TextView beerNameEngText = null;
    RatingBar ratingBar = null;
    ImageView bookMark = null;

    TextView categoryTextView = null;

    TextView alcoholicityTextView = null;

    TextView countryTextView = null;

    TextView refreshMentText = null;
    TextView refreshMentTextView = null;
    ImageView refreshMentImageView = null;

    TextView ibuText = null;
    TextView ibuTextView = null;
    TextView ibuDescriptionText = null;
    ImageView ibuDescriptionImageView = null;

    TextView manufactureTextView = null;
    TextView beerDescriptionTextView = null;

    Call beerInfoCall = null;

    ArrayList<DetailBeerInfo> similarBeerInfos = null;

    FloatingActionButton scanFloatingBt = null;
    Toast toast = null;
    Context context = null;

    private static final int MESSAGE_ID_DIALOG_START = -1;
    private static final int MESSAGE_ID_UPDATE_DETAIL_BEER_INFO = 0;
    private static final int MESSAGE_ID_DIALOG_ERROR_NOT_FOUND_BEER = 1;
    private static final int MESSAGE_ID_DIALOG_ERROR_OTHERS = 2;

    private static final int MESSAGE_ID_DIALOG_NO_NETWORK = 3;
    private static final int MESSAGE_ID_DIALOG_END = 4;


    //scan error dialog
    private static final int MESSAGE_ID_DIALOG_NO_CAM_PERMISSION = 0;
    private static final int MESSAGE_ID_DIALOG_TIMEOUT = 1;
    private static final int MESSAGE_ID_DIALOG_ERROR_GENERAL = 2;
    //scan error dialog


    final Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg){
            int messageId = msg.what;

            switch (messageId) {
                case MESSAGE_ID_UPDATE_DETAIL_BEER_INFO:
                    showDetailBeerInfo();
                    break;
                case MESSAGE_ID_DIALOG_ERROR_NOT_FOUND_BEER:
                case MESSAGE_ID_DIALOG_ERROR_OTHERS:
                case MESSAGE_ID_DIALOG_NO_NETWORK:
                    showErrorDialog(messageId);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(Constants.TAG, "DetailBeerInfoActivity: onCreate");
        setContentView(R.layout.activity_detail_beer_info);

        TextView title = (TextView) findViewById(R.id.detail_title);
        title.setText("맥주 정보");

        ImageButton button = (ImageButton) findViewById(R.id.detail_home_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        context = this;
        App app = (App)getApplication();
        sqLiteManager = app.getSQLiteManager();
        imgLoadTaskMngr = new ImageLoadTaskManager();

        Intent intent = getIntent();

        //TODO , barcode에서 오는 것이랑 beerId로 오는 것이랑 따로 챙길 것. 그리고 호출한 activity이름 넘겨줄것
        String typeVal = intent.getStringExtra(Constants.INTENT_KEY_TYPE);
        String barcode = intent.getStringExtra(Constants.INTENT_KEY_BARCODE);
        String beerId = intent.getStringExtra(Constants.INTENT_KEY_BEERID);
        String from = intent.getStringExtra(Constants.INTENT_KEY_FROM);

        Log.i(Constants.TAG, "DetailBeerInfoActivity: typeVal = " + typeVal + "," +
                " barcode = " + barcode + ", beerId = " + beerId + " , from = " + from);

        detailInfoNoNetworkLayout = findViewById(R.id.detailInfoNoNetworkLayout);
        detailInfoFullLayout = findViewById(R.id.detailInfoFullLayout);

        beerImageView = findViewById(R.id.beerImageView);
        beerNameText = findViewById(R.id.beerNameText);
        beerNameEngText = findViewById(R.id.beerNameEngText);
        ratingBar = findViewById(R.id.ratingBar);
        bookMark = findViewById(R.id.markingStateView);
        categoryTextView = findViewById(R.id.categoryTextView);

        alcoholicityTextView = findViewById(R.id.alcoholicityTextView);
        countryTextView = findViewById(R.id.countryTextView);
        manufactureTextView = findViewById(R.id.manufactureTextView);

        refreshMentText = findViewById(R.id.refreshMentText);
        refreshMentTextView = findViewById(R.id.refreshMentTextView);
        refreshMentImageView = findViewById(R.id.refreshMentImageView);

        ibuText = findViewById(R.id.ibuText);
        ibuTextView = findViewById(R.id.ibuTextView);
        ibuDescriptionText = findViewById(R.id.ibuDescriptionText);
        ibuDescriptionImageView = findViewById(R.id.ibuDescriptionImageView);

        beerDescriptionTextView = findViewById(R.id.beerDescriptionTextView);
        scanFloatingBt = findViewById(R.id.scanFloatingBt);

        if (intent.getBooleanExtra(Constants.INTENT_KEY_TEST_MODE, false)) {
            JSONObject testJson = new DetailBeerInfo().getTestData();
            objDetailBeerInfo = new DetailBeerInfo(testJson);
        } else {
            if (barcode != null && !barcode.isEmpty()) { //barcode로 요청하는 경우
                requestDataByBarCode(barcode);
                //Toast.makeText(this, "barcode :: " + barcode, 60*60).show();
            } else if (beerId != null && !beerId.isEmpty()){
                requestDataByBeerId(beerId);
                //Toast.makeText(this, "beerId :: " + barcode, 60*60).show();
            }
        }

        scanFloatingBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BeerClassifierActivity.class);
                startActivity(intent);
                finish();
            }
        });

        if (typeVal != null && typeVal.equalsIgnoreCase(Constants.INTENT_VAL_SCAN)) {
            scanBeerBarcode();
            return;
        }
        //Server에서 DetailBeerInfo를 get해 온 후 , 맞게 UPDATE함.
        showDetailBeerInfo();
    }

    protected void onResume() {
        super.onResume();
        Log.i(Constants.TAG, "DetailBeerInfoActivity: onResume");
    }

    protected void onDestroy() {
        if (handler != null) {
            for (int i = MESSAGE_ID_DIALOG_START + 1; i < MESSAGE_ID_DIALOG_END; i++) {
                handler.removeMessages(i);
            }
        }

        if (beerInfoCall != null) {
            beerInfoCall.cancel();
        }

        if (imgLoadTaskMngr != null) {
            imgLoadTaskMngr.cancelAllImageLoadTask();
        }

        clearBitMap(beerImageView);
        clearBitMap(findViewById(R.id.smilarBeerImageView_1));
        clearBitMap(findViewById(R.id.smilarBeerImageView_2));
        clearBitMap(findViewById(R.id.smilarBeerImageView_3));

        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(Constants.TAG, "DetailBeerInfoActivity :: resultCode::" + resultCode + ", data: " + data);
        if (data != null) {
            Log.i(Constants.TAG, "DetailBeerInfoActivity :: resultCode cameraPermission::" + data.getBooleanExtra(Intents.Scan.MISSING_CAMERA_PERMISSION, false));
            Log.i(Constants.TAG, "DetailBeerInfoActivity :: resultCode timeout::" + data.getBooleanExtra(Intents.Scan.TIMEOUT, false));

        }

        if (resultCode == RESULT_OK) {
            if(requestCode == IntentIntegrator.REQUEST_CODE) {
                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if (result != null) {
                    String barcode = result.getContents();
                    if (barcode != null && !barcode.isEmpty()) { //barcode로 요청하는 경우
                        requestDataByBarCode(barcode);
                    }
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (data == null) { //backPressed
                finish();
            } else {
                boolean missingCameraPermission = data.getBooleanExtra(Intents.Scan.MISSING_CAMERA_PERMISSION, false);
                boolean scanTimeOut = data.getBooleanExtra(Intents.Scan.TIMEOUT, false);
                if (missingCameraPermission) {
                    showScanErrorDialog(MESSAGE_ID_DIALOG_NO_CAM_PERMISSION);
                } else if (scanTimeOut) {
                    showScanErrorDialog(MESSAGE_ID_DIALOG_ERROR_GENERAL);
                } else {
                    //backPressed
                    finish();
                }
            }
        } else { //error dialog 표시
            showScanErrorDialog(MESSAGE_ID_DIALOG_ERROR_GENERAL);
        }
    }

    private void showScanErrorDialog(int errorType) { //에러 발생
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.dialog_error_title);

        if (errorType == MESSAGE_ID_DIALOG_NO_CAM_PERMISSION) {
            builder.setMessage(R.string.dialog_error_camera_permission);

            //사용자가 재시도하고자 하는 경우
            builder.setPositiveButton(R.string.text_confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish(); //activity 종료시킴
                }
            });

        } else {
            builder.setMessage(R.string.dialog_error_try_again);

            //사용자가 재시도하고자 하는 경우
            builder.setPositiveButton(R.string.text_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    scanBeerBarcode();
                }
            });
            //사용자가 재시도하고자 하지 않는 경우
            builder.setNegativeButton(R.string.text_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish(); //activity 종료시킴
                }
            });

        }

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void scanBeerBarcode() {
        new IntentIntegrator(DetailBeerInfoActivity.this)
                .setBeepEnabled(false)
                .initiateScan();
    }

    private void clearBitMap(ImageView iv) {
        Log.i(Constants.TAG, "clearBitMap");
        if (iv == null) {
            return;
        }

        Drawable d = iv.getDrawable();
        if (d != null && d instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
            bitmap.recycle();
            bitmap = null;
            d.setCallback(null);
        }
    }

    private String getCategoryText(String categoryId) {
        try {
            BeerCategoryJsonParser beerCategoryJsonParser = new BeerCategoryJsonParser(this);
            List<CategoryItem> categoryItemLists = beerCategoryJsonParser.GetCategoryItemLists();;

            String parentCategory = beerCategoryJsonParser.GetParentCategoryName(categoryItemLists, categoryId);
            String detailCategory = beerCategoryJsonParser.GetDetailCategoryName(categoryItemLists, categoryId);

            if (parentCategory != null && detailCategory != null) {
                return parentCategory + " > " + detailCategory;
            } else {
                if (parentCategory == null) {
                    return detailCategory;
                } else {
                    return parentCategory;
                }
            }
        } catch (Exception e) {
            return "";
        }
    }

    private void showDetailBeerInfo() {

        boolean networkConnected = NetworkConnectionUtil.isNetworkAvailable(context);

        Log.i(Constants.TAG, "DetailBeerInfoActivity: isNetworkAvailable::" + networkConnected);

        detailInfoNoNetworkLayout.setVisibility(networkConnected? View.GONE : View.VISIBLE);
        Log.i(Constants.TAG, "DetailBeerInfoActivity detailInfoNoNetworkLayout::" + detailInfoNoNetworkLayout.getVisibility());
        detailInfoFullLayout.setVisibility(networkConnected? View.VISIBLE : View.GONE);

        if (!networkConnected) {
            return;
        }
        
        handleBookMarkAndRatingView();

        if (objDetailBeerInfo != null) {
            String thumbnailUrl = objDetailBeerInfo.getThumbnail();

            if (thumbnailUrl != null && thumbnailUrl.length() > 0) {
                ImageLoadTask task = imgLoadTaskMngr.createImageLoadTask(thumbnailUrl, beerImageView, R.drawable.app_icon);
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
            beerNameText.setText(objDetailBeerInfo.getName());

            String engName = objDetailBeerInfo.getEngName();
            if (engName != null && !engName.isEmpty()) {
                beerNameEngText.setVisibility(View.VISIBLE);
                beerNameEngText.setText(engName);
            } else {
                beerNameEngText.setVisibility(View.GONE);
            }

            String categoryText = getCategoryText(objDetailBeerInfo.getCategoryId());
            if (categoryText != null && !categoryText.isEmpty()) {
                SpannableStringBuilder ssb = new SpannableStringBuilder();
                SpannableStringBuilder append = ssb.append(categoryText);
                ssb.setSpan(new URLSpan("#"), 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                categoryTextView.setText(ssb, TextView.BufferType.SPANNABLE);
            }

            categoryTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //beerList activity 띄우기 TODO
                    if (!NetworkConnectionUtil.isNetworkAvailable(DetailBeerInfoActivity.this)) {
                        showNoNetworkDialog();
                        return;
                    }
                    Intent intent = new Intent(getApplicationContext(), BeerListActivity.class);
                    intent.putExtra(Constants.INTENT_KEY_CALLER, Constants.INTENT_VAL_CATEGORY);
                    intent.putExtra(Constants.INTENT_KEY_CATEGORY_ID, objDetailBeerInfo.getCategoryId());
                    startActivity(intent);
                }
            });
            alcoholicityTextView.setText(objDetailBeerInfo.getAlcoholicity());
            countryTextView.setText(objDetailBeerInfo.getCountry());
            manufactureTextView.setText(objDetailBeerInfo.getManufacturer());
            beerDescriptionTextView.setText(objDetailBeerInfo.getDescription());

            String ibu = objDetailBeerInfo.getIbu();
            if (ibu != null && !ibu.isEmpty()) {
                ibuText.setVisibility(View.VISIBLE);
                ibuTextView.setVisibility(View.VISIBLE);
                ibuDescriptionText.setVisibility(View.VISIBLE);
                ibuDescriptionImageView.setVisibility(View.VISIBLE);
                ibuTextView.setText(ibu);
            } else {
                ibuText.setVisibility(View.GONE);
                ibuTextView.setVisibility(View.GONE);
                ibuDescriptionText.setVisibility(View.GONE);
                ibuDescriptionImageView.setVisibility(View.GONE);
            }

            ibuDescriptionImageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return false;
                }
            });

            showCarbonicAcideInfo(objDetailBeerInfo.getCarbonicAcidLevel());

            similarBeerInfos = objDetailBeerInfo.getRelatedBeersInfos();
            if (similarBeerInfos != null && similarBeerInfos.size() > 0) {
                TextView smilarBeerText = findViewById(R.id.smilarBeerText);
                smilarBeerText.setVisibility(View.VISIBLE);

                TableLayout similarBeerTableLayout = findViewById(R.id.similarBeerTableLayout);
                similarBeerTableLayout.setVisibility(View.VISIBLE);
                showSimilarBeer();
            }
        }
    }

    private void showCarbonicAcideInfo(int carbonicAcidLevel) {
        int visible = (carbonicAcidLevel >= 1)? View.VISIBLE : View.GONE;
        refreshMentText.setVisibility(visible);
        refreshMentTextView.setVisibility(visible);
        refreshMentImageView.setVisibility(visible);

        int drawableId = 1;
        int levelTextId = 1;
        switch (carbonicAcidLevel) {
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
            default:
                return;

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

    private void handleBookMarkAndRatingView () {
        if (objDetailBeerInfo == null) {
            bookMark.setVisibility(View.GONE);
            ratingBar.setVisibility(View.GONE);
            return;
        } else {
            bookMark.setVisibility(View.VISIBLE);
            ratingBar.setVisibility(View.VISIBLE);
        }

        int rating = sqLiteManager.getRating(objDetailBeerInfo.getBeerId());
        Log.i(Constants.TAG, "handleBookMarkAndRatingView" + (rating/2));
        if (rating != -1) { //-1이면, DB에 없음, nothing to do
            markingState_marked = true;
            bookMark.setImageResource(R.drawable.book_marked);
            float rateVal = (rating/2);
            if (rating % 2 == 1) {
                rateVal += 0.5;
            }
            ratingBar.setRating(rateVal);
        } else {
            markingState_marked = false;
            bookMark.setImageResource(R.drawable.book_unmarked);
        }

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
    }

    private void showSimilarBeer() {
        if (similarBeerInfos == null || similarBeerInfos.isEmpty()) {
            return;
        }
        int similarBeerCount = similarBeerInfos.size();
        if (similarBeerCount >= 1) {
            showSimilarBeerSub(0, findViewById(R.id.smilarBeerImageView_1));
        }

        if (similarBeerCount >= 2) {
            showSimilarBeerSub(1, findViewById(R.id.smilarBeerImageView_2));
        }

        if (similarBeerCount >= 3) {
            showSimilarBeerSub(2, findViewById(R.id.smilarBeerImageView_3));
        }
    }

    private void showSimilarBeerSub(int listIndex, ImageView imgView) {
        DetailBeerInfo similarBeerInfo = similarBeerInfos.get(listIndex);
        String thumbnailUrl = similarBeerInfo.getThumbnail();

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetworkConnectionUtil.isNetworkAvailable(DetailBeerInfoActivity.this)) {
                    showNoNetworkDialog();
                    return;
                }

                Intent intent = new Intent(getApplicationContext(), DetailBeerInfoActivity.class);
                intent.putExtra(Constants.INTENT_KEY_BEERID, similarBeerInfo.getBeerId());
                startActivity(intent);
            }
        });

        if (thumbnailUrl != null && thumbnailUrl.length() > 0) {
            ImageLoadTask task = imgLoadTaskMngr.createImageLoadTask(thumbnailUrl, imgView, R.drawable.app_icon);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private void showNoNetworkDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(DetailBeerInfoActivity.this)
                .setTitle("네트워크 에러")
                .setMessage("네트워크가 연결 된 이후에 재시도 해주세요.")
                .setPositiveButton(R.string.text_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create()
                .show();
    }
    private void showToast(int textId) {
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(this, textId, Toast.LENGTH_SHORT);
        toast.show();
    }

    /** 웹 서버로 데이터 요청 전송 */
    private void requestDataByBarCode(String barcode) {
        new Thread() {
            public void run() {
                //파라미터 2개와 미리정의해논 콜백함수를 매개변수로 전달하여 호출
                beerInfoCall = ((App)getApplication()).getServerMngr()
                        .send(ServerManager.SUB_API_INFO_BY_BARCODE + barcode, getCallback());
            }
        }.start();
    }

    private void requestDataByBeerId(String beerId) {
        new Thread() {
            public void run() {
                //파라미터 2개와 미리정의해논 콜백함수를 매개변수로 전달하여 호출
                beerInfoCall = ((App)getApplication()).getServerMngr()
                        .send(ServerManager.SUB_API_INFO_BY_BEER_ID + beerId, getCallback());
            }
        }.start();
    }

    private Callback getCallback() {
        return new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                System.out.println("hyejeongyang :: DetailBeerInfoActivity::onResponse:" + response);
                if (response.code() == 200) {
                    // Get response
                    String jsonData = response.body().string();
                    System.out.println("hyejeongyang :: DetailBeerInfoActivity::" + jsonData);
                    try {
                        objDetailBeerInfo
                                = new DetailBeerInfoHelper()
                                .getDetailBeerInfo(new JSONObject(jsonData));

                        if (objDetailBeerInfo != null) {
                            handler.sendEmptyMessage(MESSAGE_ID_UPDATE_DETAIL_BEER_INFO);
                        } else {
                            handler.sendEmptyMessage(MESSAGE_ID_DIALOG_ERROR_OTHERS);
                        }
                    } catch (JSONException e) {
                        System.out.println("hyejeongyang :: DetailBeerInfoActivity:: Exception");
                        e.printStackTrace();
                        handler.sendEmptyMessage(MESSAGE_ID_DIALOG_ERROR_OTHERS);
                        throw new RuntimeException(e);
                    }
                } else if (response.code() == 404) {
                    handler.sendEmptyMessage(MESSAGE_ID_DIALOG_ERROR_NOT_FOUND_BEER);
                } else {
                    handler.sendEmptyMessage(MESSAGE_ID_DIALOG_ERROR_OTHERS);
                }

            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("DetailBeerInfoActivity::onFailure:" + e.getLocalizedMessage());

                if (call.isCanceled()) {
                    //ignore
                } else {
                    if (!NetworkConnectionUtil.isNetworkAvailable(DetailBeerInfoActivity.this)) {
                        handler.sendEmptyMessage(MESSAGE_ID_DIALOG_NO_NETWORK); //Network 연결 안되어 있을 때
                    } else {
                        handler.sendEmptyMessage(MESSAGE_ID_DIALOG_ERROR_OTHERS); //알 수 없는 에러가 발생한 경우
                    }
                }
            }
        };
    }

    private void showErrorDialog(int messageIdErrDialog) { //에러 발생
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.dialog_error_title);

        if (messageIdErrDialog == MESSAGE_ID_DIALOG_ERROR_NOT_FOUND_BEER) {
            builder.setMessage(R.string.dialog_error_beer_not_found);

            builder.setPositiveButton(R.string.text_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) { //TODO :: category activity에서 검색 메뉴로 jump
                    if (!NetworkConnectionUtil.isNetworkAvailable(DetailBeerInfoActivity.this)) {
                        showNoNetworkDialog();
                        return;
                    }
                    Intent intent = new Intent(getApplicationContext(), BeerCategoryActivity.class);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton(R.string.text_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish(); //activity 종료시킴
                }
            });

        } else if (messageIdErrDialog == MESSAGE_ID_DIALOG_NO_NETWORK) {
            builder.setTitle("네트워크 에러");
            builder.setMessage("네트워크가 연결 된 이후에 재시도 해주세요.");
            builder.setPositiveButton(R.string.text_confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        } else {
            builder.setMessage(R.string.dialog_error_server_error);

            builder.setPositiveButton(R.string.text_confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish(); //activity 종료시킴
                }
            });
        }

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
