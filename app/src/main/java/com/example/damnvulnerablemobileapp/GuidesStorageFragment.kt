package com.example.damnvulnerablemobileapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.damnvulnerablemobileapp.databinding.FragmentGuidesStorageBinding

class GuidesStorageFragment : Fragment() {

    private var _binding: FragmentGuidesStorageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGuidesStorageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGuidesStorageSecure.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_guidesStorageFragment_to_secureStorageFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}