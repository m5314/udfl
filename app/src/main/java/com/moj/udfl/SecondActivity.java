package com.moj.udfl;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moj.widget.UpOrDownDragDismissFrameLayout;

public class SecondActivity extends AppCompatActivity {

	UpOrDownDragDismissFrameLayout mUpOrDownDragDismissFrameLayout;
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		mViewPager= (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setAdapter(new PagerAdapter() {
			@Override
			public int getCount() {
				return 100;
			}

			@Override
			public boolean isViewFromObject(View view, Object object) {
				return view == object;
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				RecyclerView listView = new RecyclerView(SecondActivity.this);
				listView.setLayoutParams(new ViewGroup.LayoutParams(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT));
				listView.setLayoutManager(new LinearLayoutManager(SecondActivity.this, LinearLayoutManager.VERTICAL, false));
				listView.setAdapter(new RecyclerView.Adapter() {
					@Override
					public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
						return new RecyclerView.ViewHolder(LayoutInflater.from(SecondActivity.this).inflate(R.layout.textview, null)) {

						};
					}

					@Override
					public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

					}

					@Override
					public int getItemCount() {
						return 100;
					}
				});
				container.addView(listView);
				return listView;
			}

			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				container.removeView((View) object);
			}
		});
		mUpOrDownDragDismissFrameLayout = (UpOrDownDragDismissFrameLayout) findViewById(R.id.ud_fl);
		mUpOrDownDragDismissFrameLayout.setListener(new UpOrDownDragDismissFrameLayout.Listener() {
			@Override
			public void onUpdate(float percent) {
				Log.i("test",percent+"");
				getWindow().getDecorView().getBackground().setAlpha((int) ((1-percent)*255));
			}

			@Override
			public void dismiss() {
				finish();
			}
		});
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		ViewCompat.setBackground(getWindow().getDecorView(), ContextCompat.getDrawable(this,android.R.color.darker_gray));
	}
}
