package bug.rv.selection

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.postDelayed
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.SelectionTracker.SelectionObserver
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import kotlinx.android.synthetic.main.activity_main.recyclerView

class MainActivity : AppCompatActivity() {

  companion object {
    val data = arrayListOf(
        "Cat 0",
        "Cat 1",
        "Cat 2",
        "Cat 3",
        "Cat 4",
        "Cat 5",
        "Cat 6",
        "Cat 7",
        "Cat 8",
        "Cat 9",
        "Cat 10",
        "Cat 11",
        "Cat 12",
        "Cat 13",
        "Cat 14",
        "Cat 15",
        "Cat 16",
        "Cat 17",
        "Cat 18",
        "Cat 19"
    )
  }

  lateinit var selectionTracker: SelectionTracker<Long>
  private val items = ArrayList(data)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val adapter = MyAdapter(items)
    recyclerView.adapter = adapter

    selectionTracker = SelectionTracker.Builder<Long>(
        "My-Cool-App",
        recyclerView,
        StableIdKeyProvider(recyclerView),
        MyItemLookup(recyclerView),
        StorageStrategy.createLongStorage()
    )
        .withSelectionPredicate(SelectionPredicates.createSelectSingleAnything())
        .build()
    selectionTracker.onRestoreInstanceState(savedInstanceState)

    selectionTracker.addObserver(object : SelectionObserver<Long>() {
      override fun onItemStateChanged(
        key: Long,
        selected: Boolean
      ) {
        super.onItemStateChanged(key, selected)
        Log.d("Bugger:Selection", "State: $key ~ $selected")
      }

      override fun onSelectionChanged() {
        super.onSelectionChanged()
        Log.d("Bugger:Selection", "onSelectionChanged()")
      }
    })

    adapter.tracker = selectionTracker
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    selectionTracker.onSaveInstanceState(outState)
  }

  override fun onStart() {
    super.onStart()
    // below: reproduce the issue
    // 1. select item on top
    // 2. scroll to second item --> first item will be detached from RecyclerView.
    // 3. select second item (now sits on top).
    // 4. scroll back so that first item is back.
    // 5. result: both first item and second item appear to be selected.
    // Expect: only one/latest item appears to be selected.
    // Actual: both first item and second item appear to be selected.
    recyclerView.postDelayed(2000) {
      val itemOnTop = recyclerView.findViewHolderForAdapterPosition(0)
      if (itemOnTop != null) {
        // Should be an actual 'click' or 'touch'
        selectionTracker.select(itemOnTop.itemId)
      }
    }

    recyclerView.postDelayed(5000) {
      val target = recyclerView.findViewHolderForAdapterPosition(1)
      if (target != null) {
        val scrollAmount = target.itemView.top
        recyclerView.smoothScrollBy(0, scrollAmount)
        recyclerView.postDelayed(2000) {
          // Should be an actual 'click' or 'touch'
          selectionTracker.select(target.itemId)
        }

        recyclerView.postDelayed(5000) {
          recyclerView.smoothScrollBy(0, -scrollAmount)
        }
      }
    }
  }
}
