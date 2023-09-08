package com.example.a23_hf069

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class C_loginFragment : Fragment() {
    lateinit var login : Button
    lateinit var signUp : Button
    lateinit var btnFindId : Button // 아이디 찾기 버튼

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View?
    {
        return inflater.inflate(R.layout.fragment_c_login, container, false)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        login = view.findViewById<Button>(R.id.login_btn)
        signUp = view.findViewById<Button>(R.id.signUp_btn)
        btnFindId = view.findViewById<Button>(R.id.findID_btn)

        login.setOnClickListener() {
            // login버튼을 클릭하면 CorporateHomeActivity로 전환
            val intent = Intent(getActivity(), CorporateHomeActivity::class.java)
            startActivity(intent)
        }
        signUp.setOnClickListener() {
            // signUp버튼을 클릭하면 CorporateSignUpActivity로 전환
            val intent = Intent(getActivity(), CorporateSignUpActivity::class.java)
            startActivity(intent)
        }

        btnFindId.setOnClickListener {
            // findId 버튼을 클릭하면 FindCorporateIdActivity로 전환
            val intent = Intent(getActivity(), FindCorporateIdActivity::class.java)
            startActivity(intent)
        }
    }

}


