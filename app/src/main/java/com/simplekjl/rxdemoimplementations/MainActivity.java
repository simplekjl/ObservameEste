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
 * Composite Disposable
 * In one class you can have more than one observers . So you will have so many observers to dispose.
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "myApp";
    private String mTestString = "Hello from RxJava";
    private Observable<String> myObservable;
    private DisposableObserver<String> myObserver;
    private DisposableObserver<String> myObserver2;
    private TextView mtextView;

    //key object for composite disposable
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

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
        compositeDisposable.add(myObserver);
        myObservable = Observable.just(mTestString);
        //
        // let's create a second object to observe our main observable and merge this in a Composite disposable
        myObserver2 = new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                Log.i(TAG, "onNext: ");
                // launching a toast with the same observer
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
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
        // adding  second observer into the composite Object
        compositeDisposable.add(myObserver2);
        // subscriptions to the observer
        myObservable.subscribe(myObserver2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        // if we forget to dispose one of the observers we can get a memory leak, this is why we use the Coposite Disposable
//        myObserver.dispose();
//        myObserver2.dispose();
    }
}
