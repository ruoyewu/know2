package com.wuruoye.know.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.wuruoye.know.R

/**
 * Created at 2019/3/19 13:38 by wuruoye
 * Description:
 */
class BottomAlertDialog private constructor(context: Context, view: View)
    : BottomDialog(context, view) {
    class Builder(private val mContext: Context) {
        private var mContentView: View? = null
        private var mTitle: String? = null
        private var mMessage: String? = null
        private var mCancelListener: View.OnClickListener? = null
        private var mConfirmListener: View.OnClickListener? = null
        private var mConfirmGravity: Int = Gravity.BOTTOM
        private var mCancelable: Boolean = true

        fun setContentView(view: View): Builder {
            mContentView = view
            return this
        }

        fun setTitle(title: String): Builder {
            this.mTitle = title
            return this
        }

        fun setMessage(message: String): Builder {
            this.mMessage = message
            return this
        }

        fun setCancelListener(listener: View.OnClickListener): Builder {
            this.mCancelListener = listener
            return this
        }

        fun setConfirmListener(listener: View.OnClickListener): Builder {
            return setConfirmListener(listener, Gravity.BOTTOM)
        }

        fun setConfirmListener(listener: View.OnClickListener, gravity: Int): Builder {
            this.mConfirmListener = listener
            this.mConfirmGravity = gravity
            return this
        }

        fun setCancelable(cancelable: Boolean): Builder {
            this.mCancelable = cancelable
            return this
        }

        @SuppressLint("InflateParams")
        fun build(): BottomAlertDialog {
            val view = LayoutInflater.from(mContext)
                    .inflate(R.layout.dlg_bottom_alert, null)
            val tvTitle = view.findViewById<TextView>(R.id.tv_title_dlg_bottom_alert)
            val tvMsg = view.findViewById<TextView>(R.id.tv_msg_dlg_bottom_alert)
            val llContent = view.findViewById<LinearLayout>(R.id.ll_content_dlg_bottom_alert)
            val btnCancel = view.findViewById<Button>(R.id.btn_cancel_dlg_bottom_alert)
            val btnConfirm = view.findViewById<Button>(R.id.btn_confirm_dlg_bottom_alert)
            val btnConfirmTop = view.findViewById<Button>(R.id.btn_confirm_top_dlg_bottom_alert)
            val dialog = BottomAlertDialog(mContext, view)

            if (mContentView != null) {
                llContent.addView(mContentView)
            }

            if (mTitle == null) {
                tvTitle.visibility = View.GONE
            } else {
                tvTitle.text = mTitle
            }

            if (mMessage == null) {
                tvMsg.visibility = View.GONE
            } else {
                tvMsg.text = mMessage
            }

            if (mCancelListener == null) {
                btnCancel.visibility = View.GONE
            } else {
                btnCancel.setOnClickListener(mCancelListener)
            }

            if (mConfirmListener == null) {
                btnConfirm.visibility = View.GONE
            } else {
                if (mConfirmGravity == Gravity.BOTTOM) {
                    btnConfirm.setOnClickListener(mConfirmListener)
                } else {
                    btnConfirm.visibility = View.GONE
                    btnConfirmTop.visibility = View.VISIBLE
                    btnConfirmTop.setOnClickListener(mConfirmListener)
                }
            }

            dialog.setCancelable(mCancelable)
            return dialog
        }
    }
}