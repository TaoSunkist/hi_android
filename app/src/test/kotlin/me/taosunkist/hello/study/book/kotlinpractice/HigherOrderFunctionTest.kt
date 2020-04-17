package me.taosunkist.hello.study.book.kotlinpractice

import org.junit.Test
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

class HigherOrderFunctionTest {

	/*我们在写代码的时候难免会遇到这种情况,就是很多处的代码是一样的,于是乎我们就会抽取出一个公共方法来进行调用,这样
	看起来就会很简洁;但是也出现了一个问题,就是这个方法会被频繁调用,就会很耗费资源*/
	private inline fun <T> synchronized(lock: Lock, action: () -> T): T {
		lock.lock()
		try {
			return action()
		} finally {
			lock.unlock()
		}
	}

	@Test
	fun inlineTest() {
		val l = ReentrantLock()
		synchronized(lock = l) {
			println("TODO... ...")
		}
		/*等同于
		lock.lock()
		try {
			return action()
		} finally {
			lock.unlock()
		}
		*/
	}
}