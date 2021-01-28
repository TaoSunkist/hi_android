package me.taosunkist.hello.kotlinpractice

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ActorScope
import org.junit.Test
import kotlin.concurrent.thread

/* https://www.kotlincn.net/docs/reference/coroutines/basics.html */
/**
 * 当我们使用 GlobalScope.launch 时，我们会创建一个顶层协程。
 */
class CoroutineTest {

    /**
     * delay和thread.sleep的区别 ?
     */
    @Test
    fun coroutineTest() {
        println(Thread.currentThread().name)
        /* 实际开发中不要这样使用GlobalScope启动一个协程, GlobalScope.launch会创建一个顶级协程和Go的协程类似 */

        thread {
            println("thread block in " + Thread.currentThread().name + " ${Thread.currentThread().id}")
            Thread.sleep(1000)

            GlobalScope.launch {
                println("GlobalScope.launch in " + Thread.currentThread().name + " ${Thread.currentThread().id}")
                println("World")
                /* 这是因为 delay 是一个特殊的 挂起函数 ，它不会造成线程阻塞，但是会 挂起 协程，并且只能在协程中使用 */
                delay(timeMillis = 1000L)
            }

            runBlocking {
                println("thread block runBlocking in " + Thread.currentThread().name + " ${Thread.currentThread().id}")
                delay(timeMillis = 500L)
            }
        }
        println("Hello,")
        /* block main-thread, 调用了 runBlocking 的主线程会一直 阻塞 直到 runBlocking 内部的协程执行完毕。 */
        runBlocking {
            println("runBlocking in " + Thread.currentThread().name + " ${Thread.currentThread().id}")
            delay(timeMillis = 5000L)
        }
    }

    @Test
    fun coroutineScope() {
    }

    @Test
    fun testRunBlocking() = runBlocking {
        var a = 0
        val job = GlobalScope.launch() {
            while (a < 4) {
                a++
                println("taohui $a")
                delay(500)
            }
        }
        println("taohui $this")
        job.join()
    }

    @Test
    fun runBlockingAssertTest() = runBlocking {
        assert(value = (0..5).random() == 1, lazyMessage = {
            println("lazyMessage")
        })
    }

    /**
     * explain this
     */
    @Test
    fun waitGlobalScopeCoroutineCompleteTest() = runBlocking {
        println(Thread.currentThread().name)
        val job = GlobalScope.launch {
            println(Thread.currentThread().name)
            delay(3000L)
            println("coroutine completed")
        }
        println("coroutine was launch")
        job.join()
        println("coroutine was callback to main-coroutine")
    }

    /**
     * https://www.kotlincn.net/docs/reference/coroutines/basics.html#%E7%BB%93%E6%9E%84%E5%8C%96%E7%9A%84%E5%B9%B6%E5%8F%91
     * 在我们的示例中，我们使用 runBlocking 协程构建器将 waitRunBlockingScopeCoroutineCompleteTest 函数转换为协程。 包括 runBlocking 在内的每个协程
     * 构建器都将 CoroutineScope 的实例添加到其代码块所在的作用域中。 我们可以在这个作用域中启动协程而无需显式 join
     * 之，因为外部协程（示例中的 runBlocking）直到在其作用域中启动的所有协程都执行完毕后才会结束。因此，可以将我们
     * 的示例简化为
     */
    @Test
    fun waitRunBlockingScopeCoroutineCompleteTest() = runBlocking(block = {
        val job = launch {
            println("2." + Thread.currentThread().name)
            delay(3000L)
            println("coroutine completed")
        }
        println("coroutine was launch")
        job.join()
        println("coroutine was callback to main-coroutine")
    })

    /**
     * 作用域构建器，自定义作用域
     * runBlocking 与 coroutineScope 可能看起来很类似，因为它们都会等待其协程体以及所有子协程结束。 主要区别在
     * 于，runBlocking 方法会阻塞当前线程来等待， 而 coroutineScope 只是挂起，会释放底层线程用于其他用途。 由于
     * 存在这点差异，runBlocking 是常规函数，而 coroutineScope 是挂起函数。
     */
    @Test
    fun customCoroutineScopeTest() = runBlocking {/* 开始执行一个主协程 */
        println(Thread.currentThread().name + "-")
        launch { /* 启动一个新协程并继续 */
            println(Thread.currentThread().name + "--")
            delay(200L)
            println("Task from runBlocking")
        }
        coroutineScope { /* coroutine 作用域 */
            println(Thread.currentThread().name + "---")
            launch {
                println(Thread.currentThread().name + "----")
                delay(500L)
                println("Task from nested launch")
            }
            delay(100L)
            println("Task from coroutine scope")
        }
        println("Coroutine scope is over")
    }

    @Test
    fun coroutineLaunchCancel() = runBlocking {
        val job = launch {
            var i = 0
            while (i < 5) {
                println("You're rock: $i, ${Thread.currentThread().name}")
                delay(200L)
                i++
            }
        }
        println("I'm will cancel: ${Thread.currentThread().name}")
        job.cancel()
        println("I'm was canceled: ${Thread.currentThread().name}")
    }

    /**
     * launch中不同的context区别
     */
    @Test
    fun coroutineLaunchHasArgCancel() = runBlocking {
        val job = launch(context = Dispatchers.Default) {
            var i = 0
            while (i < 5) {
                println("You're rock: $i, ${Thread.currentThread().name}")
                delay(200L)
                i++
            }
        }
        println("I'm will cancel: ${Thread.currentThread().name}")
        job.cancel()
        println("I'm was canceled: ${Thread.currentThread().name}")
    }

    @Test
    fun coroutineCancelAndJoin() = runBlocking {
        val startTime = System.currentTimeMillis()
        val job = launch(context = Dispatchers.Default) {
            try {
                var nextPrintTime = startTime
                var i = 0
                while (isActive) { // 一个执行计算的循环，只是为了占用 CPU
                    // 每秒打印消息两次
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        println("job: I'm sleeping ${i++} ...")
                        nextPrintTime += 1000L
                    }
                }
            } finally {
                /* println("main: Now I can quit.")与这一行顺序随机 */
                println("job: i'm running finally.")
            }
        }
        delay(1300L) // 等待一段时间
        println("main: I'm tired of waiting!")
        job.cancel() // 取消一个作业并且等待它结束
        println("main: Now I can quit.")
    }

    /* 挂起一个被取消的协程，你可以将相应的代码包装在 withContext(NonCancellable) {……} 中，
       并使用 withContext 函数以及 NonCancellable 上下文,
       TODO 搞不懂什么叫挂起一个被取消的协程, 意思是被取消了还能存活一阵子? */
    @Test
    fun coroutineWidthContext() = runBlocking {
        val job = launch {
            try {
                repeat(1000) { i ->
                    println("job: I'm sleeping $i ...")
                    delay(500L)
                }
            } finally {
                withContext(NonCancellable) {
                    /* 这样一个被取消的协程就被挂起了呢! 但是, 有什么用呢? */
                    println("job: I'm running finally")
                    delay(1000L)
                    println("job: And I've just delayed for 1 sec because I'm non-cancellable")
                }
            }
        }
        delay(1300L) // 延迟一段时间
        println("main: I'm tired of waiting!")
        job.cancelAndJoin() // 取消该作业并等待它结束
        println("main: Now I can quit.")
    }

    @Test
    fun coroutineTimeout() = runBlocking(block = {
        /* withTimeout 抛出了 TimeoutCancellationException，它是 CancellationException 的子类 */
        withTimeout(1300L) {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
            /* 如果你需要做一些各类使用超时的特别的额外操作 */
        }
    })

    @Test
    fun coroutineFrequency(): Unit = runBlocking {
        val job = launch(Dispatchers.Default) {
            delay(100L)
            println("1")
        }
        val job1 = launch {
            println("2")
        }
        val job2 = launch {
            println("3")
        }

    }

    @Test
    fun coroutineDispatcherDefault() {

        val coroutineScope = CoroutineScope(Dispatchers.IO)
        (0..20).forEach { _ ->
            unreadMessageCount = 1
            coroutineScope.launch { // 将会获取默认调度器
                println("Default               : I'm working in thread ${unreadMessageCount}")
            }
        }

        runBlocking { delay(1000) }
    }

    var unreadMessageCount: Int = 0
        set(value) {
            field += value
        }
}