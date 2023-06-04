package com.sese.showmethebeer.manager;

import android.widget.ImageView;

import java.util.ArrayList;

public class ImageLoadTaskManager { //사용할지 아직 고민 중

    private ArrayList<ImageLoadTask> imgLoadTaskList = new ArrayList<>();

    // 어떤 url 로 요청할 지, 응답을 받은 후 어떤 이미지뷰에 설정할 지 전달받음
    public ImageLoadTaskManager(){
    }

    public ImageLoadTask createImageLoadTask(String urlStr, ImageView imageView) {
        ImageLoadTask imgLoadTask = new ImageLoadTask(urlStr, imageView);
        imgLoadTaskList.add(imgLoadTask);

        return imgLoadTask;
    }

    public void cancelAllImageLoadTask() {
        for (int i = 0; i < imgLoadTaskList.size(); i++) {
            ImageLoadTask imgLoadTask = imgLoadTaskList.get(i);
            if (imgLoadTask.isCancelled()) {
                imgLoadTask.cancel(true);
            }
        }
    }


}
