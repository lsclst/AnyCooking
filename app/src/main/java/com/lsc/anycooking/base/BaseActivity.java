package com.lsc.anycooking.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.lsc.anycooking.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lsc on 2017/11/22 0022.
 *
 * @author lsc
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected Unbinder mUnBinder;
    @BindView(R.id.id_top_container)
    CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(getLayoutResId());
        mUnBinder = ButterKnife.bind(this);
        initView();
        initData();
    }

    protected void initData() {

    }


    @LayoutRes
    protected abstract int getLayoutResId();

    protected void initView() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnBinder.unbind();
    }

    @NonNull
    public CoordinatorLayout provideCoordinatorLayout() {
        return mCoordinatorLayout;
    }
}
