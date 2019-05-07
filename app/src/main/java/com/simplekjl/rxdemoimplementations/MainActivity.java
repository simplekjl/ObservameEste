package com.simplekjl.rxdemoimplementations;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "myApp";
    private String mTestString = "Hello from RxJava";
    private Observable<String> myObservable;
    private Observer<String> myObserver;
    private TextView mtextView;
    // disposable object to be access in the class
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mtextView = findViewById(R.id.my_text);

        myObservable = Observable.just(mTestString);
        // the observer has to be subscribe to get the data from the observable
        myObserver = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                // let's assign from the observer the disposable Object to our member class variable
                disposable = d;
                Log.i(TAG, "onSubscribe: ");

            }

            @Override
            public void onNext(String s) {
                Log.i(TAG, "onNext: ");
                // setting the value when the Observable dispatches it
                mtextView.setText(s);

            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: ");

            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: ");
            }
        };
        // subscriptions to the observer
        myObservable.subscribe(myObserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // disposing the subscription. This will prevent the activity from crashing while
        // performing actions in the UI
        disposable.dispose();
    }
}
