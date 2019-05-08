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
    private String[] greetings = {"Hola", ",", "Welcome to RxAndroid, +", " RxJava"};
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Observable<String[]> myObservable;
    private Observable<String> myObservableArray;
    private DisposableObserver<String[]> myObserver;
    private DisposableObserver<String> myObserver2;
    private TextView mtextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mtextView = findViewById(R.id.my_text);

        myObservable = Observable.just(greetings);
        // let's test same string but know as an array
        myObservableArray = Observable.just("Hola", ",", "Welcome to RxAndroid, +", " RxJava");


        // Rx Java allow to write very small blocks of code that allow you to work faster and cleaner since the
        // code becomes readable and effective with the right use.
        // Lets start Adding to the ComposableDisposable our first Observer which is going to be performing it's
        // logic on the io() thread and observing in the Mainthread
        compositeDisposable
                .add(myObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(getSingleItemObserver()));
        // adding the second observer and subscribing to the composite disposable
        compositeDisposable
                .add(myObservableArray
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(getArrayObserver()));

    }

    private DisposableObserver<String[]> getSingleItemObserver() {
        myObserver = new DisposableObserver<String[]>() {
            @Override
            public void onNext(String[] s) {
                Log.i(TAG, "onNext: ");
                // since the just operator will send the data as it is we will get the array at once so we iterate on it
                // to add values in the textView
                for (String note : s) {
                    mtextView.append(note);
                    mtextView.append(" ");
                }
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

    private DisposableObserver<String> getArrayObserver() {

        myObserver2 = new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                Log.i(TAG, "onNext: ");
                // passing several objects to the just operator will end up sending each item individually
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
        return myObserver2;
    }
}
