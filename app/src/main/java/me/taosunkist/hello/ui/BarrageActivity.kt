package me.taosunkist.hello.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import me.taosunkist.hello.R
import me.taosunkist.hello.ui.barrage.MarqueeView
import java.util.*


class BarrageActivity : AppCompatActivity() {
    val randomStrings = arrayOf(
        "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed.", "do eiusmod tempor incididunt",
        "fugiat nulla pariatur. Excepteur sint occaecat cupidatat", "sunt in culpa qui officia", "nisi ut aliquid",
        "aliquid ex ea commodi consequatur", "inventore veritatis et quasi architecto",
        "beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem"
    )

    /**
     * Called when the activity is first created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barrage)

        // Marquee #1: Configuration using code.
        val mv = findViewById<View>(R.id.marqueeView100) as MarqueeView
        mv.setPauseBetweenAnimations(500)
        mv.setSpeed(10)
        window.decorView.post { mv.startMarquee() }
        val textView1: TextView = findViewById<View>(R.id.textView1) as TextView
        window.decorView.postDelayed({
            textView1.setText(randomStrings[Random().nextInt(randomStrings.size)])
            mv.startMarquee()
        }, 1000)

        // Marquee #2: Configured via XML.
        val textView2: TextView = findViewById<View>(R.id.textView2) as TextView
        findViewById<View>(R.id.btnChangeText).setOnClickListener { textView2.setText(randomStrings[Random().nextInt(randomStrings.size)]) }

        // Marquee #3: Manual Start/Stop
        val marqueeView3 = findViewById<View>(R.id.marqueeView3) as MarqueeView
        findViewById<View>(R.id.btnStart).setOnClickListener { marqueeView3.startMarquee() }
        findViewById<View>(R.id.btnStop).setOnClickListener { marqueeView3.reset() }
    }
}