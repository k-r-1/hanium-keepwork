package com.example.a23_hf069

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.a23_hf069.databinding.ActivityLoginBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 기본 툴바 숨기기
        supportActionBar?.hide()

        val adapter = PagerAdapter(supportFragmentManager)
        adapter.addFragment(P_loginFragment(), "개인회원")
        adapter.addFragment(C_loginFragment(), "기업회원")

        binding.viewpager01.adapter = adapter
        binding.tablayout01.setupWithViewPager(binding.viewpager01)
    }
}
