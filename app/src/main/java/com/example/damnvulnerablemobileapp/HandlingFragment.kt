package com.example.damnvulnerablemobileapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class HandlingFragment : Fragment() {
    private var  mAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_handling, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(mAuth.currentUser != null) {
            mAuth.signOut()
        }
        view.findViewById<MaterialButton>(R.id.btn_login).setOnClickListener {
            login(view)
        }
    }

    private fun login(view: View) {
        val emailText = view.findViewById<TextInputLayout>(R.id.edt_email).editText!!.text.toString()
        val passwordText = view.findViewById<TextInputLayout>(R.id.edt_password).editText!!.text.toString()
        try {
            mAuth.signInWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(requireActivity(), OnCompleteListener { task ->
                    if(task.isSuccessful) {
                        Log.d("username", emailText)
                        Log.d("password", passwordText)
                        Snackbar.make(view, resources.getString(R.string.login_success), Snackbar.LENGTH_SHORT).show()
                    } else {
                        Snackbar.make(view, resources.getString(R.string.login_error), Snackbar.LENGTH_SHORT).show()
                    }

                })
        } catch (exc: Throwable) {
            Snackbar.make(view, resources.getString(R.string.login_empty), Snackbar.LENGTH_LONG).show()
        }
    }

}