package com.tistory.lky1001.rxjavatutorial;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.SchedulerSupport;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by kai.1001 on 2017. 11. 16..
 */
public class ObservableExample {

    public static void main(String[] args) {
        ObservableExample observableExample = new ObservableExample();
        observableExample.testObservable();
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
