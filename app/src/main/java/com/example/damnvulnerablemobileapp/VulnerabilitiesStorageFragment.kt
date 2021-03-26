package com.example.damnvulnerablemobileapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.damnvulnerablemobileapp.databinding.FragmentVulnerabilitiesStorageBinding
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class VulnerabilitiesStorageFragment : Fragment() {

    private var _binding: FragmentVulnerabilitiesStorageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVulnerabilitiesStorageBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnVulnerabilitiesStorageLogIn.setOnClickListener {
            storeUser(view)
        }
    }

    private fun storeUser(view: View) {
        if (binding.edtUsername.editText!!.text.isEmpty() ||
                binding.edtPassword.editText!!.text.isEmpty()) {
            binding.edtUsername.error = getString(R.string.txt_empty)
            binding.edtPassword.error = getString(R.string.txt_empty)
        } else {
            binding.edtUsername.error = null
            binding.edtPassword.error = null
            val username = binding.edtUsername.editText!!.text.toString()
            val password = binding.edtPassword.editText!!.text.toString()

            sharedPreferences(username, password)
            internalStorage(username, password)
            externalStorage(username, password)
            database(username, password)

            Snackbar.make(view, R.string.snk_saved_credentials, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun sharedPreferences(username: String, password: String) {
        val sharedPrefs: SharedPreferences = requireActivity().getSharedPreferences("userProfileSP", Context.MODE_PRIVATE)
        val sharedPrefsEdit: SharedPreferences.Editor = sharedPrefs.edit()
        sharedPrefsEdit.putString("username", username)
        sharedPrefsEdit.putString("password", password)
        sharedPrefsEdit.apply()
    }

    private fun internalStorage(username: String, password: String) {
        val myFile = File(requireContext().filesDir, "userProfileIS.txt")
        fileWriter(myFile, username, password)
    }

    private fun externalStorage(username: String, password: String) {
        if(Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val myFile = File(requireContext().getExternalFilesDir(null), "userProfileES.txt")
            fileWriter(myFile, username, password)
        }
    }

    private fun fileWriter(myFile: File, username: String, password: String) {
        try {
            val fileOutputStream = FileOutputStream(myFile)
            fileOutputStream.write("username: $username\n".toByteArray())
            fileOutputStream.write("password: $password\n".toByteArray())
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun database(username: String, password: String) {
        val db = DatabaseHelper(requireContext())
        db.addUser(username, password)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}