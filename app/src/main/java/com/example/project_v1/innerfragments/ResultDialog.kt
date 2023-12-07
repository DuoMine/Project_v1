package com.cookandroid.testproject.Dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.project_v1.R
import com.example.project_v1.databinding.FragmentResultDialogBinding
import com.example.project_v1.innerfragments.ChoiceCheckDialog


class ResultDialog : DialogFragment(), View.OnClickListener { // 탕후루 뽑기 확인 클래스
    private lateinit var binding: FragmentResultDialogBinding
    private var image: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultDialogBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뽑힌 탕후루에 대한 정보를 Bundle() 객체를 통해 DialogFragment에 값을 저장
        var mArgs : Bundle? = arguments
        var name: String? = null
        var rank: String? = null

        var display = mArgs?.getString("display")
        if (display.equals("background")){
            image = mArgs?.getInt("background")
            name = mArgs?.getString("name")
            binding.tanghuluResultRank.text = null
            binding.tanghuluResultRank.text = null
            binding.result.text = "구매 완료"
        } else {
            image = mArgs?.getInt("tanghulu")
            name = mArgs?.getString("name")
            rank = mArgs?.getString("rank")
            binding.tanghuluResultRank.text = rank
            binding.result.text = "결과"
            if (rank == "Common"){
                binding.tanghuluResultRank.setTextColor(Color.BLACK)
            } else if (rank == "Rare"){
                binding.tanghuluResultRank.setTextColor(Color.BLUE)
            } else{
                binding.tanghuluResultRank.setTextColor(Color.MAGENTA)
            }
        }
        binding.tanghuluResultImage.setImageResource(image!!)
        binding.tanghuluResultName.text = name

        binding.btnPositive.setOnClickListener(this)
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
        when (v?.id) {
            R.id.btnPositive -> {
                //데이터베이스와 연동해서 값을 저장해야 함
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