package com.example.project_v1.innerfragments

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
import androidx.fragment.app.DialogFragment
import com.example.project_v1.R
import com.example.project_v1.databinding.FragmentResultDialogBinding


class TanghuluDialog : DialogFragment(), View.OnClickListener { // 탕후루 뽑기 확인 클래스
    private lateinit var binding: FragmentResultDialogBinding
    private var tanghulu: Int? = null

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
        val mArgs : Bundle? = arguments
        tanghulu = mArgs?.getInt("tanghuru")
        val name = mArgs?.getString("name")
        val rank = mArgs?.getString("rank")

        /*var rankarray = arrayOf("Common","Rare","Unique")*/
        binding.tanghuluResultImage.setImageResource(tanghulu!!)
        binding.tanghuluResultName.text = name
        binding.tanghuluResultRank.text = rank
        if (rank == "Common"){
            binding.tanghuluResultRank.setTextColor(Color.BLACK)
        } else if (rank == "Rare"){
            binding.tanghuluResultRank.setTextColor(Color.BLUE)
        } else{
            binding.tanghuluResultRank.setTextColor(Color.MAGENTA)
        }
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