package com.example.damnvulnerablemobileapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class BroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals("com.example.damnvulnerablemobileapp.SEND_BROADCAST")) {
            val username = intent?.getStringExtra("com.example.damnvulnerablemobileapp.USERNAME")
            val password = intent?.getStringExtra("com.example.damnvulnerablemobileapp.PASSWORD")
            Toast.makeText(context, "Username: $username\nPassword: $password", Toast.LENGTH_LONG).show()
        }
    }
}