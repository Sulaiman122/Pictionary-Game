package com.example.paint;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class Viewer extends AppCompatActivity {

    private ViewerPaint paintVView;

    private int currentApiVersion;
    private String textAnswer = "bird";
    private int maxPresCounter = 4;
    ImageView pain,chat;
    TextView dashes;
    String dasss[] = {"", "-", "--", "---", "----", "-----", "------", "-------", "--------", "---------"};
    EditText answer;


    @Override
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);

        paintVView = (ViewerPaint) findViewById(R.id.painvieww);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintVView.init(metrics);

        answer = findViewById(R.id.answer);
        dashes = findViewById(R.id.dashes);
        InputFilter[] editFilters = answer.getFilters();
        InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
        System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
        newFilters[editFilters.length] = new InputFilter.LengthFilter(maxPresCounter);
        answer.setFilters(newFilters);


        
        dashes.setText(dasss[maxPresCounter]);


        //Live editing
        lifeChange();

        fullscreenInit();

    }


    private void fullscreenInit() {
        currentApiVersion = Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        if(currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        }
    }

    private void lifeChange() {
        answer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                int lengthAnswer = answer.getText().length();
                if(lengthAnswer >= 0) {
                    dashes.setText(answer.getText().toString() + dasss[maxPresCounter-lengthAnswer]);
                    if(lengthAnswer == maxPresCounter){
                        doValidate();
                    } } }
        } );
    }

    private void doValidate() {
        if(answer.getText().toString().equals(textAnswer)){
            Toast.makeText(this, "Right", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Try again", Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

}



/*
MY TRY WITH DASHES
            int len = 0;
            int charIndex;
            String currentValue,before;
            boolean first = true;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String str = answer.getText().toString();
                String dsh = dashes.getText().toString();

                len = str.length();

                if (s.toString().length() > 0) {
                    before = String.valueOf(s.toString().charAt(s.toString().length()-1));
            }}
                String str = answer.getText().toString();
//                char current = String.valueOf(s.toString().charAt(str.length()-1));
                String dsh = dashes.getText().toString();
//                dsh = dsh.substring(0, charIndex) + dsh.substring(charIndex+1);


//                if( String.valueOf(s).charAt(str.length()-1)
//                char mychar = String.valueOf(s).charAt(str.length()-1);
//                String thenew = dsh + mychar;


                try {
                    if (!str.isEmpty())
                        dashes.setText(dsh.replaceFirst("-", String.valueOf(s.toString().charAt(str.length() - 1))));//.replaceFirst(" ",""));
                    if (len > str.length() && !str.isEmpty()) {
                            Log.d("SSSS", "removed before: " + before);
                            dashes.setText(dsh.replaceFirst(before, "-"));
                    }
                    if(len > str.length() && before.length() > str.length()){
                        dashes.setText(dsh.replaceFirst(String.valueOf(s.toString().charAt(str.length() - 1)), "-"));
                    }
                }catch (Exception e) {
                    Log.d("SSSS", "Exceptionnnnnnnnnnnn :" + e.getMessage() + String.valueOf(s.toString().charAt(str.length()-1)) );
                    if (len > str.length() && str.length() == 1) {

                }




//        int charIndex;
//        String text = ed1.getText().toString();
//        text = text.substring(0, charIndex) + text.substring(charIndex+1);
//        ed1.setText(text);
//        ed1.setText((Spanned)ed1.getText().delete(indexStart , indexEnd));




//                    dashes.append(String.valueOf(s.charAt(str.length()-1)));


//                if(str.length() > 0 || len > str.length()) {
//                    dashes.setText(str.replace("-", "n"));
//
//                    if (!str.isEmpty()){
//                        String lastChar = str.substring( str.length() - 1);
//                        dashes.setText(lastChar + dashes.getText());
//                    }

                }



                        //focus in edit text
//        answer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus){
//                    answer.setText("");
//                }
//            }
//        });
 */