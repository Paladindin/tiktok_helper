package com.zwb.auto.ui.fragment

import androidx.recyclerview.widget.GridLayoutManager
import com.video.subtitle.ui.dialog.ConfirmDialog
import com.zwb.auto.App
import com.zwb.auto.config.Constants
import com.zwb.auto.R
import com.zwb.auto.base.BaseFragment
import com.zwb.auto.bean.*
import com.zwb.auto.ui.activity.SettingActivity
import com.zwb.auto.ui.adapter.FunctionTypeAdapter
import com.zwb.auto.ui.adapter.SpaceDecoration
import com.zwb.auto.ui.dialog.OperationDialog
import com.zwb.auto.ui.service.AccessService
import com.zwb.auto.utils.ServiceAvailableUtils
import com.zwb.auto.utils.ToastUtils
import com.zwb.auto.utils.UiUtils
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Description:
 * Author: zwb
 * Date: 2020/4/6
 */
class HomeFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {
        iv_setting.setOnClickListener {
            SettingActivity.launch(getContext2())
        }
        initOperationRecyclerView()
    }

    override fun lazyLoad() {
    }

    private fun initOperationRecyclerView() {
        rv_content.layoutManager = GridLayoutManager(getContext2(), 2)
        rv_content.addItemDecoration(SpaceDecoration(UiUtils.dp2px(getContext2(), 16)))
        val adapter = FunctionTypeAdapter(FunctionType.values().toList())
        rv_content.adapter = adapter
        adapter.setOnItemClickListener { _, view, position ->
            adapter.getItem(position)?.let {
                onItemClick(it)
            }

        }
    }

    private fun onItemClick(item: FunctionType) {
        if (!ServiceAvailableUtils.isServiceAvailable(getContext2(), AccessService::class.java)) {
            ToastUtils.showShort(getContext2(), "请先到设置中心打开辅助权限")
            ServiceAvailableUtils.checkService(getContext2())
            return
        }
        App.getInstance().setFunctionType(item)
        val listOperation = ArrayList<BaseOperationBean>();

        when (item) {
            FunctionType.ASSIGN -> {
                listOperation.add(
                    OperationBean(
                        OperationDetail.OPERATE_TARGET,
                        OperationDetail.OPERATE_TARGET.type()[0]
                    )
                )
                listOperation.add(
                    OperationBean(
                        OperationDetail.OPERATE_AREA,
                        OperationDetail.OPERATE_AREA.type()[0]
                    )
                )
                listOperation.add(
                    OperationBean(
                        OperationDetail.OPERATE_TYPE_COMMON,
                        OperationDetail.OPERATE_TYPE_COMMON.type()[0]
                    )
                )
            }
            FunctionType.RECOMMEND -> {
                listOperation.add(
                    OperationBean(
                        OperationDetail.OPERATE_AREA,
                        OperationDetail.OPERATE_AREA.type()[0]
                    )
                )
                listOperation.add(
                    OperationBean(
                        OperationDetail.OPERATE_TYPE_COMMON,
                        OperationDetail.OPERATE_TYPE_COMMON.type()[0]
                    )
                )
                listOperation.add(EditTextBean("单用户操作数量", "请输入单个首页用户可操作抖友数", 0))
            }
            FunctionType.KEYWORD -> {
                listOperation.add(
                    OperationBean(
                        OperationDetail.OPERATE_TYPE_COMMON,
                        OperationDetail.OPERATE_TYPE_COMMON.type()[0]
                    )
                )
                listOperation.add(EditTextBean("单关键词操作数量", "请输入单个关键词可操作抖友数", 0))
            }
            FunctionType.ADDRESS_BOOK -> {
                listOperation.add(
                    OperationBean(
                        OperationDetail.OPERATE_TYPE_COMMON,
                        OperationDetail.OPERATE_TYPE_COMMON.type()[0]
                    )
                )
            }
            FunctionType.COMMENT -> {
                listOperation.add(
                    OperationBean(
                        OperationDetail.OPERATE_TYPE_COMMON,
                        OperationDetail.OPERATE_TYPE_COMMON.type()[0]
                    )
                )
            }
            FunctionType.LIVE -> {
                listOperation.add(
                    OperationBean(
                        OperationDetail.OPERATE_TYPE_COMMON,
                        OperationDetail.OPERATE_TYPE_COMMON.type()[0]
                    )
                )
            }
            FunctionType.COMMENT_AREA -> {
                listOperation.add(
                    OperationBean(
                        OperationDetail.OPERATE_AREA_COMMENT,
                        OperationDetail.OPERATE_AREA_COMMENT.type()[0]
                    )
                )
            }
            FunctionType.FRIENDS_LIST -> {
                listOperation.add(
                    OperationBean(
                        OperationDetail.OPERATE_TYPE_FRIENDS_LIST,
                        OperationDetail.OPERATE_TYPE_FRIENDS_LIST.type()[0]
                    )
                )
            }
            FunctionType.MESSAGES -> {
                ConfirmDialog.newInstance(item, null)
                    .setOnButtonClickListener(object :
                        ConfirmDialog.OnButtonClickListener {
                        override fun onAgree(
                            operationType: OperationDetail.TYPE?,
                            operationTarget: OperationDetail.TYPE?,
                            operationCount: Int
                        ) {
                            App.getInstance().setOperationType(operationType)
                            App.getInstance().holdApp(Constants.DOUYIN_PACKAGE_NAME)
                        }

                        override fun onDisagree() {
                        }

                    })
                    .setAnimStyle(R.style.normalAnimationDialog)
                    .setMargin(50)
                    .setDimAmout(0.6f)
                    .showAllowingStateLoss(fragmentManager)
                return
            }
            FunctionType.TRAIN -> {
                ConfirmDialog.newInstance(item, null)
                    .setOnButtonClickListener(object :
                        ConfirmDialog.OnButtonClickListener {
                        override fun onAgree(
                            operationType: OperationDetail.TYPE?,
                            operationTarget: OperationDetail.TYPE?,
                            operationCount: Int
                        ) {
                            App.getInstance().setOperationType(operationType)
                            App.getInstance().holdApp(Constants.DOUYIN_PACKAGE_NAME)
                        }

                        override fun onDisagree() {
                        }

                    })
                    .setAnimStyle(R.style.normalAnimationDialog)
                    .setMargin(50)
                    .setDimAmout(0.6f)
                    .showAllowingStateLoss(fragmentManager)
                return
            }
            FunctionType.PARISE -> {
                listOperation.add(
                    OperationBean(
                        OperationDetail.OPERATE_AREA_PARISE,
                        OperationDetail.OPERATE_AREA_PARISE.type()[0]
                    )
                )
            }
            FunctionType.PRIVATE_MESSAGES -> {
                ConfirmDialog.newInstance(item, null)
                    .setOnButtonClickListener(object :
                        ConfirmDialog.OnButtonClickListener {
                        override fun onAgree(
                            operationType: OperationDetail.TYPE?,
                            operationTarget: OperationDetail.TYPE?,
                            operationCount: Int
                        ) {
                            App.getInstance().setOperationType(operationType)
                            App.getInstance().holdApp(Constants.DOUYIN_PACKAGE_NAME)
                        }

                        override fun onDisagree() {
                        }

                    })
                    .setAnimStyle(R.style.normalAnimationDialog)
                    .setMargin(50)
                    .setDimAmout(0.6f)
                    .showAllowingStateLoss(fragmentManager)
                return
            }
            FunctionType.SAME_CITY_USER -> {
                listOperation.add(
                    OperationBean(
                        OperationDetail.OPERATE_TYPE_COMMON,
                        OperationDetail.OPERATE_TYPE_COMMON.type()[0]
                    )
                )
            }
            FunctionType.BATCH_STAR -> {
                listOperation.add(
                    OperationBean(
                        OperationDetail.OPERATE_TYPE_BATCH_STAR,
                        OperationDetail.OPERATE_TYPE_BATCH_STAR.type()[0]
                    )
                )
            }
        }
        OperationDialog.newInstance(item, listOperation)
            .setOnConfirmClickListener(object : OperationDialog.OnConfirmClickListener {
                override fun onConfirm(listOperationBen: List<BaseOperationBean>) {
                    ConfirmDialog.newInstance(item, listOperationBen)
                        .setOnButtonClickListener(object :
                            ConfirmDialog.OnButtonClickListener {
                            override fun onAgree(
                                operationType: OperationDetail.TYPE?,
                                operationTarget: OperationDetail.TYPE?,
                                operationCount: Int
                            ) {
                                App.getInstance().setOperationType(operationType)
                                App.getInstance().setOperationTarget(operationTarget)
                                App.getInstance().setOperationCount(operationCount)
                                App.getInstance().holdApp(Constants.DOUYIN_PACKAGE_NAME)
                            }

                            override fun onDisagree() {
                            }

                        })
                        .setMargin(50)
                        .setDimAmout(0.6f)
                        .showAllowingStateLoss(fragmentManager)
                }
            })
            .setAnimStyle(R.style.normalAnimationDialog)
            .setMargin(50)
            .setDimAmout(0.6f)
            .showAllowingStateLoss(fragmentManager)
    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

}