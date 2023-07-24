package com.example.a23_hf069

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.ActionBar
import android.webkit.SslErrorHandler
import android.app.AlertDialog
import android.content.DialogInterface

class SaeilSearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saeil_search)

        // 기본 툴바 숨기기
        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()

        // WebView 초기화
        val webView: WebView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true // JavaScript 사용 허용 (선택사항)

        // 웹뷰 디버깅 활성화
        webView.settings.allowContentAccess = true
        webView.settings.allowFileAccess = true
        webView.settings.allowUniversalAccessFromFileURLs = true
        webView.settings.allowFileAccessFromFileURLs = true
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        // 오버레이 비활성화
        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null)

        // WebViewClient 설정
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                super.onPageStarted(view, url, favicon)
                android.util.Log.d("WebView", "Page started loading: $url")
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                android.util.Log.d("WebView", "Page finished loading: $url")
            }

            override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                android.util.Log.e("WebView", "Error loading page: $description")
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: android.net.http.SslError?) {
                // SSL 인증서 오류가 발생했을 때 사용자에게 허용 여부를 묻는 다이얼로그 표시
                val dialogBuilder = AlertDialog.Builder(this@SaeilSearchActivity)
                dialogBuilder.setMessage("SSL 인증서 오류가 발생했습니다. 계속 진행하시겠습니까?")
                    .setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                        handler?.proceed() // SSL 인증서 오류를 무시하고 계속 진행
                    })
                    .setNegativeButton("아니오", DialogInterface.OnClickListener { dialog, which ->
                        handler?.cancel() // SSL 인증서 오류를 처리하지 않고 취소
                    })
                    .setCancelable(false)
                    .show()
            }
        }

        // URL 로드
        webView.loadUrl("https://saeil.mogef.go.kr/hom/info/search.do")
    }
}
