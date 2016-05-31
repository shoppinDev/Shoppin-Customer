package com.dealwala.main.dealwala.util;

import android.app.ActionBar;
import android.support.v4.view.ViewPager;

public class PageChangeListener extends ViewPager.SimpleOnPageChangeListener {

  private final ActionBar actionBar;
  
    public PageChangeListener(ActionBar paramActionBar) {
    this.actionBar = paramActionBar;
  }
  
    public void onPageSelected(int paramInt) {
    super.onPageSelected(paramInt);
    this.actionBar.setSelectedNavigationItem(paramInt);
  }
}

