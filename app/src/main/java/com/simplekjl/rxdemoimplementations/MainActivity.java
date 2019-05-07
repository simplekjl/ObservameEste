package com.simplekjl.rxdemoimplementations;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Scanner;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Note: Different types of Schedulers available in RxJava
 *
 * Schedulers.io() :
 *
 * This can have a limitless thread pool. Used for non CPU intensive tasks. Such as database interactions, performing network  communications and interactions with the file system.
 *
 * AndroidSchedulers.mainThread()
 *
 * This is the main thread or the UI thread. This is where user interactions happen
 *
 * Schedulers.newThread()
 *
 * This scheduler creates a new thread for each unit of work scheduled.
 *
 * Schedulers.single()
 *
 * This scheduler has a single thread executing tasks one after another following the given order.
 *
 * Schedulers.trampoline()
 *
 * This scheduler executes tasks following first in, first out basics.We use this when implementing recurring tasks.
 *
 * Schedulers.from(Executor executor)
 *
 * This creates and returns a custom scheduler backed by the specified executor.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "myApp";
    private String mTestString = "Hello from RxJava";
    private Observable<String> myObservable;
    private Observer<String> myObserver;
    private TextView mtextView;

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
        // in the following line we are Observing the actions for the dispatcher on the main thread and running the
        // operation from the io thread
        // subscriptions to the observer
        myObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(myObserver);
    }
}
