package com.zwb.auto.ui.activity

import android.content.Intent
import android.os.Handler
import android.util.SparseArray
import android.view.Gravity
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.enums.ShowPattern
import com.lzf.easyfloat.enums.SidePattern
import com.lzf.easyfloat.interfaces.OnInvokeView
import com.zwb.auto.App
import com.zwb.auto.R
import com.zwb.auto.base.BaseActivity
import com.zwb.auto.base.BaseFragment
import com.zwb.auto.ui.fragment.AssistantFragment
import com.zwb.auto.ui.fragment.HomeFragment
import com.zwb.auto.ui.fragment.PersonalFragment
import com.zwb.auto.ui.fragment.RelationshipFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {
    private lateinit var mFragmentMap: SparseArray<BaseFragment>

    override fun layoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
        initAppFloat()
    }

    private fun initAppFloat() {

        EasyFloat.with(this)
            .setShowPattern(ShowPattern.ALL_TIME)
            .setSidePattern(SidePattern.RESULT_SIDE)
            .setGravity(Gravity.CENTER)
            .setLayout(R.layout.float_window, OnInvokeView {
                it.findViewById<TextView>(R.id.tv_back).setOnClickListener {
                    EasyFloat.hideAppFloat()
                    App.getInstance().apply {
                        it.findViewById<TextView>(R.id.tv_start)?.setText("开始")
                        setStartRun(false)
                        cancelCommand()
                        Handler().postDelayed({
                            getCurrentActivity()?.moveTaskToFront()
                        }, 500)
                    }
                }
                it.findViewById<TextView>(R.id.tv_start).let {tvStart->
                    tvStart.setOnClickListener {
                        App.getInstance().apply {
                            setStartRun(!getStartRun())
                            if (getStartRun()){
                                tvStart.setText("结束")
                                doCommand()
                            }else {
                                tvStart.setText("开始")
                                cancelCommand()
                            }
                        }
                    }
                }
            })
            .show()
        Handler().postDelayed({
            EasyFloat.hideAppFloat()
        },200)

    }

    override fun initView() {
        mFragmentMap = SparseArray()
        showFragment(R.id.rb_home)
    }

    override fun initListener() {
        initRadioButton()
    }

    private fun initRadioButton() {
        rg_tab.setOnCheckedChangeListener { rg, idRes ->
            showFragment(idRes)
        }
    }

    private fun getFragment(index: Int): BaseFragment {
        return when (index) {
            R.id.rb_home -> HomeFragment.newInstance()
            R.id.rb_assistant -> AssistantFragment.newInstance()
            R.id.rb_relationship -> RelationshipFragment.newInstance()
            R.id.rb_personal -> PersonalFragment.newInstance()
            else -> HomeFragment.newInstance()
        }
    }


    private fun showFragment(idRes: Int) {
        var fragment = mFragmentMap.get(idRes)
        if (fragment == null) {
            fragment = getFragment(idRes)
            mFragmentMap.put(idRes, fragment)
        }
        mFragmentMap.takeIf { it.size() > 0 }?.let {
            for (i in 0 until mFragmentMap.size()) {
                val baseFragment = mFragmentMap.valueAt(i)
                if (baseFragment.isVisible) {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.hide(baseFragment).commit()
                }
            }
        }
        val transaction = supportFragmentManager.beginTransaction()
        if (!fragment.isAdded()) { //  判断传入的fragment是否已经被add()过
            transaction.add(R.id.fl_fragment_container, fragment).show(fragment).commit();
        } else {
            transaction.show(fragment).commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EasyFloat.dismissAppFloat()
    }

}
