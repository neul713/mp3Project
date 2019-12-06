package com.example.neul7.monkeymp3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class MyPlayListActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnTitleSort, btnExit;
    RecyclerView myRecyclerView;

    ArrayList<SingData> list = new ArrayList<>();
    MyAdapter myAdapter;

    LinearLayoutManager linearLayoutManager;

    MyDBHelper myDBHelper;
    SQLiteDatabase sqLiteDatabase;


    int position;

    String id, albumArt, title, artist, duration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_play_list);

        btnTitleSort = findViewById(R.id.btnTitleSort);
        btnExit = findViewById(R.id.btnExit);
        myRecyclerView = findViewById(R.id.myRecyclerView);

        Intent intent = getIntent();

        myDBHelper = new MyDBHelper(this);

        linearLayoutManager = new LinearLayoutManager(this);
        myRecyclerView.setLayoutManager(linearLayoutManager);
        myAdapter = new MyAdapter(this, R.layout.item_data, list);
        myRecyclerView.setAdapter(myAdapter);

        btnTitleSort.setOnClickListener(this);
        btnExit.setOnClickListener(this);

        btnTitleSort.callOnClick();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTitleSort:
                list.clear();
                sqLiteDatabase = myDBHelper.getReadableDatabase();
                Cursor cursor2;
                cursor2 = sqLiteDatabase.rawQuery("SELECT * FROM singTBL ORDER BY singTitle ASC;", null);
                while (cursor2.moveToNext()) {
                    albumArt = cursor2.getString(0);
                    title = cursor2.getString(1);
                    artist = cursor2.getString(2);
                    duration = cursor2.getString(3);

                    SingData singData2 = new SingData(albumArt, title, artist, duration);
                    list.add(singData2);
                }

                myAdapter.notifyDataSetChanged();

                cursor2.close();
                sqLiteDatabase.close();
                break;
            case R.id.btnExit:
                finish();
                break;

        }
    }
}
