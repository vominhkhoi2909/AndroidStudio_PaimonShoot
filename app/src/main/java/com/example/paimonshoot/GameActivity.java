package com.example.paimonshoot;

import android.graphics.Point;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    private com.example.paimonshoot.GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Lấy thông tin kích thước màn hình.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Tạo biến và lưu tọa độ của thiết bị.
        Point point= new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        //Khởi tạo class gameview với activity hiện tại, tọa độ x, y của thiết bị.
        gameView = new com.example.paimonshoot.GameView(this, point.x, point.y);

        //Xét nội dung hiển thị.
        setContentView(gameView);
    }

    //Ghi đề hàm gọi hàm onPause đã định dạng nghĩa trước đó.
    @Override
    protected void onPause() {

        super.onPause();
        gameView.pause();
    }

    //Ghi đề hàm gọi hàm onResume đã định dạng nghĩa trước đó.
    @Override
    protected void onResume() {

        super.onResume();
        gameView.resume();
    }
}