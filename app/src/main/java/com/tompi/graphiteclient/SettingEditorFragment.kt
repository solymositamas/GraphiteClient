package com.tompi.graphiteclient

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.tompi.graphiteclient.SettingEditorActivity.Companion.SETTING_ID
import com.tompi.graphiteclient.data.GenericServer
import com.tompi.graphiteclient.data.GraphiteServer
import com.tompi.graphiteclient.data.GraphiteSettingItem
import com.tompi.graphiteclient.data.GraphiteSettings
import org.slf4j.LoggerFactory

class SettingEditorFragment : Fragment() {
    private lateinit var toolbar: Toolbar
    private lateinit var settingsId: String

    lateinit var server1SecureSwitch: SwitchCompat
    lateinit var server2SecureSwitch: SwitchCompat
    lateinit var server1AdressEdit: EditText
    lateinit var server2AdressEdit: EditText
    lateinit var server1PortEdit: EditText
    lateinit var server2PortEdit: EditText
    lateinit var targetEditor: EditText
    lateinit var urlFeedback: TextView

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

        server1SecureSwitch = view.findViewById(R.id.server1_is_secure)
        server2SecureSwitch = view.findViewById(R.id.server2_is_secure)
        server1AdressEdit = view.findViewById(R.id.edit_server1)
        server2AdressEdit = view.findViewById(R.id.edit_server2)
        server1PortEdit = view.findViewById(R.id.edit_port1)
        server2PortEdit = view.findViewById(R.id.edit_port2)
        targetEditor = view.findViewById(R.id.edit_target)
        urlFeedback = view.findViewById(R.id.url_feedback)

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
        refreshUrlFeedback()
        return view
    }

    fun setupEditor(settingItem: GraphiteSettingItem?) {
        if(settingItem == null) {
            logger.debug("new item")
            //TODO: create empty
        } else {
            logger.debug( "item: $settingItem")
            val serverEntries = settingItem.servers
            if(serverEntries.size > 0) {
                setServerUi(serverEntries.elementAt(0), server1SecureSwitch, server1AdressEdit, server1PortEdit)

            }
            if(serverEntries.size > 1) {
                setServerUi(serverEntries.elementAt(1), server2SecureSwitch, server2AdressEdit, server2PortEdit)
            }
            targetEditor.setText(settingItem.target)
        }
    }

    fun refreshUrlFeedback() {
        val data = collectData()
        urlFeedback.text = data.servers.joinToString(separator = "\n") { it.url }
    }

    private fun setServerUi(data: GraphiteServer, switch: SwitchCompat, serverEditText: EditText, portEditText: EditText) {
        switch.isChecked = data.isSecure
        serverEditText.setText(data.server)
        portEditText.setText(data.port)
    }

    private fun collectData(): GraphiteSettingItem {
        val server1 = GenericServer(server1SecureSwitch.isChecked, server1AdressEdit.text.toString(), server1PortEdit.text.toString())
        val server2 = GenericServer(server2SecureSwitch.isChecked, server2AdressEdit.text.toString(), server2PortEdit.text.toString())
        return GraphiteSettingItem(listOf(server1, server2), targetEditor.text.toString())
    }
}

