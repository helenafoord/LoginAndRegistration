package com.example.loginandregistration

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.example.loginandregistration.databinding.ActivityLoanDetailBinding
import java.lang.Integer.parseInt
import java.util.*

class LoanDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoanDetailBinding
    private var loanIsEditable = false
    private var cal = Calendar.getInstance()
    lateinit var loan: Loan

    companion object {
        const val EXTRA_LOAN = "EXTRA_LOAN"
        const val EXTRA_USERID = "EXTRA_USERID"
        const val TAG = "LoanDetailActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoanDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extraLoan = intent.getParcelableExtra<Loan>(EXTRA_LOAN)
        if (extraLoan == null) {
            Log.d(TAG, "onCreate: Loan is Null")
            Log.d(TAG, "onCreate: UserID is ${intent.getStringExtra(LoginActivity.EXTRA_USERID)}")
            loan = Loan()
            toggleEditable()
            binding.buttonLoanDetailSave.setOnClickListener {
                Log.d(TAG, "onCreate: Saving Loan")
                loan.lendee = binding.editTextLoanDetailBorrower.text.toString()
                loan.reasonForLoan = ""
                loan.initialLoanValue = Integer
                    .parseInt(binding.editTextLoanDetailInitialLoan.text.toString())
                loan.dateLent = cal.time
                loan.initialLoanValue = Integer
                    .parseInt(binding.editTextLoanDetailInitialLoan.text.toString())
                loan.ownerId = intent.getStringExtra(LoginActivity.EXTRA_USERID)!!
                loan.objectId = null // Set when saving on database
                Backendless.Data.of(Loan::class.java)
                    .save(loan, object : AsyncCallback<Loan> {
                        override fun handleResponse(response: Loan?) {
                            Log.d(TAG, "handleResponse: Created new loan $loan")
                        }

                        override fun handleFault(fault: BackendlessFault?) {
                            Log.e(TAG, "handleFault: $fault")
                        }

                    })
            }
        } else {
            Log.d(TAG, "onCreate: Mutating loan")
            loan = extraLoan
            binding.checkBoxLoanDetailIsFullyRepaid.isChecked = loan.isRepaid
            binding.editTextLoanDetailInitialLoan.setText(loan.initialLoanValue.toString())
            binding.editTextLoanDetailBorrower.setText(loan.lendee)
            binding.editTextLoanDetailAmountRepaid.setText(loan.amountRepaid.toString())
            binding.textViewLoanDetailAmountStillOwed.text =
                String.format("Still Owed %.2f", loan.balanceRemaining() / 100.0)
            binding.buttonLoanDetailSave.setOnClickListener {
                Log.d(
                    TAG,
                    "onCreate: isChecked ${binding.checkBoxLoanDetailIsFullyRepaid.isChecked}"
                )
                loan.lendee = binding.editTextLoanDetailBorrower.text.toString()
                loan.reasonForLoan = ""
                loan.initialLoanValue = Integer
                    .parseInt(binding.editTextLoanDetailInitialLoan.text.toString())
                loan.dateLent = cal.time
                loan.amountRepaid = Integer
                    .parseInt(binding.editTextLoanDetailAmountRepaid.text.toString())
                loan.isRepaid = binding.checkBoxLoanDetailIsFullyRepaid.isChecked
                Backendless.Persistence.of(Loan::class.java)
                    .save(loan, object : AsyncCallback<Loan> {
                        override fun handleResponse(response: Loan?) {
                            Log.d(TAG, "handleResponse: Edited successfully $response")
                        }

                        override fun handleFault(fault: BackendlessFault?) {
                            Log.e(TAG, "handleFault: $fault")
                        }
                    })
            }
        }
        loan = intent.getParcelableExtra(EXTRA_LOAN) ?: Loan()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_loan_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.menu_item_loan_detail_edit -> {
                toggleEditable()
                true
            }
            R.id.menu_item_loan_detail_delete -> {
                deleteFromBackendless()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteFromBackendless() {
        Backendless.Data.of(Loan::class.java).remove( loan,
            object : AsyncCallback<Long?> {
                override fun handleResponse(response: Long?) {
                    // Person has been deleted. The response is the
                    // time in milliseconds when the object was deleted
                    Toast.makeText(this@LoanDetailActivity, "${loan.lendee} Deleted", Toast.LENGTH_SHORT).show()
                    finish()
                }

                override fun handleFault(fault: BackendlessFault) {
                    Log.d(TAG, "handleFault: ${fault.message}")
                }
            })
    }


    private fun toggleEditable() {
        if (loanIsEditable) {
            loanIsEditable = false
            binding.buttonLoanDetailSave.isEnabled = false
            //binding.buttonLoanDetailSave.visibility = View.GONE
            binding.checkBoxLoanDetailIsFullyRepaid.isEnabled = false
            binding.editTextLoanDetailBorrower.inputType = InputType.TYPE_NULL
            binding.editTextLoanDetailBorrower.isEnabled = false
            binding.editTextLoanDetailAmountRepaid.inputType = InputType.TYPE_NULL
            binding.editTextLoanDetailAmountRepaid.isEnabled = false
            binding.editTextLoanDetailInitialLoan.inputType = InputType.TYPE_NULL
            binding.editTextLoanDetailInitialLoan.isEnabled = false
            binding.checkBoxLoanDetailIsFullyRepaid.isClickable = false
        } else {
            loanIsEditable = true
            binding.buttonLoanDetailSave.isEnabled = true
            //binding.buttonLoanDetailSave.visibility = View.VISIBLE
            binding.checkBoxLoanDetailIsFullyRepaid.isEnabled = true
            binding.editTextLoanDetailBorrower.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
            binding.editTextLoanDetailBorrower.isEnabled = true
            binding.editTextLoanDetailAmountRepaid.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
            binding.editTextLoanDetailAmountRepaid.isEnabled = true
            binding.editTextLoanDetailInitialLoan.inputType = InputType.TYPE_NUMBER_VARIATION_NORMAL
            binding.editTextLoanDetailInitialLoan.isEnabled = true
            binding.checkBoxLoanDetailIsFullyRepaid.isClickable = true
        }
    }


}