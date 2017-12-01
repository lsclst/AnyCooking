package com.lsc.anycooking;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lsc.anycooking.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by lsc on 2017/11/28 0028.
 *
 * @author lsc
 */

public class TestFragment extends BaseFragment {
    private static final String TAG = "key_tag";
    @BindView(R.id.id_content_list)
    RecyclerView mRv_List;

    public static TestFragment getInstance(String tag) {
        Bundle bundle = new Bundle();
        bundle.putString(TAG, tag);
        TestFragment testFragment = new TestFragment();
        testFragment.setArguments(bundle);
        return testFragment;
    }

    @NonNull
    @Override
    protected int getLayoutId() {
        return R.layout.test;
    }

    @Override
    protected void initView() {
        super.initView();
        mRv_List.setAdapter(new Adapter(getContext(), getArguments().getString(TAG)));
    }

    public void scrollToTop() {
        mRv_List.smoothScrollToPosition(0);
    }

    static class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

        private Context mContext;
        private String tag;

        public Adapter(Context context, String tag) {
            mContext = context;
            this.tag = tag;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.test_item, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.tv.setText(String.format("%s item %s", tag, position));
        }

        @Override
        public int getItemCount() {
            return 50;
        }

        class Holder extends RecyclerView.ViewHolder {
            TextView tv;

            public Holder(View itemView) {
                super(itemView);
                tv = itemView.findViewById(R.id.id_test);
            }
        }
    }
}
