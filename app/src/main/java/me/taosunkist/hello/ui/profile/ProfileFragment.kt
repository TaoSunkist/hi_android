package me.taosunkist.hello.ui.profile

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.*
import me.taosunkist.hello.R
import me.taosunkist.hello.data.net.module.UserService
import me.taosunkist.hello.databinding.FragmentProfileBinding
import me.taosunkist.hello.ui.BaseFragment

class ProfileFragment : BaseFragment() {

    private var _binding: FragmentProfileBinding? = null

    private val binding: FragmentProfileBinding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        FragmentProfileBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profileViewModel = ProfileViewModel()

        profileViewModel.getUserDetails().observe(viewLifecycleOwner, {
            binding.fullscreenContent.text = it.data?.toString() ?: "N/A"
        })

    }
}