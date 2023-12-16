package com.example.project_v1.innerfragments

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.project_v1.R
import com.example.project_v1.activity.userData
import com.example.project_v1.databinding.FragmentCheckDialogBinding
import com.google.firebase.auth.FirebaseAuth

class BackgroundDialog : DialogFragment(), OnClickListener { // 배경 구매 확인 클래스
    private lateinit var binding: FragmentCheckDialogBinding
    private var name: String? = null
    private var content: String? = null
    private var coin: String? = null
    private var image: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckDialogBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Bundle() 객체에 의해 값을 전달받은 후 각 정보를 DialogFragment 데이터에 삽입
        val mArgs : Bundle? = arguments
        image = mArgs?.getInt("image")
        name = mArgs?.getString("name")
        content = mArgs?.getString("content")
        coin = mArgs?.getString("coin")

        binding.resultImage.setImageResource(image!!)
        binding.resultName.text = name
        binding.resultContent.text = content
        binding.resultCoin.text = coin

        isCancelable = false
        binding.btnPositive.setOnClickListener(this)
        binding.btnNegative.setOnClickListener(this)

    }

    override fun onResume() {
        super.onResume()
        // Dialog를 화면 크기에 맞게 재구성
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val width = getScreenWidth(requireContext())

        // 디바이스 크기를 기반으로 가로는 0.7, 세로는 Dialog 컴포넌트 요소들에 맞춰서 구성
        params?.width = (width * 0.7).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnPositive -> {
                // 테스트를 위한 토스트 메세지, 데이터베이스와 연동해서 값을 저장해야 함
                userData.gold - 20
                Toast.makeText(activity, "구매", Toast.LENGTH_SHORT).show()
                dialog?.dismiss()
            }
            R.id.btnNegative -> {
                // 테스트를 위한 토스트 메세지
                Toast.makeText(activity, "취소", Toast.LENGTH_SHORT).show()
                dialog?.dismiss()
            }
        }
    }

    @Suppress("DEPRECATION")
    fun getScreenWidth(context: Context): Int { // 디바이스의 가로 길이를 구하는 메소드
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = wm.currentWindowMetrics
            val insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.width() - insets.left - insets.right
        } else {
            val displayMetrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.widthPixels
        }
    }

    @Suppress("DEPRECATION")
    fun getScreenHeight(context: Context): Int { // 디바이스의 세로 길이를 구하는 메소드
        //따로 사용하고 있지 않으나 디바이스의 세로 길이도 구하고 싶다면 사용
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = wm.currentWindowMetrics
            val insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.height() - insets.bottom - insets.top
        } else {
            val displayMetrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.heightPixels
        }
    }
}