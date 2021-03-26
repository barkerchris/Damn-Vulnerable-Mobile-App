package com.example.damnvulnerablemobileapp

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.damnvulnerablemobileapp.databinding.FragmentSecureStorageBinding
import com.google.android.material.snackbar.Snackbar
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

class SecureStorageFragment : Fragment() {

    private var _binding: FragmentSecureStorageBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainKey: MasterKey

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecureStorageBinding.inflate(inflater, container, false)

        mainKey = MasterKey.Builder(requireContext())
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnVulnerabilitiesStorageLogIn.setOnClickListener {
            storeUser(view)
        }
        binding.btnVulnerabilitiesStorageRetrieve.setOnClickListener {
            retrieveUser()
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

            //encryptedPreferences(username, password)
            encryptedFile(username, password)

            Snackbar.make(view, R.string.snk_saved_credentials, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun retrieveUser() {
        //decryptPreferences()
        decryptFile()
    }

    private fun encryptedPreferences(username: String, password: String) {
        val sharedPrefsFile = "encryptedUserProfileSP"
        val sharedPrefs: SharedPreferences = EncryptedSharedPreferences.create(
                requireContext(),
                sharedPrefsFile,
                mainKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        with (sharedPrefs.edit()) {
            putString("username", username)
            putString("password", password)
            apply()
        }
    }

    private fun encryptedFile(username: String, password: String) {
        val myFile = File(requireContext().filesDir, "encryptedUserProfileFile.txt")
        if (myFile.exists()) {
            myFile.delete()
        }
        val myEncryptedFile = EncryptedFile.Builder(
                requireContext(),
                myFile,
                mainKey,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()

        val fileContent = "$username,$password".toByteArray(StandardCharsets.UTF_8)
        myEncryptedFile.openFileOutput().apply {
            write(fileContent)
            flush()
            close()
        }
    }

    private fun decryptPreferences() {
        val sharedPrefsFile = "encryptedUserProfileSP"
        val sharedPrefs: SharedPreferences = EncryptedSharedPreferences.create(
                requireContext(),
                sharedPrefsFile,
                mainKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        binding.txtSecureStorageUsername.text = sharedPrefs.getString("username", "")
        binding.txtSecureStoragePassword.text = sharedPrefs.getString("password", "")
    }

    private fun decryptFile() {
        val myFile = File(requireContext().filesDir, "encryptedUserProfileFile.txt")
        val myEncryptedFile = EncryptedFile.Builder(
                requireContext(),
                myFile,
                mainKey,
                EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()

        myEncryptedFile.openFileInput().apply {
            val br = BufferedReader(InputStreamReader(this))
            val line = br.readLine().toString().split(",")
            binding.txtSecureStorageUsername.text = line[0]
            binding.txtSecureStoragePassword.text = line[1]
            br.close()
            close()
        }
    }

}