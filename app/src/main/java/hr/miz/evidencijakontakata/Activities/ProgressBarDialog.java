package hr.miz.evidencijakontakata.Activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.wang.avi.AVLoadingIndicatorView;

import hr.miz.evidencijakontakata.R;
import hr.miz.evidencijakontakata.Utilities.Util;


public class ProgressBarDialog extends DialogFragment {
    private AVLoadingIndicatorView circle;
    private Window window;

    private static ProgressBarDialog instance;

    public static ProgressBarDialog getInstance(){
        if(instance==null){
            instance = new ProgressBarDialog();
        }

        return instance;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (true) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    boolean hasAddedOnView = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_progress_bar, container, false);
        circle = (AVLoadingIndicatorView) view.findViewById(R.id.avi);
        circle.setVisibility(View.VISIBLE);
        return view;
    }

    private View decorView;

    @Override
    public void onStart() {
        super.onStart();
        decorView = getDialog()
                .getWindow()
                .getDecorView();

        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(decorView,
                PropertyValuesHolder.ofFloat("alpha", 0.5f, 1.0f));
        scaleDown.setDuration(100);
        scaleDown.start();
        if (!hasAddedOnView && getDialog() != null) {
            getDialog().dismiss();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    return true;
                }
                return false;
            }
        });
    }

    public static void showDialog(FragmentManager manager, String tag) {
        getInstance().show(manager, tag);
    }



    @Override
    public void show(FragmentManager manager, String tag) {
        if (!isAdded()) {
            super.show(Util.getAnimatedFragmentTransaction(manager.beginTransaction()), "");
        }
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        if (!isAdded()) {
            return super.show(transaction, tag);
        }

        return 0;
    }

    public void dismiss() {
        if (decorView == null) {
            hasAddedOnView = false;
            return;
        }
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(decorView, PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.0f));
        scaleDown.setDuration(500);
        scaleDown.start();
        scaleDown.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if (getDialog() != null && getView() != null) {
                    getDialog().dismiss();
                    getView().setFocusableInTouchMode(false);
                    getView().clearFocus();
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    public static void dismissDialog() {
       getInstance().dismiss();
    }
}
