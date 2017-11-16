package com.tistory.lky1001.rxjavatutorial;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.SchedulerSupport;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kai.1001 on 2017. 11. 16..
 */
public class ObservableExample {

    public static void main(String[] args) {
        ObservableExample observableExample = new ObservableExample();
        observableExample.testObservable();
        observableExample.testFromArray();
    }

    public void testFromArray() {
        // 생성한 스레드에서 observable이 실행됨
        (new Thread() {
            public void run() {
                Observable
                        .fromArray(intList())
                        .blockingForEach(integer -> {
                                System.out.println(Thread.currentThread().getName() + " : " + integer);
                        });
                // 아래와 같은 코드이기 때문
                /*
                Integer[] list = intList();
                Observable
                        .fromArray(list)
                        .blockingForEach(integer -> {
                            System.out.println(Thread.currentThread().getName() + " : " + integer);
                        });
                */
            }
        }).start();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Integer[] intList() {
        System.out.println("intList : " + Thread.currentThread().getName());
        return new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
    }

    public void testObservable() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> observer) {
                try {
                    System.out.println("subscribe :" + Thread.currentThread().getName());

                    if (!observer.isDisposed()) {
                        for (int i = 0; i < 5; i++) {
                            observer.onNext(i);
                        }

                        observer.onComplete();
                    }
                } catch (Exception e) {
                    if (!observer.isDisposed()) {
                        observer.onError(e);
                    }
                }
            }
        })
        .subscribeOn(Schedulers.io())
        .subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                System.out.println("onSubscribe : " + d.isDisposed());
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println(Thread.currentThread().getName() + " : " + integer);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }
}
