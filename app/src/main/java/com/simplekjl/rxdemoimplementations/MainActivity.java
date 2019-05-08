package com.simplekjl.rxdemoimplementations;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "myApp";
    // array of string we are going to emit with the observable
    private String[] greetings = {"Hola", ",", "Welcome to RxAndroid, +", " RxJava"};
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    // sincce we are going to emit one string at the time we have to change the value the observable emits
    private Observable<String> myObservable;
    private DisposableObserver<String> myObserver;
    private TextView mtextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mtextView = findViewById(R.id.my_text);

        // we are changing from 'just' to 'fromArray' which is going to emit one string at the time when we subscribe
        // to it.
        myObservable = Observable.fromArray(greetings);


        // Rx Java allow to write very small blocks of code that allow you to work faster and cleaner since the
        // code becomes readable and effective with the right use.
        // Lets start Adding to the ComposableDisposable our first Observer which is going to be performing it's
        // logic on the io() thread and observing in the Mainthread
        compositeDisposable
                .add(myObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(getSingleItemObserver()));


    }

    private DisposableObserver<String> getSingleItemObserver() {
        myObserver = new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                Log.i(TAG, "onNext: ");
                // since the fromArray operator will send one string at the time we proceed
                // to add values in the textView
                mtextView.append(s);
                mtextView.append(" ");
                // let's make a toast to see it work in a different way
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
        return myObserver;
    }
}
