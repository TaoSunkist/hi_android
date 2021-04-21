package me.taosunkist.hello.utility

import android.graphics.Color
import android.util.Log
import me.taosunkist.hello.HiApplication
import me.taosunkist.hello.R
import java.util.ArrayList

fun printf(vararg args: Any?) {
//    if (BuildConfig.DEBUG) {
    Log.d("hello-android", args.joinToString(" "))
//    }
}

fun printfE(vararg args: Any?) {
//    if (BuildConfig.DEBUG) {
    Log.e("hello-android", args.joinToString(" "))
//    }
}

fun printfNetwork(vararg args: Any?) {
//    if (BuildConfig.DEBUG) {
    Log.d("hello-android", args.joinToString(" "))
//    }
}

class Debug {
    companion object {
        val images = listOf(
                "https://cdn.pixabay.com/photo/2019/11/30/04/03/bharatanatyam-4662487_1280.jpg",
                "https://cdn.pixabay.com/photo/2013/05/01/21/46/tango-108483_1280.jpg",
                "https://cdn.pixabay.com/photo/2016/05/12/23/03/lamb-1388937_1280.png",
                "https://cdn.pixabay.com/photo/2015/04/03/16/36/bee-705412_1280.png",
                "https://cdn.pixabay.com/photo/2016/09/14/20/50/teeth-1670434_1280.png",
                "https://cdn.pixabay.com/photo/2015/12/05/08/25/fairy-tale-1077863_1280.jpg",
                "https://cdn.pixabay.com/photo/2012/12/27/19/41/halloween-72939_1280.jpg",
                "https://cdn.pixabay.com/photo/2016/09/07/11/37/tropical-1651423_1280.jpg",
                "https://cdn.pixabay.com/photo/2013/06/07/06/51/tree-117582_1280.jpg",
                "https://cdn.pixabay.com/photo/2014/12/22/00/07/tree-576847_1280.png",
                "https://cdn.pixabay.com/photo/2015/07/27/20/16/book-863418_1280.jpg",
                "https://cdn.pixabay.com/photo/2013/07/18/10/56/house-163526_1280.jpg",
                "https://cdn.pixabay.com/photo/2014/12/08/11/49/love-560783_1280.jpg",
                "https://cdn.pixabay.com/photo/2016/06/14/14/09/skeleton-1456627_1280.png",
                "https://cdn.pixabay.com/photo/2016/10/19/02/21/moon-1751987_1280.png",
                "https://cdn.pixabay.com/photo/2016/07/02/12/21/eclipse-1492818_1280.jpg",
                "http://photocdn.sohu.com/20111115/Img325612940.jpg",
                "http://img3.cache.netease.com/photo/0031/2016-05-06/BMBHD1O56LRJ0031.jpg",
                "http://i.gongxiao8.com/uploads/i_1_1027848948x2494305301_26.jpg",
                "http://img.wxcha.com/file/201901/10/56f2ece12e.jpg",
                "http://img.wxcha.com/file/201812/21/89d683ab85.jpg",
                "http://img.wxcha.com/file/201807/18/e605ec3f55.jpg",
                "http://img.wxcha.com/file/201809/30/b8293c6a8a.jpg",
                "http://img.wxcha.com/file/201812/11/93bcbfd5f3.jpg",
                "http://img4.imgtn.bdimg.com/it/u=3175508956,2902264390&fm=26&gp=0.jpg",
                "http://img0.imgtn.bdimg.com/it/u=1560417182,3068121638&fm=26&gp=0.jpg",
                "http://img5.imgtn.bdimg.com/it/u=1647889927,4015832972&fm=26&gp=0.jpg",
                "http://img0.imgtn.bdimg.com/it/u=1330428072,249467376&fm=26&gp=0.jpg",
                "http://img3.imgtn.bdimg.com/it/u=1483331499,286472910&fm=26&gp=0.jpg",
                "http://img.wxcha.com/file/201812/11/93bcbfd5f3.jpg",
                "http://img3.imgtn.bdimg.com/it/u=3722249721,1705042876&fm=26&gp=0.jpg",
                "http://pic.962.net/up/2017-10/20171026113429875970.jpg",
                "http://wx3.sinaimg.cn/large/994b6f2egy1fr2kio27mij20j80iuwfe.jpg"
        )
        val medias = listOf("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-10.mp3")


        var colors = ArrayList<Int>().apply {
            add(Color.parseColor("#FFA54F"))
            add(Color.parseColor("#FFFF00"))
            add(Color.parseColor("#FF83FA"))
            add(Color.parseColor("#63B8FF"))
            add(Color.parseColor("#EE2C2C"))
            add(Color.parseColor("#9B30FF"))
            add(Color.parseColor("#6B8E23"))
            add(Color.parseColor("#404040"))
        }
    }
}
