package com.example.damnvulnerablemobileapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.damnvulnerablemobileapp.databinding.FragmentVulnerabilitiesMenuBinding

class VulnerabilitiesMenuFragment : Fragment() {

    private var _binding: FragmentVulnerabilitiesMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVulnerabilitiesMenuBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnVulnerabilitiesLogging.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_fragment_vulnerabilities_menu_to_vulnerabilitiesLoggingFragment)
        }
        binding.btnVulnerabilitiesStorage.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_fragment_vulnerabilities_menu_to_vulnerabilitiesStorageFragment)
        }
        binding.button.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_fragment_vulnerabilities_menu_to_secureStorageFragment)
        }
        binding.btnVulnerabilitiesAuthentication.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_fragment_vulnerabilities_menu_to_vulnerabilitiesAuthenticationFragment)
        }
        binding.btnVulnerabilitiesBroadcast.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_fragment_vulnerabilities_menu_to_vulnerabilitiesBroadcastFragment)
        }
        binding.btnVulnerabilitiesActivity.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_fragment_vulnerabilities_menu_to_vulnerabilitiesActivityFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}