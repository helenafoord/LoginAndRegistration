package com.example.loginandregistration


import android.R.attr.password
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import com.backendless.servercode.annotation.Async
import com.example.loginandregistration.databinding.ActivityLoginBinding


class LoginActivity:AppCompatActivity() {
    private lateinit var binding :ActivityLoginBinding

    companion object{
        val EXTRA_USERNAME = "username"
        val EXTRA_PASSWORD = "password"
        val TAG = "LoginActivity"
        val EXTRA_USERID = "userID"

    }

    val startRegistrationForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            // Handle the Intent
            binding.editTextLoginUsername.setText(intent?.getStringExtra(EXTRA_USERNAME))
            binding.editTextLoginPassword.setText(intent?.getStringExtra(EXTRA_PASSWORD))
        }
    }
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Backendless.initApp(this, Constants.APP_ID, Constants.API_KEY)

       binding.textViewLoginSignup.setOnClickListener{
            val registrationIntent = Intent(this, RegistrationActivity::class.java)
            registrationIntent.putExtra(EXTRA_USERNAME, binding.editTextLoginUsername.text.toString())
            registrationIntent.putExtra(EXTRA_PASSWORD, binding.editTextLoginPassword.text.toString())
            registrationIntent.putExtra(EXTRA_USERID, binding.editTextLoginUsername.text.toString())



            startActivity(registrationIntent)
            //3a launch new activity using the intent startActivity(registrationIntent)
            //3b launch activity for result using the varaible from register for contract above
            startRegistrationForResult.launch(registrationIntent)
        }
        binding.buttonLoginLogin.setOnClickListener {
            // do not forget to call Backendless.initApp in the app  initialization code

            // do not forget to call Backendless.initApp in the app initialization code
            Backendless.UserService.login(
                binding.editTextLoginUsername.text.toString(), //could be a pos problem bc vid has editTextLoginUsername
                binding.editTextLoginPassword.text.toString(),
                object : AsyncCallback<BackendlessUser?> {
                    override fun handleResponse(response: BackendlessUser?) {
                        // user has been logged in
                        Log.d(TAG, "handleResponse: $response")
                        //val userId = user!!.objectId
                        if (response != null) {
//                            retrieveAllData(response.userId)
                            val loanListActivity =
                                Intent(this@LoginActivity, LoanListActivity::class.java)
                            loanListActivity.putExtra(EXTRA_USERID, response.userId)
                            startActivity(loanListActivity)
                        }

                    }

                    override fun handleFault(fault: BackendlessFault) {
                        // login failed, to get the error code call fault.getCode()
                        Log.d(TAG, "handleFault: ${fault.message})")
                    }
                })

        }
    }


    private fun retrieveAllData(userId: String) {
        val whereClause = "ownerId = '$userId'" // userID = objectID of user
        val queryBuilder = DataQueryBuilder.create()
        queryBuilder.whereClause = whereClause
       Backendless.Data.of(Loan::class.java).find(queryBuilder, object : AsyncCallback<List<Loan?>?> {
            override fun handleResponse(foundLoans: List<Loan?>?) {
                //all Contact instances have been found
                Log.d(TAG, "handleResponse: $foundLoans")
            }

            override fun handleFault(fault: BackendlessFault) {
                Log.d(TAG, "handleFault: ${fault.message}")
            }
        })
   }


}

