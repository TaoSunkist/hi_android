package top.thsunkist.brainhealthy.network.model

import java.io.IOException
import kotlin.reflect.full.declaredMemberProperties

/**
 * 用Reflection的方式recursive的检查一个kotlin object里面
 * 是否所有的non-null field都不是null，因为默认Gson是不管optional的
 * 有些特定的数据我们必须要确保不能是null，如果是null就应该当是错误.
 */
fun validate(obj: Any?) {
    if (obj == null) {
        return
    }
    if (obj is List<*>) {
        for (v in obj) {
            validate(obj = v)
        }
        return
    }
    obj::class.declaredMemberProperties.forEach { field ->
        val value = field.getter.call(obj)
        if (field.returnType.isMarkedNullable.not()) {
            if (value == null) {
                val message = "Field $field is missing"
                println(message)
                throw IOException(message)
            }
        }

        value?.let {
            validate(obj = value)
        }
    }
}