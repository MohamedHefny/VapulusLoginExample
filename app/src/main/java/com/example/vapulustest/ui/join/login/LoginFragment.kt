package com.example.vapulustest.ui.join.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.vapulustest.R
import com.example.vapulustest.ui.join.JoinViewModel
import com.example.vapulustest.utils.hide
import com.example.vapulustest.utils.rotate
import com.example.vapulustest.utils.show
import com.example.vapulustest.utils.stopRotate
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.layout_loading.*

class LoginFragment : Fragment() {

    private val joinViewModel: JoinViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        login_btn.setOnClickListener {
            val userName = login_username_et.text.toString()
            val password = login_password_et.text.toString()

            if (joinViewModel.isLoginDataValid(userName, password)) {
                changeUiInteraction(false)
                joinViewModel.performLogin(userName, password)
                    .observe(viewLifecycleOwner, Observer {
                        when (it) {
                            is LoginViewState.Loading -> changeUiInteraction(false)
                            is LoginViewState.LoginSuccess -> {
                                changeUiInteraction(true)
                                findNavController().navigate(R.id.action_loginFragment_to_pinCodeFragment)
                            }
                            is LoginViewState.LoginError -> {
                                changeUiInteraction(true)
                                if (it.message == "Unknown")
                                    Toast.makeText(context, R.string.login_error, Toast.LENGTH_LONG)
                                        .show()
                                else
                                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                            }
                        }
                    })
            } else
                Toast.makeText(context, R.string.enter_valid_data, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Enable or disable user interaction with ui.
     * @param isActive indicates to the status of the ui that should be.
     */
    private fun changeUiInteraction(isActive: Boolean) {
        login_btn.isEnabled = isActive
        login_username_et.isEnabled = isActive
        login_password_et.isEnabled = isActive
        if (isActive) {
            loading_icon.stopRotate()
            login_loading_layout.hide()
        } else {
            loading_icon.rotate()
            login_loading_layout.show()
        }
    }

}