package com.wuruoye.know.ui.setting.adapter

import android.annotation.SuppressLint
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wuruoye.know.R
import com.wuruoye.know.util.orm.table.RecordType
import com.wuruoye.know.widgets.EventHorizontalScrollView

/**
 * Created at 2019/4/13 15:00 by wuruoye
 * Description:
 */
class RecordTypeSetAdapter :
    ListAdapter<RecordType, RecordTypeSetAdapter.ViewHolder>(Callback()) {
    private var onClickListener: OnClickListener? = null

    private var mLastMoveVH: ViewHolder? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_record_type_set, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        with(holder) {
            tvTitle.text = item.title
            tvTitle.setOnClickListener {
                onClickListener?.onClick(item)
            }
            llDel.setOnClickListener {
                delete()
            }
            hsv.tag = item
            hsv.scrollTo(0, 0)
        }
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.onClickListener = listener
    }

    @SuppressLint("ClickableViewAccessibility")
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val hsv: EventHorizontalScrollView = view.findViewById(R.id.hsv_record_type_set)
        val tvTitle : TextView = view.findViewById(R.id.tv_record_type_set)
        val llDel: LinearLayout = view.findViewById(R.id.ll_del_record_type_set)
        val v: View = view.findViewById(R.id.v_record_type_set)

        private var maxMove: Int = 0
        private var deleteMove: Int = 0
        private var maxWidth: Int = 0
        private var isOnePass: Boolean = false
        private var moveAdapter = ViewMoveAdapter(llDel)

        init {
            view.post {
                val width = hsv.width

                var lp = tvTitle.layoutParams
                lp.width = width
                tvTitle.layoutParams = lp
                lp = v.layoutParams
                lp.width = width
                v.layoutParams = lp

                llDel.post { llDel.x = (width).toFloat() }

                maxWidth = width
                deleteMove = width / 2
                maxMove = llDel.width
            }

            hsv.setOnEventListener { event ->
                if (event.action == MotionEvent.ACTION_UP ||
                    event.action == MotionEvent.ACTION_CANCEL) {
                    val move = hsv.scrollX
                    when {
                        move > deleteMove -> delete()
                        move > maxMove/2 -> open()
                        else -> close()
                    }
                } else if (event.action == MotionEvent.ACTION_DOWN) {
                    isOnePass = false
                    if (mLastMoveVH != null && mLastMoveVH != this) {
                        mLastMoveVH!!.close()
                    }
                }
            }

            hsv.setOnScrollChangedListener { oldX, _, newX, _ ->
                if (!isOnePass) {
                    if (newX < maxMove) {
                        val end = (maxWidth - newX).toFloat()
                        moveAdapter.moveTo(end)
                    } else if (oldX < deleteMove && newX >= deleteMove) {
                        val end = deleteMove.toFloat()
                        moveAdapter.moveTo(end)
                        llDel.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                    } else if (newX < deleteMove && oldX >= deleteMove) {
                        val end = (maxWidth - llDel.width).toFloat()
                        moveAdapter.moveTo(end)
                        llDel.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                    } else if (newX > deleteMove) {
                        val end = (maxWidth - newX).toFloat()
                        moveAdapter.moveTo(end)
                    }
                } else {
                    val end = (maxWidth - newX).toFloat()
                    moveAdapter.moveTo(end)
                }

                if (newX == maxWidth) {
                    val item = hsv.tag
                    if (item != null) {
                        onClickListener?.onDelClick(item as RecordType)
                    }
                }
            }
        }

        fun close() {
            hsv.post { hsv.smoothScrollTo(0, 0) }
        }

        fun open() {
            hsv.post { hsv.smoothScrollTo(maxMove, 0) }
            mLastMoveVH = this
        }

        fun delete() {
            hsv.post { hsv.smoothScrollTo(maxWidth, 0) }
            isOnePass = true
        }
    }

    class Callback : DiffUtil.ItemCallback<RecordType>() {
        override fun areItemsTheSame(oldItem: RecordType, newItem: RecordType): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RecordType, newItem: RecordType): Boolean {
            return oldItem == newItem
        }
    }

    interface OnClickListener {
        fun onClick(item: RecordType)
        fun onDelClick(item: RecordType)
    }
}