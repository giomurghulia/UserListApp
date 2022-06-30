package com.example.userlistapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import androidx.core.view.WindowCompat
import com.example.userlistapp.databinding.ActivityMainBinding
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val userMap = mutableMapOf<String, User>()

    private var deleteUserCtn = 0
        set(value) {
            field = value
            updateDeleteUserCtn()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        updateActiveUserCtn()
        updateDeleteUserCtn()

        binding.addButton.setOnClickListener {
            submitFormToAddUser()
        }

        binding.deleteButton.setOnClickListener {
            submitFormToDeleteUser()
        }
        binding.updateButton.setOnClickListener {
            submitFormToUpdateUser()
        }
    }

    private fun submitFormToAddUser() = with(binding) {
        val firstName = binding.firstNameEditText.text?.toString()
        val lastName = binding.lastNameEditText.text?.toString()
        val email = binding.emailEditText.text?.toString()
        val ageString = binding.ageEditText.text?.toString()

        var isFormValid = true

        var age = 0
        if (!(ageString != null && ageString.isNotBlank())) {
            ageEditText.error = ("Incorrect Age")
        } else {
            try {
                age = ageString.toInt()
            } catch (e: Exception) {
                ageEditText.error = ("Incorrect Age")
            }
        }


        if (!(firstName != null && firstName.isNotBlank())) {
            firstNameEditText.error = ("Incorrect First Name")
            isFormValid = false
        }

        if (!(lastName != null && lastName.isNotBlank())) {
            lastNameEditText.error = ("Incorrect Last Name")
            isFormValid = false
        }

        if (age == 0) {
            ageEditText.error = ("Incorrect Age")
            isFormValid = false
        }

        if (!isValidEmail(email)) {
            emailEditText.error = ("Incorrect Email")
            isFormValid = false
        }

        if (isFormValid) {
            val user = User(firstName!!, lastName!!, age, email!!)
            val result = addUser(user)
            updateStatus(result, UserAction.ADD)
        }

        updateActiveUserCtn()

    }

    private fun submitFormToUpdateUser() = with(binding) {
        val firstName = binding.firstNameEditText.text?.toString()
        val lastName = binding.lastNameEditText.text?.toString()
        val email = binding.emailEditText.text?.toString()
        val age = binding.ageEditText.text?.toString()?.toIntOrNull()

        if (email != null) {
            val user = User(firstName, lastName, age, email)
            val result = updateUser(user)
            updateStatus(result, UserAction.UPDATE)

        } else {
            emailEditText.error = ("Incorrect Email")
        }

    }

    private fun submitFormToDeleteUser() = with(binding) {
        val isSuccessful = deleteUser(emailEditText.text?.toString())
        updateStatus(isSuccessful, UserAction.DELETE)
        if (isSuccessful) {
            deleteUserCtn++
            updateActiveUserCtn()
        }
    }

    private fun isValidEmail(email: String?): Boolean {
        return if (email.isNullOrBlank()) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }

    private fun isUserExists(email: String): Boolean {
        return userMap[email] != null
    }

    private fun addUser(user: User): Boolean {
        if (!isUserExists(user.email)) {
            userMap[user.email] = user
            return true
        }
        return false
    }

    private fun deleteUser(email: String?): Boolean {
        if (email == null) return false
        return userMap.remove(email) != null
    }

    private fun updateUser(user: User): Boolean {
        if (!isUserExists(user.email)) return false

        userMap[user.email] = user
        return true
    }

    private fun updateStatus(isSuccessful: Boolean, action: UserAction) {
        when (action) {
            UserAction.ADD -> {
                if (isSuccessful) {
                    binding.statusText.text = "User added successfully"
                } else {
                    binding.statusText.text = "User already exists"
                }
            }
            UserAction.DELETE -> {
                if (isSuccessful) {
                    binding.statusText.text = "User deleted successfully"
                } else {
                    binding.statusText.text = "User does not exits"
                }
            }
            UserAction.UPDATE -> {
                if (isSuccessful) {
                    binding.statusText.text = "User successfully update"
                } else {
                    binding.statusText.text = "User does not exits"
                }
            }
        }


    }

    private fun updateActiveUserCtn() = with(binding) {
        userQtyText.text = "User Qty Is : ${userMap.size}"
    }

    private fun updateDeleteUserCtn() {
        binding.deleteUserQtyText.text = "Delete User Count Is : $deleteUserCtn"
    }

}

enum class UserAction {
    ADD,
    DELETE,
    UPDATE;

}



