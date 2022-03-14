package com.example.paimonshoot;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Background {

    int x = 0, y = 0; //Biến tọa độ x, y.
    Bitmap bg;        //Biến vẽ điểm ảnh của background.

    //Constructor của class (độ dài trục x, độ dài trục y, nguồn tài nguyên)
    Background(int screenX, int screenY, Resources res){

        //Xét ảnh background khi gọi hàm.
        bg = BitmapFactory.decodeResource(res, R.drawable.background2);
        //Tạo background theo tỉ lệ dài trục x, y.
        bg = Bitmap.createScaledBitmap(bg, screenX, screenY, false);
    }
}