package top.thsunkist.tatame.utilities.annotation

import androidx.annotation.IntegerRes

@MustBeDocumented
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class ViewRes(@IntegerRes val res: Int)
