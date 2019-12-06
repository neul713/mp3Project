package com.example.neul7.monkeymp3;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final String TAG = "Mp3Player";
    Button btnMyPlayList, btnPlay, btnPause, btnLike;
    RecyclerView recyclerView;
    TextView txtNowPlaying;
    SeekBar seekBar;

    public static ArrayList<SingData> list = new ArrayList<>();
    public static MyAdapter myAdapter;

    LinearLayoutManager linearLayoutManager;

    MyDBHelper myDBHelper;
    SQLiteDatabase sqLiteDatabase;

    private MediaPlayer mediaPlayer;
    private String seletedMP3;


    int position;

    String id, albumArt, title, artist, duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("TAG","onCreate()");


        btnMyPlayList = findViewById(R.id.btnMyPlayList);
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnLike = findViewById(R.id.btnLike);
        recyclerView = findViewById(R.id.recyclerView);
        txtNowPlaying = findViewById(R.id.txtNowPlaying);
        seekBar = findViewById(R.id.seekBar);

        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, MODE_PRIVATE);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, MODE_PRIVATE);

        myDBHelper = new MyDBHelper(this);

        setMusicDataList(myDBHelper);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        myAdapter = new MyAdapter(this, R.layout.item_data, list);
        recyclerView.setAdapter(myAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                position = recyclerView.getChildAdapterPosition(child);
                seletedMP3 = list.get(position).getTxtTitle();
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });

        btnPlay.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnLike.setOnClickListener(this);
        btnMyPlayList.setOnClickListener(this);
    }

    private void setMusicDataList(MyDBHelper myDBHelper) {
        list.clear();

        String[] data = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION};

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                data, null, null, MediaStore.Audio.Media.TITLE + " ASC");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                //음악데이터 가져오기
                id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                albumArt = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));

                SingData singData = new SingData(id, albumArt, title, artist, duration);
                list.add(singData);
            }

            cursor.close();

            //myAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPlay:
                mediaPlayer = new MediaPlayer();
                try {
                    Uri musicURI = Uri.withAppendedPath(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, list.get(position).getId());
                    mediaPlayer.setDataSource(this, musicURI);
                    mediaPlayer.prepare();
                    mediaPlayer.start();

                    btnPlay.setEnabled(false);
                    btnPause.setEnabled(true);

                    txtNowPlaying.setText(seletedMP3);

                    Thread thread = new Thread() {

                        @Override
                        public void run() {
                            if (mediaPlayer == null) {
                                return;
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    seekBar.setMax(mediaPlayer.getDuration());
                                }
                            });
                            while (mediaPlayer.isPlaying()) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        seekBar.setProgress(mediaPlayer.getCurrentPosition());

                                    }
                                });
                                SystemClock.sleep(200);
                            }// end of while
                        }
                    };

                    thread.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnPause:
                mediaPlayer.stop();
                mediaPlayer.reset();

                btnPlay.setEnabled(true);
                btnPause.setEnabled(false);

                txtNowPlaying.setText("Now Playing..");
                seekBar.setProgress(0);
                break;
            case R.id.btnLike:
                // DB에 넣어야함
                sqLiteDatabase = myDBHelper.getWritableDatabase();

                String image = list.get(position).getImageView();
                String title = list.get(position).getTxtTitle();
                String singer = list.get(position).getTxtSinger();
                String time = list.get(position).getTxtTime();

                String str = "INSERT INTO singTBL values (" + image + ",'" + title + "','" +
                        singer + "','" + time + "');";

                sqLiteDatabase.execSQL(str);
                sqLiteDatabase.close();

                Toast.makeText(this,"찜!되었습니다^0^~~",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnMyPlayList:
                Intent intent = new Intent(getApplicationContext(), MyPlayListActivity.class);
                startActivity(intent);
                break;
        }
    }
}
