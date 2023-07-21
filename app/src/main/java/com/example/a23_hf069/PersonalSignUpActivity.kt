package com.example.a23_hf069

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import androidx.appcompat.app.ActionBar
import android.app.ProgressDialog
import android.os.AsyncTask
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class PersonalSignUpActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    // IP 주소와 태그를 초기화
    private var IP_ADDRESS = "3.39.231.84" // 본인 IP주소를 넣으세요.
    private var TAG = "phptest" // phptest log 찍으려는 용도

    // 뷰 요소들을 선언
    private lateinit var backButton: ImageButton // go back to prev page
    private lateinit var id_text_input_edit_text: EditText // id
    private lateinit var idcheck_button: Button // id duplicate check
    private lateinit var password_text_input_edit_text: EditText // password
    private lateinit var password_recheck_text_input_edit_text: EditText // password recheck
    private lateinit var name_textview_input_edit_text: EditText // name
    private lateinit var email_textview_input_edit_text: EditText // email
    private lateinit var phoneNumber_textview_input_edit_text: EditText // phone number
    private lateinit var phoneNumber_button: Button // identification
    private lateinit var phoneNumberCheck_textview_input_edit_text: EditText // identification number
    private lateinit var phoneNumberCheck_button: Button // identification check button
    private lateinit var signUp_button: Button // sign up button

    // TextView 요소인 mTextViewResult 선언
    private lateinit var mTextViewResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_individual_sign_up)

        // 기본 툴바 숨기기
        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.hide()
        }

        // 각 뷰의 요소들과 레이아웃 파일에서의 아이디 연결
        backButton = findViewById(R.id.backButton)  // go back to prev page
        id_text_input_edit_text = findViewById(R.id.id_text_input_edit_text) // personal id
        idcheck_button = findViewById(R.id.idCheck_button) // id duplicate check
        password_text_input_edit_text = findViewById(R.id.password_text_input_edit_text) // personal password
        password_recheck_text_input_edit_text = findViewById(R.id.password_recheck_text_input_edit_text) // password recheck
        name_textview_input_edit_text = findViewById(R.id.name_textview_input_edit_text) // personal name
        email_textview_input_edit_text = findViewById(R.id.email_textview_input_edit_text) // personal email
        phoneNumber_textview_input_edit_text = findViewById(R.id.phoneNumber_textview_input_edit_text) // personal phonenum
        phoneNumber_button = findViewById(R.id.phoneNumber_button) // identification
        phoneNumberCheck_textview_input_edit_text = findViewById(R.id.phoneNumberCheck_textview_input_edit_text) // identification number
        phoneNumberCheck_button = findViewById(R.id.phoneNumberCheck_button) // identification check button
        signUp_button = findViewById(R.id.signUp_button) // sign up button

        // mTextViewResult를 스크롤 가능하도록 설정
        mTextViewResult = findViewById(R.id.textView_main_result)
        mTextViewResult.movementMethod = ScrollingMovementMethod()

        // 클릭 시 현재 액티비티 종료
        backButton.setOnClickListener {
            finish()
        }

        // 버튼 클릭 시 회원가입 과정 수행
        signUp_button.setOnClickListener {
            val id = id_text_input_edit_text.text.toString().trim()
            val password = password_text_input_edit_text.text.toString().trim()
            val password_recheck = password_recheck_text_input_edit_text.text.toString().trim()
            val name = name_textview_input_edit_text.text.toString().trim()
            val email = email_textview_input_edit_text.text.toString().trim()
            val phoneNumber = phoneNumber_textview_input_edit_text.text.toString().trim()
            val phoneNumberCheck = phoneNumberCheck_textview_input_edit_text.toString().trim()

            if (id.isEmpty() || password.isEmpty() || password_recheck.isEmpty() || name.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
                Toast.makeText(this@PersonalSignUpActivity, "정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                if (password == password_recheck) {
                    if (password.length <= 5) {
                        Toast.makeText(this@PersonalSignUpActivity, "비밀번호를 6자리 이상 입력해주세요.", Toast.LENGTH_SHORT).show()
                    } else if (!email.contains("@")) {
                        Toast.makeText(this@PersonalSignUpActivity, "아이디에 @ 및 .com을 포함시키세요.", Toast.LENGTH_SHORT).show()
                    } else if (phoneNumber.contains("-") || !(phoneNumber[1] == '1')) {
                        Toast.makeText(this@PersonalSignUpActivity, "올바른 전화번호 형식으로 입력해주세요.", Toast.LENGTH_SHORT).show()
                    } else {
                        // 'InsertData' 클래스의 인스턴스인 'task'를 생성
                        val task = InsertData()

                        // 'task'의 'execute'메서드를 호출해 백그라운드에서 데이터를 삽입
                        task.execute(
                            // 'execute' 메서드에 서버 URL과 회원가입에 필요한 개인정보를 전달
                            "http://$IP_ADDRESS/android_log_insert_php.php",
                            id,
                            password,
                            password_recheck,
                            name,
                            email,
                            phoneNumber
                        )
                        Toast.makeText(this@PersonalSignUpActivity, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    Toast.makeText(this@PersonalSignUpActivity, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // AsyncTask를 상속받고, 서버로 데이터를 전송
    inner class InsertData : AsyncTask<String, Void, String>() {
        private var progressDialog: ProgressDialog? = null

        // 백그라운드 작업 실행 전 실행, 프로그레스 다이얼로그 표시
        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog = ProgressDialog.show(
                this@PersonalSignUpActivity,
                "Please Wait",
                null,
                true,
                true
            )
        }

        // 백그라운드 작업 완료 후 실행, 결과를 처리하고 프로그레스 다이얼로그 종료
        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            progressDialog?.dismiss()
            mTextViewResult.text = result
            Log.d(TAG, "POST response  - $result")
        }

        // 백그라운드에서 수행될 작업 정의, 서버로 데이터 전송 & 응답을 받아 처리
        // AsyncTask의 Params 매개변수로 가변 인자를 받아 String을 반환
        override fun doInBackground(vararg params: String): String {

            // param 배열에서 서버 URL과 각각의 개인정보 추출
            val serverURL = params[0]
            val personal_id = params[1]
            val personal_password = params[2]
            val personal_password_chk = params[3]
            val personal_name = params[4]
            val personal_email = params[5]
            val personal_phonenum = params[6]

            // POST 요청으로 전송할 파라미터 문자열 구성
            val postParameters =
                "personal_id=$personal_id&personal_password=$personal_password&personal_password_chk=$personal_password_chk&personal_name=$personal_name&personal_email=$personal_email&personal_phonenum=$personal_phonenum"

            // 'serverURL'을 기반으로 URL 객체 생성, 'openConnection'메서드를 사용해 HttpURLconnection 객체 얻음
            try {
                val url = URL(serverURL)
                val httpURLConnection = url.openConnection() as HttpURLConnection

                // 연결과 읽기 타임아웃 설정
                httpURLConnection.readTimeout = 5000
                httpURLConnection.connectTimeout = 5000

                // 요청 메서드를 POST로 설정정
                httpURLConnection.requestMethod = "POST"

                // 서버에 연결
                httpURLConnection.connect()

                // 연결에 대한 출력 스트림을 얻고, 파라미터를 'UTF-8'로 인코딩하여 전송
                val outputStream = httpURLConnection.outputStream
                outputStream.write(postParameters.toByteArray(charset("UTF-8")))
                outputStream.flush()
                outputStream.close()

                // 서버로부터 응답 상태 코드 얻음
                val responseStatusCode = httpURLConnection.responseCode
                Log.d(TAG, "POST response code - $responseStatusCode")

                // 응답 상태 코드가 'HTTP_OK(200)'인 경우, 'inputStream'을 얻고, 아닌 경우 'errorStream'을 얻음
                val inputStream: InputStream
                inputStream = if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    httpURLConnection.inputStream
                } else {
                    httpURLConnection.errorStream
                }

                // 'inputStream'을 'UTF-8'로 읽기 위해 'InputStreadReader'와 'BufferedReader'를 생성
                val inputStreamReader = InputStreamReader(inputStream, "UTF-8")
                val bufferedReader = BufferedReader(inputStreamReader)

                // 'StringBuilder'를 사용해 응답 데이터를 한 줄씩 읽어 연결
                val sb = StringBuilder()
                var line: String? = null

                while (bufferedReader.readLine().also { line = it } != null) {
                    sb.append(line)
                }

                // 'bufferReader' 닫기
                bufferedReader.close()
                Log.d("php 값 :", sb.toString())

                // 'sb.toString()'을 반환하여 응답 데이터를 반환
                return sb.toString()
            } catch (e: Exception) {
                Log.d(TAG, "InsertData: Error", e)
                return "Error " + e.message
            }
        }
    }

    // 아이템이 선택되었을 때 호출
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}

    // 아무것도 선택되지 않았을 때 호출
    override fun onNothingSelected(parent: AdapterView<*>?) {}

}
