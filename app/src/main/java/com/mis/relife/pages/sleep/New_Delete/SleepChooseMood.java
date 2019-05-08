package com.mis.relife.pages.sleep.New_Delete;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.mis.relife.R;
import com.mis.relife.pages.sleep.Adapter.GridviewMoodChooseAdapter;

@SuppressLint("ValidFragment")
public class SleepChooseMood extends DialogFragment {

    Context context;
    private GridView gv_mood;
    private GridviewMoodChooseAdapter gridviewAdapter;
    private sleep_plus sleepplus;
    private Button Album;

    public static final int CHOOSE_PHOTO=2;

    public SleepChooseMood(Context context,sleep_plus sleepplus){
        this.context = context;
        this.sleepplus = sleepplus;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.sleep_dialog_choose_mood, null);
        Dialog dialog = new Dialog(getActivity());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle("選擇圖片");
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);

        Album = view.findViewById(R.id.open);
        gv_mood = view.findViewById(R.id.gv_choose_img);
        gridviewAdapter = new GridviewMoodChooseAdapter(context);
        gv_mood.setAdapter(gridviewAdapter);
        gv_mood.setOnItemClickListener(gvMoodClick);
        run_Permissions();
        return dialog;
    }

    private AdapterView.OnItemClickListener gvMoodClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            sleepplus.ib_mood.setImageResource(gridviewAdapter.moodImg[position]);
            String str = context.getString(gridviewAdapter.moodImg[position]);
            String[] tokens = str.split("/|/|\\.");
            for (int i = 0;i < tokens.length;i++) {
                if(i == 2){
                    sleepplus.mood = tokens[i];
                }
            }
            System.out.println("!!!!!!!!!!!!!!!!!!" + sleepplus.mood);
            dismiss();
        }
    };

    private void run_Permissions(){
        Album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //動態申請對SD卡讀寫的許可權
                if(ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else {
                    openAlbum();
                }
            }
        });
    }
    private void openAlbum(){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);  //開啟相簿
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0 &&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else {
                    Toast.makeText(context, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CHOOSE_PHOTO:
                handleImageOnKitKat(data);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data){   //處理圖片
        String imagePath = null;
        if(data != null) {
            Uri uri = data.getData();
            if (DocumentsContract.isDocumentUri(context, uri)) {     //如果是document型別的Uri,則通過document id處理
                String docId = DocumentsContract.getDocumentId(uri);
                if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    String id = docId.split(":")[1];   //解析出數字格式的id
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                    imagePath = getImagePath(contentUri, null);
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {  //如果是cntent型別的Uri,則使用普通方式處理
                imagePath = getImagePath(uri, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {   //如果是file型別的Uri,直接獲取圖片路徑即可
                imagePath = uri.getPath();
            }
            displayImage(imagePath);
        }
    }

    private String getImagePath(Uri uri,String selection){   //通過Uri和selection來獲取真實的圖片路徑
        String path=null;
        Cursor cursor= context.getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private void displayImage(String imagePath){   //顯示圖片
        if(imagePath != null){
            System.out.println("!!!!!!!!!!!!!!!!" + imagePath);
            sleepplus.mood = imagePath;
            Bitmap bitmap= BitmapFactory.decodeFile(imagePath);
            sleepplus.ib_mood.setImageBitmap(bitmap);
        }else {
            Toast.makeText(context, "failed to get image", Toast.LENGTH_SHORT).show();
        }
        dismiss();
    }
}
