package com.example.damnvulnerablemobileapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.damnvulnerablemobileapp.databinding.FragmentVulnerabilitiesLoggingBinding
import com.google.android.material.snackbar.Snackbar
import java.io.*

class VulnerabilitiesLoggingFragment : Fragment() {

    private var _binding: FragmentVulnerabilitiesLoggingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVulnerabilitiesLoggingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnVulnerabilitiesLoggingLogIn.setOnClickListener {
            if (binding.edtUsername.editText!!.text.isEmpty() ||
                    binding.edtPassword.editText!!.text.isEmpty()) {
                binding.edtUsername.error = getString(R.string.txt_empty)
                binding.edtPassword.error = getString(R.string.txt_empty)
            } else {
                binding.edtUsername.error = null
                binding.edtPassword.error = null
                //clear logcat before logging
                Runtime.getRuntime().exec("logcat -c")
                //log user input
                Log.d("Username: ", binding.edtUsername.editText!!.text.toString())
                Log.d("Password: ", binding.edtPassword.editText!!.text.toString())
                dumpLogs()

                Snackbar.make(view, R.string.snk_saved_credentials, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun dumpLogs() {
        val fileName = "logs.txt"
        val logFile = File(requireContext().filesDir, fileName)
        try {
            val logcat = Runtime.getRuntime().exec("logcat -d")
            val reader = BufferedReader(InputStreamReader(logcat.inputStream))
            val writer = BufferedWriter(FileWriter(logFile))
            while (true) {
                //If null then break
                val line = reader.readLine() ?: break
                writer.append(line).append('\n')
            }
            writer.flush()
            writer.close()
            reader.close()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}