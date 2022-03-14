package com.example.paimonshoot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;          //Biến chủ đề.
    private Paint paint;
    private Homu[] homus;           //Mảng enemy.
    private SharedPreferences prefs;//Biến tạo keyword lưu giá trị.
    private Random random;          //Hàm rand để tạo enemy ngẫu nhiên.
    private SoundPool soundPool;    //Định dạng âm thanh.
    private List<Bullet> bullets;   //Mảng đạn.
    private int sound;              //Âm thanh.
    private Flight flight;          //Máy bay.
    private GameActivity activity;  //Class activity.
    private Background bg1, bg2;    //2 background để lập lại nền khi di chuyển.
    private boolean isPlaying, isGameOver = false;  //Hàm check dừng, thua.
    private int screenX, screenY, score = 0;        //Độ dài trục xy, điểm khi chơi.
    public static float screenRatioX, screenRatioY; //Tỉ lệ tọa độ trục xy.


    public GameView(GameActivity activity, int screenX, int screenY) {

        super(activity);

        //Gán activity.
        this.activity = activity;

        //Keyword game.
        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);

        //Kiểm tra version SDK của thiết bị.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();

        }
        else
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        sound = soundPool.load(activity, R.raw.shoot, 1);

        //Thiết lập tỉ lệ khung hình với trục xy.
        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;

        //Khởi tạo background, máy bay, danh sách đạn.
        bg1 = new Background(screenX,screenY, getResources());
        bg2 = new Background(screenX,screenY, getResources());

        flight = new Flight(this, screenY, getResources());

        bullets = new ArrayList<>();

        bg2.x = screenX;

        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

        homus = new Homu[4];

        for (int i = 0; i < 4; i++){
            Homu homu = new Homu(getResources());
            homus[i] = homu;
        }

        random = new Random();
    }

    @Override
    public void run() {

        while(isPlaying){

            update();
            draw();
            sleep();
        }
    }

    private void update(){

        /*bg1.x -= 10 * screenRatioX;
        bg2.x -= 10 * screenRatioX;

        if(bg1.x + bg1.bg.getWidth() < 0){
            bg1.x = screenX;
        }

        if(bg2.x + bg2.bg.getWidth() < 0){
            bg2.x = screenX;
        }*/

        if(flight.isGoingUp){
            flight.y -= 20 * screenRatioY;
            flight.isGoingUp = false;
        }
        else if(flight.isGoingDown){
            flight.y += 20 * screenRatioY;
            flight.isGoingDown = false;
        }

        if (flight.y < 0)
            flight.y = 0;
        if (flight.y >= 580)
            flight.y = 580;

        /*if(flight.y > screenY - flight.h)
            flight.y = screenY - flight.h;*/

        List<Bullet> trash = new ArrayList<>();

        for (Bullet bullet : bullets){

            if (bullet.x > screenX)
                trash.add(bullet);

            bullet.x += 10 * screenRatioX;

            //Vòng lặp kiểm tra tọa độ enemy với đạn va chạm.
            for (Homu homu : homus){
                if(Rect.intersects(homu.getCollisionShape(), bullet.getCollisionShape())){
                    score++;
                    homu.x -= 1500;
                    bullet.x = screenX + 500;
                    homu.wasShot = true;
                }
            }
        }

        //Xóa những đạn trong lish trash ra khỏi list đạn.
        for (Bullet bullet : trash)
            bullets.remove(bullet);

        for(Homu homu :homus ){

            homu.x -= homu.speed;

            if(homu.x + homu.w < 0){

                //Khi enemy vượt qua máy bay = Lose.
                /*if(!homu.wasShot){
                    isGameOver = true;
                    return;
                }*/

                //Random tốc độ enemy.
                /*int bound = (int) (30 *  screenRatioX);
                homu.speed = random.nextInt(bound);

                if(homu.speed < 10 * screenRatioX)
                    homu.speed = (int) (10 * screenRatioX);*/

                homu.x = screenX;
                homu.y = random.nextInt(screenY - homu.h);

                homu.wasShot = false;
            }

            //Kiểm tra tọa độ enemy với máy bay va chạm.
            if (Rect.intersects(homu.getCollisionShape(), flight.getCollisionShape())){
                isGameOver = true;
                return;
            }
        }
    }

    private void draw(){

        if(getHolder().getSurface().isValid()){
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(bg1.bg, bg1.x, bg1.y, paint);
            canvas.drawBitmap(bg2.bg, bg2.x, bg2.y, paint);

            for(Homu homu : homus)
                canvas.drawBitmap(homu.getHomu(), homu.x, homu.y, paint);

            canvas.drawText(score + "", screenX / 2f, 164, paint);

            if(isGameOver){
                isPlaying = false;
                canvas.drawBitmap(flight.getDead(), flight.x, flight.y, paint);
                getHolder().unlockCanvasAndPost(canvas);
                saveIfHighScore();
                waitBeforeExiting();
                return;
            }

            canvas.drawBitmap(flight.getFlight(), flight.x, flight.y, paint);

            for (Bullet bullet : bullets){
                canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y, paint);
            }

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void waitBeforeExiting() {

        try {
            //Time chờ trước khi về màn hình chính.
            Thread.sleep(3000);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void saveIfHighScore() {

        if(prefs.getInt("highscore", 0) < score){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }
    }

    private void sleep(){

        //Độ trễ chờ.
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume(){

        //Trở lại chơi game.
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause(){

        //Pause game.
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(event.getY() > screenY / 2 && event.getX() < screenX / 2){
                    flight.isGoingDown = true;
                    flight.toShoot++ ;
                }
                break;

            /*case MotionEvent.ACTION_UP:
                flight.isGoingUp = false;
                if(event.getX() > screenX / 2)
                    flight.toShoot++;
                break;*/

            case MotionEvent.ACTION_UP:
                if(event.getY() < screenY / 2 && event.getX() < screenX / 2) {
                    flight.isGoingUp = true;
                    flight.toShoot++ ;
                }
                break;

            /*case MotionEvent.ACTION_BUTTON_PRESS:
                    flight.toShoot++;
                break;*/
        }

        return true;
    }

    public void newBullet() {

        if(!prefs.getBoolean("isMute", false)){
            soundPool.play(sound, 1, 1, 0, 0, 1);
        }

        Bullet bullet = new Bullet(getResources());
        bullet.x = flight.x + flight.w;
        bullet.y = flight.y + (flight.h / 2);
        bullets.add(bullet);
    }
}