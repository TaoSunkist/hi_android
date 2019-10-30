package me.taosunkist.hello.ui.aliplayer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aliyun.player.source.UrlSource
import com.example.aliplayer.AliyunVodPlayerView
import com.example.aliplayer.PlayParameter
import me.taosunkist.hello.R

class AliplayerFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = AliplayerFragment().apply { }
    }

    private lateinit var mAliyunVodPlayerView: AliyunVodPlayerView

    private val defaultVid = "8bb9b7d5c7c64cf49d51fa808b1f0957"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAliyunVodPlayerView = view.findViewById(R.id.fragment_blank_aliyun_vod_player_view)
        mAliyunVodPlayerView.keepScreenOn = true
        PlayParameter.PLAY_PARAM_URL = "http://player.alicdn.com/video/aliyunmedia.mp4"
        PlayParameter.PLAY_PARAM_VID = defaultVid

        val sdDir = context!!.cacheDir.absolutePath + "/save_cache"
        mAliyunVodPlayerView.setPlayingCache(false, sdDir, 60 * 60 /*时长, s */, 300 /*大小，MB*/)
        mAliyunVodPlayerView.setTheme(AliyunVodPlayerView.Theme.Blue)
        mAliyunVodPlayerView.setAutoPlay(true)
        setPlaySource()
    }

    private fun setPlaySource() {
        val urlSource = UrlSource()
        urlSource.uri = PlayParameter.PLAY_PARAM_URL
        var maxDelayTime = 5000
        val playerConfig = mAliyunVodPlayerView.playerConfig
        playerConfig.mMaxDelayTime = maxDelayTime //开启SEI事件通知
        playerConfig.mEnableSEI = true
        mAliyunVodPlayerView.setPlayerConfig(playerConfig)
        mAliyunVodPlayerView.setLocalSource(urlSource)
    }
}
