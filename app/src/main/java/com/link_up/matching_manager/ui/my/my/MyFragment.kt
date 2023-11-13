package com.link_up.matching_manager.ui.my.my

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import coil.load
import com.link_up.matching_manager.R
import com.link_up.matching_manager.databinding.MyFragmentBinding
import com.link_up.matching_manager.ui.my.bookmark.MyBookmarkActivity
import com.link_up.matching_manager.ui.my.match.MyMatchActivity
import com.link_up.matching_manager.ui.my.team.MyTeamActivity
import com.link_up.matching_manager.ui.signin.SignInActivity
import com.link_up.matching_manager.util.UserInformation
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth

class MyFragment : Fragment() {
    private var _binding: MyFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var mGoogleSignInClient: GoogleSignInClient


    companion object {
        fun newInstance() = MyFragment()
        const val OBJECT_DATA = "item_object"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        _binding = MyFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
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
            val intent = Intent(requireContext(), MyBookmarkActivity::class.java)
            startActivity(intent)
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
