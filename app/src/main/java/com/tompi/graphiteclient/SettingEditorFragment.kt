package com.tompi.graphiteclient

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import com.tompi.graphiteclient.SettingEditorActivity.Companion.SETTING_ID
import com.tompi.graphiteclient.data.GraphiteSettingItem
import com.tompi.graphiteclient.data.GraphiteSettings

import org.slf4j.LoggerFactory

class SettingEditorFragment : Fragment() {
    private lateinit var toolbar: Toolbar
    private lateinit var settingsId: String


    companion object {
        private val logger = LoggerFactory.getLogger("SettingEditorFragment")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setting_editor, container, false)

        settingsId = requireActivity().intent.getStringExtra(SETTING_ID) ?: "noope"
        val settingItem = GraphiteSettings.getSettingsByID(settingsId)
        logger.debug("id: $settingsId - settings: ${settingItem.toString()}")

        targetEditor = view.findViewById(R.id.edit_target)

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

        setupEditor(settingItem)
        return view
    }

    lateinit var targetEditor: EditText
    fun setupEditor(settingItem: GraphiteSettingItem?) {
        if(settingItem == null) {
            //TODO: create empty
        } else {
            targetEditor.setText(settingItem.target)

        }
    }

}

