package com.example.damnvulnerablemobileapp

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.damnvulnerablemobileapp.databinding.FragmentVulnerabilitiesAuthenticationBinding
import com.google.android.material.snackbar.Snackbar


class VulnerabilitiesAuthenticationFragment : Fragment() {

    private var _binding: FragmentVulnerabilitiesAuthenticationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVulnerabilitiesAuthenticationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnVulnerabilitiesAuthenticationLogIn.setOnClickListener {
            if (binding.edtPin.editText!!.text.isEmpty()) {
                binding.edtPin.error = getString(R.string.txt_empty)
            } else if (binding.edtPin.editText!!.text.toString() == "1234") {
                binding.edtPin.error = null

                //Closes keyboard
                val imm = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)

                binding.edtPin.editText!!.text.clear()
                Snackbar.make(view, R.string.snk_correct_pin, Snackbar.LENGTH_SHORT).show()
            } else {
                binding.edtPin.error = getString(R.string.txt_wrong_pin)
                binding.edtPin.editText!!.text.clear()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}