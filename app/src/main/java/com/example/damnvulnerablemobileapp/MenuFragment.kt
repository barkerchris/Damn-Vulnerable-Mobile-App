package com.example.damnvulnerablemobileapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.button.MaterialButton

class MenuFragment : Fragment(), View.OnClickListener {
    lateinit var navController: NavController

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<MaterialButton>(R.id.btn_data).setOnClickListener (this)
        view.findViewById<MaterialButton>(R.id.btn_platform).setOnClickListener (this)
        view.findViewById<MaterialButton>(R.id.btn_communication).setOnClickListener (this)
        view.findViewById<MaterialButton>(R.id.btn_handling).setOnClickListener (this)
        view.findViewById<MaterialButton>(R.id.btn_authentication).setOnClickListener (this)

    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.btn_data -> navController.navigate(R.id.action_menuFragment_to_dataFragment)
            R.id.btn_platform -> navController.navigate(R.id.action_menuFragment_to_platformFragment)
            R.id.btn_communication -> navController.navigate(R.id.action_menuFragment_to_communicationFragment)
            R.id.btn_handling -> navController.navigate(R.id.action_menuFragment_to_handlingFragment)
            R.id.btn_authentication -> navController.navigate(R.id.action_menuFragment_to_authenticationFragment)
        }
    }

}