package com.mistershorr.loginandregistration

// object keyword makes it so all the functions are
// static functions
object RegistrationUtil {
    // use this in the test class for the is username taken test
    // make another similar list for some taken emails
    var existingUsers = listOf("cosmicF", "cosmicY", "bob", "alice")
//    you can use listOf<type>() instead of making the list & adding individually
//    List<String> blah = new ArrayList<String>();
//    blah.add("hi")
//    blah.add("hello")
//

    // isn't empty
    // already taken
    // minimum number of characters is 3
    var result = false
    fun validateUsername(username: String): Boolean {
        if (username.isEmpty())
            result
        if (this.toString().length >= 3) {
            result = true
        }
        return result
    }

    // make sure meets security requirements (deprecated ones that are still used everywhere)
    // min length 8 chars
    // at least one digit
    // at least one capital letter
    // both passwords match
    // not empty
    var answer = false
    fun validatePassword(password: String, confirmPassword: String): Boolean {
        if (confirmPassword.equals(confirmPassword) && password.length >= 8) {
            if (password.lowercase() != password) {
                for (x in password) {
                    if (x.isDigit()) {
                        answer = true
                    }
                }
            }
        }
        return answer
    }


    // isn't empty
    fun validateName(name: String): Boolean {
        return true
    }

    // isn't empty
    // make sure the email isn't used
    // make sure it's in the proper email format user@domain.tld
    fun validateEmail(email: String): Boolean {
        return email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email)
            .matches()
    }


}

