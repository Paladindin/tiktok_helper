package com.tiktok.auto.ui.dialog

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import com.tiktok.auto.R
import com.tiktok.auto.base.BaseDialog
import com.tiktok.auto.base.ViewHolder
import com.tiktok.auto.config.ConfigManager

/**
 * Description:
 * Author: zwb
 * Date: 2020/3/10
 */
class ConfigSetDialog : BaseDialog() {
    private lateinit var tvConfirm: TextView
    private lateinit var etConfig: EditText

    override fun setUpLayoutId(): Int {
        return R.layout.dialog_config_set
    }

    override fun convertView(holder: ViewHolder, dialog: BaseDialog) {
        etConfig = holder.getView<EditText>(R.id.et_config)
        tvConfirm = holder.getView<TextView>(R.id.tv_confirm)
        initEtConfig(etConfig)
        tvConfirm.setOnClickListener {
            etConfig.text.toString().takeIf { it.isNotEmpty() }?.let {
                if (type == TYPE_GREET_WORD) {
                    ConfigManager.instance.saveGreetWordStr(it)
                } else {
                    ConfigManager.instance.saveBatchUserStr(it)
                }
            }
            dismiss()
            mListener?.onConfirm()

        }
    }

    private fun initEtConfig(etConfig: EditText) {
        if (type == TYPE_GREET_WORD) {
            etConfig.hint = "每条招呼语以%隔开"
            ConfigManager.instance.getGreetWordStr().takeIf { it.isNotEmpty() }
                .let(etConfig::setText)
        } else {
            etConfig.hint = "每个关键词以%隔开"
            ConfigManager.instance.getBatchUserStr().takeIf { it.isNotEmpty() }
                .let(etConfig::setText)
        }
        etConfig.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                tvConfirm.isEnabled = s.isNotEmpty() == true
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    override fun onKeyBack() {
        dismiss()
    }

    companion object {
        const val TYPE_GREET_WORD = 0x100
        const val TYPE_BATCH_USER = 0x200
        var type: Int = TYPE_GREET_WORD
        fun newInstance(type: Int): ConfigSetDialog {
            Companion.type = type
            return ConfigSetDialog()
        }
    }


    private var mListener: OnOnConfirmListener? = null

    fun setOnConfirmListener(listener: OnOnConfirmListener): BaseDialog {
        mListener = listener
        return this
    }

    interface OnOnConfirmListener {
        fun onConfirm()
    }
}