package com.simplekjl.rxdemoimplementations;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "myApp";

    // composite disposable
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    // views
    private TextView content;
    private EditText input;
    private Button clearButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // getting the viws

        content = findViewById(R.id.content);
        input = findViewById(R.id.inputText);
        clearButton = findViewById(R.id.clearBtn);

        //Rx Android reduces de boiler plate and the inconsistency of the code
        //talke a look to the normal implementation of a text watcher

//        input.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    content.setText(s);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        clearButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                    input.setText("");
//                    content.setText("");
//            }
//        });

        // RxAndroid Approach
        Disposable inputDisposable = RxTextView.textChanges(input)
                .subscribe(charSequence -> content.setText(charSequence));
        Disposable buttonDisposable = RxView.clicks(clearButton)
                .subscribe(o -> {
                    input.setText("");
                    content.setText("");
                });

        compositeDisposable.add(inputDisposable);
        compositeDisposable.add(buttonDisposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
