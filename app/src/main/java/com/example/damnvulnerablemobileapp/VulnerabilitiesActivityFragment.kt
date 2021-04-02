package com.example.damnvulnerablemobileapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.damnvulnerablemobileapp.databinding.FragmentVulnerabilitiesActivityBinding

class VulnerabilitiesActivityFragment : Fragment() {

    private var _binding: FragmentVulnerabilitiesActivityBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVulnerabilitiesActivityBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnVulnerabilitiesActivityLogIn.setOnClickListener {
            if (binding.edtUsername.editText!!.text.isEmpty() ||
                    binding.edtPassword.editText!!.text.isEmpty()) {
                binding.edtUsername.error = getString(R.string.txt_empty)
                binding.edtPassword.error = getString(R.string.txt_empty)
            } else {
                binding.edtUsername.error = null
                binding.edtPassword.error = null

                openActivity()
            }
        }
    }

    private fun openActivity() {
        val intent = Intent("com.example.damnvulnerablemobileapp.OPEN_INFO")
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}