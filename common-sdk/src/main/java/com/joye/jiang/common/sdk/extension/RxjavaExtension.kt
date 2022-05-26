package com.joye.jiang.common.sdk.extension

import android.annotation.SuppressLint
import androidx.annotation.Keep
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers


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
 * 切换到io线程
 */
@Keep
fun <T> Observable<T>.thread2New(): Observable<T> {
    return this.observeOn(Schedulers.newThread())
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
    return this.onErrorResumeNext(Function {
        it.printStackTrace()
        Observable.empty()
    })
}
@Keep
@SuppressLint("CheckResult")
fun <T> Observable<T>.toObservable(): io.reactivex.rxjava3.core.Observable<T> {
    return io.reactivex.rxjava3.core.Observable.create<T> { emmit ->
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


