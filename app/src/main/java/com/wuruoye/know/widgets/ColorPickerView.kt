package com.wuruoye.know.widgets

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import com.wuruoye.know.R
import com.wuruoye.know.util.ColorUtil

/**
 * Created at 2019/3/21 15:36 by wuruoye
 * Description:
 */
class ColorPickerView(context: Context)
    : LinearLayout(context), SeekBar.OnSeekBarChangeListener {
    private val acs: Array<AppCompatSeekBar>
    private var colors: FloatArray

    private val cvColor: View
    private val etColor: EditText

    init {
        orientation = LinearLayout.VERTICAL
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)

        LayoutInflater.from(context).inflate(R.layout.dlg_color_picker, this)
        colors = floatArrayOf(0F, 0F, 0F, 0F)

        acs = arrayOf(findViewById(R.id.acs_alpha_dlg_color_picker),
                findViewById(R.id.acs_red_dlg_color_picker),
                findViewById(R.id.acs_green_dlg_color_picker),
                findViewById(R.id.acs_blue_dlg_color_picker))
        for (sb in acs) {
            sb.setOnSeekBarChangeListener(this)
        }

        cvColor = findViewById(R.id.v_dlg_color_picker)
        etColor = findViewById(R.id.et_dlg_color_picker)
        etColor.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                setColor(ColorUtil.hex2color(etColor.text.toString()))
            }
            true
        }
    }

    fun setColor(color: Int) {
        colors = ColorUtil.color2argb(color)
        etColor.setText(ColorUtil.color2hex(color))

        for (i in 0 until 4) {
            acs[i].progress = getProgress(colors[i])
        }
    }

    fun getColor(): Int {
        return ColorUtil.argb2color(colors)
    }

    private fun getColor(progress: Int): Float {
        return progress * 1F / 100 * 255;
    }

    private fun getProgress(color: Float): Int {
        return (color / 255 * 100).toInt()
    }

    private fun change() {
        val color = ColorUtil.argb2color(colors)
        cvColor.setBackgroundColor(color)
        etColor.clearFocus()
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        val index = acs.indexOf(seekBar)
        if (index != -1) {
            colors[index] = getColor(progress)
            change()
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        etColor.setText(ColorUtil.argb2hex(colors))
    }
}