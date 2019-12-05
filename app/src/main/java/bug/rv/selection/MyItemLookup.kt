package bug.rv.selection

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

// Ref: https://developer.android.com/reference/androidx/recyclerview/selection/ItemDetailsLookup
class MyItemLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {

  override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
    val view = recyclerView.findChildViewUnder(e.x, e.y)
    if (view != null) {
      val holder = recyclerView.getChildViewHolder(view)
      if (holder is MyViewHolder) {
        return holder.getItemDetails()
      }
    }
    return null
  }
}