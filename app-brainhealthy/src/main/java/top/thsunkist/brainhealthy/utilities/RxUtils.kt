package top.thsunkist.brainhealthy.utilities

import com.tencent.bugly.crashreport.CrashReport
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.OnErrorNotImplementedException
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.Subject

val MainThread: Scheduler = AndroidSchedulers.mainThread()

/* if succeed*/
private val onNextStub: (Any) -> Unit = {}

/* failure */
private val onErrorStub: (Throwable) -> Unit =
    {
        CrashReport.postCatchedException(it)
        RxJavaPlugins.onError(OnErrorNotImplementedException(it))
    }

/* whatever */
private var onTerminateStub: () -> Unit = {}

fun <T : Any> Single<T>.observeOnMainThread(
    onSuccess: (T) -> Unit = onNextStub,
    onError: (Throwable) -> Unit = onErrorStub,
    onTerminate: () -> Unit = onTerminateStub,
    subscribeOnBackground: Boolean = true
): Disposable {
    return observeOn(MainThread).let {
        if (subscribeOnBackground) it.subscribeOn(Schedulers.io()) else it
    }.doAfterTerminate(onTerminate).subscribe(onSuccess, onError)
}

fun <T : Any> Subject<T>.observeOnMainThread(
    onNext: (T) -> Unit = onNextStub,
    onError: (Throwable) -> Unit = onErrorStub,
    onTerminate: () -> Unit = onTerminateStub,
    subscribeOnBackground: Boolean = true
): Disposable {
    return observeOn(MainThread).let {
        if (subscribeOnBackground) it.subscribeOn(Schedulers.io()) else it
    }.subscribe(onNext, onError, onTerminate)
}

fun <T> Single<T>.with(disposables: CompositeDisposable): Single<T> =
    doOnSubscribe { disposable -> disposables.add(disposable) }