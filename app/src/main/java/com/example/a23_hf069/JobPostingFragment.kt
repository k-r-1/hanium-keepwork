package com.example.a23_hf069

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.databinding.DataBindingUtil.setContentView
import com.example.a23_hf069.databinding.ActivityResumeEducationBinding
import com.example.a23_hf069.databinding.FragmentJobPostingBinding
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [JobPostingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class JobPostingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var calendar = Calendar.getInstance()
    private var postingYear = calendar.get(Calendar.YEAR)
    private var postingMonth = calendar.get(Calendar.MONTH)
    private var postingDay = calendar.get(Calendar.DAY_OF_MONTH)
    lateinit var postingCalButton : Button
    lateinit var edtpostingYear : EditText
    lateinit var edtpostingMonth : EditText
    lateinit var edtpostingDay : EditText
    lateinit var binding : FragmentJobPostingBinding
    lateinit var careerSpinner: Spinner
    lateinit var educationSpinner: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentJobPostingBinding.inflate(inflater, container, false)
        val view = binding.root

        postingCalButton = view.findViewById(R.id.posting_calendar)
        edtpostingYear = view.findViewById(R.id.edt_posting_year)
        edtpostingMonth = view.findViewById(R.id.edt_posting_month)
        edtpostingDay = view.findViewById(R.id.edt_posting_day)

        postingCalButton.setOnClickListener{
            val datePickerDialog1 = DatePickerDialog(requireContext(), { _, year, month, day ->
                edtpostingYear.setText(year.toString())
                edtpostingMonth.setText((month + 1).toString())
                edtpostingDay.setText(day.toString())
            }, postingYear, postingMonth, postingDay)
            datePickerDialog1.show()
        }

        val careerList = listOf(
            "경력무관",
            "신입",
            "경력",
        )

        val adapter1 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, careerList)
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.careerSpinner.adapter = adapter1

        val educationList = listOf(
            "학력무관",
            "고등학교 졸업 이상",
            "전문학사 이상",
            "학사 이상",
            "석사 이상",
            "박사 이상"
        )

        val adapter2 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, educationList)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.educationSpinner.adapter = adapter2

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment JobPostingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            JobPostingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}