package com.lsc.anycooking.widget.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.lsc.anycooking.R;
import com.lsc.anycooking.widget.behavior.helper.HeaderScrollingViewBehavior;

import java.util.List;

/**
 * Created by lsc on 2017/11/22 0022.
 *
 * @author lsc
 */

public class MainPageContentListBehavior extends HeaderScrollingViewBehavior {

    private static final String TAG = MainPageContentListBehavior.class.getSimpleName();
    private int mStartOverlapTop;
    private int mHeaderScrollRange;

    public MainPageContentListBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mStartOverlapTop = context.getResources().getDimensionPixelSize(R.dimen.overLapTop);
        mHeaderScrollRange = context.getResources().getDimensionPixelOffset(R.dimen.headerScrollRange);
        setOverlayTop(mStartOverlapTop);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency.getId() == R.id.id_main_top;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        child.setTranslationY(dependency.getTranslationY());
        float progress = 1.0f - dependency.getTranslationY() / mHeaderScrollRange * 1.0f;
        setOverlayTop((int) (progress * mStartOverlapTop));
        return false;
    }

    //头部可滑动距离=头部高度 - tabbar - toolbar
    @Override
    protected int getScrollRange(View v) {
        return - mHeaderScrollRange;
    }

    @Override
    protected View findFirstDependency(List<View> views) {
        for (View view : views) {
            if (view.getId() == R.id.id_main_top) {
                return view;
            }
        }
        return null;
    }

}
