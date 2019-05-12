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
 * Filter operator using Predicates
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

        myObservable = Observable.range(1,20);

        myObserver = getRangeIntegerObserver();

        myObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        // here we can filter the object simply using the logic we need to easily
                        // skip some of them from the rest
                        return integer %3 == 0;
                    }
                }).subscribe(myObserver);

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
