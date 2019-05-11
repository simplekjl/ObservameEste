package com.simplekjl.rxdemoimplementations;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.simplekjl.rxdemoimplementations.model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * FlatMap
 * <p>
 *
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "myApp";
    private String mTestString = "Hello from RxJava";
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Observable<Student> myObservable;
    private DisposableObserver<Student> myObserver;

    private TextView mtextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mtextView = findViewById(R.id.my_text);

        // starting to create the observer using the data source
        myObservable = Observable.create(new ObservableOnSubscribe<Student>() {
            @Override
            public void subscribe(ObservableEmitter<Student> emitter) throws Exception {
                // here we placed the object we want to emit
                List<Student> mList = getStudents();
                for (Student student : mList) {
                    emitter.onNext(student);
                }
                // once we are done emmiting data we call onNext
                emitter.onComplete();
            }
        });

        myObserver = getObserverOne();
        compositeDisposable
                .add(myObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .concatMap( new Function<Student, ObservableSource<Student>>(){
                            @Override
                            public Observable<Student> apply(Student student) throws Exception {
                                // concat will preserve the order of the elements if
                                // speed is important for the requirement use flatmap
                                student.setName(student.getName().toUpperCase());
                                return Observable.just(student);
                            }
                        })
                        .subscribeWith(myObserver));

    }

    private DisposableObserver<Student> getObserverOne() {
        return new DisposableObserver<Student>() {
            @Override
            public void onNext(Student s) {
                Log.i(TAG, "onNext: ");
                // here we are setting all the names to the same TextView
                mtextView.append(s.getName());
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

    // this elements represent a stream of data that can come form the network or database
    public List<Student> getStudents() {
        List<Student> mList = new ArrayList<>();
        String[] names = {"Paola", "Juan", "Jhon", "Erick", "Lukas"};
        String[] emails = {"anoes@gmail.com", "oksfd@gmail.com", "wer23@gmail.com"
                , "asdfw3@gmail.com", "3rt4@gmail.com"};

        String[] registration = {"TODAY", "12.03.19", "01.02.19", "19.03.18", "07.05.19"};

        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            Student student = new Student();
            student.setName(names[random.nextInt(4)]);
            student.setAge(random.nextInt(27));
            student.setEmail(emails[random.nextInt(4)]);
            student.setRegistrationDate(registration[random.nextInt(4)]);
            mList.add(student);
        }
        return mList;
    }
}
