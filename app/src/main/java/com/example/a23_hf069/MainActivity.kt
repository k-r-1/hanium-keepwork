package com.example.a23_hf069

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.activity_login.*


class MainActivity : AppCompatActivity(){
    lateinit var login : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 기본 툴바 숨기기
        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.hide()
        }

        val adapter = PagerAdapter(supportFragmentManager)
        adapter.addFragment(P_loginFragment(), "개인회원")
        adapter.addFragment(C_loginFragment(), "기업회원")


        viewpager01.adapter = adapter
        tablayout01.setupWithViewPager(viewpager01)


    }





}
