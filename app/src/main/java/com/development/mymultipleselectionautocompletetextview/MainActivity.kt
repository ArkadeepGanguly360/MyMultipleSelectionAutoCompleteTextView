package com.development.mymultipleselectionautocompletetextview

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import com.development.mymultipleselectionautocompletetextview.databinding.ActivityMainBinding
import com.google.android.material.textfield.MaterialAutoCompleteTextView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var friendList = ArrayList<FriendData>()

   /*Todo For Multi Selection*/
    var selectedFriends = ObservableField("")
    var selectedFriendList = ArrayList<FriendData>()
    private lateinit var friendAutoCompletetTextViewAdapter: FriendForMultiSelectionAutoCompletetTextViewAdapter

    /*Todo For Search*/
    var searchedSelectedFriends = ObservableField("")
    private lateinit var searchFriendAutoCompleteAdapter: SearchFriendAutoCompleteAdapter

    /*Todo For Single Selection*/
    var selectedSingleFriends = ObservableField("")
    private lateinit var friendForSingleSelectionAutoCompletetTextViewAdapter: FriendForSingleSelectionAutoCompletetTextViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.fragment = this

        getFriendList()
        initMultiSelectFriendAdapter()
        initSearchFriendAdapter()
        initSingleSelectFriendAdapter()
    }

    private fun getFriendList() {
        friendList.add(
            FriendData(
                "1",
                "Arkadeep"
            )
        )
        friendList.add(
            FriendData(
                "2",
                "Dipanwita"
            )
        )
        friendList.add(
            FriendData(
                "3",
                "Atrijit"
            )
        )
        friendList.add(
            FriendData(
                "4",
                "Rajat"
            )
        )
        friendList.add(
            FriendData(
                "5",
                "Arghya"
            )
        )
    }


    /*Todo For Multi Selection*/
    private fun initMultiSelectFriendAdapter() {
        friendAutoCompletetTextViewAdapter = FriendForMultiSelectionAutoCompletetTextViewAdapter(
            this,
            R.layout.autocomplete_view_list_item,
            friendList,
            object : OnCheckedAutoCompleteItemClicked {
                override fun onIemClicked(position: Int?, icChecked: Boolean?) {
                    if (icChecked!!)
                        selectedFriendList.add(friendList.get(position!!))
                    else
                        selectedFriendList.remove(friendList.get(position!!))

                    selectedFriends.set("")

                    for (i in selectedFriendList) {
                        if (selectedFriends.get() != "") {
                            selectedFriends.set(selectedFriends.get() + ", " + i.name)
                        } else {
                            selectedFriends.set(selectedFriends.get() + " " + i.name)
                        }
                    }
                }
            })

        binding.tvAutoFriend.dropDownVerticalOffset = 20
        binding.tvAutoFriend.setDropDownBackgroundDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.rounded_less_corner_white_bg
            )
        )
        binding.tvAutoFriend?.setAdapter(friendAutoCompletetTextViewAdapter)
        binding.tvAutoFriend?.setOnTouchListener { v, event ->
            if ((v as MaterialAutoCompleteTextView).text.toString() != "")
                friendAutoCompletetTextViewAdapter?.filter?.filter(null)
                binding.tvAutoFriend?.showDropDown()
                false
            }
        }

    /*Todo For Single Selection*/
    private fun initSingleSelectFriendAdapter() {
        friendForSingleSelectionAutoCompletetTextViewAdapter = FriendForSingleSelectionAutoCompletetTextViewAdapter(
            this,
            R.layout.autocomplete_new_view_list_item,
            friendList,
            object : OnCheckedAutoCompleteItemClicked {
                override fun onIemClicked(position: Int?, icChecked: Boolean?) {
                    selectedSingleFriends.set(friendList.get(position!!).name)
                    binding.tvSingleAutoFriend.dismissDropDown()
                }
            })

        binding.tvSingleAutoFriend.dropDownVerticalOffset = 20
        binding.tvSingleAutoFriend.setDropDownBackgroundDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.rounded_less_corner_white_bg
            )
        )
        binding.tvSingleAutoFriend?.setAdapter(friendForSingleSelectionAutoCompletetTextViewAdapter)
        binding.tvSingleAutoFriend?.setOnTouchListener { v, event ->
            if ((v as MaterialAutoCompleteTextView).text.toString() != "")
                friendForSingleSelectionAutoCompletetTextViewAdapter?.filter?.filter(null)
            binding.tvSingleAutoFriend?.showDropDown()
            false
        }
    }

    /*Todo For Search*/
    private fun initSearchFriendAdapter() {
        searchFriendAutoCompleteAdapter =
            SearchFriendAutoCompleteAdapter(
                this,
                R.layout.search_autocomplete_view_list_item,
                friendList
            )

        binding.tvSearchAutoFriend.dropDownVerticalOffset = 20
        binding.tvSearchAutoFriend.setDropDownBackgroundDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.rounded_less_corner_white_bg
            )
        )
        binding.tvSearchAutoFriend?.setAdapter(searchFriendAutoCompleteAdapter)
        binding.tvSearchAutoFriend.onClickAutoCompleteItem { it, position ->
            val data = it as FriendData
            searchedSelectedFriends.set(data.name)
            hideKeyBoard(binding.tvSearchAutoFriend)
        }
    }

    /*Todo For Search*/
    private fun AppCompatAutoCompleteTextView.onClickAutoCompleteItem(listener: (Any?, Int) -> Unit) {
        this.setOnItemClickListener { parent: AdapterView<*>?,
                                      view: View?,
                                      position: Int,
                                      id: Long ->

            val item = parent?.getItemAtPosition(position)

            when (item) {
                is FriendData -> {
                    this.setText(item.name)
                }

            }
            listener(item, position)
            this.setSelection(this.text.length)
        }
    }

    /*Todo For Search*/
    private fun hideKeyBoard(et: EditText?) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(et?.windowToken, 0);
    }
}