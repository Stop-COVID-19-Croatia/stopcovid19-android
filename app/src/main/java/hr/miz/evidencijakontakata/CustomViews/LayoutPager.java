package hr.miz.evidencijakontakata.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;

public class LayoutPager extends ViewPager {

    private int currentPagePosition = 0;
    private LayoutPagerAdapter layoutPagerAdapter;

    public LayoutPager(Context context) {
        super(context);
        setup();
    }

    public LayoutPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    private void setup() {
        this.addOnPageChangeListener(onPageChangeListener);
        this.layoutPagerAdapter = new LayoutPagerAdapter(this);
        this.setAdapter(layoutPagerAdapter);
        this.setScrollDuration(400);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            boolean wrapHeight = MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST;
            if (wrapHeight) {
                View child = getChildAt(currentPagePosition);
                if (child != null) {
                    child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                    int h = child.getMeasuredHeight();
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void reMeasureCurrentPage(int position) {
        currentPagePosition = position;
        requestLayout();
    }

    public int nextPage() {
        int currentItem = LayoutPager.this.getCurrentItem();
        if(currentItem < layoutPagerAdapter.getCount()-1) {
            setCurrentItem(currentItem + 1);
        }
        return currentItem + 1;
    }

    public int previousPage() {
        int currentItem = LayoutPager.this.getCurrentItem();
        if(currentItem > 0) {
            setCurrentItem(currentItem - 1);
        }
        return currentItem - 1;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if(state == SCROLL_STATE_IDLE) {
                reMeasureCurrentPage(LayoutPager.this.getCurrentItem());
            }
        }
    };

    public void setScrollDuration(int millis) {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new CustomScroller(getContext(), millis));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class CustomScroller extends Scroller {

        private int durationScrollMillis = 1;

        public CustomScroller(Context context, int durationScroll) {
            super(context, new DecelerateInterpolator());
            this.durationScrollMillis = durationScroll;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, durationScrollMillis);
        }
    }

    public void notifyDataSetChanged() {
        layoutPagerAdapter.notifyDataSetChanged();
    }
}
