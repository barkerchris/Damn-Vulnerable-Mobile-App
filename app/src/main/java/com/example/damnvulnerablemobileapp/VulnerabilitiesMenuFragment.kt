package com.example.damnvulnerablemobileapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.damnvulnerablemobileapp.databinding.FragmentVulnerabilitiesMenuBinding

class VulnerabilitiesMenuFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentVulnerabilitiesMenuBinding? = null
    private val binding get() = _binding!!
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVulnerabilitiesMenuBinding.inflate(inflater, container, false)

        //navController = Navigation.findNavController(this.requireView())

        return binding.root
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            //
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}