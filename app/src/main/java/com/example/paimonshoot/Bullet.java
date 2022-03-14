package com.example.paimonshoot;

import static com.example.paimonshoot.GameView.screenRatioX;
import static com.example.paimonshoot.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Bullet {

    int x, y, w, h; //Biến tọa độ xy, biến kích thước wh.
    Bitmap bullet;  //Biến vẽ điểm ảnh của viên đạn.

    //Constructor của class (nguồn tài nguyên)
    Bullet(Resources res){

        //Xét hình ảnh viên đạn theo hình trong res.
        bullet = BitmapFactory.decodeResource(res, R.drawable.shoot1);

        //Lấy độ dài rộng.
        w = bullet.getWidth();
        h = bullet.getHeight();

        //Giảm kích thước viên đạn xuống gấp 6 lần.
        w /= 6;
        h /= 6;

        //Kích thước viên đạn * tỉ lệ khung hình để độ dài rộng tương thích với trục xy.
        w *= (int) screenRatioX;
        h *= (int) screenRatioY;

        //Tạo tỉ lệ viên đạn dựa trên độ dài rộng.
        bullet = Bitmap.createScaledBitmap(bullet, w, h, false);
    }

    //Hàm để kiểm tra vị trí tọa độ viên đạn trong quá trình di chuyển.
    Rect getCollisionShape(){

        return new Rect(x, y, x + w, y + h);
    }
}