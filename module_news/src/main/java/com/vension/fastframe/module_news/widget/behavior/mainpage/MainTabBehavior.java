package com.vension.fastframe.module_news.widget.behavior.mainpage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.vension.fastframe.module_news.R;

/**
 * Tab Behavior
 */
public class MainTabBehavior extends CoordinatorLayout.Behavior<View> {

    private Context mContext;

    public MainTabBehavior() {
    }

    public MainTabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return isDependOn(dependency);
    }


    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        float tabScrollY = dependency.getTranslationY() / getHeaderOffset() * (dependency.getHeight() - getTitleHeight());
        float y = dependency.getHeight() - tabScrollY;
        child.setY(y);
        return true;
    }

    private int getHeaderOffset() {
        return mContext.getResources().getDimensionPixelOffset(R.dimen.header_offset);
    }

    private int getTitleHeight() {
        return mContext.getResources().getDimensionPixelOffset(R.dimen.title_height);
    }


    private boolean isDependOn(View dependency) {
        return dependency != null && dependency.getId() == R.id.header;
    }
}
