package com.example.damnvulnerablemobileapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.damnvulnerablemobileapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeFragment = VulnerabilitiesMenuFragment()
        val guidesFragment = GuidesMenuFragment()
        val settingsFragment = SettingsMenuFragment()

        setCurrentFragment(homeFragment)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.fragment_vulnerabilities_menu -> setCurrentFragment(homeFragment)
                R.id.fragment_guides_menu -> setCurrentFragment(guidesFragment)
                R.id.fragment_settings_menu -> setCurrentFragment(settingsFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment, fragment)
                commit()
            }

}