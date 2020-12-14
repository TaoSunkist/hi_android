package top.thsunkist.brainhealthy.utilities.animation;

import android.content.Context;
import android.view.ViewGroup;

/**
 * Created by ksbk on 2017/5/17.
 */

public interface ImmViewDispatcher {
    public void bind(Context context, ViewGroup layout);

    public void unBind();

}
