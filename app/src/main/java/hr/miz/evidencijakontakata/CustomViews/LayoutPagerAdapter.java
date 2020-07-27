package hr.miz.evidencijakontakata.CustomViews;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class LayoutPagerAdapter extends PagerAdapter {

    LayoutPager layoutPager;
    LayoutPagerAdapter(LayoutPager layoutPager) {
        this.layoutPager = layoutPager;
    }

    @NonNull
    public Object instantiateItem(@NonNull ViewGroup collection, int position) {
        return layoutPager.getChildAt(position);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
}