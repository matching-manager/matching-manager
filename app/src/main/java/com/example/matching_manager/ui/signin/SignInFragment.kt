package com.example.matching_manager.ui.signin

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.matching_manager.R
import com.example.matching_manager.databinding.SignInFragmentBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

data class User(val userId: String, val userName: String, val userEmail: String)
class SignInFragment : Fragment() {

    private var _binding: SignInFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var progressDialog: ProgressDialog

    companion object {
        private const val RC_SIGN_IN = 40
        private const val TAG = "GoogleActivity"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SignInFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() = with(binding) {
        // FirebaseAuth 및 FirebaseDatabase 인스턴스를 초기화합니다.
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // ProgressDialog를 초기화합니다.
        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Creating account")
        progressDialog.setMessage("we are creating your account")

        // GoogleSignInOptions를 설정합니다.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        // GoogleSignInClient를 초기화합니다.
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        // 구글 로그인 버튼에 클릭 signIn 함수를 호출
        btnSinginGoogle.setOnClickListener {
            signIn()
        }
    }


    private fun signIn() {
        val signIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signIntent, RC_SIGN_IN)
    }

    private fun saveUserDataToDatabase(userId: String, userName: String, userEmail: String) {
        val usersRef = database.getReference("users")

        // 유저 정보를 생성하여 Firebase Realtime Database에 저장합니다.
        val user = User(userId, userName, userEmail)
        usersRef.child(userId).setValue(user)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google 로그인에 성공했을 때
                val account = task.getResult(ApiException::class.java)!!

                firebaseAuthWithGoogle(account)
                account.idToken?.let { idToken ->

                        account.displayName?.let { name ->
                            account.email?.let { email ->
                                // 사용자 데이터를 데이터베이스에 저장
                                saveUserDataToDatabase(idToken, name, email)
                            }
                        }
                    }
                } catch (e: ApiException) {
                    //Google 로그인 실패시
                    Log.w(TAG, "Google sign in failed", e)

                }
            }
        }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // 승인 성공
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    Toast.makeText(context, "승인 실패", Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}