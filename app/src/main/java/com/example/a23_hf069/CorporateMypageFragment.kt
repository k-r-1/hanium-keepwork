package com.example.a23_hf069

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import android.util.Log
import android.widget.LinearLayout

// CorporateMypageFragment 클래스 정의. Fragment를 상속받음.
class CorporateMypageFragment : Fragment() {

    // 이미지 선택 요청 코드. startActivityForResult 호출 시 사용됨.
    private val REQUEST_IMAGE_PICK = 1
    private val STORAGE_PERMISSION_REQUEST_CODE = 1234


    // 프래그먼트 뷰가 생성될 때 호출되는 메서드.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // fragment_corporate_mypage.xml 레이아웃을 프래그먼트의 뷰로 인플레이트.
        return inflater.inflate(R.layout.fragment_corporate_mypage, container, false)
    }

    // 프래그먼트의 뷰가 완전히 생성된 후 호출되는 메서드.
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // profileImageView라는 ID를 가진 ImageView를 찾아서 변수에 할당.
        val profileImageView = view.findViewById<ImageView>(R.id.profileImageView)

        val profileName = view.findViewById<LinearLayout>(R.id.profile_name_btn)

        // 회사정보수정화면으로 전환
        profileName.setOnClickListener{
            val fragment1 = CorporateProfileFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_container, fragment1)
                .addToBackStack(null)
                .commit()
        }
        // profileImageView를 클릭하면 실행될 클릭 리스너 설정.
        profileImageView.setOnClickListener {
            Log.d("permission","클릭됨")
            // 외부 저장소 읽기 권한이 허용되어 있는지 확인.
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // 권한이 없으면 권한 요청.
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_REQUEST_CODE)
                Log.d("permission","권한요청함")

            } else {
                // 권한이 있으면 갤러리를 여는 메서드 호출.
                openGalleryForImage()
                Log.d("permission","바로 갤러리 열어버림")

            }
        }
    }

    // 갤러리를 여는 메서드.
    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
        Log.d("permission","갤러리 메소드 호출됨!!")

    }

    // 권한 요청에 대한 결과를 받는 메서드.
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Log.d("permission","권한요청 메소드 호출됨")
        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE
                && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // 권한 요청이 승인되면 갤러리를 여는 메서드 호출.
            Log.d("permission","갤러리 메소드 호출됨")
            openGalleryForImage()
        } else {
            // 권한 요청이 거부되면 토스트 메시지 출력.
            Toast.makeText(context, "퍼미션을 허용해주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    // startActivityForResult로 시작된 액티비티의 결과를 받는 메서드.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 이미지 선택 요청 코드와 결과 코드가 OK인 경우.
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            // 선택된 이미지의 URI를 가져옴.
            val selectedImageUri = data?.data

            // profileImageView에 선택된 이미지를 설정.
            view?.findViewById<ImageView>(R.id.profileImageView)?.setImageURI(selectedImageUri)
        }
    }
}
