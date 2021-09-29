package com.development.mymultipleselectionautocompletetextview

import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import com.google.android.material.textview.MaterialTextView

class FriendForSingleSelectionAutoCompletetTextViewAdapter(
    context: Context,
    var resource: Int,
    val list: ArrayList<FriendData>,
    val listener: OnCheckedAutoCompleteItemClicked
) :
    ArrayAdapter<FriendData>(context, resource, list) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            val inflater = LayoutInflater.from(parent.context)
            view = inflater.inflate(resource, null)
        }
        val textView: MaterialTextView = view!!.findViewById(R.id.tv_auto)
        textView.text = list[position].name

        textView.setOnClickListener {
            listener.onIemClicked(
                position,
                true
            )
        }
        return view
    }

    override fun getCount(): Int {
        return if (list.isEmpty())
            0
        else
            list.size
    }

    override fun getItem(position: Int): FriendData? {
        return list[position]
    }
}