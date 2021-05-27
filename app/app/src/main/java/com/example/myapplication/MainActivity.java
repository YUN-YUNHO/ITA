package com.example.myapplication;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    final static String V_URL = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";//"http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
            //"http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
    VideoView videoView;
    SeekBar seekBar;
    Handler updateHandler = new Handler();
    String url = "http://3.35.150.138/waves";
    String[] FILE_LIST = {};
    List file_list = new ArrayList();
    ArrayAdapter adapter = null;
    ListView listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText tvURL = (EditText)findViewById(R.id.etVieoURL);
        tvURL.setText(V_URL);

        videoView = (VideoView)findViewById(R.id.videoView);
        seekBar = (SeekBar)findViewById(R.id.seekBar);
        listView = (ListView)findViewById(R.id.listView);
        List file_list = new ArrayList();

        listView = (ListView)findViewById(R.id.listView);


        Log.d("jihoons", "??");
        Thread t = new Thread(){
            @Override
            public void run(){
                Document doc = null;
                try {
                    doc = Jsoup.connect(url).get();
                    Elements elements = doc.select("a[href]");
                    for (Element link:elements)
                    {
                        if(link.text().trim().contains("wav") || link.text().trim().contains("mp3")) {
                            Log.d("jej", link.text().trim());
                            file_list.add(link.text().trim());
                        }
                    }

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        };
        boolean tIsAlive = t.isAlive();
        t.start();
        while(true)
        {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
                if(!tIsAlive)
                {
                    adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, file_list);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String strText = (String)parent.getItemAtPosition(position);
                            EditText tvURL = (EditText)findViewById(R.id.etVieoURL);
                            tvURL.setText(url + "/" + strText);
                        }
                    });
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void loadVideo(View view) {
        //Sample video URL : http://www.sample-videos.com/video/mp4/720/big_buck_bunny_720p_2mb.mp4
        EditText tvURL = (EditText) findViewById(R.id.etVieoURL);
        String url = tvURL.getText().toString();

        Toast.makeText(getApplicationContext(), "Loading Video. Plz wait : " + url, Toast.LENGTH_LONG).show();
        videoView.setVideoURI(Uri.parse(url));
        videoView.requestFocus();

        videoView.setOnInfoListener(
                new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        switch(what){
                            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                                // Progress Diaglog 출력
                                Toast.makeText(getApplicationContext(), "Buffering", Toast.LENGTH_LONG).show();
                                break;
                            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                                // Progress Dialog 삭제
                                Toast.makeText(getApplicationContext(), "Buffering finished.\nResume playing", Toast.LENGTH_LONG).show();
                                videoView.start();
                                break;
                        }
                        return false;
                    }
                }
        );

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
                long finalTime = videoView.getDuration();
                TextView tvTotalTime = (TextView) findViewById(R.id.tvTotalTime);
                tvTotalTime.setText(String.format("%d:%d",
                        TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                        TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
                );
                seekBar.setMax((int) finalTime);
                seekBar.setProgress(0);
                updateHandler.postDelayed(updateVideoTime, 100);
                //Toast Box
                Toast.makeText(getApplicationContext(), "Playing Video", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void playVideo(View view){
        videoView.requestFocus();
        videoView.start();

    }

    public void pauseVideo(View view){
        videoView.pause();
    }

    private Runnable updateVideoTime = new Runnable(){
        public void run(){
            long currentPosition = videoView.getCurrentPosition();
            seekBar.setProgress((int) currentPosition);
            updateHandler.postDelayed(this, 100);

        }
    };
}