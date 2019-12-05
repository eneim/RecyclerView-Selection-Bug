package bug.rv.selection

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemDetailsLookup.ItemDetails
import androidx.recyclerview.widget.RecyclerView.ViewHolder

// https://code.tutsplus.com/tutorials/how-to-add-selection-support-to-a-recyclerview--cms-32175
class MyViewHolder(
  parent: ViewGroup
) : ViewHolder(
  LayoutInflater.from(parent.context).inflate(R.layout.holder_text, parent, false)
) {
  fun getItemDetails(): ItemDetails<Long>? {
    return object : ItemDetailsLookup.ItemDetails<Long>() {
      override fun getSelectionKey(): Long? {
        return this@MyViewHolder.itemId
      }

      override fun getPosition(): Int {
        return this@MyViewHolder.adapterPosition
      }

      override fun inSelectionHotspot(e: MotionEvent): Boolean {
        return true
      }
    }
  }

  val rootView = itemView.findViewById(R.id.holderRoot) as FrameLayout
  val messageView = itemView.findViewById(R.id.message) as TextView
}