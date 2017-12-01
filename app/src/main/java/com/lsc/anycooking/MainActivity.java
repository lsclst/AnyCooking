package com.lsc.anycooking;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.lsc.anycooking.base.BaseActivity;
import com.lsc.anycooking.widget.behavior.HeaderBehavior;

import butterknife.BindView;

/**
 * Created by lsc on 2017/11/22 0022.
 *
 * @author lsc
 */

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String[] TITLES = new String[]{"a", "b", "c"};
    private HeaderBehavior mHeaderBehavior;
    private MainAdapter mMainAdapter;

    @BindView(R.id.id_main_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.id_tab_bar)
    TabLayout mTabLayout;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
        mHeaderBehavior = (HeaderBehavior) ((CoordinatorLayout.LayoutParams) this.findViewById(R.id.id_main_top).getLayoutParams()).getBehavior();
        mHeaderBehavior.setPagerStateListener(new HeaderBehavior.OnPagerStateListener() {
            @Override
            public void onPagerClosed() {
                Snackbar.make(provideCoordinatorLayout(), "close", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onPagerOpened() {
                Snackbar.make(provideCoordinatorLayout(), "open", Snackbar.LENGTH_SHORT).show();
            }
        });
        mViewPager.setAdapter(mMainAdapter = new MainAdapter(getSupportFragmentManager(), TITLES));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void onBackPressed() {
        if (mHeaderBehavior != null && mHeaderBehavior.isCollspaned()) {
            mHeaderBehavior.openPager();
            mMainAdapter.scrollToTop();
        } else {
            super.onBackPressed();
        }
    }

    static class MainAdapter extends FragmentStatePagerAdapter {
        private String[] mTitles;
        private TestFragment mCurFragment;

        public void scrollToTop() {
            if (mCurFragment != null) mCurFragment.scrollToTop();
        }

        public MainAdapter(FragmentManager fm, String[] titles) {
            super(fm);
            mTitles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return TestFragment.getInstance(mTitles[position]);
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            mCurFragment = (TestFragment) object;
        }
    }
}
