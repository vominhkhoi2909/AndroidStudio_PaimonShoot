package com.example.paimonshoot;

import static com.example.paimonshoot.GameView.screenRatioX;
import static com.example.paimonshoot.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Flight {

    int toShoot = 0; //Số hiệu ứng bắn.
    boolean isGoingUp = false; //Kiểm tra di chuyển.
    boolean isGoingDown = false;
    int x, y, w, h, wingCounter = 0, shootCounter = 1; //Biến tọa độ xy, biến kích thước wh, biến đếm máy bay wingcounter, biến đếm hiệu ứng bắn shootcounter.
    Bitmap flight1, flight2, shoot1, shoot2, shoot3, shoot4, shoot5, dead; //Biến tọa độ cho máy bay, đạn, icon dead.
    private com.example.paimonshoot.GameView gameView; //Gọi class.

    //Constructor của class (class điều khiển, độ dài trục y, nguồn tài nguyên)
    Flight(com.example.paimonshoot.GameView gameView, int screenY, Resources res){

        this.gameView = gameView;

        //Xét hình ảnh máy bay, hiệu ứng bắn, dead theo hình trong res.
        flight1 = BitmapFactory.decodeResource(res, R.drawable.paimon);
        //flight2 = BitmapFactory.decodeResource(res, R.drawable.paimon2);

        shoot1 = BitmapFactory.decodeResource(res, R.drawable.paimon2);
        /*shoot2 = BitmapFactory.decodeResource(res, R.drawable.shoot2);
        shoot3 = BitmapFactory.decodeResource(res, R.drawable.shoot3);
        shoot4 = BitmapFactory.decodeResource(res, R.drawable.shoot4);
        shoot5 = BitmapFactory.decodeResource(res, R.drawable.shoot5);*/

        dead = BitmapFactory.decodeResource(res, R.drawable.boom);

        //Lưu kích thước của máy bay.
        w = flight1.getWidth();
        h = flight1.getHeight();

        //Giảm kích thước máy bay xuống 6 lần.
        w /= 6;
        h /= 6;

        //Kích thước máy bay * tỉ lệ khung hình để độ dài rộng tương thích với trục xy.
        w *= (int) screenRatioX;
        h *= (int) screenRatioY;

        //Vẽ tỉ lệ máy bay, hiệu ứng bắn, dead theo kích thước với máy bay.
        flight1 = Bitmap.createScaledBitmap(flight1, w, h, false);
        //flight2 = Bitmap.createScaledBitmap(flight2, w, h, false);

        shoot1 = Bitmap.createScaledBitmap(shoot1, w, h, false);
        /*shoot2 = Bitmap.createScaledBitmap(shoot2, w, h, false);
        shoot3 = Bitmap.createScaledBitmap(shoot3, w, h, false);
        shoot4 = Bitmap.createScaledBitmap(shoot4, w, h, false);
        shoot5 = Bitmap.createScaledBitmap(shoot5, w, h, false);*/

        dead = Bitmap.createScaledBitmap(dead, w, h, false);

        y = screenY / 2;
        x = (int) (64 * screenRatioX);
    }

    //Hàm tạo hiệu ứng bắn.
    Bitmap getFlight(){

        if(toShoot != 0){

            /*if(shootCounter == 1){
                shootCounter++;
                return shoot1;
            }

            if(shootCounter == 2){
                shootCounter++;
                return shoot2;
            }

            if(shootCounter == 3){
                shootCounter++;
                return shoot3;
            }

            if(shootCounter == 4){
                shootCounter++;
                return shoot4;
            }

            shootCounter = 1;
            toShoot--;*/

            if(toShoot % 3 == 0){
                gameView.newBullet();
            }

            /*return shoot5;
            return shoot1;*/
        }

        /*if(wingCounter == 0){
            wingCounter++;
            return flight1;
        }

        wingCounter--;

        return flight2;*/
        return flight1;
    }

    //Hàm để kiểm tra vị trí tọa độ máy bay trong quá trình di chuyển.
    Rect getCollisionShape(){

        return new Rect(x, y, x + w, y + h);
    }

    //Geter của dead.
    Bitmap getDead (){

        return dead;
    }
}