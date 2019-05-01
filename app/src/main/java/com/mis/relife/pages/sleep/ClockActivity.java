package com.mis.relife.pages.sleep;

import android.Manifest;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mis.relife.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class ClockActivity extends AppCompatActivity implements View.OnClickListener{

    Button startRecordingButton, stopRecordingButton,play;//开始录音、停止录音
    boolean isRecording = false;

    private static final String TAG = "AudioRecord";
    static final int SAMPLE_RATE_IN_HZ = 8000;
    static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
            AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT);
    AudioRecord mAudioRecord;
    boolean isGetVoiceRun;
    Object mLock;
    private TextView tv;
    private String str;
    File parent = null;

    //linechart
    private LineChart chart;
    private LineData data;
    private ArrayList<String> xVals = new ArrayList<>();
    private LineDataSet dataSet;
    private ArrayList<Entry> yVals = new ArrayList<>();
    private Handler handler;

    int filenumber = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        chart = findViewById(R.id.linechart1);
        play = findViewById(R.id.play);
        tv = findViewById(R.id.volum);
        stopRecordingButton = findViewById(R.id.stop);
        //設立停止監聽
        stopRecordingButton.setOnClickListener(this);

        //子線程傳資料給主線程 用來更新圖表
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String file = "mychart" + filenumber + ".jpg";
                dataSet = new LineDataSet(yVals,"分貝圖");
                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                data = new LineData(dataSet);
                chart.setData(data);
                chart.invalidate();
                chart.saveToGallery(file, 85);
                filenumber++;
                yVals = new ArrayList<>();
            }
        };

        //要求開啟錄音機權限和寫檔權限
        requestAudio();
        requstFile();

        //用來做排隊的物件 不太了
        mLock = new Object();
        //初始化錄音機
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_DEFAULT,
                AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE);


        //開始錄音的監聽
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNoiseLevel();
            }
        });
    }

    //開始錄製
    public void getNoiseLevel() {
        if (isGetVoiceRun) {
            Log.e(TAG, "还在录着呢");
            return;
        }
        if (mAudioRecord == null) {
            Log.e("sound", "mAudioRecord初始化失败");
        }
        isGetVoiceRun = true;
        Toast.makeText(ClockActivity.this,"開始錄製",Toast.LENGTH_LONG).show();

        //需要子執行緒來做這件事 不然會造成ANR
        new Thread(new Runnable() {
            @Override
            public void run() {
                    //寫檔動作預備
                    FileWriter fw = null;
                    try {
                        fw = new FileWriter("/sdcard/output.txt", false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    BufferedWriter bw = new BufferedWriter(fw);
                    //開始錄音
                        mAudioRecord.startRecording();
                        short[] buffer = new short[BUFFER_SIZE];

                        //圖表資料數量初始化
                        int l = 0;

                        while (isGetVoiceRun) {
                            Calendar now = Calendar.getInstance();
                            //r是实际读取的数据长度，一般而言r会小于buffersize
                            int r = mAudioRecord.read(buffer, 0, BUFFER_SIZE);
                            long v = 0;
                            // 将 buffer 内容取出，进行平方和运算
                            for (int i = 0; i < buffer.length; i++) {
                                v += buffer[i] * buffer[i];
                            }
                            // 平方和除以数据总长度，得到音量大小。
                            double mean = v / (double) r;
                            double volume = 10 * Math.log10(mean);
                            Log.d(TAG, "分贝值:" + volume + "時間" + now.get(Calendar.HOUR) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND));
                            //寫檔進去
                            try {
                                //寫進去
                                bw.write("分贝值:" + volume + "時間" + now.get(Calendar.HOUR) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND));
                                //加一行
                                bw.newLine();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //如果分貝大於零才要放進去 圖表
                            if(volume >= 0) {
                                yVals.add(new Entry(l, (int) volume));
                                l++;
                            }
                            //如果圖表內容等於多少就要製作出一個圖表
                            if(l == 7200){
                                Message message = Message.obtain();
                                message.arg1 = 0;
                                handler.sendMessage(message);
                                l = 0;
                            }

                            // 大概一秒十次
                            synchronized (mLock) {
                                try {
                                    mLock.wait(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        //執行完錄音 關閉寫檔
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //錄音停止
                mAudioRecord.stop();
                mAudioRecord.release();
                mAudioRecord = null;
            }
        }).start();
    }

    //權限套件使用 用來請求錄音
    private void requestAudio() {
        if(PermissionsUtil.hasPermission(this, Manifest.permission.RECORD_AUDIO)){
            System.out.println("HAVE!!!!!!!!!!!!!!!!!!!!");
        }
        else {
            PermissionsUtil.requestPermission(this, new PermissionListener() {
                @Override
                public void permissionGranted(@NonNull String[] permission) {
                    mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                            SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_DEFAULT,
                            AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE);
                }

                @Override
                public void permissionDenied(@NonNull String[] permission) {

                }
            }, new String[]{Manifest.permission.RECORD_AUDIO});
        }
    }
    //權限套件使用 用來請求開啟手機資料夾
    private void requstFile() {
        if(PermissionsUtil.hasPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            System.out.println("HAVE!!!!!!!!!!!!!!!!!!!!");
        }
        else {
            PermissionsUtil.requestPermission(this, new PermissionListener() {
                @Override
                public void permissionGranted(@NonNull String[] permission) {

                }

                @Override
                public void permissionDenied(@NonNull String[] permission) {

                }
            }, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //停止录音
            case R.id.stop:
            {
                stopRecording();
                break;
            }
        }
    }

    //停止录音
    public void stopRecording()
    {
        System.out.println("Stop!!!!!!!!!");
        Toast.makeText(ClockActivity.this,"錄製結束",Toast.LENGTH_LONG).show();
        isGetVoiceRun = false;
        dataSet = new LineDataSet(yVals,"分貝圖");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        data = new LineData(dataSet);
        chart.setData(data);
        chart.invalidate();
        chart.saveToGallery("end.jpg", 85);
    }

}
