package me.taosunkist.hello.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlinx.coroutines.*
import me.taosunkist.hello.core.viewmodel.ViewModelFactory
import me.taosunkist.hello.data.net.ServerApiCore
import me.taosunkist.hello.databinding.FragmentProfileBinding
import me.taosunkist.hello.ui.BaseFragment

class ProfileFragment : BaseFragment() {

    private var _binding: FragmentProfileBinding? = null

    private val binding: FragmentProfileBinding get() = _binding!!

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        FragmentProfileBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        binding.dummyButton.setOnClickListener { dummyButtonPressed() }

    }

    private fun setupViewModel() {
        profileViewModel = ViewModelProviders.of(this, ViewModelFactory(ServerApiCore.serverApi)).get(ProfileViewModel::class.java)
    }

    private fun dummyButtonPressed() {
        profileViewModel.getUserDetails().observe(viewLifecycleOwner) {
            if (it.isOk()) {
                binding.fullscreenContent.text = it.data?.toString() ?: "N/A"
            } else {
                binding.fullscreenContent.text = it.message
            }
        }
    }
}