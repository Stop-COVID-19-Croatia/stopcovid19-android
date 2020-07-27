package hr.miz.evidencijakontakata.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.internal.BaselineLayout;

public class IconlessBottomNavBar extends BottomNavigationView {
    private int baselineHeight = 0;

    public IconlessBottomNavBar(@NonNull Context context) {
        super(context);
    }

    public IconlessBottomNavBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public IconlessBottomNavBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(changed) {
            removeIcons();
        }
    }

    private void removeIcons() {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            BottomNavigationItemView itemView = (BottomNavigationItemView) (menuView.getChildAt(i));
            BaselineLayout baseline = (BaselineLayout) itemView.getChildAt(1);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) baseline.getLayoutParams();
            baselineHeight = baselineHeight > 0 ? baselineHeight : (menuView.getHeight() + baseline.getHeight()) / 2;
            layoutParams.height = baselineHeight;
            baseline.setLayoutParams(layoutParams);
        }
    }
}
