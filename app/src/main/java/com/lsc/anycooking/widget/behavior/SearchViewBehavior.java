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

public class SearchViewBehavior extends CoordinatorLayout.Behavior<View> {


    private final int mHeaderHeight;
    private final int mSearchHeight;
    private final int mHeaderScrollRange;
    private float mScrollRange;
    private int mStartMargin;
    private int mFinalMargin;

    public SearchViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        Resources resources = context.getResources();
        mHeaderHeight = resources.getDimensionPixelSize(R.dimen.main_header_height);
        mSearchHeight = resources.getDimensionPixelSize(R.dimen.main_search_bar_height);
        mHeaderScrollRange = resources.getDimensionPixelOffset(R.dimen.headerScrollRange);
        mScrollRange = -mHeaderHeight + mSearchHeight / 2;
        mStartMargin = resources.getDimensionPixelOffset(R.dimen.layout_margin);
        mFinalMargin = resources.getDimensionPixelOffset(R.dimen.searchBarFinalMargin);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        int left = params.leftMargin;
        int right = left + child.getMeasuredWidth() ;
        int top = mHeaderHeight - mSearchHeight / 2;
        int bottom = top + child.getMeasuredHeight();
        child.layout(left, top, right, bottom);
        return true;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency != null && dependency.getId() == R.id.id_main_top;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        float progress = dependency.getTranslationY() / mHeaderScrollRange * 1.0f;
        child.setTranslationY(progress * mScrollRange);
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        int margin = (int) (mStartMargin - (mStartMargin - mFinalMargin) * progress);

        lp.setMargins(margin, 0,margin, 0);
        child.setLayoutParams(lp);
        return false;
    }

}
