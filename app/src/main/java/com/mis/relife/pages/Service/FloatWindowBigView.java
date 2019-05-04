package com.mis.relife.pages.Service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import com.imangazaliev.circlemenu.CircleMenu;
import com.imangazaliev.circlemenu.CircleMenuButton;
import com.mis.relife.R;
import com.mis.relife.pages.MainActivity;
import com.squareup.picasso.Picasso;

public class FloatWindowBigView  extends ConstraintLayout {

    /**
     * 记录大悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录大悬浮窗的高度
     */
    public static int viewHeight;
    private CircleMenu circleMenu;
    private CircleMenuButton home,run,sleep,eat,close;
    private Context context;

    public FloatWindowBigView(final Context context) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.big, this);
        View view = findViewById(R.id.big_window_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        circleMenu = view.findViewById(R.id.circle_menu);
        run = view.findViewById(R.id.run);
        eat = view.findViewById(R.id.eat);
        sleep = view.findViewById(R.id.sleep);
        home = view.findViewById(R.id.home);
        close = view.findViewById(R.id.close);
        ini_circlebutton();
        circleMenu.setEventListener(eventListener);

    }

    //menu的監聽器
    private CircleMenu.EventListener eventListener = new CircleMenu.EventListener() {
        @Override
        public void onMenuOpenAnimationStart() {

        }

        @Override
        public void onMenuOpenAnimationEnd() {

        }

        @Override
        public void onMenuCloseAnimationStart() {

        }

        @Override
        public void onMenuCloseAnimationEnd() {

        }

        @Override
        public void onButtonClickAnimationStart(@NonNull CircleMenuButton menuButton) {

        }

        @Override
        public void onButtonClickAnimationEnd(@NonNull CircleMenuButton menuButton) {
            if(menuButton.getHintText().equals("home")){

                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                context.startActivity(intent);

            }
            else if(menuButton.getHintText().equals("eat")){
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                Bundle bundle = new Bundle();
                bundle.putInt("choose",1);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
            else if(menuButton.getHintText().equals("run")){
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                Bundle bundle = new Bundle();
                bundle.putInt("choose",2);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
            else if(menuButton.getHintText().equals("sleep")){
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                Bundle bundle = new Bundle();
                bundle.putInt("choose",3);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
            else if(menuButton.getHintText().equals("close")){
                MyWindowManager.removeBigWindow(context);
                MyWindowManager.createSmallWindow(context);
            }
        }
    };


    //初始化鈉些circlebutton
    private void ini_circlebutton(){
        picasso(R.mipmap.home_service,home);
        picasso(R.mipmap.eat_service,eat);
        picasso(R.mipmap.run_service,run);
        picasso(R.mipmap.sleep_service,sleep);
        picasso(R.mipmap.close_service,close);
    }
    //壓縮圖片
    private void picasso(int res,CircleMenuButton circleMenuButton){
        Picasso
                .with(context)
                .load(res)
                .into(circleMenuButton);
    }


}