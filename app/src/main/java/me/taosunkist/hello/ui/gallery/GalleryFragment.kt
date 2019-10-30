package me.taosunkist.hello.ui.gallery

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import me.taosunkist.hello.picturechooser.PictureCfg
import me.taosunkist.hello.picturechooser.PictureChooser
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import me.taosunkist.hello.R
import me.taosunkist.hello.utility.permissions.RxPermissions


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class GalleryFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) listener = context
        else throw RuntimeException("$context must implement OnFragmentInteractionListener")
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GalleryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun onButtonPressed(uri: Uri) = listener?.onFragmentInteraction(uri)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rxPermissions = RxPermissions(activity as FragmentActivity)
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
            .subscribe(object : Observer<Boolean> {
                override fun onSubscribe(d: Disposable) {}

                override fun onNext(aBoolean: Boolean) {
                    Log.i("taohui", "" + aBoolean)
                    if (aBoolean!!) {
                        readLocalMedia()
                    } else {
                        readLocalMedia()
                    }
                }

                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            })

    }

    /**
     * 读取所有的本地媒体图片
     */
    private fun readLocalMedia() {
        val mediaLoader = PictureChooser(PictureCfg.TYPE_IMAGE.tag, activity, false)
        mediaLoader.loadAllMedia()

    }
}







