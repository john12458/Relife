package com.mis.relife.pages.login;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.mis.relife.R;

public class SawtoothView extends View {
    private int size = 36;
    private int width = 20;
    private Paint paint = new Paint();

    public SawtoothView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(getResources().getColor(R.color.colorLoginTitle));
        paint.setColor(getResources().getColor(R.color.colorLoginbg));
        //消除锯齿
        paint.setAntiAlias(true);
        paint.setStrokeWidth(0);
        paint.clearShadowLayer();
        //设置镂空（方便查看效果）
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int halfHeight = getHeight() >> 1;// 相当于num除以2 此处装逼请忽略
        for (int i = 0; i <= getWidth(); i += size) {
            int j = i/size;
            Path path = new Path();
            path.moveTo(j*(size+width), halfHeight);
            path.addArc(new RectF(j*(size+width), (getHeight() - 20) / 2, j*(size+width)+size, (getHeight() + 20) / 2), 0, 180);
            path.moveTo((j+1)*size+j*width, halfHeight);
            path.lineTo((j+1)*(width+size), halfHeight);
            path.lineTo((j+1)*(width+size), 0);
            path.lineTo(j*(width+size), 0);
            path.lineTo(j*(width+size), halfHeight);
            canvas.drawPath(path, paint);
        }

    }
}