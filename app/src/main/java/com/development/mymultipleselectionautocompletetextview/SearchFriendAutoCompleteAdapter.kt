package com.development.mymultipleselectionautocompletetextview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import com.google.android.material.textview.MaterialTextView
import java.util.*
import kotlin.collections.ArrayList

class SearchFriendAutoCompleteAdapter(
    context: Context,
    var resource: Int,
    listl: List<FriendData>
) :
    ArrayAdapter<FriendData>(context, resource, listl) {
    private val tempList = ArrayList<FriendData>()
    private val dataList = ArrayList<FriendData>()

    init {
        tempList.addAll(listl)
        dataList.addAll(listl)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            val inflater = LayoutInflater.from(parent.context)
            view = inflater.inflate(resource, null)
        }
        val data = getItem(position)
        val textView = view?.findViewById<MaterialTextView>(R.id.tv)
        textView?.text = data.name
        return view!!
    }

    override fun getItem(position: Int): FriendData {
        return dataList[position]
    }

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getFilter(): Filter {
        return titleFilter
    }

    val titleFilter = object : Filter() {
        val lock = Object()
        override fun performFiltering(prefix: CharSequence?): FilterResults {
            val results = FilterResults()
            if (prefix.toString().isNullOrEmpty()) {
                synchronized(lock) {
                    results.values = tempList
                    results.count = tempList.size
                }
            } else {
                val searchStrLowerCase: String = prefix.toString().toLowerCase(Locale.ROOT)
                val newList = tempList.filter {
                    it.name.toLowerCase(Locale.getDefault()).contains(searchStrLowerCase)
                }
                results.values = newList
                results.count = newList.size
            }

            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            dataList.clear()
            dataList.addAll(results?.values as ArrayList<FriendData>)
            notifyDataSetChanged()
        }
    }
}