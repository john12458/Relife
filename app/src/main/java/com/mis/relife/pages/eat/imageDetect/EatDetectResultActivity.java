package com.mis.relife.pages.eat.imageDetect;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mis.relife.R;
import com.mis.relife.pages.eat.eat_listview_recipe;
import com.mis.relife.pages.eat.eat_new_activity;
import com.mis.relife.pages.eat.recipe_adapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EatDetectResultActivity extends AppCompatActivity {
    public static final int TAKE_POTHO=1;
    private ImageView imageView;
    private Uri uri;
    private ImageClassifier classifier;
    private ListView lv_detect;

    private void createClassfier(){
        Activity activity = this;
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
        setContentView(R.layout.eat_detect_result_activity);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        lv_detect =(ListView)findViewById(R.id.listview_detect);
        imageView=(ImageView)findViewById(R.id.picture);
        createClassfier();
        goToCamera();
    }

    private void goToCamera(){
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
            uri= FileProvider.getUriForFile(EatDetectResultActivity.this,"com.mis.relife.cameraalbumtest.fileprovider",outImage);
        }else {
            uri= Uri.fromFile(outImage);
        }
        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        startActivityForResult(intent,TAKE_POTHO);
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
        classifier.close();

        Log.d("d",textToShow);
        lv_detect.setAdapter(new recipe_adapter(getLayoutInflater(), getHandleText(textToShow), this));
    }
    private List<eat_listview_recipe> getHandleText(String textToShow){
        List<eat_listview_recipe>  resultList = new ArrayList<eat_listview_recipe>();
        String[] tokens = textToShow.split("\n|:");
        String[] result = {translate(tokens[1]),translate(tokens[3]),translate(tokens[5])};
        SQLiteDatabase db = openOrCreateDatabase("relife",0,null);
        for(String data:result){
            Log.d("fef",data);
            Cursor cursor = db.rawQuery("SELECT * FROM search WHERE name LIKE '%" + data + "%'", null);
            if (cursor.moveToFirst()) {
                do {
                    resultList.add(new eat_listview_recipe(cursor.getInt(1), cursor.getString(0), cursor.getDouble(2)));
                } while (cursor.moveToNext());
            }
        }
        db.close();
        return resultList;
    }
    private String translate(String text){
        HashMap<String,String> dictionary = getDictionary();
        for(String key:dictionary.keySet()){
            if(key.equals(text.trim()))
                return dictionary.get(key);
        }
        return "Not in dictionary";
    }
    private HashMap<String,String> getDictionary(){
        HashMap<String,String> dictionary = new HashMap<>();
        dictionary.put("Apple pie","蘋果派");
        dictionary.put("Baby back ribs","小排骨");
        dictionary.put("Baklava","巴卡拉");
        dictionary.put("Beef carpaccio","牛肉薄片");
        dictionary.put("Beef tartare","韃靼牛肉");
        dictionary.put("Beet salad","甜菜沙拉");
        dictionary.put("Beignets","Beignets");
        dictionary.put("Bibimbap","拌飯");
        dictionary.put("Bread pudding","麵包布丁");
        dictionary.put("Breakfast burrito","早餐捲餅");
        dictionary.put("Bruschetta","布魯斯凱塔");
        dictionary.put("Caesar salad","凱撒沙拉");
        dictionary.put("Cannoli","香炸奶酪卷");
        dictionary.put("Caprese salad","卡普雷塞沙拉");
        dictionary.put("Carrot cake","胡蘿蔔蛋糕");
        dictionary.put("Ceviche","酸橘汁醃魚");
        dictionary.put("Cheesecake","芝士蛋糕");
        dictionary.put("Cheese plate","奶酪拼盤");
        dictionary.put("Chicken curry","雞肉咖哩");
        dictionary.put("Chicken quesadilla","雞油炸玉米餅");
        dictionary.put("Chicken wings","雞翅膀");
        dictionary.put("Chocolate cake","巧克力蛋糕");
        dictionary.put("Chocolate mousse","巧克力慕斯");
        dictionary.put("Churros","Churros");
        dictionary.put("Clam chowder","蛤蜊雜燴");
        dictionary.put("Club sandwich","俱樂部三明治");
        dictionary.put("Crab cakes","蟹餅");
        dictionary.put("Creme brulee","焦糖布丁");
        dictionary.put("Croque madame","法式夫人");
        dictionary.put("Cup cakes","紙杯蛋糕");
        dictionary.put("Deviled eggs","芥末蛋");
        dictionary.put("Donuts","甜甜圈");
        dictionary.put("Dumplings","水餃");
        dictionary.put("Edamame","毛豆");
        dictionary.put("Eggs benedict","班尼迪克蛋");
        dictionary.put("Escargots","蝸牛");
        dictionary.put("Falafel","沙拉三明治");
        dictionary.put("Filet mignon","烤里脊肉片");
        dictionary.put("Fish and chips","魚和薯條");
        dictionary.put("Foie gras","鵝肝");
        dictionary.put("French fries","炸薯條");
        dictionary.put("French onion soup","法式洋蔥湯");
        dictionary.put("French toast","法式吐司");
        dictionary.put("Fried calamari","炸魷魚");
        dictionary.put("Fried rice","炒飯");
        dictionary.put("Frozen yogurt","冰凍酸奶");
        dictionary.put("Garlic bread","大蒜麵包");
        dictionary.put("Gnocchi","湯糰");
        dictionary.put("Greek salad","希臘式沙拉");
        dictionary.put("Grilled cheese sandwich","烤奶酪三明治");
        dictionary.put("Grilled salmon","烤三文魚");
        dictionary.put("Guacamole","鱷梨");
        dictionary.put("Gyoza","餃子");
        dictionary.put("Hamburger","漢堡包");
        dictionary.put("Hot and sour soup","酸辣湯");
        dictionary.put("Hot dog","熱狗");
        dictionary.put("Huevos rancheros","墨西哥煎蛋");
        dictionary.put("Hummus","鷹嘴豆泥");
        dictionary.put("Ice cream","冰淇淋");
        dictionary.put("Lasagna","烤寬麵條");
        dictionary.put("Lobster bisque","龍蝦濃湯");
        dictionary.put("Lobster roll sandwich","龍蝦卷三明治");
        dictionary.put("Macaroni and cheese","通心粉和奶酪");
        dictionary.put("Macarons","馬卡龍");
        dictionary.put("Miso soup","味噌湯");
        dictionary.put("Mussels","青口貝");
        dictionary.put("Nachos","玉米片");
        dictionary.put("Omelette","煎蛋");
        dictionary.put("Onion rings","洋蔥圈");
        dictionary.put("Oysters","生蠔");
        dictionary.put("Pad thai","墊泰國");
        dictionary.put("Paella","西班牙海鮮飯");
        dictionary.put("Pancakes","薄煎餅");
        dictionary.put("Panna cotta","潘納陶磚");
        dictionary.put("Peking duck","北京烤鴨");
        dictionary.put("Pho","河粉");
        dictionary.put("Pizza","比薩");
        dictionary.put("Pork chop","豬排");
        dictionary.put("Poutine","肉汁奶酪薯條");
        dictionary.put("Prime rib","牛排");
        dictionary.put("Pulled pork sandwich","拉豬肉三明治");
        dictionary.put("Ramen","拉麵");
        dictionary.put("Ravioli","餛飩");
        dictionary.put("Red velvet cake","紅色天鵝絨蛋糕");
        dictionary.put("Risotto","燴飯");
        dictionary.put("Samosa","咖哩角");
        dictionary.put("Sashimi","生魚片");
        dictionary.put("Scallops","扇貝");
        dictionary.put("Seaweed salad","海藻沙拉");
        dictionary.put("Shrimp and grits","蝦和粉打窩沙食");
        dictionary.put("Spaghetti bolognese","肉醬意粉");
        dictionary.put("Spaghetti carbonara","意粉培根蛋麵");
        dictionary.put("Spring rolls","春捲");
        dictionary.put("Steak","牛扒");
        dictionary.put("Strawberry shortcake","草莓脆餅");
        dictionary.put("Sushi","壽司");
        dictionary.put("Tacos","玉米餅");
        dictionary.put("Takoyaki","章魚燒");
        dictionary.put("Tiramisu","提拉米蘇");
        dictionary.put("Tuna tartare","金槍魚韃靼");
        dictionary.put("Waffles","威化餅");

        return dictionary;
    }

}
