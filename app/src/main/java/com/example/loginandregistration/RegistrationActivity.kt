package com.example.loginandregistration

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.example.loginandregistration.databinding.ActivityRegistrationBinding
import com.mistershorr.loginandregistration.RegistrationUtil


class RegistrationActivity: AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    companion object{
        val TAG = "RegistrationActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(LoginActivity.EXTRA_USERNAME)?:""
        val password = intent.getStringExtra(LoginActivity.EXTRA_PASSWORD) ?:""

        binding.editTextRegistrationUsername?.setText(username)
        binding.editTextRegistrationPassword.setText(password)


        //register an account and send back the username and pass
        //to the login activity to prefill those fields
        binding.buttonRegistrationRegister.setOnClickListener {
            val password = binding.editTextRegistrationPassword?.text.toString()
            val confirm = binding.editTextRegistrationConfirmPassword?.text.toString()
            val username = binding.editTextRegistrationUsername.text.toString()
            val name = binding.editTextRegistrationName.text.toString()
            val email = binding.editTextRegistrationEmail.text.toString()

//            if(RegistrationUtil.validatePassword(password,confirm) &&
//                RegistrationUtil.validateUsername(username)) {

                registerUserOnBackendless(username, password, name, email)
                }
//        }


    }

    private fun registerUserOnBackendless(
        username: String,
        password: String,
        name: String,
        email: String
    ) {
        // do not forget to call Backendless.initApp when your app initializes

        // do not forget to call Backendless.initApp when your app initializes
        val user = BackendlessUser()
        user.setProperty("email", email)
        user.password = password
        user.setProperty("name", name)
        user.setProperty("username", username)

        Backendless.UserService.register(user, object : AsyncCallback<BackendlessUser?> {
            override fun handleResponse(registeredUser: BackendlessUser?) {
                Log.d(TAG, "handleResponse: ${user.getProperty("username")} successfully registered")
                val resultIntent = Intent().apply {
                    putExtra(
                        LoginActivity.EXTRA_USERNAME,
                        binding.editTextRegistrationUsername.text.toString()
                    )
                    putExtra(LoginActivity.EXTRA_PASSWORD, password)
                }

                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }

            override fun handleFault(fault: BackendlessFault) {
                Log.d(TAG, "handleFault: ${fault.message}")
            }
        })

    }
}

