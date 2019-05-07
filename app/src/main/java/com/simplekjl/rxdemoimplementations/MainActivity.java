package com.simplekjl.rxdemoimplementations;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

/**
 * What are Disposable Observers?
 * DisposableObserver class implements both Observer and Disposable interfaces. DisposableObserver
 * is much efficient than Observer if you have more than one observers in the activity or fragment.
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "myApp";
    private String mTestString = "Hello from RxJava";
    private Observable<String> myObservable;
    private DisposableObserver<String> myObserver;
    private TextView mtextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mtextView = findViewById(R.id.my_text);

        myObservable = Observable.just(mTestString);
        // creating the new instance of the DisposableObserver note that this object just have
        // 3 override methods instead of 4
        myObserver = new DisposableObserver<String>() {
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

        // old implementation with and observer
//        myObserver = new Observer<String>() {
//            @Override
//            public void onSubscribe(Disposable d) {
//
//                Log.i(TAG, "onSubscribe: ");
//
//            }
//
//            @Override
//            public void onNext(String s) {
//                Log.i(TAG, "onNext: ");
//                // setting the value when the Observable dispatches it
//                mtextView.setText(s);
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.i(TAG, "onError: ");
//
//            }
//
//            @Override
//            public void onComplete() {
//                Log.i(TAG, "onComplete: ");
//            }
//        };
        // subscriptions to the observer
        myObservable.subscribe(myObserver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // since we are creating the Disposable observer we can dispose the subscription
        // from the same object
        myObserver.dispose();
    }
}
