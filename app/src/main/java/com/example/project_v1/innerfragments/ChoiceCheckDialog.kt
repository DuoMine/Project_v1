package com.example.project_v1.innerfragments

import android.app.Dialog
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
import com.cookandroid.testproject.Dialog.ResultDialog
import com.cookandroid.testproject.Model.TanghuluModel
import com.example.project_v1.R
import com.example.project_v1.databinding.FragmentChoiceCheckDialogBinding

class ChoiceCheckDialog: DialogFragment(), View.OnClickListener {
    private lateinit var binding: FragmentChoiceCheckDialogBinding
    private var text: String? = null
    private var count: Int? = null
    private var countR: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChoiceCheckDialogBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Bundle() 객체에 의해 값을 전달받은 후 각 정보를 DialogFragment 데이터에 삽입
        var mArgs : Bundle? = arguments
        text = mArgs?.getString("hint")
        count = mArgs?.getInt("count")

        binding.checkText.text = text

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
                if (count == 1){
                    choiceTanghuru()
                    dialog?.dismiss()
                } else {
                    for (i in 0..4){
                        choiceTanghuru()
                    }
                    dialog?.dismiss()
                }
            }
            R.id.btnNegative -> {
                // 테스트를 위한 토스트 메세지
                Toast.makeText(activity, "취소", Toast.LENGTH_SHORT).show()
                dialog?.dismiss()
            }
        }
    }

    private fun choiceTanghuru(){
        //탕후루 뽑기 함수, TanghuruModel 클래스에서 구분한 탕후루를 확률 메소드로 가챠 구현
        //현재는 중복으로 단순 구현한 상태, 리스트들이 많아지면 확률 조정
        var tanghuluModel = TanghuluModel()
        val random = Math.random()
        var tanghulu: Int? = null
        var name: String? = null
        var rank: String? = null
        if (random < 0.5){
            val num = (Math.random()*(tanghuluModel.commonTanghulu.size)).toInt()
            tanghulu = tanghuluModel.commonTanghulu[num]
            name = tanghuluModel.commonName[num]
            rank = "Common"
        } else if (random < 0.9){
            val num = (Math.random()*(tanghuluModel.rareTanghulu.size)).toInt()
            name = tanghuluModel.rareName[num]
            tanghulu = tanghuluModel.rareTanghulu[num]
            rank = "Rare"
        } else{
            val num = (Math.random()*(tanghuluModel.epicTanghulu.size)).toInt()
            tanghulu = tanghuluModel.epicTanghulu[num]
            name = tanghuluModel.epicName[num]
            rank = "Epic"
        }
        // 뽑힌 탕후루의 정보 값을 DialogFragment에 Bundle()객체로 전달
        var args = Bundle()

        args.putInt("tanghulu", tanghulu)
        args.putString("name", name)
        args.putString("rank", rank)

        //뽑힌 탕후루의 정보를 간략하게 표기할 Dialog 생성
        val dialog = ResultDialog()
        dialog.arguments = args
        dialog.show(parentFragmentManager,"TanghuluDialog")
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