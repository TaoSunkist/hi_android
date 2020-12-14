package top.thsunkist.brainhealthy.ui.home

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import top.thsunkist.brainhealthy.R
import top.thsunkist.brainhealthy.ui.BaseActivity

class MainActivity : BaseActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setSupportActionBar(findViewById(R.id.toolbar))

		findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
			Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show()
		}
	}
}