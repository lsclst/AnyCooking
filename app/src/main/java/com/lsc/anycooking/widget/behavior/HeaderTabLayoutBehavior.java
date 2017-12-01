package com.lsc.anycooking.widget.behavior;

import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.lsc.anycooking.R;

/**
 * Created by lsc on 2017/11/28 0028.
 *
 * @author lsc
 */

public class HeaderTabLayoutBehavior extends CoordinatorLayout.Behavior<View> {

    //tab 移动范围从0往下移动自身一个高度
    private int mScrollRange;
    private int mHeaderScrollRange;
    private final int mHeaderHieght;
    private final int mSearchHeight;
    private final int mTabbarHeight;

    public HeaderTabLayoutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        Resources resources = context.getResources();
        mHeaderHieght = context.getResources().getDimensionPixelSize(R.dimen.main_header_height);
        mSearchHeight = resources.getDimensionPixelSize(R.dimen.main_search_bar_height);
        mTabbarHeight = resources.getDimensionPixelSize(R.dimen.main_tab_bar_height);
        mHeaderScrollRange = resources.getDimensionPixelOffset(R.dimen.headerScrollRange);
        mScrollRange = mHeaderScrollRange + mSearchHeight/2;
    }


    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        int leftMargin = params.leftMargin;
        int right = leftMargin + child.getMeasuredWidth() ;
        int top = mHeaderHieght - mSearchHeight / 2 - mTabbarHeight;
        int bottom = top + child.getMeasuredHeight();
        child.layout(leftMargin, top, right, bottom);
        return true;
    }


    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency != null && dependency.getId() == R.id.id_main_top;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        float progerss = dependency.getTranslationY() / mHeaderScrollRange * 1.0f;
        child.setTranslationY(progerss * mScrollRange);
        return false;
    }

}
