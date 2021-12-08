package me.taosunkist.hello.utility

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
        fun findWordPosition(text: String = "I love you so much", targetWord: String = "love"): Pair<Int, Int> {
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
    }

}