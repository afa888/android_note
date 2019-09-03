package com.example.android.Rxjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.android.EventBusDemo.Person;
import com.example.android.R;
import com.example.android.Student;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

//http://gank.io/post/560e15be2dca930e00da1083
public class RxjavaActivity extends AppCompatActivity {
    Subscription sub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);

        sub = RxBus1.getDefault().toObservable(Person.class).subscribe(new Action1<Person>() {
            @Override
            public void call(Person data) {
                Log.e("RxBus1","我收到消息了");
                Toast.makeText(RxjavaActivity.this, "我收到消息了==" + data.getName(), Toast.LENGTH_SHORT).show();
            }
        });


        final Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.e("observer", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("observer", e.toString());

            }

            @Override
            public void onNext(String s) {
                Log.e("observer", s);

            }
        };


        final Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.e("subscriber", "onCompleted");

            }

            @Override
            public void onError(Throwable e) {
                Log.e("subscriber", e.toString());

            }

            @Override
            public void onNext(String s) {
                Log.e("subscriber", s);

            }
        };

        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("setOnClickListener", "button5");

                Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext("hello");
                        subscriber.onCompleted();
                    }
                });
                //两种方法都可以
                observable.subscribe(subscriber);
                // observable.subscribe(observer);
            }
        });

        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Observable observable = Observable.just("Hello", "Hi", "Aloha");
                observable.subscribe(observer);
            }
        });


        final Action1<String> onNextAction = new Action1<String>() {
            // onNext()
            @Override
            public void call(String s) {
                Log.e("onNextAction", s);
            }
        };
        final Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            // onError()
            @Override
            public void call(Throwable throwable) {
                // Error handling
                Log.e("onErrorAction", throwable.toString());

            }
        };
        final Action0 onCompletedAction = new Action0() {
            // onCompleted()
            @Override
            public void call() {
                Log.e("onCompletedAction", "completed");
            }
        };

        findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Observable observable = Observable.just("Hello", "Hi", "Aloha");
                // 自动创建 Subscriber ，并使用 onNextAction 来定义 onNext()
                observable.subscribe(onNextAction);
// 自动创建 Subscriber ，并使用 onNextAction 和 onErrorAction 来定义 onNext() 和 onError()
                observable.subscribe(onNextAction, onErrorAction);
// 自动创建 Subscriber ，并使用 onNextAction、 onErrorAction 和 onCompletedAction 来定义 onNext()、 onError() 和 onCompleted()
                observable.subscribe(onNextAction, onErrorAction, onCompletedAction);
            }
        });
        //将字符串数组 names 中的所有字符串依次打印出来
        findViewById(R.id.button8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] names = {"第一", "第二", "第三", "第四"};
                Observable.from(names)
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                Log.e("button8", s);

                            }
                        });
            }
        });

        findViewById(R.id.button9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // map(): 一对一的事件对象的直接变换
                Student[] students = {new Student(), new Student()};
                Subscriber<String> subscriber = new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String name) {
                        Log.e("button9", name);
                    }

                };
                Observable.from(students)
                        .map(new Func1<Student, String>() {
                            @Override
                            public String call(Student student) {
                                return student.name;
                            }
                        })
                        .subscribe(subscriber);
            }
        });

        findViewById(R.id.button10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // map(): 一对一的事件对象的直接变换
                Student[] students = {new Student(), new Student()};
                Subscriber<String> subscriber = new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String name) {
                        Log.e("button9", name);
                    }

                };
                Observable.from(students)
                        .map(new Func1<Student, String>() {
                            @Override
                            public String call(Student student) {
                                return student.name;
                            }
                        })
                        .subscribe(subscriber);
            }
        });

        findViewById(R.id.button11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // map(): 一对一的事件对象的直接变换
                Student[] students = {new Student(), new Student()};
                Subscriber<String> subscriber = new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String name) {
                        Log.e("button9", name);
                    }

                };
                Observable.from(students)
                        .map(new Func1<Student, String>() {
                            @Override
                            public String call(Student student) {
                                return student.name;
                            }
                        })
                        .subscribe(subscriber);
            }
        });

        findViewById(R.id.button12).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // data 为任意数据类型，以 Data 类型代表
                RxBus1.getDefault().post(new Person("afa", "18"));
            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 监听使用离开之后（如关闭监听所在界面）时记得解绑监听，避免引起内存泄漏
        if (sub != null && !sub.isUnsubscribed()) {
            sub.unsubscribe();
        }
    }
}
