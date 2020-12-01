package com.example.damnvulnerablemobileapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_data.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DataFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<MaterialButton>(R.id.btn_login).setOnClickListener {
            storeUser(view)
        }
    }

    private fun storeUser(view: View) {
        val email = view.findViewById<TextInputLayout>(R.id.edt_email).editText!!.text
        val password = view.findViewById<TextInputLayout>(R.id.edt_password).editText!!.text
        //SharedPreferences
        //WORLD_READABLE may still be used in older programs, and PRIVATE can still be read through an ADB
        val sharedPrefs: SharedPreferences = requireActivity().getSharedPreferences("userProfile", Context.MODE_PRIVATE)
        val sharedPrefsEdit: SharedPreferences.Editor = sharedPrefs.edit()
        sharedPrefsEdit.putString("username", email.toString())
        sharedPrefsEdit.putString("password", password.toString())
        sharedPrefsEdit.apply()

        //External storage
        if(Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val myFile = File(requireContext().getExternalFilesDir(null), "userProfile.txt")
            try {
                val fileOutputStream = FileOutputStream(myFile)
                fileOutputStream.write(email.toString().toByteArray())
                fileOutputStream.write(password.toString().toByteArray())
                fileOutputStream.close()
                Snackbar.make(view, "Success!", Snackbar.LENGTH_SHORT).show()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            Snackbar.make(view, "No external storage detected", Snackbar.LENGTH_LONG).show()
        }
    }

}