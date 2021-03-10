package com.example.damnvulnerablemobileapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.damnvulnerablemobileapp.databinding.FragmentVulnerabilitiesStorageBinding
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
            storeUser()
        }
    }

    private fun storeUser() {
        val email = binding.edtUsername.editText!!.text.toString()
        val password = binding.edtPassword.editText!!.text.toString()

        sharedPreferences(email, password)
        internalStorage(email, password)
        externalStorage(email, password)
        database(email, password)

        Toast.makeText(requireContext(), R.string.tst_storage_success, Toast.LENGTH_LONG).show()
    }

    private fun sharedPreferences(email: String, password: String) {
        val sharedPrefs: SharedPreferences = requireActivity().getSharedPreferences("userProfileSP", Context.MODE_PRIVATE)
        val sharedPrefsEdit: SharedPreferences.Editor = sharedPrefs.edit()
        sharedPrefsEdit.putString("email", email)
        sharedPrefsEdit.putString("password", password)
        sharedPrefsEdit.apply()
    }

    private fun internalStorage(email: String, password: String) {
        val myFile = File(requireContext().filesDir, "userProfileIS.txt")
        fileWriter(myFile, email, password)
    }

    private fun externalStorage(email: String, password: String) {
        if(Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val myFile = File(requireContext().getExternalFilesDir(null), "userProfileES.txt")
            fileWriter(myFile, email, password)
        }
    }

    private fun fileWriter(myFile: File, email: String, password: String) {
        try {
            val fileOutputStream = FileOutputStream(myFile)
            fileOutputStream.write("username: $email\n".toByteArray())
            fileOutputStream.write("password: $password\n".toByteArray())
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun database(email: String, password: String) {
        val db = DatabaseHelper(requireContext())
        db.addUser(email, password)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}