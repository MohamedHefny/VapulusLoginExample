package com.example.vapulustest.ui.join.pinCode

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.vapulustest.R
import com.example.vapulustest.ui.join.JoinViewModel
import com.example.vapulustest.utils.rotate
import com.example.vapulustest.utils.show
import kotlinx.android.synthetic.main.fragment_pin_code.*
import kotlinx.android.synthetic.main.layout_loading.*


class PinCodeFragment : Fragment(), View.OnClickListener, View.OnTouchListener {

    private val joinViewModel: JoinViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pin_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Register clicks and touch listeners on num-pad.
        arrayOf<TextView>(
            pin_num0, pin_num1, pin_num2, pin_num3, pin_num4,
            pin_num5, pin_num6, pin_num7, pin_num8, pin_num9,
            pin_num_logout, pin_num_reset
        ).forEach {
            it.setOnClickListener(this)
            it.setOnTouchListener(this)
        }

        //Observe PIN-Code changes and update the ui.
        joinViewModel.pinCode.observe(viewLifecycleOwner, Observer {
            when (it.length) {
                0 -> pin_code_indicator_iv.setImageResource(R.drawable.dot0)
                1 -> pin_code_indicator_iv.setImageResource(R.drawable.dot1)
                2 -> pin_code_indicator_iv.setImageResource(R.drawable.dot2)
                3 -> pin_code_indicator_iv.setImageResource(R.drawable.dot3)
                4 -> { //PIN-Code is complete
                    pin_code_indicator_iv.setImageResource(R.drawable.dot4)
                    pin_code_loading_layout.show()
                    loading_icon.rotate()
                }
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.pin_num_logout -> {
                findNavController().navigateUp()
            }
            R.id.pin_num_reset -> joinViewModel.resetPinCode()
            else -> {
                val view = v as TextView
                joinViewModel.addToPinCode(view.text.toString())
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        //Handle num-pad buttons press background.
        if (event?.action == MotionEvent.ACTION_DOWN)
            v?.setBackgroundResource(R.drawable.ic_star_number_press)
        else if (event?.action == MotionEvent.ACTION_UP)
            v?.setBackgroundColor(Color.TRANSPARENT)

        return false
    }
}