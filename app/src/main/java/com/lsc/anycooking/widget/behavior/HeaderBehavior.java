package com.lsc.anycooking.widget.behavior;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import com.lsc.anycooking.BuildConfig;
import com.lsc.anycooking.R;
import com.lsc.anycooking.widget.behavior.helper.ViewOffsetBehavior;

import java.lang.ref.WeakReference;

/**
 * Created by lsc on 2017/11/24 0024.
 *
 * @author lsc
 */

public class HeaderBehavior extends ViewOffsetBehavior<View> {
    public static final int STATE_EXPANDED = 0x2;
    public static final int STATE_CLLOSPANED = 0x4;
    public static final int DURATION_SHORT = 300;
    public static final int DURATION_LONG = 600;
    private static final String TAG = HeaderBehavior.class.getSimpleName();
    private Scroller mScroller;
    private int mCurrentState = STATE_EXPANDED;
    private FlingRunnable mFlingRunnable;
    private WeakReference<View> mChild;
    private WeakReference<CoordinatorLayout> mParent;
    private OnPagerStateListener mPagerStateListener;
    private int mMaxScrollRange;
    private int mTabHeight;

    public void setPagerStateListener(OnPagerStateListener pagerStateListener) {
        mPagerStateListener = pagerStateListener;
    }

    public HeaderBehavior() {
    }

    public HeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
        mMaxScrollRange = context.getResources().getDimensionPixelOffset(R.dimen.headerScrollRange);
        mTabHeight = context.getResources().getDimensionPixelOffset(R.dimen.main_tab_bar_height);

    }

    private float getMaxScrollRange() {
        return mMaxScrollRange;
    }


    @Override
    protected void layoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        super.layoutChild(parent, child, layoutDirection);
        mChild = new WeakReference<View>(child);
        mParent = new WeakReference<CoordinatorLayout>(parent);
    }


    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes) {
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0 && !isClose(child) && canScroll(child.getTranslationY());
    }


    @Override
    public void onNestedScrollAccepted(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes) {
        Log.e(TAG, "onNestedScrollAccepted: ");
        //初始化 取消动画
        mScroller.abortAnimation();
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed) {
        Log.e(TAG, "onNestedPreScroll: ");
        //dy > 0 往上移动 dy<0 往下移动
        float newTranslationY = child.getTranslationY() - dy / 3;
        //往上移动,并且并未达到最大折叠高度
        if (canScroll(newTranslationY)) {
            child.setTranslationY(newTranslationY);
        } else {
            child.setTranslationY(dy > 0 ? getMaxScrollRange() : 0.f);
        }
        consumed[1] = dy;

    }


    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int type) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type);
        Log.e(TAG, "onStopNestedScroll: ");
        if (!isCollspaned() && !mIsHandlingActionUp) {
            handleActionUp(coordinatorLayout, child);
        }
    }

    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, float velocityX, float velocityY) {
        Log.e(TAG, "onNestedPreFling: ");
        return !isClose(child);
    }


    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, final View child, MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP && !isCollspaned() && !mIsHandlingActionUp) {
            handleActionUp(parent, child);
        }
        return super.onInterceptTouchEvent(parent, child, ev);
    }

    private boolean mIsHandlingActionUp;

    private void handleActionUp(CoordinatorLayout parent, final View child) {
        mIsHandlingActionUp = true;
        if (mFlingRunnable != null) {
            child.removeCallbacks(mFlingRunnable);
            mFlingRunnable = null;
        }
        mFlingRunnable = new FlingRunnable(parent, child);
        Log.e(TAG, "handleActionUp: " + child.getTranslationY() + " " + getThresholdHeight(child));
        if (child.getTranslationY() < getThresholdHeight(child)) {
            mFlingRunnable.scrollToClosed(DURATION_SHORT);
        } else {
            mFlingRunnable.scrollToOpen(DURATION_SHORT);
        }

    }

    private float getThresholdHeight(View child) {
        return -mTabHeight;
    }

    /**
     * For animation , Why not use {@link android.view.ViewPropertyAnimator } to play animation is of the
     * other {@link android.support.design.widget.CoordinatorLayout.Behavior} that depend on this could not receiving the correct result of
     * {@link View#getTranslationY()} after animation finished for whatever reason that i don't know
     */
    private class FlingRunnable implements Runnable {
        private final CoordinatorLayout mParent;
        private final View mLayout;

        FlingRunnable(CoordinatorLayout parent, View layout) {
            mParent = parent;
            mLayout = layout;
        }

        public void scrollToClosed(int duration) {
            float curTranslationY = mLayout.getTranslationY();
            float dy = getMaxScrollRange() - curTranslationY;
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "scrollToClosed:offest:" + getMaxScrollRange());
                Log.d(TAG, "scrollToClosed: cur0:" + curTranslationY + ",end0:" + dy);
                Log.d(TAG, "scrollToClosed: cur:" + Math.round(curTranslationY) + ",end:" + Math.round(dy));
                Log.d(TAG, "scrollToClosed: cur1:" + (int) (curTranslationY) + ",end:" + (int) dy);
            }
            mScroller.startScroll(0, Math.round(curTranslationY - 0.1f), 0, Math.round(dy + 0.1f), duration);
            start();
        }

        public void scrollToOpen(int duration) {
            float curTranslationY = ViewCompat.getTranslationY(mLayout);
            mScroller.startScroll(0, (int) curTranslationY, 0, (int) -curTranslationY, duration);
            start();
        }

        private void start() {
            if (mScroller.computeScrollOffset()) {
                mFlingRunnable = new FlingRunnable(mParent, mLayout);
                ViewCompat.postOnAnimation(mLayout, mFlingRunnable);
            } else {
                onFlingFinished(mParent, mLayout);
            }
        }


        @Override
        public void run() {
            if (mLayout != null && mScroller != null) {
                if (mScroller.computeScrollOffset()) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "run: " + mScroller.getCurrY());
                    }
                    ViewCompat.setTranslationY(mLayout, mScroller.getCurrY());
                    ViewCompat.postOnAnimation(mLayout, this);
                } else {
                    onFlingFinished(mParent, mLayout);
                }
            }
        }
    }

    private void onFlingFinished(CoordinatorLayout coordinatorLayout, View layout) {
        changeState(layout.getTranslationY() == getMaxScrollRange() ? STATE_CLLOSPANED : STATE_EXPANDED);
        mIsHandlingActionUp = false;
        Log.e(TAG, "onFlingFinished: ");
    }

    private void changeState(int newState) {
        if (mCurrentState != newState) {
            mCurrentState = newState;
            if (mCurrentState == STATE_EXPANDED) {
                if (mPagerStateListener != null) {

                    mPagerStateListener.onPagerOpened();
                }
            } else {
                if (mPagerStateListener != null) {

                    mPagerStateListener.onPagerClosed();
                }
            }
        }

    }

    private boolean canScroll(float translateY) {
        return translateY >= getMaxScrollRange() && translateY <= 0.f;
    }

    private boolean isClose(View child) {
        return child.getTranslationY() == getMaxScrollRange();
    }

    public boolean isCollspaned() {
        return mCurrentState == STATE_CLLOSPANED;
    }

    public void openPager() {
        openPager(DURATION_SHORT);
    }

    /**
     * @param duration open animation duration
     */
    private void openPager(int duration) {
        View child = mChild.get();
        CoordinatorLayout parent = mParent.get();
        if (mCurrentState == STATE_CLLOSPANED && child != null) {
            if (mFlingRunnable != null) {
                child.removeCallbacks(mFlingRunnable);
                mFlingRunnable = null;
            }
            mFlingRunnable = new FlingRunnable(parent, child);
            mFlingRunnable.scrollToOpen(duration);
        }
    }

    public void closePager() {
        closePager(DURATION_SHORT);
    }

    /**
     * @param duration close animation duration
     */
    private void closePager(int duration) {
        View child = mChild.get();
        CoordinatorLayout parent = mParent.get();
        if (mCurrentState == STATE_EXPANDED) {
            if (mFlingRunnable != null) {
                child.removeCallbacks(mFlingRunnable);
                mFlingRunnable = null;
            }
            mFlingRunnable = new FlingRunnable(parent, child);
            mFlingRunnable.scrollToClosed(duration);
        }
    }

    /**
     * callback for HeaderPager 's state
     */
    public interface OnPagerStateListener {
        /**
         * do callback when pager closed
         */
        void onPagerClosed();

        /**
         * do callback when pager opened
         */
        void onPagerOpened();
    }


}
