package com.noundla.centerviewpagersample;

import java.util.Random;

import com.noundla.centerviewpagersample.comps.PagerAdapter;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MyViewPagerAdapter extends PagerAdapter {
	private Activity mActivity;
	private int mPageCount;
	public MyViewPagerAdapter(Activity activity,int pageCount) {
		mActivity = activity;
		mPageCount = pageCount;
	}
	
	@Override
	public int getCount() {
		return mPageCount;
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return (view ==(View)obj);
	}
	
	@Override
	public Object instantiateItem(ViewGroup container,final int position) {
		ViewGroup viewGroup = (ViewGroup)mActivity.getLayoutInflater().inflate(
				R.layout.item_view, null);
		
		viewGroup.setBackgroundColor(randomColor());
		
		TextView textView = (TextView)viewGroup.findViewById(R.id.textView1);
		textView.setText("Page: "+(position+1));
		Button button = (Button) viewGroup.findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(mActivity, "Hey, Its clicked!!! at page "+(position+1), Toast.LENGTH_LONG).show();
			}
		});
		
		
		container.addView(viewGroup);
		
		return viewGroup;
	}
	
	Random rnd = new Random();
	private int randomColor(){
		return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
	}
	
	@Override
	public void destroyItem(ViewGroup collection, int position, Object view) {
		//must be overridden else throws exception as not overridden.
		Log.d("Tag", collection.getChildCount()+"");
		collection.removeView((View) view);
	}
	
	@Override
	public float getPageWidth(int position) {
		return 0.8f;
	}
}
