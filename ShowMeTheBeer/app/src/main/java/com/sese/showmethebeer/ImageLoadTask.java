package com.sese.showmethebeer;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.net.URL;
import java.util.HashMap;

public class ImageLoadTask extends AsyncTask <Void, Void, Bitmap> {
    private String urlStr;
    private ImageView imageView;
    private HashMap<String, Bitmap> hashMap = new HashMap<>();

    // 어떤 url 로 요청할 지, 응답을 받은 후 어떤 이미지뷰에 설정할 지 전달받음
    public ImageLoadTask(String urlStr, ImageView imageView){
        this.urlStr = urlStr;
        this.imageView = imageView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        // 웹서버의 이미지 데이터를 받아 비트맵 객체로 만든다.
        Bitmap bitmap = null;
        try {
            // 메모리에 만들어진 후 해제되지 않으면 메모리에 계속 남아있는다.
            // 여러 이미지를 로딩하게 되면 메모리가 부족해지는 문제가 발생할 수 있으므로
            // 사용하지 않는 비트맵 객체는 recycle() 메소드를 이용해 즉시 해제시킨다.
            if(hashMap.containsKey(urlStr)){  // 요청 주소가 들어있다면 비트맵을 꺼냄
                Bitmap oldBitmap = hashMap.remove(urlStr);
                if(oldBitmap != null){
                    oldBitmap.recycle();    // 들어왔던 비트맵을 메모리에 제거
                    oldBitmap = null;
                }
            }

            URL url = new URL(urlStr);
            // 주소로 접속하여 이미지 스트림을 받고, decodeStream 을 통해 비트맵으로 바꿈
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            hashMap.put(urlStr, bitmap); // 새 비트맵을 넣음
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        imageView.setImageBitmap(bitmap);   // 비트맵을 이미지뷰에 설정
        imageView.invalidate(); // 이미지를 다시 그림
    }
}
