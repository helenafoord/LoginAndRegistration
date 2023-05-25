package com.example.loginandregistration

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*
@Parcelize
data class Loan (
    var lendee : String = "Someone",
    var initialLoanValue: Int = 5,
    var reasonForLoan : String = "Lunch",
    var dateLent : Date = Date(1678725583616),
    var amountRepaid : Int = 0,
    var dateFullyRepaid : Date? = null,
    var isRepaid : Boolean = false,
    var ownerId: String = "",
    var objectId:String? = null,

):Parcelable{
    fun balanceRemaining() : Int {
        return initialLoanValue - amountRepaid
    }
}