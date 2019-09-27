package com.tompi.graphiteclient

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import androidx.appcompat.widget.Toolbar
import com.tompi.graphiteclient.data.GraphiteSettingItem
import com.tompi.graphiteclient.data.GraphiteSettings

import kotlinx.android.synthetic.main.fragment_settingselector_listitem.view.*
import kotlinx.android.synthetic.main.graphite_app_widget.view.*

class SettingEditorFragment : Fragment() {
    private lateinit var toolbar: Toolbar
//    private lateinit var settingsId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setting_editor, container, false)


        toolbar = view.findViewById(R.id.toolbar)
        toolbar.apply {
            inflateMenu(R.menu.menu_editor)
            setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.action_cancel -> {
                        val returnIntent = Intent()
                        requireActivity().setResult(Activity.RESULT_CANCELED, returnIntent)
                        requireActivity().finish()
                        true
                    }
                    R.id.action_done -> {
                        val returnIntent = Intent()
                        returnIntent.putExtra("settingsId", "asd")
                        requireActivity().setResult(Activity.RESULT_OK, returnIntent)
                        requireActivity().finish()
                        true
                    }
                    else -> {
                        super.onOptionsItemSelected(it)
                    }
                }
            }
        }

        return view
    }



}

