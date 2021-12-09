package top.thsunkist.appkit.engine;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

/**
 * @author：luck
 * @date：2020/4/30 10:54 AM
 * @describe：Picasso加载引擎
 */
public class PicassoEngine implements ImageEngine {


    private PicassoEngine() {
    }

    private static PicassoEngine instance;

    public static PicassoEngine createPicassoEngine() {
        if (null == instance) {
            synchronized (PicassoEngine.class) {
                if (null == instance) {
                    instance = new PicassoEngine();
                }
            }
        }
        return instance;
    }

    @Override
    public void loadImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
        Picasso.get().load(Uri.parse(url)).into(imageView);
    }
}

