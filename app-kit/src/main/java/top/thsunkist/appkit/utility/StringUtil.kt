package top.thsunkist.appkit.utility

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.AnyRes
import androidx.annotation.DrawableRes
import java.util.regex.Matcher
import java.util.regex.Pattern

class StringUtil {

    companion object {

        /**
         * @param text
         * @param targetWord
         *
         * @return return target start-position and end-position in text-content
         */
        @JvmStatic
        fun findKeyWordPositionInTextPart(text: String = "I love you so much", targetWord: String = "love"): Pair<Int, Int> {
            val word: Pattern = Pattern.compile(targetWord)
            val match: Matcher = word.matcher(text)

            var startPosition = -1
            var endPosition = -1
            while (match.find()) {
                startPosition = match.start()
                endPosition = match.end()
                println("Found love at index " + match.start().toString() + " - " + (match.end()))
            }

            return Pair(startPosition, endPosition)
        }


        /**
         */
        @AnyRes
        fun getAnyResID(context: Context, defType: String = "mipmap", resName: String?): Int? {
            return if (resName.isNullOrBlank()) {
                val resID = context.resources.getIdentifier(resName, "mipmap", context.packageName)
                return if (resID > 0) {
                    resID
                } else {
                    null
                }
            } else {
                null
            }
        }
    }

}