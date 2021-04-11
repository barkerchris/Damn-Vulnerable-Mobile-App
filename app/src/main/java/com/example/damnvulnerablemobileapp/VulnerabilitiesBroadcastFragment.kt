package com.example.damnvulnerablemobileapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.damnvulnerablemobileapp.databinding.FragmentVulnerabilitiesBroadcastBinding


class VulnerabilitiesBroadcastFragment : Fragment() {

    private var _binding: FragmentVulnerabilitiesBroadcastBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVulnerabilitiesBroadcastBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnVulnerabilitiesBroadcastLogIn.setOnClickListener {
            sendDetails()
        }
    }

    private fun sendDetails() {
        if (binding.edtUsername.editText!!.text.isEmpty() ||
            binding.edtPassword.editText!!.text.isEmpty()) {
            binding.edtUsername.error = getString(R.string.txt_empty)
            binding.edtPassword.error = getString(R.string.txt_empty)
        } else {
            binding.edtUsername.error = null
            binding.edtPassword.error = null
            val username = binding.edtUsername.editText!!.text.toString()
            val password = binding.edtPassword.editText!!.text.toString()

            val intent = Intent("com.example.damnvulnerablemobileapp.SEND_BROADCAST")
            intent.putExtra("com.example.damnvulnerablemobileapp.USERNAME", username)
            intent.putExtra("com.example.damnvulnerablemobileapp.PASSWORD", password)
            activity?.sendBroadcast(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}