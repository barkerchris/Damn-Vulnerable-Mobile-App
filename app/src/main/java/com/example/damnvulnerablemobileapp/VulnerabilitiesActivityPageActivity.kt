package com.example.damnvulnerablemobileapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import com.example.damnvulnerablemobileapp.databinding.ActivityVulnerabilitiesActivityPageBinding

class VulnerabilitiesActivityPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVulnerabilitiesActivityPageBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme((R.style.Theme_DamnVulnerableMobileApp))
        binding = ActivityVulnerabilitiesActivityPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}