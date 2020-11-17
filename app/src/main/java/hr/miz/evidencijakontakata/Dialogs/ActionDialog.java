package hr.miz.evidencijakontakata.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;

import hr.miz.evidencijakontakata.databinding.ActionDialogBinding;

public class ActionDialog extends Dialog {
    private ActionDialogBinding binding;
    private IExposureActivationDialog listener;
    private String title = "";
    private String description = "";
    private String actionBtnText = "";

    private ActionDialog(@NonNull Context context, String title, String description, String actionBtnText, IExposureActivationDialog listener) {
        super(context);
        this.listener = listener;
        this.title = title;
        this.description = description;
        this.actionBtnText = actionBtnText;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActionDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setup();
    }

    public static void start(@NonNull Context context, String title, String description, String actionBtnText, IExposureActivationDialog listener) {
        ActionDialog actionDialog = new ActionDialog(context, title, description, actionBtnText, listener);
        actionDialog.show();
    }

    private void setup() {
        binding.tvTitle.setText(title);
        binding.tvDescription.setText(description);
        binding.tvAction.setText(actionBtnText);
        binding.tvAction.setOnClickListener(v -> okAction());
        binding.tvCancel.setOnClickListener(v -> cancelAction());
    }

    private void okAction() {
        if(listener != null) {
            listener.onDialogAction(true);
        }
        dismiss();
    }

    private void cancelAction() {
        if(listener != null) {
            listener.onDialogAction(false);
        }
        dismiss();
    }

    public interface IExposureActivationDialog {
        void onDialogAction(boolean accept);
    }
}
