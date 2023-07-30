package com.example.a23_hf069

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView

class MemberInfoEditActivity : AppCompatActivity() {
    private var IP_ADDRESS = "3.34.126.115" // Replace with your IP address.
    private var userId: String = "" // User ID

    private lateinit var edtID: TextView
    private lateinit var edtName: TextView
    private lateinit var edtBirth: EditText
    private lateinit var edtEmail: EditText
    private lateinit var btnVerify: Button
    private lateinit var edtVerification: EditText
    private lateinit var btnOk: Button
    private lateinit var edtPhone: EditText
    private lateinit var edtAddress: EditText
    private lateinit var edtAddressDetail: EditText
    private lateinit var btnSubmit: Button

    private lateinit var backButton_edit: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_member_info_edit)

        edtID = findViewById(R.id.edtEdit_memberinfo_id)
        edtName = findViewById(R.id.edtEdit_memberinfo_name)
        edtBirth = findViewById(R.id.edtEdit_memberinfo_birth)
        edtEmail = findViewById(R.id.edtEdit_memberinfo_email)
        btnVerify = findViewById(R.id.btnEdit_memberinfo_verify)
        edtVerification = findViewById(R.id.edtEdit_memberinfo_verification)
        btnOk = findViewById(R.id.btnEdit_memberinfo_ok)
        edtPhone = findViewById(R.id.edtEdit_memberinfo_phone)
        edtAddress = findViewById(R.id.edtEdit_memberinfo_address_serach)
        edtAddressDetail = findViewById(R.id.edtEdit_memberinfo_address_detail)
        btnSubmit = findViewById(R.id.buttonSubmit_complete_edit)

        backButton_edit = findViewById(R.id.backButton)

        backButton_edit.setOnClickListener {
            onBackPressed()
        }

        userId = intent.getStringExtra("userId") ?: ""
        edtID.text = userId
    }
}