package com.link_up.matching_manager.ui.signin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.replace
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.link_up.matching_manager.databinding.SignInActivityBinding

class SignInActivity : AppCompatActivity() {

    private val binding by lazy { SignInActivityBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val loginFragment = SignInFragment()
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()

        transaction.replace(binding.fragmentContainer.id, loginFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}



