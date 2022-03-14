package com.example.paimonshoot;

import static com.example.paimonshoot.GameView.screenRatioX;
import static com.example.paimonshoot.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Homu {

    public int speed = 9;              //Tốc độ di chuyển của enemy.
    public boolean wasShot = true;      //Biến dùng kiểm tra xem enemy đã va chạm với đạn chưa.
    int x = 0, y, w, h, homuCount = 1;  //Hàm đếm số lượng enemy.
    Bitmap homu1, homu2, homu3, homu4;  //Biển vẽ điểm ảnh của 4 enemy (Enemy sẽ xuất hiện ở 4 line).

    //Constructor của class (nguồn tài nguyên)
    Homu(Resources res){

        //Xét hình ảnh cho 4 enemy.
        homu1 = BitmapFactory.decodeResource(res, R.drawable.homu1);
        /*homu2 = BitmapFactory.decodeResource(res, R.drawable.homu1);
        homu3 = BitmapFactory.decodeResource(res, R.drawable.homu1);
        homu4 = BitmapFactory.decodeResource(res, R.drawable.homu1);*/

        //Lưu lại kích thước của enemy.
        w = homu1.getWidth();
        h = homu1.getHeight();

        //Giảm kích thước enemy xuống 6 lần.
        w /= 6;
        h /= 6;

        //Kích thước enemy * tỉ lệ khung hình để độ dài rộng tương thích với trục xy.
        w *= (int) screenRatioX;
        h *= (int) screenRatioY;

        //Tạo tỉ lệ enemy dựa trên độ dài rộng.
        homu1 = Bitmap.createScaledBitmap(homu1, w, h, false);
        /*homu2 = Bitmap.createScaledBitmap(homu2, w, h, false);
        homu3 = Bitmap.createScaledBitmap(homu3, w, h, false);
        homu4 = Bitmap.createScaledBitmap(homu4, w, h, false);*/

        //Enemy sẽ xuất hiện từ trái sang phải ở màn hình nằm ngang.
        y = -h;
    }

    //Hàm tạo enemy theo count.
    Bitmap getHomu() {

        if(homuCount == 1){
            homuCount++;
            return homu1;
        }

        /*if(homuCount == 2){
            homuCount++;
            return homu2;
        }

        if(homuCount == 3){
            homuCount++;
            return homu3;
        }*/

        homuCount = 1;

        //return homu4;
        return homu1;
    }

    //Hàm để kiểm tra vị trí tọa độ enemy trong quá trình di chuyển.
    Rect getCollisionShape(){

        return new Rect(x, y, x + w, y + h);
    }
}