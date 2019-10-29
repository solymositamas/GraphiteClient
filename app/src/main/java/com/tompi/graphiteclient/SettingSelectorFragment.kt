package com.tompi.graphiteclient

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.tompi.graphiteclient.data.GraphiteSettingItem
import com.tompi.graphiteclient.data.GraphiteSettings

import kotlinx.android.synthetic.main.fragment_settingselector_listitem.view.*
import kotlinx.android.synthetic.main.graphite_app_widget.view.*

class SettingSelectorFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 1

    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settingselector_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MySettingselectorRecyclerViewAdapter(GraphiteSettings.getMap(), listener)
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListItemClicked(id: String, item: GraphiteSettingItem?)
        fun onListItemEditClicked(id: String, item: GraphiteSettingItem?)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            SettingSelectorFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}


class MySettingselectorRecyclerViewAdapter(
    private val mValueMap:  Map<String, GraphiteSettingItem>,
    private val listClickListener: SettingSelectorFragment.OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MySettingselectorRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
//            val item = v.tag as GraphiteSettingItem
            val item = v.tag as Map.Entry<String, GraphiteSettingItem>
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            listClickListener?.onListItemClicked(item.key, item.value)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_settingselector_listitem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val elementAt = mValueMap.entries.elementAt(position)
//        holder.idView.text = elementAt.value.urlList.first()
        holder.id.text = elementAt.key

        holder.description.text = elementAt.value.servers.joinToString(separator = "\n")
        holder.settingsButton.setOnClickListener {
            listClickListener?.onListItemEditClicked(elementAt.key, elementAt.value)
        }
        with(holder.view) {
            tag = elementAt
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValueMap.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val id: TextView = view.item_number
        val description: TextView = view.content
        val settingsButton: ImageButton = view.list_item_edit

        override fun toString(): String {
            return super.toString() + " '" + description.text + "'"
        }
    }
}
