package com.link_up.matching_manager.ui.signin

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.link_up.matching_manager.R
import com.link_up.matching_manager.databinding.SignInFragmentBinding
import com.link_up.matching_manager.ui.fcm.FcmActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

class SignInFragment : Fragment() {

    private var _binding: SignInFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var progressDialog: ProgressDialog

    private val viewModel: SignInSharedViewModel by activityViewModels {
        SignInViewModelFactory(
            requireContext()
        )
    }

    // ActivityResultLauncher 선언
    private lateinit var startGoogleLoginForResult: ActivityResultLauncher<Intent>

    companion object {
        private const val TAG = "GoogleActivity"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = SignInFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // Firebase Auth 인스턴스 초기화
        auth = Firebase.auth
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startBlinkingAnimation()
        initView()
        initViewModel()
    }

    private fun initViewModel() = with(viewModel) {
        checkUserType()

        signInUserType.observe(viewLifecycleOwner, Observer { SignInType ->
            if (SignInType) {
                startSignInGoogle()
            } else {
                saveUserType()
            }
        })

        userType.observe(viewLifecycleOwner, Observer { type ->
            when (type) {
                CheckUserType.NEW_USER.name -> {
                    startActivity(SignUpActivity.newIntent(requireContext()))
                }

                CheckUserType.EXISTING_USER.name -> {
                    startActivity(FcmActivity.newIntent(requireContext()))
                }
            }
        })
    }

    private fun initView() {
        // FCM 토큰 가져오기
        var fcmToken: String? = null

        Firebase.messaging.token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(FcmActivity.TAG, "FCM 등록 토큰 가져오기 실패", task.exception)
                    return@OnCompleteListener
                }

                // 새 FCM 등록 토큰 가져오기
                fcmToken = task.result

                // 로깅 및 토스트
                val msg = getString(R.string.msg_token_fmt, fcmToken)
                Log.d(TAG, msg)
//                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                Log.w(TAG, msg, task.exception)
            },
        )


        // FirebaseAuth 및 FirebaseDatabase 인스턴스를 초기화합니다.
        FirebaseAuth.getInstance()
        FirebaseDatabase.getInstance()

        // ProgressDialog를 초기화합니다.
        progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("계정 생성 중")
        progressDialog.setMessage("계정을 생성 중입니다")

        // GoogleSignInOptions를 설정합니다.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)) // 이거를 꼭 넣어야됨!!
            .requestEmail().build()

        // GoogleSignInClient를 초기화합니다.
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        // 구글 로그인 버튼 클릭 시 Google 로그인 과정을 시작합니다.
        binding.btnSingInGoogle.setOnClickListener {
            startSignInGoogle()
        }

        // ActivityResultLauncher 초기화
        startGoogleLoginForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    result.data?.let { data ->

                        val task = GoogleSignIn.getSignedInAccountFromIntent(data)

                        try {
                            // 구글 승인 성공, firebase 인증
                            val account = task.getResult(ApiException::class.java)!!
                            Log.d(TAG, "firebaseAuthWithGoogle id:" + account.id)
                            Log.d(TAG, "firebaseAuthWithGoogle id token:" + account.idToken)
                            Log.d(TAG, "firebaseAuthWithGoogle email:" + account.email)
                            Log.d(TAG, "firebaseAuthWithGoogle photoUrl:" + account.photoUrl)
                            Log.d(TAG, "firebaseAuthWithGoogle account:" + account.displayName)

                            firebaseAuthWithGoogle(account.idToken!!)
//                            Toast.makeText(context, "승인성공", Toast.LENGTH_SHORT).show()

                            //firebase realtime database에 user정보 등록
                            viewModel.checkUserinfo(
                                com.link_up.matching_manager.data.model.UserInfoModel(
                                    uid = account.id,
                                    uidToken = account.idToken,
                                    email = account.email,
                                    fcmToken = fcmToken.toString(),
                                    photoUrl = account.photoUrl.toString(),
                                    username = null,
                                    phoneNumber = null,
                                    realName = account.displayName
                                )
                            )
                        } catch (e: ApiException) {
                            // 구글 승인 실패, 업데이트 UI 적절하게
                            Log.w(TAG, "Google sign in failed", e)
                            Toast.makeText(context, "승인실패", Toast.LENGTH_SHORT).show()
                        }
                    }
                    // Google 로그인 성공
                } else {
                    Log.e(TAG, "Google Result Error ${result}")
                }
            }
    }

    private fun getFcmToken(function: () -> Unit) {

    }

    private fun startSignInGoogle() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startGoogleLoginForResult.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        // Google 로그인을 통해 받은 idToken을 사용하여 Firebase에 인증을 시도합니다.
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        // Firebase에 구글 계정 정보를 사용하여 로그인 시도를 합니다.
        auth.signInWithCredential(credential).addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                // 로그인이 성공한 경우, 사용자의 정보와 함께 UI를 업데이트합니다
                Log.d(TAG, "signInWithCredential:success")
                val user = auth.currentUser
                updateUI(user)
            } else {
                // 로그인이 실패한 경우, 사용자에게 메시지를 표시합니다.
                Log.w(TAG, "signInWithCredential:failure", task.exception)
                updateUI(null)
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // 로그인 성공 시 UI 업데이트
            progressDialog.dismiss()
            Log.d(TAG, "로그인 성공: ${user.displayName}")
            // 필요한 UI 업데이트 작업을 수행하세요.
        } else {
            // 로그인 실패 시 UI 업데이트
            progressDialog.dismiss()
            Log.d(TAG, "로그인 실패")
            // 실패한 경우 사용자에게 알림을 표시하는 등의 작업을 수행하세요.
        }
    }

    private fun startBlinkingAnimation() = with(binding) {
        val startAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.blink_animation)
        btnSingInGoogle.startAnimation(startAnimation)
        ivGoogle.startAnimation(startAnimation)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
