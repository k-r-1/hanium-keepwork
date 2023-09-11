import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.a23_hf069.C_MemberModel
import com.example.a23_hf069.CorporateHomeActivity
import com.example.a23_hf069.CorporateSignUpActivity
import com.example.a23_hf069.FindCorporateIdActivity
import com.example.a23_hf069.R
import com.example.a23_hf069.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class C_loginFragment : Fragment() {
    lateinit var login: Button
    lateinit var signUp: Button
    lateinit var btnFindId: Button
    private lateinit var id: String
    private lateinit var id_text_input_edit_text: EditText
    private lateinit var password_text_input_edit_text: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_c_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        login = view.findViewById<Button>(R.id.login_btn)
        signUp = view.findViewById<Button>(R.id.signUp_btn)
        btnFindId = view.findViewById<Button>(R.id.findID_btn)

        id_text_input_edit_text = view.findViewById<EditText>(R.id.id_text)
        password_text_input_edit_text = view.findViewById<EditText>(R.id.pw_text)

        login.setOnClickListener() {
            id = id_text_input_edit_text.text.toString().trim()
            val password = password_text_input_edit_text.text.toString().trim()

            if (id.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "정보를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                val retrofit = Retrofit.Builder()
                    .baseUrl(RetrofitInterface.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val apiService = retrofit.create(RetrofitInterface::class.java)

                apiService.getCorporateData(id).enqueue(object : Callback<List<C_MemberModel>> {
                    override fun onResponse(call: Call<List<C_MemberModel>>, response: Response<List<C_MemberModel>>) {
                        if (response.isSuccessful) {
                            val result = response.body()
                            if (result != null && result.isNotEmpty()) {
                                for (data in result) {
                                    if (data.company_id == id) {
                                        Toast.makeText(requireContext(), "로그인 성공", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(requireActivity(), CorporateHomeActivity::class.java)
                                        intent.putExtra("userCompanyName", data.company_name)
                                        intent.putExtra("userCompanyId", data.company_id)
                                        startActivity(intent)
                                    }
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<C_MemberModel>>, t: Throwable) {
                        Toast.makeText(
                            requireContext(),
                            "통신 오류: " + t.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        }

        signUp.setOnClickListener() {
            val intent = Intent(getActivity(), CorporateSignUpActivity::class.java)
            startActivity(intent)
        }

        btnFindId.setOnClickListener {
            val intent = Intent(getActivity(), FindCorporateIdActivity::class.java)
            startActivity(intent)
        }
    }
}
