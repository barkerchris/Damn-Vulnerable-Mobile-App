package com.example.damnvulnerablemobileapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.damnvulnerablemobileapp.databinding.FragmentGuidesMenuBinding

class GuidesMenuFragment : Fragment() {

    private var _binding: FragmentGuidesMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGuidesMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGuidesLogging.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_fragment_guides_menu_to_guidesLoggingFragment)
        }
        binding.btnGuidesStorage.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_fragment_guides_menu_to_guidesStorageFragment)
        }
        binding.btnGuidesAuthentication.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_fragment_guides_menu_to_guidesAuthenticationFragment)
        }
        binding.btnGuidesBroadcast.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_fragment_guides_menu_to_guidesBroadcastFragment)
        }
        binding.btnGuidesActivity.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_fragment_guides_menu_to_guidesActivityFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}