package com.lsc.anycooking.base;

import android.support.v7.widget.Toolbar;

import com.lsc.anycooking.R;

import butterknife.BindView;

/**
 * Created by lsc on 2017/11/22 0022.
 *
 * @author lsc
 */

public abstract class BaseToolBarActivity extends BaseActivity {
    @BindView(R.id.id_tool_bar)
    protected Toolbar mToolbar;
    @Override
    protected void initView() {
        super.initView();
        setupToolBar(mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(canGoBack());
    }

    protected abstract boolean canGoBack();

    protected abstract void setupToolBar(Toolbar toolbar);

}
