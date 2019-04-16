package com.mis.relife.rewrite;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

public class expandablelistview_super extends ExpandableListView {

    public expandablelistview_super(Context context) {
        super(context);
    }

    public expandablelistview_super(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public expandablelistview_super(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
