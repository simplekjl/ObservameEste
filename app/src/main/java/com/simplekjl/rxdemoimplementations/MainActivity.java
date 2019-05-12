package com.simplekjl.rxdemoimplementations;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Skip last operator
 *
 * will allow you to skip n number of the list
 * <p>
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "myApp";
    private String mTestString = "Hello from RxJava";
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Observable<Integer> myObservable;
    private DisposableObserver<Integer> myObserver;

    private TextView mtextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mtextView = findViewById(R.id.my_text);

        myObservable = Observable.just(1,2,4,4,2,2,4,5,7,12);

        myObserver = getRangeIntegerObserver();

        myObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                // skip operator will allow you to skip the LAST n numbers in the list
                .skipLast(3)
                .subscribe(myObserver);

        //we add the observer into the disposable
        compositeDisposable.add(myObserver);

    }

    private DisposableObserver<Integer> getRangeIntegerObserver() {
        return new DisposableObserver<Integer>() {
            @Override
            public void onNext(Integer integer) {
                Log.i(TAG, "onNext: ");

                // we will add the values to the TextView
                mtextView.append(" number accepted: "+ integer);
                mtextView.append("\n");

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
        // we clear the subscriptions
        compositeDisposable.clear();
    }
}
