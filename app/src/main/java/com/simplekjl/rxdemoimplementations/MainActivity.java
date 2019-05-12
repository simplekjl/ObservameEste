package com.simplekjl.rxdemoimplementations;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;

/**
 * Async subject
 * <p>
 * this operator just emmits the last object given from an observable
 * A subject can be an observable and an observer.
 *
 * Behavioral Subject
 * will provide the most recent object before any obervers gets subscribe to it
 *
 * PublishSubject
 * it emits all the objects of the observable at subscription time
 *
 * Replay subject
 * it emits all the items of the observable without considering when the subscriber subscribed
 *
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "myApp";
    private String mTestString = "Hello from RxJava";

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private Observable<String> myObservable;

    private DisposableObserver<String> firstObserver;
    private DisposableObserver<String> secondObserver;
    private DisposableObserver<String> thirdObserver;

    private TextView mtextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mtextView = findViewById(R.id.my_text);

        // starting the observers
        firstObserver = getFirstObserver();
        secondObserver = getSecondObserver();
        thirdObserver = getThirdObserver();

        // starting the example 1
        //asyncSubjectDemoOne();
        // starting example 2
        //asyncSubJectDemoTwo();
        // starting the example 3
        // behavioral Subject
        //behaviorSubjectDemoOne();

        // behavior Subject number 2
        //behaviorSubjectDemoTwo();

        // publish subject demo 1
        //publishSubjectDemoOne();
        // publish subject demo 2
        //publishSubjectDemoTwo();

        //replay subject
        //replaySubjectDemoOne();
        replaySubjectDemoTwo();


        //add the observers into the compositeDisposable
        compositeDisposable.add(firstObserver);
        compositeDisposable.add(secondObserver);
        compositeDisposable.add(thirdObserver);

    }


    void asyncSubJectDemoTwo(){

        AsyncSubject<String> asyncSubject = AsyncSubject.create();
        //we can start emmiting object since we have the option with the asyncSubject
        // check this out, I will do this in different places and also subscribing object after
        // I completed the process
        asyncSubject.onNext("Kotlin");

        asyncSubject.onNext("JAVA");
        // the async object will be observing the main observable and reply with the last item
        // coming from it.
        asyncSubject.subscribe(firstObserver);
        asyncSubject.subscribe(secondObserver);
        asyncSubject.onNext("BERLIN");
        // this line ends the emmition of objects and we are subscribing one more item to the
        // asyncSubject
        asyncSubject.onComplete();
        // the observer three will also get the last value emitted by the subject
        asyncSubject.subscribe(thirdObserver);

    }

    void asyncSubjectDemoOne() {

        myObservable = Observable.just("JAVA", "KOTLIN", "GERMANY", "BERLIN")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        AsyncSubject<String> asyncSubject = AsyncSubject.create();
        // we subscribe using the async object
        myObservable.subscribe(asyncSubject);

        // the async object will be observing the main observable and reply with the last item
        // coming from it.
        asyncSubject.subscribe(firstObserver);
        asyncSubject.subscribe(secondObserver);
        asyncSubject.subscribe(thirdObserver);

    }

    void behaviorSubjectDemoOne() {

        myObservable = Observable.just("JAVA", "KOTLIN", "GERMANY", "BERLIN")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        BehaviorSubject<String> behaviorSubject = BehaviorSubject.create();
        // we subscribe using the async object
        myObservable.subscribe(behaviorSubject);

        // the async object will be observing the main observable and reply with the last item
        // coming from it.
        behaviorSubject.subscribe(firstObserver);
        behaviorSubject.subscribe(secondObserver);
        behaviorSubject.subscribe(thirdObserver);

    }

    void behaviorSubjectDemoTwo(){

        BehaviorSubject<String> behaviorSubject = BehaviorSubject.create();
        behaviorSubject.onNext("Kotlin");
        behaviorSubject.onNext("JAVA");
        // the async object will be observing the main observable and reply with the last item
        // coming from it.
        behaviorSubject.subscribe(firstObserver);
        behaviorSubject.subscribe(secondObserver);
        behaviorSubject.onNext("BERLIN");
        // this line ends the emmition of objects and we are subscribing one more item to the
        // behaviorSubject
        behaviorSubject.onComplete();
        // behavior subject emits the most recent one to the observers
        // in this case the third observer is not going to get anything since the behaviorSubject has completed
        behaviorSubject.subscribe(thirdObserver);

    }

    void publishSubjectDemoOne() {

        myObservable = Observable.just("JAVA", "KOTLIN", "GERMANY", "BERLIN")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        PublishSubject<String> publishSubject = PublishSubject.create();
        // we subscribe the publish object
        myObservable.subscribe(publishSubject);

        // now we subscribe the observers to the publish object
        // the sult is that all of them get the same value
        publishSubject.subscribe(firstObserver);
        publishSubject.subscribe(secondObserver);
        publishSubject.subscribe(thirdObserver);

    }

    void publishSubjectDemoTwo(){

        PublishSubject<String> publishSubject = PublishSubject.create();

        publishSubject.subscribe(firstObserver);
        publishSubject.onNext("Kotlin");
        publishSubject.onNext("JAVA");
        publishSubject.subscribe(secondObserver);
        publishSubject.onNext("BERLIN");

        publishSubject.onComplete();

        publishSubject.subscribe(thirdObserver);

    }

    void replaySubjectDemoOne() {

        myObservable = Observable.just("JAVA", "KOTLIN", "GERMANY", "BERLIN")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        ReplaySubject<String> replay = ReplaySubject.create();
        // we subscribe the publish object
        myObservable.subscribe(replay);

        // now we subscribe the observers to the publish object
        // the sult is that all of them get the same value
        replay.subscribe(firstObserver);
        replay.subscribe(secondObserver);
        replay.subscribe(thirdObserver);

    }

    void replaySubjectDemoTwo(){

        ReplaySubject<String> replaySubject = ReplaySubject.create();

        replaySubject.subscribe(firstObserver);
        replaySubject.onNext("Kotlin");
        replaySubject.onNext("JAVA");
        replaySubject.subscribe(secondObserver);
        replaySubject.onNext("BERLIN");

        replaySubject.onComplete();

        replaySubject.subscribe(thirdObserver);

    }


    private DisposableObserver<String> getFirstObserver() {
        return new DisposableObserver<String>() {
            @Override
            public void onNext(String string) {
                Log.i(TAG, "onNext: ");

                // we will add the values to the TextView
                mtextView.append(" Observer 1: " + string);
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

    private DisposableObserver<String> getSecondObserver() {
        return new DisposableObserver<String>() {
            @Override
            public void onNext(String string) {
                Log.i(TAG, "onNext: ");

                // we will add the values to the TextView
                mtextView.append(" Observer 2: " + string);
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

    private DisposableObserver<String> getThirdObserver() {
        return new DisposableObserver<String>() {
            @Override
            public void onNext(String string) {
                Log.i(TAG, "onNext: ");

                // we will add the values to the TextView
                mtextView.append(" Observer 3 : " + string);
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
