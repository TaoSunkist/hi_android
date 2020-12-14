package top.thsunkist.brainhealthy.utilities.view

import android.content.Context
import android.widget.Toast
import top.thsunkist.brainhealthy.R
import top.thsunkist.brainhealthy.utilities.getDrawable
import es.dmoral.toasty.Toasty

class ToastyExt {

	companion object {

		fun success(context: Context, content: String): Toast = Toasty.normal(
			context,
			content,
			getDrawable(context.resources, R.drawable.ic_baseline_check_circle_outline_24, 0)
		)

		fun error(context: Context, content: String): Toast = Toasty.normal(
			context,
			content,
			getDrawable(context.resources, R.drawable.ic_baseline_error_outline_24, 0)
		)
	}
}

