package com.noundla.centerviewpagersample;

import com.noundla.centerviewpagersample.comps.ViewPager;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {

	private ViewPager viewPager;
	private MyViewPagerAdapter mAdapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		viewPager = (ViewPager) findViewById(R.id.viewPager1);
		mAdapter = new MyViewPagerAdapter(this,10);
		viewPager.setAdapter(mAdapter);

		//enabling the center lock for the ViewPager. This must be done after calling the setAdapter() by passing the PagerAdapter object in it.
		viewPager.enableCenterLockOfChilds();
		viewPager.setCurrentItemInCenter(0);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
