package com.simplekjl.rxdemoimplementations;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;

/**
 * What are Composite Disposable?
 * Composite Disposable allow us to add many Disposable observables and dispose them all at once.
 *
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "myApp";
    private String mTestString = "Hello from RxJava";
    private Observable<String> myObservable;
    private DisposableObserver<String> myObserver;
    private DisposableObserver<String> myObserver2;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private TextView mtextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mtextView = findViewById(R.id.my_text);

        myObservable = Observable.just(mTestString);

        myObserver = getObserverOne();

        myObserver2 = getObserverTwo();

        compositeDisposable.add(myObserver);
        compositeDisposable.add(myObserver2);
        // subscriptions to the observer
        myObservable.subscribe(myObserver);
        myObservable.subscribe(myObserver2);
    }

    private DisposableObserver<String> getObserverOne() {
        // creating the new instance of the DisposableObserver note that this object just have
        // 3 override methods instead of 4
        return new DisposableObserver<String>() {
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
    }

    private DisposableObserver<String> getObserverTwo() {
        // creating the new instance of the DisposableObserver note that this object just have
        // 3 override methods instead of 4
        return new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                Log.i(TAG, "onNext: ");
                // setting the value when the Observable dispatches it
                Toast.makeText(getApplicationContext()
                        , getString(R.string.greetings_from_observer_2)
                        , Toast.LENGTH_LONG).show();
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // composite disposable allow us to dispose many objects at once, using clear() if we use
        // dispose() the object will no longer being able to be used to keep adding and dispose
        // Objects
        compositeDisposable.clear();
    }
}
