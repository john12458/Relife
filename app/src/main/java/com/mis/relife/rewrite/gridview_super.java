package com.mis.relife.rewrite;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class gridview_super extends GridView {

    public gridview_super(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public gridview_super(Context context) {
        super(context);
    }

    public gridview_super(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}