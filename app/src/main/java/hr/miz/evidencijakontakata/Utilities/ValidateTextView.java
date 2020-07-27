package hr.miz.evidencijakontakata.Utilities;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputType;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.style.MetricAffectingSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import hr.miz.evidencijakontakata.R;

import hr.miz.evidencijakontakata.Listeners.IEditTextDoneListener;

import java.util.ArrayList;



public class ValidateTextView extends LinearLayout {
    private static final String empty_string = "";

    public EditText editText;

    //    public Button clearTextButton;
    private Context context;
    private IEditTextDoneListener listener;
    private View view;
    private String hintText, inputType;
    private int idNextfocus, idResource;
    private String imeOption;
    // public TextView tvCustomError;
    public TextInputLayout textInputLayout;

    public void addEditorActionListener(IEditTextDoneListener listener) {
        this.listener = listener;
    }

    public ValidateTextView(Context context) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.custom_edit_textview, this, false);
        this.context = context;
    }

    public ValidateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = LayoutInflater.from(context).inflate(R.layout.custom_edit_textview, this, false);
        this.context = context;
        initViews(context, attrs);
    }

    public ValidateTextView(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
        view = LayoutInflater.from(context).inflate(R.layout.custom_edit_textview, this, false);
        this.context = context;
        initViews(context, attrs);
    }

    private void initViews(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.ValidateTextView, 0, 0);
        try {
            // get the text and colors specified using the names in attrs.xml
            hintText = a.getString(R.styleable.ValidateTextView_hintText);
            inputType = a.getString(R.styleable.ValidateTextView_inputType);
            idNextfocus = a.getInteger(R.styleable.ValidateTextView_nextFocusForward, 0);
            imeOption = a.getString(R.styleable.ValidateTextView_imeOptions);
            idResource = a.getInteger(R.styleable.ValidateTextView_customId, 0);
        } finally {
            a.recycle();
        }

        this.addView(view);

        textInputLayout = (TextInputLayout) findViewById(R.id.textInputLayout);
        setupEditText();
        //   clearTextButton = (Button) findViewById(R.id.clearButton);
        //      tvCustomError = (TextView) findViewById(R.id.tvCustomError);
        //   clearTextButton.setVisibility(GONE);

        //changeFontStyle();


        editText.addTextChangedListener(txtEntered());
        editText.setOnEditorActionListener(editorActionListener);

        editText.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                /*if (hasFocus && editText.getText().toString().length() > 0)
                    clearTextButton.setVisibility(View.VISIBLE);
                else
                    clearTextButton.setVisibility(View.GONE);*/

            }
        });
        /*clearTextButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });*/
    }

    public String getInputText() {
        return editText.getText().toString();
    }

    public void setInputText(String inputText) {
        if(inputText != null) {
            editText.setText(inputText);
        }
    }

    public void setHintText(String hintText) {
        editText.setHint(hintText);
    }

    public void clearInputText() {
        editText.getText().clear();
    }

    private void setupEditText() {
        editText = (EditText) findViewById(R.id.editTextCustom);
        if (idResource != 0) {
            editText.setId(idResource);
        }
        textInputLayout.setHint(hintText);
        editText.setNextFocusForwardId(idNextfocus);
        editText.setSingleLine();
        setImeOptions();
        setEditTextInputType();
    }

    public void validate(ArrayList<String> error) {
        if (error == null || error.size() == 0) {
            clearErrorText();
            return;
        }

        //     tvCustomError.setVisibility(VISIBLE);
        textInputLayout.setErrorEnabled(true);


        textInputLayout.setError(getErrorMessage(error));

    }

    public void clearErrorText() {
        //      tvCustomError.setText(empty_string);
        textInputLayout.setError(null);
        textInputLayout.setErrorEnabled(false);
    }

    public void errorTextViewVisibility(int visibility) {
        //       tvCustomError.setVisibility(visibility);
    }

/*
    public void clearFocusOfEditText() {
        editText.clearComposingText();
        editText.clearFocus();
    }
*/

    private String getErrorMessage(ArrayList<String> errorString) {
        StringBuilder errorText = new StringBuilder();

        errorText.append(errorString.get(0));

        for (int i = 1; i < errorString.size(); i++) {
            errorText.append("\n" + errorString.get(i));

        }

        return errorText.toString();
    }

    private void setEditTextInputType() {

        if (inputType == null) {
            return;
        }

        if (inputType.equals("text")) {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else if (inputType.equals("textPassword")) {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else if (inputType.equals("number")) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else if (inputType.equals("phone")) {
            editText.setInputType(InputType.TYPE_CLASS_PHONE);
        } else if (inputType.equals("textCapSentences")) {
            editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        } else if (inputType.equals("textMultiLine")) {
            editText.setSingleLine(false);
            editText.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        }
    }

    private void setImeOptions() {
        if (imeOption == null) {
            return;
        }

        if (imeOption.equals("actionNext")) {
            editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        } else if (imeOption.equals("actionDone")) {
            editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        }
    }

    private EditText.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (listener != null) {
                    listener.clickKeyboardDone();
                }
                return true;
            }

           /* if(actionId==EditorInfo.IME_ACTION_NEXT)
            {
                if(listener!=null)
                {
                    listener.nextFocusOfEditText();
                }
                return true;

            }*/
            return false;
        }
    };

    /*private SpannableString getCustomFontForError(String error) {
        final SpannableString ss = new SpannableString(error);
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/SegoeuiRegular.ttf");
        ss.setSpan(new FontSpan(font), 0, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }*/

    private static final class FontSpan extends MetricAffectingSpan {

        private final Typeface mNewFont;

        private FontSpan(Typeface newFont) {
            mNewFont = newFont;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setTypeface(mNewFont);
        }

        @Override
        public void updateMeasureState(TextPaint paint) {
            paint.setTypeface(mNewFont);
        }

    }

    public TextWatcher txtEntered() {
        return new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
               /* if (editTextListener != null)
                    editTextListener.onTextChanged(s, start, before, count);*/

            }

            @Override
            public void afterTextChanged(Editable s) {
          /*      if (editTextListener != null)
                    editTextListener.afterTextChanged(s);*/
               /* if (editText.getText().toString().length() > 0)
                    clearTextButton.setVisibility(View.VISIBLE);
                else
                    clearTextButton.setVisibility(View.GONE);*/
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                /*if (editTextListener != null)
                    editTextListener.beforeTextChanged(s, start, count, after);
*/
            }

        };
    }

    private void changeFontStyle() {
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/SkolaSans_Medium.ttf");
        editText.setTypeface(font);
        textInputLayout.setTypeface(font);
    }
}
