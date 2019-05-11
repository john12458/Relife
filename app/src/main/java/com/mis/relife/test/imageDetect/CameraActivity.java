package com.mis.relife.test.imageDetect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mis.relife.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity {

    public static final int TAKE_POTHO=1;
    private static final String HANDLE_THREAD_NAME = "CameraBackground";
    /**
     * An additional thread for running tasks that shouldn't block the UI.
     */
    private HandlerThread backgroundThread;

    /**
     * A {@link Handler} for running tasks in the background.
     */
    private Handler backgroundHandler;
    private final Object lock = new Object();
    private boolean runClassifier = false;
    private ImageView imageView;
    private Button button;
    private Uri uri;
    private ImageClassifier classifier;
    private HandlerThread mThread;
    private Handler mThreadHandler;
    private TextView textView;


    private void createClassfier(){
        CameraActivity activity = this;
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    classifier = new ImageClassifier(activity);
                    Log.d("TAG", "Failed to initialize an image classifier.");
                } catch (IOException e) {
                    Log.e("TAG", "Failed to initialize an image classifier.");
                }
            }
        }).start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        imageView=(ImageView)findViewById(R.id.picture);
        textView = (TextView) findViewById(R.id.textView);
        button=(Button)findViewById(R.id.button);
        createClassfier();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File outImage=new File(getExternalCacheDir(),"output_image.jpg");
                try{
                    if(outImage.exists())
                    {
                        outImage.delete();
                    }
                    outImage.createNewFile();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                if(Build.VERSION.SDK_INT>=24) {
                    uri= FileProvider.getUriForFile(CameraActivity.this,"com.mis.relife.cameraalbumtest.fileprovider",outImage);
                }else {
                    uri=Uri.fromFile(outImage);
                }
                Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                startActivityForResult(intent,TAKE_POTHO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        switch (requestCode)
        {
            case TAKE_POTHO:
                if(resultCode==RESULT_OK)
                {
                    try{
                        Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        Bitmap newbitmap = reviceBitmap(bitmap);
                        imageView.setImageBitmap(bitmap);
                        Log.d("mThread","Runung...");
                        classifyFrame(newbitmap);
//                        //聘請一個特約工人，有其經紀人派遣其工人做事 (另起一個有Handler的Thread)
//                        mThread = new HandlerThread("name");
//                        //讓Worker待命，等待其工作 (開啟Thread)
//                        mThread.start();
//                        //找到特約工人的經紀人，這樣才能派遣工作 (找到Thread上的Handler)
//                        mThreadHandler=new Handler(mThread.getLooper());
//                        //請經紀人指派工作名稱 r，給工人做
//                        mThreadHandler.post(new Runnable() {
//                            @Override
//                            public void run() {
//
//                            }
//                        });


                    }catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }
    private Bitmap reviceBitmap(Bitmap bitmap){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        int newWidth = classifier.DIM_IMG_SIZE_X;
        int newHeight = classifier.DIM_IMG_SIZE_Y;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,true);
    }

    /**
    /**
     * Classifies a frame from the preview stream.
     */
    private void classifyFrame(Bitmap bitmap) {
        if (classifier == null) {
            Log.d("d","Uninitialized Classifier or invalid context.");
            return;
        }
        String textToShow = classifier.classifyFrame(bitmap);
        bitmap.recycle();

        Log.d("d",textToShow);
        textView.setText(textToShow);
    }
}
