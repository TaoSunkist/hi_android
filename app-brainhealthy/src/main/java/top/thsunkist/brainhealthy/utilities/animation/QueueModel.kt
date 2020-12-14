package top.thsunkist.brainhealthy.utilities.animation

import android.os.Handler
import android.os.Message
import java.util.concurrent.LinkedBlockingQueue

/**
 *  author: Fzq
 *  date: 2020-04-23
 *  description:
 */
class QueueModel<T> {
    private var queue: LinkedBlockingQueue<T> = LinkedBlockingQueue()
    private var handler: Handler? = null//推送数据
//    private var coroutineScope : CoroutineScope = CoroutineScope(Job() + Dispatchers.IO )
    private var canTakeValue : Int  = 0
    var delayTime = 20L
    private val runnable = Runnable {
        while (true) {
            try {
                Thread.sleep(delayTime)
                if (canTakeValue <= 0) continue
                take()
                canTakeValue --
            } catch (e : InterruptedException) {
                break
            }

        }
    }

    private val thread = Thread(runnable)

    init {
//        coroutineScope.launch {
//            while (true) {
//                delay(20)
//                if (canTakeValue <= 0) continue
//                take()
//                canTakeValue --
//            }
//
//        }
        thread.start()
    }

    fun offer(obj: T) {
        queue.offer(obj)
    }

    fun takeValue() {
//        coroutineScope.launch {
//            delay(50)
//            take()
//        }
        canTakeValue++
    }

    fun getQueue(): LinkedBlockingQueue<T> {
        return queue
    }

    private fun take() {
        try {
            val result = queue.take()
            if (result != null && handler != null) {
                val message = Message()
                message.obj = result
                handler?.sendMessage(message)
            } else {
                takeValue()
            }
        } catch (e: InterruptedException) {
//            LogUtil.t(e)
//            takeValue()
        } catch (e: NullPointerException) {
//            LogUtil.t(e)
//            takeValue()
        }

    }

    fun getHandler(): Handler? {
        return handler
    }

    fun setHandler(handler: Handler) {
        this.handler = handler
    }

    /**
     * 销毁之前调用
     */
    fun clear() {
        //        if(thread!=null && thread.isAlive()){
        //            thread.interrupt();
        //        }
        handler = null
        queue.clear()
//        if (coroutineScope.isActive) coroutineScope.cancel()
        if (thread.isAlive) thread.interrupt()
    }

    fun takeQueueValue(): T? {
        try {
            return queue.take() as T
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        return null
    }
}