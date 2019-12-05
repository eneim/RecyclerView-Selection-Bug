package bug.rv.selection

import android.annotation.SuppressLint
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView.Adapter

class MyAdapter(private val items: ArrayList<String>) : Adapter<MyViewHolder>() {

  init {
    setHasStableIds(true)
  }

  var tracker: SelectionTracker<Long>? = null

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): MyViewHolder {
    return MyViewHolder(parent)
  }

  override fun getItemCount(): Int {
    return items.size
  }

  @SuppressLint("SetTextI18n")
  override fun onBindViewHolder(
    holder: MyViewHolder,
    position: Int
  ) {
    val selected = tracker?.isSelected(getItemId(position)) == true
    holder.messageView.text = "${items[position]}, selected: $selected"
    if (selected) {
      holder.itemView.setBackgroundResource(R.color.colorAccent)
    } else {
      holder.itemView.setBackgroundResource(R.color.colorPrimary)
    }
  }

  override fun onBindViewHolder(holder: MyViewHolder, position: Int, payloads: MutableList<Any>) {
    super.onBindViewHolder(holder, position, payloads)
    Log.i("Bugger:Adapter", "bind: $position, $payloads")
  }

  override fun getItemId(position: Int): Long {
    return position.toLong()
  }

  override fun onViewAttachedToWindow(holder: MyViewHolder) {
    super.onViewAttachedToWindow(holder)
    Log.i("Bugger:Adapter", "attach: $holder")
  }

  override fun onViewDetachedFromWindow(holder: MyViewHolder) {
    super.onViewDetachedFromWindow(holder)
    Log.d("Bugger:Adapter", "detach: $holder")
  }

  override fun onViewRecycled(holder: MyViewHolder) {
    super.onViewRecycled(holder)
    Log.w("Bugger:Adapter", "recycle: $holder")
  }
}