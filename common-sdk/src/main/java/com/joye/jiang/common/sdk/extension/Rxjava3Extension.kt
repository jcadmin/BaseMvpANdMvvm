package com.joye.jiang.common.sdk.extension

import androidx.annotation.Keep
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

@Keep
fun <T> Observable<T>.io2main(): Observable<T> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

/**
 * 切换到io线程
 */
@Keep
fun <T> Observable<T>.thread2Io(): Observable<T> {
    return this.observeOn(Schedulers.io())
}

/**
 * 切换到ui线程
 */
@Keep
fun <T> Observable<T>.thread2Main(): Observable<T> {
    return this.observeOn(AndroidSchedulers.mainThread())
}

@Keep
fun <T> Observable<T>.errorEmpty(): Observable<T> {
    return this.onErrorResumeNext {
        it.printStackTrace()
        Observable.empty()
    }
}

@Keep
fun <T> Observable<T>.toObservable(): io.reactivex.Observable<T> {
    return io.reactivex.Observable.create<T> { emmit ->
        this.doOnNext {
            emmit.onNext(it)
        }.doOnError {
            emmit.onError(it)
        }.doOnComplete {
            emmit.onComplete()
        }.subscribe({

        }, {

        })
    }
}

