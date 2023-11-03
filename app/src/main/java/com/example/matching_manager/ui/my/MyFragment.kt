package com.example.matching_manager.ui.my

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import coil.load
import com.example.matching_manager.R
import com.example.matching_manager.databinding.DialogEditBinding
import com.example.matching_manager.databinding.MyFragmentBinding
import com.example.matching_manager.ui.signin.SignInActivity
import com.example.matching_manager.ui.signin.UserInformation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class MyFragment : Fragment() {
    private var _binding: MyFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var editName: String? = null // editName 선언
    private var editImageUri: Uri? = null // editImageUri 선언
    private lateinit var mGoogleSignInClient: GoogleSignInClient


    private var context: Context? = null
    private lateinit var dialogBinding: DialogEditBinding
    private var selectedImageUri: Uri? = null

    companion object {
        fun newInstance() = MyFragment()
        const val OBJECT_DATA = "item_object"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = MyFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var myFileDialog: MyFileDialog
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        myFileDialog =
            MyFileDialog(editName, editImageUri) { newName, newLocation, newId, newImageUri ->
                // newName, newLocation, newId, newImageUri를 이곳에서 사용할 수 있습니다.
            }


        setFragmentResultListener("imageResult") { _, result ->
            val selectedImageUri = result.getParcelable<Uri>("selectedImageUri")
            if (selectedImageUri != null) {
                //dialogBinding.ivProfile.setImageURI(selectedImageUri) // 다이얼로그 내에서의 이미지 업데이트
                //binding.ivMypageFace.setImageURI(selectedImageUri) // ivMypageFace 이미지 업데이트
                Log.d("myFragment1", "After save click: selectedImageUri = $selectedImageUri")
            }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()

        // GoogleSignInClient를 초기화합니다.
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

    }


    private fun initView() = with(binding) {
        val userData = UserInformation.userInfo
        ivPhoto.load(userData.photoUrl)
        tvUsername.text = userData.username
        tvRealName.text = userData.realName
        tvPhoneNumber.text = userData.phoneNumber
        tvEmail.text = userData.email

        layoutMatch.setOnClickListener {
            val intent = Intent(requireContext(), MyMatchActivity::class.java)
            startActivity(intent)
        }
        layoutTeam.setOnClickListener {
            val intent = Intent(requireContext(), MyTeamActivity::class.java)
            startActivity(intent)
        }
        layoutBookmark.setOnClickListener {
            // 현준님 여기에 관심목록 하시면 됩니다!
        }

        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(context, "로그아웃", Toast.LENGTH_SHORT).show()

            resultLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        val data: Intent? = result.data
                        selectedImageUri = data?.data
                        if (selectedImageUri != null) {
                            // 이미지를 선택한 후의 로직을 수행합니다.
                            dialogBinding.ivProfile.setImageURI(selectedImageUri)

                            //ivMypageFace.setImageURI(selectedImageUri)
                            //setProfileImage(selectedImageUri)
                            Log.d(
                                "MyFragment",
                                "After save click: selectedImageUri = $selectedImageUri"
                            )
                        }
                    }
                }
        }
        binding.btnLogout.setOnClickListener {
            logOut()
        }
    }
    private var logoutDialog: AlertDialog? = null

    private fun createLogoutDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("구글 로그아웃")
        builder.setMessage("로그 아웃 하시겠습니까?")
        builder.setIcon(R.mipmap.ic_launcher)

        val listener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE -> {
                    logOut()
                    FirebaseAuth.getInstance().signOut()
                    mGoogleSignInClient.signOut()

                    val currentContext = requireContext()
                    Toast.makeText(currentContext, "로그아웃", Toast.LENGTH_SHORT).show()

                    val intent = Intent(requireContext(), SignInActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }

                DialogInterface.BUTTON_NEGATIVE -> {
                        logoutDialog?.dismiss()
                }
            }
        }
        builder.setPositiveButton("Logout", listener)
        builder.setNegativeButton("Cancel", listener)

        logoutDialog = builder.create()
    }

    private fun logOut() {
        if (logoutDialog == null) {
            createLogoutDialog()
        }

        if (!logoutDialog?.isShowing!!) {
            logoutDialog?.show()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
