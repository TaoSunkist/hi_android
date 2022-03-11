package me.taosunkist.hello.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable

open class BaseFragment<in T>(@LayoutRes private val layoutResId: Int) : Fragment() {

    private val compositeDisposable = CompositeDisposable()

    open var _binding: ViewDataBinding? = null
    open val binding: ViewDataBinding get() = _binding!!

    open fun T.onCreateView(binding: T) {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)

        binding.lifecycleOwner = viewLifecycleOwner

        (binding as T).let { it.onCreateView(it) }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}