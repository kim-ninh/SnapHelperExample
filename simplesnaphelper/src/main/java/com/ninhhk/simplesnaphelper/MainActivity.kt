package com.ninhhk.simplesnaphelper

import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.ninhhk.simplesnaphelper.databinding.ActivityMainBinding
import com.ninhhk.simplesnaphelper.databinding.RootItemBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val itemSpacing = resources.getDimensionPixelSize(R.dimen.item_spacing)

        with(binding) {
            recyclerView.layoutManager = LinearLayoutManager(
                    this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            recyclerView.addItemDecoration(SimpleItemDecoration(itemSpacing))
            recyclerView.adapter = MyAdapter()

            recyclerView.attachSnapHelperWithListener(LinearSnapHelper(),
            SnapOnScrollListener.Behaviour.NOTIFY_ON_SCROLL, object: OnSnapPositionChangeListener{
                override fun onSnapPositionChange(position: Int) {
                    textView.text = "Position $position"
                }
            }
            )
        }
    }

    class SimpleItemDecoration(private val itemSpacing: Int)
        : RecyclerView.ItemDecoration() {
        var screenWidth = -1

        override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)

            val lp = view.layoutParams as ViewGroup.MarginLayoutParams
            val spacing = (getScreenWidth(parent.context) / 2f).toInt() - lp.width / 2
            val childAdapterPosition = parent.getChildAdapterPosition(view)

            if (childAdapterPosition == 0) {
                lp.leftMargin = 0
                outRect.left = spacing
            }

            if (childAdapterPosition == state.itemCount - 1) {
                lp.rightMargin = 0
                outRect.right = spacing
            } else {
                outRect.right = itemSpacing
            }
        }

        fun getScreenWidth(context: Context): Int {
            if (screenWidth == -1) {
                val wm = ContextCompat.getSystemService(context, WindowManager::class.java)!!
                val display = wm.defaultDisplay
                val size = Point()
                display.getSize(size)
                screenWidth = size.x
            }
            return screenWidth
        }

    }

    class MyViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        val binding = RootItemBinding.bind(itemView)
        val textViewNumber = binding.textViewNumber
    }

    class MyAdapter : RecyclerView.Adapter<MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val view = inflater.inflate(R.layout.root_item, parent, false)
            return MyViewHolder(view)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.textViewNumber.text = "$position"
        }

        override fun getItemCount(): Int = 15

    }

    interface OnSnapPositionChangeListener {
        fun onSnapPositionChange(position: Int)
    }

    class SnapOnScrollListener(
            private val snapHelper: SnapHelper,
            var behaviour: Behaviour = Behaviour.NOTIFY_ON_SCROLL,
            var onSnapPositionChangeListener: OnSnapPositionChangeListener? = null
    ) : RecyclerView.OnScrollListener() {
        enum class Behaviour {
            NOTIFY_ON_SCROLL,
            NOTIFY_ON_SCROLL_STATE_IDLE
        }

        private var snapPosition = RecyclerView.NO_POSITION

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (behaviour == Behaviour.NOTIFY_ON_SCROLL) {
                maybeNotifySnapPositionChange(recyclerView)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (behaviour == Behaviour.NOTIFY_ON_SCROLL_STATE_IDLE
                    && newState == RecyclerView.SCROLL_STATE_IDLE) {
                maybeNotifySnapPositionChange(recyclerView)
            }
        }

        private fun maybeNotifySnapPositionChange(recyclerView: RecyclerView) {
            val snapPosition = snapHelper.getSnapPosition(recyclerView)
            val snapPositionChanged = this.snapPosition != snapPosition
            if (snapPositionChanged) {
                onSnapPositionChangeListener?.onSnapPositionChange(snapPosition)
                this.snapPosition = snapPosition
            }
        }
    }

}

fun SnapHelper.getSnapPosition(recyclerView: RecyclerView): Int {
    val layoutManager = recyclerView.layoutManager ?: return RecyclerView.NO_POSITION
    val snapView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
    return layoutManager.getPosition(snapView)
}

fun RecyclerView.attachSnapHelperWithListener(
        snapHelper: SnapHelper,
        behaviour: MainActivity.SnapOnScrollListener.Behaviour
        = MainActivity.SnapOnScrollListener.Behaviour.NOTIFY_ON_SCROLL,
        onSnapPositionChangeListener: MainActivity.OnSnapPositionChangeListener
) {
    snapHelper.attachToRecyclerView(this)
    val snapOnScrollListener = MainActivity.SnapOnScrollListener(snapHelper, behaviour, onSnapPositionChangeListener)
    addOnScrollListener(snapOnScrollListener)
}