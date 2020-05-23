package com.zwb.auto.utils

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast
import com.zwb.auto.App
import com.zwb.auto.bean.FunctionType
import com.zwb.auto.bean.OperationDetail
import com.zwb.auto.config.ConfigManager
import com.zwb.auto.config.Constants
import com.zwb.auto.ui.service.AccessService
import kotlinx.coroutines.*


/**
 * Description:
 * Author: zwb
 * Date: 2020/4/12
 */
object CommandUtils {

    private var job: Job? = null
    private var service: AccessService? = null

    init {
        if (service == null)
            service = App.getInstance().getService()
    }

    private fun isStartRun(): Boolean {
        return App.getInstance().getStartRun()
    }

    fun init(service: AccessService?) {
        this.service = service
    }

    fun doCommand(
        functionType: FunctionType?,
        operationType: OperationDetail.TYPE?,
        operationTarget: OperationDetail.TYPE?,
        operationCount: Int
    ) {
        if (service == null) {
            ToastUtils.showShort(App.getInstance(), "服务为null,请前往设置中心重新开启无障碍辅助权限")
            return
        }
        if (!isStartRun()) {
            return
        }
        job = GlobalScope.launch(Dispatchers.IO) {
            when (functionType) {
                FunctionType.ASSIGN -> {
                    assignCommand(operationType, operationTarget)
                }
                FunctionType.RECOMMEND -> {
                    recommandCommand(operationType, operationTarget, operationCount)
                }
                FunctionType.KEYWORD -> {
                    keywordCommand(operationType, operationCount)
                }
                FunctionType.ADDRESS_BOOK -> {
                    addressBookCommand(operationType)
                }
                FunctionType.COMMENT -> {
                    commentCommand(operationType)
                }
                FunctionType.LIVE -> {
                    liveCommand(operationType)
                }
                FunctionType.COMMENT_AREA -> {
                    commentAreaCommand(operationType)
                }
                FunctionType.FRIENDS_LIST -> {
                    friendsListCommand(operationType)
                }
                FunctionType.MESSAGES -> {
                    messageCommand()
                }
                FunctionType.TRAIN -> {
                    trainAccountCommand()
                }
                FunctionType.PARISE -> {
                    pariseCommand(operationType)
                }
                FunctionType.PRIVATE_MESSAGES -> {
                    privateMessagesCommand()
                }
                FunctionType.SAME_CITY_USER -> {
                    sameCityUserCommand(operationType)
                }
                FunctionType.BATCH_STAR -> {
                    batchStarCommand(operationType)
                }
                FunctionType.RANDOM -> {
                    randomCommand(OperationDetail.TYPE.MESSAGE, operationTarget)
                }
            }
        }
        job?.start()
    }

    fun cancelJob() {
        job?.cancel()
    }

    /**
     * 个人主页点赞 + 关注
     */
    private suspend fun starAndSendMessage(isStar: Boolean, isMessage: Boolean) {
        if (isStar) {
            var startText = ServiceUtils.findViewByContainsText(service, "回关")
            if (startText.isNullOrEmpty()) {
                startText = ServiceUtils.findViewByContainsText(service, "关注")
            }
            if (!startText.isNullOrEmpty()) {
                ServiceUtils.clickView(startText[0])
                sleep(2000)
            }
            val cancelButtonList = ServiceUtils.findViewByEqualsText(service, "取消")
            if (!cancelButtonList.isNullOrEmpty()) {
                if (ServiceUtils.clickView(cancelButtonList[0]))
                    sleep(1000)
            }

        }
        if (isMessage) {
            val moreButton =
                ServiceUtils.findViewByFirstEqualsContentDescription(service, "更多")
            Log.e("zwb", "moreButton " + (moreButton == null))
            val b = ServiceUtils.clickView(moreButton)
            Log.e("zwb", "click moreButton $b")
            sleep(2000)
            val messges = ServiceUtils.findViewByEqualsText(service, "发私信")
            messges?.let {
                ServiceUtils.clickView(it[0])
                sleep(1800)
            }
            val editText =
                ServiceUtils.findViewByFirstClassName(service, Constants.CLASS_NAME_ET)
            editText?.let {
                ServiceUtils.setText(it, ConfigManager.instance.getGreetWord())
                sleep(1500)
            }

            val send = ServiceUtils.findViewByFirstEqualsContentDescription(service, "发送")
            send?.let {
                ServiceUtils.clickView(it)
                sleep(1500)
            }

            ServiceUtils.goBack(service)
            sleep(2000)
        }
    }

    private suspend fun sleep(millis: Long = 1000) {
        delay(millis)
    }

    private suspend fun assignCommand(
        operationType: OperationDetail.TYPE?,
        operationTarget: OperationDetail.TYPE?
    ) {
        when (operationType) {
            OperationDetail.TYPE.STAR -> assignStar(operationTarget)
            OperationDetail.TYPE.MESSAGE -> assignSendMessage(operationTarget)
            OperationDetail.TYPE.STAR_AND_MESSAGE -> assignStarAndSendMessage(
                operationTarget,
                true,
                true
            )
        }
    }

    /**
     * 给指定用户或我的粉丝或者关注 点击关注(在列表中点赞)
     */
    suspend fun assignStar(operationTarget: OperationDetail.TYPE?, random: Boolean = false) {
        val rvList =
            ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_RV)
        if (rvList.isNullOrEmpty() || rvList.size < 2) return
        val rv: AccessibilityNodeInfo
        if (operationTarget == OperationDetail.TYPE.FANS_LIST) {
            rv = rvList[1]
        } else {
            rv = rvList[0]
        }
        if (rv != null) {
            Log.e("zwb", "getChildCount " + rv.childCount)
            var indexList = ArrayList<Int>()
            for (i in 0 until rv.childCount) {
                indexList.add(i)
            }
            if (random) {
                val randomList = indexList.shuffled()
                indexList.clear()
                indexList.addAll(randomList)
            }
            for (i in indexList) {
                val child = rv.getChild(i) ?: continue
                var listStar = ServiceUtils.findViewByContainsText(child, "关注")
                if (listStar.isNullOrEmpty()) {
                    listStar = ServiceUtils.findViewByContainsText(child, "回关")
                }
                if (!listStar.isNullOrEmpty()) {
                    ServiceUtils.clickView(listStar[0])
                    sleep(1800)
                } else if (i != rv.childCount - 1) {
                    continue
                }
                if (i == rv.childCount - 1) {
                    rv.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
                    sleep(1500)
                    assignStar(operationTarget)
                }
            }
        }
    }

    /**
     * 给指定用户的粉丝或者关注发送消息
     */
    suspend fun assignSendMessage(operationTarget: OperationDetail.TYPE?, random: Boolean = false) {
        assignStarAndSendMessage(operationTarget, false, true,random)
    }

    /**
     * 给指定用户的粉丝或者关注发送消息
     */
    suspend fun assignStarAndSendMessage(
        operationTarget: OperationDetail.TYPE?,
        isStar: Boolean,
        isMessage: Boolean,
        random: Boolean = false
    ) {
        val rvList =
            ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_RV)
        if (rvList.isNullOrEmpty() || rvList.size < 2) return
        var rv: AccessibilityNodeInfo
        if (operationTarget == OperationDetail.TYPE.FANS_LIST) {
            rv = rvList[1]
        } else {
            rv = rvList[0]
        }
        rv?.let {
            Log.e("zwb", "getChildCount " + it.childCount)
            val indexList = ArrayList<Int>()
            for (i in 0 until it.childCount) {
                indexList.add(i)
            }
            if (random) {
                val randomList = indexList.shuffled()
                indexList.clear()
                indexList.addAll(randomList)
            }
            for (i in indexList) {
                val child = it.getChild(i)
                Log.e("zwb", "Child $i is null" + (child == null))
                if (child == null || !child.isClickable) continue
//                child.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                ServiceUtils.clickView(child)
                sleep(1200)

                starAndSendMessage(isStar, isMessage)

                ServiceUtils.goBack(service)
                sleep(1000)

                if (i == it.childCount - 1) {
                    sleep(500)
                    it.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
                    sleep(1000)
                    assignSendMessage(operationTarget)
                }
            }
        }
    }


    private suspend fun recommandCommand(
        operationType: OperationDetail.TYPE?,
        operationTarget: OperationDetail.TYPE?,
        operationCount: Int
    ) {
        when (operationType) {
            OperationDetail.TYPE.STAR -> recommendStar(operationCount, operationTarget)
            OperationDetail.TYPE.MESSAGE -> recommendMessage(operationCount, operationTarget)
            OperationDetail.TYPE.STAR_AND_MESSAGE -> recommendStarAndSendMessage(
                operationCount,
                operationTarget,
                true,
                true
            )
        }
    }

    suspend fun recommendStar(
        operationCount: Int,
        operationTarget: OperationDetail.TYPE?
    ) {
        recommendStarAndSendMessage(operationCount, operationTarget, true, false)
    }

    /**
     * 首页推荐好友关注
     */
    suspend fun recommendMessage(
        operationCount: Int,
        operationTarget: OperationDetail.TYPE?
    ) {
        recommendStarAndSendMessage(operationCount, operationTarget, false, true)
    }

    /**
     * 首页推荐好友关注
     */
    suspend fun recommendStarAndSendMessage(
        operationCount: Int,
        operationTarget: OperationDetail.TYPE?,
        isStar: Boolean,
        isMessage: Boolean
    ) {
        var count = Math.max(1, operationCount)

        while (true) {
            val starButton = ServiceUtils.findViewById(service, Constants.ID_TITLE)
            ServiceUtils.clickView(starButton)
            sleep(3000)

            if (operationTarget == OperationDetail.TYPE.FANS_LIST) {
                val fansList = ServiceUtils.findViewByEqualsText(service, "粉丝")
                if (fansList.isNullOrEmpty()) {
                    ServiceUtils.goBack(service)
                    sleep(1500)
                    scrollHomeRecommend()
                    recommendStarAndSendMessage(operationCount, operationTarget, isStar, isMessage)
                    return
                }
                ServiceUtils.clickView(fansList[0])
                sleep(2500)
            } else {
                val starList = ServiceUtils.findViewByEqualsText(service, "关注")
                if (starList.isNullOrEmpty()) {
                    ServiceUtils.goBack(service)
                    sleep(1500)
                    scrollHomeRecommend()
                    recommendStarAndSendMessage(operationCount, operationTarget, isStar, isMessage)
                    return
                }
                var startButton: AccessibilityNodeInfo? = null
                for (startText in starList) {
                    if (!startText.isClickable) {
                        startButton = startText
                    }
                }
                ServiceUtils.clickView(startButton)
                sleep(2500)
            }

            val moreButton =
                ServiceUtils.findViewByFirstEqualsContentDescription(service, "更多")
            if (moreButton != null) {
                ServiceUtils.goBack(service)
                sleep(1500)
                scrollHomeRecommend()
                recommendStarAndSendMessage(operationCount, operationTarget, isStar, isMessage)
                return
            }
            val rvList =
                ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_RV)
            if (rvList.isNullOrEmpty() || rvList.size < 2) return
            var rv: AccessibilityNodeInfo
            if (operationTarget == OperationDetail.TYPE.FANS_LIST) {
                rv = rvList[1]
            } else {
                rv = rvList[0]
            }
            rv?.let {
                Log.e("zwb", "getChildCount " + it.childCount)
                for (i in 0 until it.childCount) {
                    if (i >= count) break
                    val child = it.getChild(i)
                    Log.e("zwb", "Child $i is null" + (child == null))
                    if (child == null || !child.isClickable) continue
                    ServiceUtils.clickView(child)
                    sleep(2000)

                    starAndSendMessage(isStar, isMessage)

                    ServiceUtils.goBack(service)
                    sleep(2000)
                }
            }

            ServiceUtils.goBack(service)
            sleep(1500)
            ServiceUtils.goBack(service)
            sleep(1500)
            scrollHomeRecommend()
            recommendStarAndSendMessage(operationCount, operationTarget, isStar, isMessage)
        }
    }

    private suspend fun scrollHomeRecommend() {
        val viewPager =
            ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_VP)
        if (!viewPager.isNullOrEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                viewPager[0]?.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
                sleep(2000)
            }
        }
    }


    private suspend fun keywordCommand(operationType: OperationDetail.TYPE?, operationCount: Int) {
        when (operationType) {
            OperationDetail.TYPE.STAR -> keywordStar(operationCount)
            OperationDetail.TYPE.MESSAGE -> keywordSendMessage(operationCount)
            OperationDetail.TYPE.STAR_AND_MESSAGE -> keywordStarAndSendMessage(
                operationCount,
                true,
                true
            )
        }
    }

    /**
     * 搜索关键词用户
     */
    suspend fun keywordStar(operationCount: Int) {
//        val count = Math.max(1, operationCount)
//        val searchEditTextList = ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_ET)
//        if (searchEditTextList.isNullOrEmpty()) {
//            return
//        }
//        val searchEditText = searchEditTextList[1] ?: return
//        val rect = Rect()
//        searchEditText.getBoundsInScreen(rect)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            Log.e("----","rect ${rect.toShortString()}")
//            ServiceUtils.clickByGesture(service, rect.left + 200,rect.top + 25)
//        } else {
//            ToastUtils.showShort(App.getInstance(), "此功能需要android 7.0以上系统")
//            return
//        }
//        sleep(1000)
//        ServiceUtils.setText(searchEditText, ConfigManager.instance.getBatchUserList()[0])
//        sleep(2000)
//        val searchButton = ServiceUtils.findViewByEqualsText(service, "搜索")
//        Log.e("----", "searchButton size ${searchButton.size}")
//        if (searchButton.isNullOrEmpty() || searchButton[0] == null) {
//            return
//        }
//        ServiceUtils.clickView(searchButton[0])
//        sleep(3000)
//        val rvUserList = ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_RV)
//        rvUserList?.takeIf { it.size >= 2 }?.let {
//            for (i in 0 until it[1].childCount) {
//                if (i >= count) {
//                    break
//                }
//                val child = it[1].getChild(i) ?: continue
//                var listStar = ServiceUtils.findViewByEqualsText(child, "关注")
//                if (!listStar.isNullOrEmpty()) {
//                    listStar[0]?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
//                    sleep(1800)
//                }
//            }
//        }
        keywordStarAndSendMessage(operationCount, true, false)
    }

    suspend fun keywordSendMessage(operationCount: Int) {
        keywordStarAndSendMessage(operationCount, false, true)
    }


    suspend fun keywordStarAndSendMessage(
        operationCount: Int,
        isStar: Boolean,
        isMessage: Boolean
    ) {
        val batchUserList = ConfigManager.instance.getBatchUserList()
        var count = Math.max(1, operationCount)

        batchUserList.forEach {
            val searchEditTextList =
                ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_ET)
            if (searchEditTextList.isNullOrEmpty()) {
                return
            }
            val searchEditText = searchEditTextList[1] ?: return
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                ServiceUtils.clickByGesture(service, searchEditText)
            } else {
                ToastUtils.showShort(App.getInstance(), "此功能需要android 7.0以上系统")
                return
            }
            ServiceUtils.setText(searchEditText, it)
            sleep(1000)

            val searchButton = ServiceUtils.findViewByEqualsText(service, "搜索")
            if (!searchButton.isNullOrEmpty() && searchButton[0] != null) {
                ServiceUtils.clickView(searchButton[0])
                sleep(3000)
                val rvUserList = ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_RV)
                rvUserList?.takeIf { it.size >= 2 }?.let {
                    val rv = it[1]
                    keywordLoopOperate(rv, 0, count, isStar, isMessage)
                    sleep(2000)
                }
            }

        }
    }

    private suspend fun keywordLoopOperate(
        rv: AccessibilityNodeInfo,
        operateCount: Int,
        count: Int,
        isStar: Boolean,
        isMessage: Boolean
    ) {
        var operateCount1 = operateCount
        for (i in 0 until rv.childCount) {
            Log.e("----", "operateCount1 $operateCount1")
            if (operateCount1++ >= count) {
                break
            }
            Log.e("----", "operateCount1 $operateCount1 child ${rv.getChild(i) == null}")
            val child = rv.getChild(i) ?: continue
            ServiceUtils.clickView(child)
            sleep(3000)

            starAndSendMessage(isStar, isMessage)

            ServiceUtils.goBack(service)

            sleep(1500)

            if (i == rv.childCount - 1) {
                rv.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
                sleep(2000)
                keywordLoopOperate(rv, operateCount1, count, isStar, isMessage)
            }
        }
    }

    private suspend fun searchListCommand(
        count: Int,
        isStar: Boolean,
        isMessage: Boolean
    ) {
        val rvUserList = ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_RV)
        rvUserList?.takeIf { it.size >= 2 }?.let {
            for (i in 0 until it[1].childCount) {
                if (i >= count) {
                    return@let
                }
                val child = it[1].getChild(i) ?: continue
                child.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                sleep(1200)

                starAndSendMessage(isStar, isMessage)

                ServiceUtils.goBack(service)
                sleep(1000)

                if (i == it[1].childCount - 1) {
                    sleep(500)
                    it[1].performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
                    sleep(1000)
                    searchListCommand(count, isStar, isMessage)
                }
            }
        }
    }

    private suspend fun addressBookCommand(operationType: OperationDetail.TYPE?) {
        when (operationType) {
            OperationDetail.TYPE.STAR -> addressBookStar()
            OperationDetail.TYPE.MESSAGE -> addressBookSendMessage()
            OperationDetail.TYPE.STAR_AND_MESSAGE -> addressBookStarAndSendMessage(true, true)
        }

    }

    /**
     * 通讯录点赞,关注
     */
    suspend fun addressBookStar() {
        addressBookStarAndSendMessage(true, false)
    }

    suspend fun addressBookSendMessage() {
        addressBookStarAndSendMessage(false, true)
    }


    suspend fun addressBookStarAndSendMessage(isStar: Boolean, isMessage: Boolean) {
        val rvContact =
            ServiceUtils.findViewByFirstClassName(service, Constants.CLASS_NAME_RV)
        rvContact ?: return
        for (i in 0 until rvContact.childCount) {
            val child = rvContact.getChild(i) ?: continue
            val clickSuccess = ServiceUtils.clickView2(child)
            if (!clickSuccess) continue
            sleep(1500)
            starAndSendMessage(isStar, isMessage)

            ServiceUtils.goBack(service)
            sleep(1500)

            if (i == rvContact.childCount - 1) {
                sleep(500)
                rvContact.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
                sleep(1500)
                addressBookStarAndSendMessage(isStar, isMessage)
            }
        }
    }

    private suspend fun commentCommand(operationType: OperationDetail.TYPE?) {
        when (operationType) {
            OperationDetail.TYPE.STAR -> commentStar()
            OperationDetail.TYPE.MESSAGE -> commentSendMessage()
            OperationDetail.TYPE.STAR_AND_MESSAGE -> commentStarAndSendMessage(true, true)
        }
    }

    private suspend fun commentStar() {
        commentStarAndSendMessage(true, false)
    }

    private suspend fun commentSendMessage() {
        commentStarAndSendMessage(false, true)
    }

    private suspend fun commentStarAndSendMessage(isStar: Boolean, isMessage: Boolean) {
        val rvUserList = ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_RV)
        val rv = rvUserList?.takeIf { it.isNotEmpty() }?.get(0)
        rv?.let {
            for (i in 0 until it.childCount) {
                val child = it.getChild(i) ?: continue
                val title = ServiceUtils.findViewById(child, Constants.ID_TITLE) ?: continue
                ServiceUtils.clickByGesture(service, title)
                sleep(1000)
                //防止弹起当前页面EditText
                val etList =
                    ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_ET)
                if (!etList.isNullOrEmpty()) {
                    ServiceUtils.goBack(service)
                    sleep(1000)
                    ServiceUtils.clickByGesture(service, title)
                }
                sleep(1500)
                starAndSendMessage(isStar, isMessage)
                ServiceUtils.goBack(service)
                sleep(1000)
                if (i == it.childCount - 1) {
                    sleep(500)
                    it.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
                    sleep(1000)
                    commentStarAndSendMessage(isStar, isMessage)
                }
            }
        }
    }

    private suspend fun liveCommand(operationType: OperationDetail.TYPE?) {
        when (operationType) {
            OperationDetail.TYPE.STAR -> liveStar()
            OperationDetail.TYPE.MESSAGE -> liveSendMessage()
            OperationDetail.TYPE.STAR_AND_MESSAGE -> liveStarAndSendMessage(true, true)
        }
    }

    private suspend fun liveStar() {
        liveStarAndSendMessage(true, false)
    }

    private suspend fun liveSendMessage() {
        liveStarAndSendMessage(false, true)
    }

    private suspend fun liveStarAndSendMessage(isStar: Boolean, isMessage: Boolean) {
        val rvUserList = ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_RV)
        val tvTabList = ServiceUtils.findViewByEqualsText(service, "本场榜")
        var rvIndex = 0
        if (!tvTabList.isNullOrEmpty()) {
            if (tvTabList[0].isSelected) {
                rvIndex = 0
            } else {
                rvIndex = 1
            }
        }
        Log.e("----", "rvIndex $rvIndex")
        val rv = rvUserList?.takeIf { it.size > 1 }?.get(rvIndex) ?: return
//        rv.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
        val childList = ArrayList<AccessibilityNodeInfo>()
        for (i in 0 until rv.childCount) {
            val child = rv.getChild(i)
            Log.e("----", "child $i is null ${child == null}")
            child ?: continue
            if (!child.isClickable) continue
            childList.add(child)
        }
        for ((i, child) in childList.withIndex()) {
            ServiceUtils.clickByGesture(service, child)
            sleep(2000)
            val tvHome = ServiceUtils.findViewByEqualsText(service, "主页")
            if (tvHome.isNullOrEmpty()) continue
            ServiceUtils.clickView(tvHome[0])
            sleep(2000)
            starAndSendMessage(isStar, isMessage)
            sleep(1000)
            ServiceUtils.goBack(service)
            sleep(1500)
            ServiceUtils.goBack(service)
            sleep(1000)
            if (i == childList.lastIndex) {
                Log.e("zwb", "ACTION_SCROLL_FORWARD")
                sleep(1000)
                rv.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
                sleep(2000)
                liveStarAndSendMessage(isStar, isMessage)
            }
        }
    }

    private suspend fun commentAreaCommand(operationType: OperationDetail.TYPE?) {

        when (operationType) {
            OperationDetail.TYPE.RECOMMEND_VDIEO -> commentRecommendVideo()
            OperationDetail.TYPE.WORKS_OF_USER -> commentWorksOfUser()
            OperationDetail.TYPE.LIVE_SQUARE -> commentLiveSquare()
            OperationDetail.TYPE.SAME_CITY_LIST -> commentSameCityList()
        }
    }

    private suspend fun commentSameCityList() {

        val screenHeight = UiUtils.getScreenHeight2(App.getInstance())
        val navBarHeight = App.getInstance().getNavBarHeight()
        val clickY = UiUtils.getScreenHeight2(App.getInstance()) - UiUtils.dp2px(
            App.getInstance(),
            25
        ) - navBarHeight
        val clickX = UiUtils.dp2px(App.getInstance(), 35)
        Log.e("----", "screenHeight ${screenHeight}")
        Thread {
            Looper.prepare()
            ToastUtils.showLong(
                App.getInstance(),
                "屏幕高度 ${screenHeight} 导航栏高度 ${navBarHeight} \n 点击区域坐标  ${clickX}:${clickY}"
            )
            Looper.loop()
        }.start()
        ServiceUtils.clickByGesture(
            service, clickX,
            clickY
        )
        sleep(2000)
        val etList = ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_ET)
        if (etList.isNullOrEmpty()) return
        ServiceUtils.setText(etList[0], ConfigManager.instance.getGreetWord())
        sleep(1500)

        val ivList = ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_IV)
        if (ivList.isNullOrEmpty()) return
        var sendIv: AccessibilityNodeInfo
        var isLive = false
        if (ivList.size > 2) {
            sendIv = ivList[2]
            isLive = false
        } else {
            sendIv = ivList[1]
            isLive = true
        }
        if (ServiceUtils.clickView(sendIv)) {
            sleep(3000)
            if (isLive) {
                ServiceUtils.goBack(service)
                sleep(2000)
            }
            ServiceUtils.scrollViewVertical(
                service,
                UiUtils.getScreenWidth(App.getInstance()) / 3,
                UiUtils.getScreenHeight(App.getInstance()) / 2
            )
            sleep(5000)
            commentSameCityList()
        }
    }

    private suspend fun commentLiveSquare() {
        val tvSayList = ServiceUtils.findViewByContainsText(service, "说点什么...")
        if (tvSayList.isNullOrEmpty()) {
            ServiceUtils.scrollViewVertical(
                service,
                UiUtils.getScreenWidth(App.getInstance()) / 4,
                UiUtils.getScreenHeight(App.getInstance()) / 2
            )
            sleep(4000)
            commentLiveSquare()
        } else if (ServiceUtils.clickView(tvSayList[0])) {
            sleep(1500)
            val etList =
                ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_ET)
            if (etList.isNullOrEmpty()) return
            ServiceUtils.setText(etList[0], ConfigManager.instance.getGreetWord())
            sleep(2000)
            val ivList =
                ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_IV)
            if (ivList.isNullOrEmpty() || ivList.size < 2) {
                return
            }
            if (ServiceUtils.clickView(ivList[1])) {
                sleep(1000)
                ServiceUtils.goBack(service)
                sleep(1500)
                val vpList =
                    ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_VP)
                if (vpList.isNullOrEmpty()) {
                    return
                }
                vpList[0]?.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
                sleep(2000)
                commentLiveSquare()
            }
        }

    }

    private suspend fun commentWorksOfUser() {
//        val rvList = ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_RV)
////        if (rvList.isNullOrEmpty())
////            return
//        var rv = rvList[0]
////        rv = ServiceUtils.findViewById(service,"com.ss.android.ugc.aweme:id/b0u")
//        Log.e("---", "rv child ${rv.childCount}")
//        for (i in 0 until rv.childCount) {
//            val child = rv.getChild(i)
//            if (child == null) continue
//            ServiceUtils.clickView2(child)
//            break
//        }
        ServiceUtils.clickByGesture(
            service, UiUtils.getScreenWidth(App.getInstance()) / 2,
            UiUtils.getScreenHeight(App.getInstance()) - UiUtils.dp2px(App.getInstance(), 25)
        )
        sleep(1500)
        val etList = ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_ET)
        if (etList.isNullOrEmpty()) return
        ServiceUtils.setText(etList[0], ConfigManager.instance.getGreetWord())
        sleep(1500)
        val ivList = ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_IV)
        if (ivList.isNullOrEmpty() || ivList.size < 3) return
        if (ServiceUtils.clickView(ivList[2])) {
            sleep(1500)
            ServiceUtils.scrollViewVertical(
                service,
                UiUtils.getScreenWidth(App.getInstance()) / 4,
                UiUtils.getScreenHeight(App.getInstance()) / 2
            )
            sleep(4000)
            commentWorksOfUser()
        }
    }

    private suspend fun commentRecommendVideo() {
        val commentButton =
            ServiceUtils.findViewByFirstContainsContentDescription(service, "评论")
        if (ServiceUtils.clickView(commentButton)) {
            sleep(1500)
            val commentEditTextList =
                ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_ET)
            if (commentEditTextList.isNullOrEmpty()) {
                return
            }
            val commentEditText = commentEditTextList[0] ?: return
            ServiceUtils.setText(commentEditText, ConfigManager.instance.getGreetWord())
            ServiceUtils.clickView(commentEditText)
            sleep(1500)
            val ivList =
                ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_IV)
            Log.e("-----", "ivList ${ivList.size}")
            if (ivList.size < 3) return
            ServiceUtils.clickView(ivList[2])
            sleep(1500)
            ServiceUtils.goBack(service)
            sleep(1000)
        }
        val viewPager =
            ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_VP)
        if (!viewPager.isNullOrEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                viewPager.get(0)?.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
                sleep(2000)
                commentRecommendVideo()
            }
        }
    }

    private suspend fun friendsListCommand(operationType: OperationDetail.TYPE?) {
        when (operationType) {
            OperationDetail.TYPE.WORD -> sendWord()
            OperationDetail.TYPE.PICTURE -> sendPicture()
            OperationDetail.TYPE.WORD_AND_PICTURE -> sendWordAndPicture(true, true)
        }
    }

    private suspend fun sendWordAndPicture(isWord: Boolean, isPicture: Boolean) {
        val rvUserList = ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_RV)
        val rv = rvUserList?.takeIf { it.isNotEmpty() }?.get(0)
        rv?.let {
            for (i in 0 until it.childCount) {
                var child = it.getChild(i)
                Log.e("----", "child $i is null ${child == null}")
                child ?: continue
                val ivList = ServiceUtils.findViewByContentDescription(child, "发消息")
                Log.e("----", "ivList $i size ${ivList.size}")
                if (ivList.isNullOrEmpty()) continue
                ServiceUtils.clickView(ivList[0])
                sleep(1500)

                if (isWord) {
                    val editText =
                        ServiceUtils.findViewByFirstClassName(service, Constants.CLASS_NAME_ET)
                    editText?.let { et ->
                        ServiceUtils.setText(et, ConfigManager.instance.getGreetWord())
                        sleep(1500)
                    }
                    val send =
                        ServiceUtils.findViewByFirstEqualsContentDescription(service, "发送")
                    send?.let { sd ->
                        ServiceUtils.clickView(sd)
                        sleep(2500)
                    }
                }
                if (isPicture) {
                    ServiceUtils.clickByGesture(
                        service,
                        UiUtils.getScreenWidth(App.getInstance()) - UiUtils.dp2px(
                            App.getInstance(),
                            20
                        ),
                        UiUtils.getScreenHeight(App.getInstance()) - UiUtils.dp2px(
                            App.getInstance(),
                            35
                        )
                    )
                    sleep(1500)
                    val tvPhoto = ServiceUtils.findViewByEqualsText(service, "照片")
                    if (!tvPhoto.isNullOrEmpty()) {
                        ServiceUtils.clickView(tvPhoto[0])
                        sleep(2000)
                        val rvPhotoList =
                            ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_RV)
                        rvPhotoList?.get(0)?.getChild(0)?.takeIf { it.childCount >= 2 }?.apply {
                            ServiceUtils.clickView(getChild(1))
                            sleep(2000)
                            ServiceUtils.findViewByContainsText(service, "发送")?.let {
                                ServiceUtils.clickView(it[0])
                                sleep(2000)
                                ServiceUtils.goBack(service)
                                sleep(2000)
                            }
                        }
                    }
                }
                ServiceUtils.goBack(service)
                sleep(2500)
                if (i == it.childCount - 1) {
                    sleep(500)
                    it.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
                    sleep(1000)
                    sendWordAndPicture(isWord, isPicture)
                }
            }
        }
    }

    private suspend fun sendPicture() {
        sendWordAndPicture(false, true)
    }

    private suspend fun sendWord() {
        sendWordAndPicture(true, false)
    }

    private suspend fun messageCommand() {
        val rvUserList = ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_RV)
        val rv = rvUserList?.takeIf { it.isNotEmpty() }?.get(0) ?: return
        val childList = ArrayList<AccessibilityNodeInfo>()
        for (i in 0 until rv.childCount) {
            val child = rv.getChild(i)
            Log.e("----", "child $i is null ${child == null}")
            child ?: continue
            Log.e("----", "child $i is isClickable ${child.isClickable}")
            if (!child.isClickable) continue
            childList.add(child)
        }
        Log.e("----", "childList ${childList.size}")
        for ((i, child) in childList.withIndex()) {
            Log.e("----", "index $i is null ${child == null}")
            ServiceUtils.clickByGesture(service, child)
            sleep(1500)

            val editText =
                ServiceUtils.findViewByFirstClassName(service, Constants.CLASS_NAME_ET)
            if (editText == null) {
                Log.e("----", "index $i editText is null")
                continue
            }
            ServiceUtils.setText(editText, ConfigManager.instance.getGreetWord())
            sleep(1500)
            val send =
                ServiceUtils.findViewByFirstEqualsContentDescription(service, "发送") ?: continue
            ServiceUtils.clickView(send)
            sleep(1500)

            ServiceUtils.goBack(service)
            sleep(1500)
            if (i == childList.lastIndex) {
                sleep(500)
                rv.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
                sleep(1500)
                messageCommand()
            }
        }
    }


    private suspend fun trainAccountCommand() {
        var operateCount = ConfigManager.instance.getTrainConfig().operateCount
        val viewPager =
            ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_VP)
        if (!viewPager.isNullOrEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                while (true) {
                    ServiceUtils.doubleClickView(
                        service,
                        UiUtils.getScreenWidth(App.getInstance()) / 2,
                        UiUtils.getScreenHeight(App.getInstance()) / 5 * 2
                    )
                    sleep(1500)

                    val commentButton =
                        ServiceUtils.findViewByFirstContainsContentDescription(service, "评论")
                    if (ServiceUtils.clickView(commentButton)) {
                        sleep(1500)
                        val commentEditTextList =
                            ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_ET)
                        if (commentEditTextList.isNullOrEmpty()) {
                            return
                        }
                        val commentEditText = commentEditTextList[0] ?: return
                        ServiceUtils.setText(commentEditText, ConfigManager.instance.getGreetWord())
                        ServiceUtils.clickView(commentEditText)
                        sleep(1500)
                        val ivList =
                            ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_IV)
                        Log.e("-----", "ivList ${ivList.size}")
                        if (ivList.size < 3) return
                        ServiceUtils.clickView(ivList[2])
                        sleep(1500)
                        ServiceUtils.goBack(service)

                        sleep(1000)
                        viewPager[0]?.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
                        sleep(3000)
                    }
                }
            }
        }
    }

    private suspend fun pariseCommand(operationType: OperationDetail.TYPE?) {
        when (operationType) {
            OperationDetail.TYPE.HOME_RECOMMEND_VDIEO -> {
                pariseHomeRecommendVideo()
            }
            OperationDetail.TYPE.PERSONAL_ALL_VIDEO -> {
                parisePersonalAllVideo()
            }
            OperationDetail.TYPE.SAME_CITY_VIDEO -> {
                parisePersonalAllVideo()
            }
            OperationDetail.TYPE.CURRENT_WORK_COMMENTER -> {
                pariseCurrentWorkCommenter()
            }
            OperationDetail.TYPE.LIVE -> {
                pariseLive()
            }
        }
    }

    private suspend fun pariseLive() {
        while (true) {
            if (!isStartRun()) {
                break
            }
            ServiceUtils.doubleClickView(
                service,
                UiUtils.getScreenWidth(App.getInstance()) / 2,
                UiUtils.getScreenHeight(App.getInstance()) / 5 * 2
            )
            sleep(500)
        }
    }

    private suspend fun parisePersonalAllVideo() {
        while (true) {
            ServiceUtils.doubleClickView(
                service,
                UiUtils.getScreenWidth(App.getInstance()) / 2,
                UiUtils.getScreenHeight(App.getInstance()) / 5 * 2
            )
            sleep(2000)
            ServiceUtils.scrollViewVertical(
                service,
                UiUtils.getScreenWidth(App.getInstance()) / 3,
                UiUtils.getScreenHeight(App.getInstance()) / 2
            )
            sleep(4000)
        }
    }

    private suspend fun pariseCurrentWorkCommenter() {
        val rvUserList = ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_RV)
        val rv = rvUserList?.takeIf { it.isNotEmpty() }?.get(0)
        rv?.let {
            for (i in 0 until it.childCount) {
                val child = it.getChild(i) ?: continue
                val starView =
                    ServiceUtils.findViewByClassName(child, Constants.CLASS_NAME_VIEW)
                if (!starView.isNullOrEmpty()) {
                    ServiceUtils.clickView(starView[0])
                    sleep(1800)
                }

                if (i == it.childCount - 1) {
                    sleep(500)
                    it.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
                    sleep(1000)
                    pariseCurrentWorkCommenter()
                }
            }
        }
    }

    suspend fun pariseHomeRecommendVideo() {
        val viewPager =
            ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_VP)
        if (!viewPager.isNullOrEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                while (true) {
                    ServiceUtils.doubleClickView(
                        service,
                        UiUtils.getScreenWidth(App.getInstance()) / 2,
                        UiUtils.getScreenHeight(App.getInstance()) / 5 * 2
                    )
                    sleep(3000)
                    viewPager[0]?.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
                    sleep(1500)
                }
            }
        }
    }


    /**
     * 私信首页推荐视频
     */
    private suspend fun privateMessagesCommand() {
        while (true) {
            val starButton = ServiceUtils.findViewById(service, Constants.ID_TITLE)
            if (starButton == null) {
                val viewPager =
                    ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_VP)
                if (!viewPager.isNullOrEmpty()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        ServiceUtils.scrollView(service, viewPager.get(0))
                        viewPager.get(0)
                            ?.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
                        sleep(3000)
                    }
                }
                continue
            }
            ServiceUtils.clickView(starButton)
            sleep(1500)
            starAndSendMessage(false, true)

            ServiceUtils.goBack(service)
            sleep(1500)

            val viewPager =
                ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_VP)
            if (!viewPager.isNullOrEmpty()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    ServiceUtils.scrollView(service, viewPager.get(0))
                    viewPager.get(0)?.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
                    sleep(3000)
                }
            }
        }
    }

    private suspend fun sameCityUserCommand(operationType: OperationDetail.TYPE?) {
        when (operationType) {
            OperationDetail.TYPE.STAR -> sameCityUserStarAndSendMessage(true, false)
            OperationDetail.TYPE.MESSAGE -> sameCityUserStarAndSendMessage(false, true)
            OperationDetail.TYPE.STAR_AND_MESSAGE -> sameCityUserStarAndSendMessage(true, true)
        }
    }

    private suspend fun sameCityUserStarAndSendMessage(isStar: Boolean, isMessage: Boolean) {
//        val rvList = ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_RV)
//        if (rvList.isNullOrEmpty()){
//            return
//        }
//        val rv = rvList[0]
//        var entrance = rv.getChild(0)
//        if (entrance?.isClickable != true){
//            entrance = rv.getChild(1)
//        }
//        ServiceUtils.clickView(entrance)
//        sleep(3000)

        ServiceUtils.scrollViewHorizontal(
            service,
            UiUtils.getScreenWidth(App.getInstance()) / 3 * 2,
            UiUtils.getScreenHeight(App.getInstance()) / 2
        )
        sleep(3000)
        val moreButton =
            ServiceUtils.findViewByFirstEqualsContentDescription(service, "更多")
        var starTvList = ServiceUtils.findViewByContainsText(service, "关注")
        if (starTvList.isNullOrEmpty()) {
            sleep(1000)
            val rootInActiveWindow = service?.rootInActiveWindow
            if (rootInActiveWindow == null) {
                ServiceUtils.goBack(service)
                sleep(2000)
                sameCityUserStarAndSendMessage(isStar, isMessage)
                return
            }
        }
        Log.e("-----", "starTvList ${starTvList.isNullOrEmpty()}")
        if (!starTvList.isNullOrEmpty()) {
            starAndSendMessage(isStar, isMessage)
            ServiceUtils.goBack(service)
            sleep(2000)
        }
        ServiceUtils.scrollViewVertical(
            service,
            UiUtils.getScreenWidth(App.getInstance()) / 3,
            UiUtils.getScreenHeight(App.getInstance()) / 2
        )
        sleep(4000)
        sameCityUserStarAndSendMessage(isStar, isMessage)
    }

    private suspend fun batchStarCommand(operationType: OperationDetail.TYPE?) {
        when (operationType) {
            OperationDetail.TYPE.BATCH_STAR -> toggleBatchStar(true)
            OperationDetail.TYPE.BATCH_UNSTAR -> toggleBatchStar(false)
        }
    }

    suspend fun toggleBatchStar(isStar: Boolean) {
        val rvList =
            ServiceUtils.findViewByClassName(service, Constants.CLASS_NAME_RV)
        Log.e("----", "rvList size ${rvList.size}")
        if (rvList.isNullOrEmpty() || rvList.size < 2) return
        val rv: AccessibilityNodeInfo
        val fansBarList = ServiceUtils.findViewByContainsText(service, "粉丝")
        Log.e("----", "fansBarList  ${fansBarList.isNullOrEmpty()}")
        if (fansBarList.isNullOrEmpty()) {
            return
        }
        if (fansBarList[0]?.isSelected == true) {
            rv = rvList[1]
        } else {
            rv = rvList[0]
        }
        if (rv != null) {
            Log.e("zwb", "getChildCount " + rv.childCount)
            for (i in 0 until rv.childCount) {
                val child = rv.getChild(i) ?: continue
                var listStar =
                    ServiceUtils.findViewByEqualsText(child, if (isStar) "关注" else "已关注")
                if (listStar.isNullOrEmpty()) {
                    listStar =
                        ServiceUtils.findViewByEqualsText(child, if (isStar) "回关" else "互相关注")
                }
                if (!listStar.isNullOrEmpty()) {
                    ServiceUtils.clickView(listStar[0])
                    sleep(1800)
                } else if (i != rv.childCount - 1) {
                    continue
                }
                if (i == rv.childCount - 1) {
                    rv.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
                    sleep(1500)
                    toggleBatchStar(isStar)
                }
            }
        }
    }


    private suspend fun randomCommand(
        operationType: OperationDetail.TYPE?,
        operationTarget: OperationDetail.TYPE?
    ) {
        when (operationType) {
            OperationDetail.TYPE.STAR -> assignStar(operationTarget, true)
            OperationDetail.TYPE.MESSAGE -> assignSendMessage(operationTarget, true)
            OperationDetail.TYPE.STAR_AND_MESSAGE -> assignStarAndSendMessage(
                operationTarget,
                true,
                true
                , true
            )
        }
    }

}