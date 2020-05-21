package com.zwb.auto.ui.activity

import android.content.Context
import android.content.Intent
import com.video.subtitle.ui.dialog.ConfigSetDialog
import com.zwb.auto.R
import com.zwb.auto.base.BaseActivity
import com.zwb.auto.bean.BaseOperationBean
import com.zwb.auto.config.ConfigManager
import com.zwb.auto.config.DataConfig
import com.zwb.auto.config.TrainConfig
import com.zwb.auto.ui.dialog.OperationDialog
import com.zwb.auto.ui.service.AccessService
import com.zwb.auto.utils.FloatAvailableUtils
import com.zwb.auto.utils.ServiceAvailableUtils
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.activity_setting.view.*

class SettingActivity : BaseActivity() {

    override fun initListener() {
        iv_back.setOnClickListener {
            finish()
        }
        tv_new_greet.setOnClickListener {
            showConfigDialog(ConfigSetDialog.TYPE_GREET_WORD)
        }
        tv_new_batch_user.setOnClickListener {
            showConfigDialog(ConfigSetDialog.TYPE_BATCH_USER)
        }
        cb_meal.setOnClickListener {
            cb_meal.isChecked != cb_meal.isChecked
            cb_female.isChecked = false
            cb_unknown.isChecked = false
        }
        cb_female.setOnClickListener {
            cb_female.isChecked != cb_female.isChecked
            cb_meal.isChecked = false
            cb_unknown.isChecked = false
        }
        cb_unknown.setOnClickListener {
            cb_unknown.isChecked != cb_unknown.isChecked
            cb_meal.isChecked != false
            cb_female.isChecked = false
        }
    }

    private fun showConfigDialog(type: Int) {
        ConfigSetDialog.newInstance(type)
            .setOnConfirmListener(object : ConfigSetDialog.OnOnConfirmListener {
                override fun onConfirm() {
                    if (type == ConfigSetDialog.TYPE_GREET_WORD) {
                        refreshGreetWordCount()
                    } else {
                        refreshBatchUserCount()
                    }
                }
            })
            .setDimAmout(0.6f)
            .setSize(-1, -1)
            .setShowBottom(true)
            .show(supportFragmentManager)
    }

    override fun layoutId(): Int {
        return R.layout.activity_setting
    }

    override fun initData() {

    }

    override fun initView() {
        initSwitchPermission()
        refreshCount()
        refreshTrainConfig()
        refreshDataConfig()
    }

    private fun refreshDataConfig() {
        ConfigManager.instance.getDataConfig().apply {
            et_operate_count.setText("$operateCount")
            et_message_start.setText("1")
            et_message_end.setText("1")
            et_per_batch_count.setText("$perBatchCount")
            et_wait_start.setText("1")
            et_wait_end.setText("1")
            et_greet_per_count.setText("$perTimeGreetWordCount")
            switch_greet_random.isChecked = greetWordRandom
            when (operateSex) {
                DataConfig.Sex.MALE -> cb_meal.isChecked = true
                DataConfig.Sex.FEMAL -> cb_female.isChecked = true
                DataConfig.Sex.UNKONWN -> cb_unknown.isChecked = true
            }
        }

    }

    private fun refreshTrainConfig() {
        ConfigManager.instance.getTrainConfig().apply {
            et_train_count.setText("$operateCount")
            et_train_comment_count.setText("$commentCount")
            et_train_praise_count.setText("$pariseCount")
            et_train_star_count.setText("$starCount")
            et_train_delivery_count.setText("$deliveryCount")
            et_train_custom_delivery_word.setText(customDeliveryWord)
        }
    }

    override fun onResume() {
        super.onResume()
        switch_assistant.isChecked =
            ServiceAvailableUtils.isServiceAvailable(this, AccessService::class.java)
        switch_float.isChecked = FloatAvailableUtils.checkFloatPermission(this)
    }

    private fun refreshCount() {
        refreshGreetWordCount()
        refreshBatchUserCount()
    }

    private fun refreshBatchUserCount() {
        val batchUserSize = ConfigManager.instance.getBatchUserSize()
        if (batchUserSize > 0) {
            tv_user_count.setTextColor(resources.getColor(R.color.color_blue))
            tv_user_count.text = "${batchUserSize}"
        } else {
            tv_user_count.setTextColor(resources.getColor(R.color.color_grey))
            tv_user_count.text = "未设置"
        }
    }

    private fun refreshGreetWordCount() {
        val greetWordSize = ConfigManager.instance.getGreetWordSize()
        if (greetWordSize > 0) {
            tv_greet_count.setTextColor(resources.getColor(R.color.color_blue))
            tv_greet_count.text = "${greetWordSize}"
        } else {
            tv_greet_count.setTextColor(resources.getColor(R.color.color_grey))
            tv_greet_count.text = "未设置"
        }
    }

    private fun initSwitchPermission() {
        switch_assistant.setOnClickListener {
            ServiceAvailableUtils.checkService(this)
        }
        switch_assistant.setOnCheckedChangeListener { buttonView, isChecked ->

        }
        switch_float.setOnClickListener {
            FloatAvailableUtils.checkFloat(this)
        }
    }

    private fun updateOperateSex(): DataConfig.Sex? {
        var sex: DataConfig.Sex? = null
        if (cb_meal.isChecked) {
            cb_unknown.isChecked = false
            cb_female.isChecked = false
            sex = DataConfig.Sex.MALE
        } else if (cb_female.isChecked) {
            cb_meal.isChecked = false
            cb_unknown.isChecked = false
            sex = DataConfig.Sex.FEMAL
        } else if (cb_unknown.isChecked) {
            cb_meal.isChecked = false
            cb_female.isChecked = false
            sex = DataConfig.Sex.UNKONWN
        }
        return sex
    }

    private fun getSex(): DataConfig.Sex? {
        var sex: DataConfig.Sex? = null
        if (cb_meal.isChecked) {
            sex = DataConfig.Sex.MALE
        } else if (cb_female.isChecked) {
            sex = DataConfig.Sex.FEMAL
        } else if (cb_unknown.isChecked) {
            sex = DataConfig.Sex.UNKONWN
        }
        return sex
    }

    override fun onStop() {
        super.onStop()
        saveTrainConfig()
        saveDataConfig()
    }

    private fun saveDataConfig() {
        DataConfig(
            et_operate_count.text.toString().trim().toIntOrNull() ?: 0,
            1000,
            et_per_batch_count.text.toString().trim().toIntOrNull() ?: 0,
            1000,
            et_greet_per_count.text.toString().trim().toIntOrNull() ?: 0,
            switch_greet_random.isChecked,
            getSex()

        ).let {
            ConfigManager.instance.saveTrainConfig(it)
        }
    }

    private fun saveTrainConfig() {
        TrainConfig(
            et_train_count.text.toString().trim().toIntOrNull() ?: 0,
            1000,
            et_train_comment_count.text.toString().trim().toIntOrNull() ?: 0,
            et_train_praise_count.text.toString().trim().toIntOrNull() ?: 0,
            et_train_star_count.text.toString().trim().toIntOrNull() ?: 0,
            et_train_delivery_count.text.toString().trim().toIntOrNull() ?: 0,
            et_train_custom_delivery_word.text.toString().trim()
        ).let {
            ConfigManager.instance.saveTrainConfig(it)
        }
    }


    companion object {
        fun launch(context: Context) {
            val intent = Intent(context, SettingActivity::class.java)
            context.startActivity(intent)
        }
    }
}
