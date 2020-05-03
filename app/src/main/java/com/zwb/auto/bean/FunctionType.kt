package com.zwb.auto.bean

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.zwb.auto.R

/**
 * Description:
 * Author: zwb
 * Date: 2020/4/6
 */
enum class FunctionType{
    ASSIGN{
        override fun drawableRes() = R.mipmap.ic_home_assign

        override fun bgColor() = Color.parseColor("#3b75ff")

        override fun title() = "定向抖友"

        override fun description() = "私/关定向抖友"

        override fun target() = "指定用户"

    },
    RECOMMEND{
        override fun drawableRes() = R.mipmap.ic_home_recommend

        override fun bgColor() = Color.parseColor("#EC3F54")

        override fun title() = "首页推荐好友"

        override fun description() = "私/关首页作品随机抖友"

        override fun target() = "首页推荐视频页"

    },
    KEYWORD{
        override fun drawableRes() = R.mipmap.ic_home_keyword

        override fun bgColor() = Color.parseColor("#F464BA")

        override fun title() = "关键词用户"

        override fun description() = "私/关指定用户,支持关键词/ID"

        override fun target() = "我的页面发现好友页面"

    },
    ADDRESS_BOOK{
        override fun drawableRes() = R.mipmap.ic_home_address_book

        override fun bgColor() = Color.parseColor("#33E0FF")

        override fun title() = "通讯录用户"

        override fun description() = "私/关通讯录用户"

        override fun target() = "我的通讯录列表"

    },
    COMMENT{
        override fun drawableRes() = R.mipmap.ic_home_comment

        override fun bgColor() = Color.parseColor("#2B7EFF")

        override fun title() = "评论用户"

        override fun description() = "私/关当前作品评论者"

        override fun target() = "对应作品评论页面"

    },
    LIVE{
        override fun drawableRes() = R.mipmap.ic_home_live

        override fun bgColor() = Color.parseColor("#FB4E58")

        override fun title() = "直播间用户"

        override fun description() = "私/关直播间本场榜/在线用户"

        override fun target() = "直播间的本行榜列表"

    },
    COMMENT_AREA{
        override fun drawableRes() = R.mipmap.ic_home_comment_area

        override fun bgColor() = Color.parseColor("#F264BF")

        override fun title() = "评论专区"

        override fun description() = "推荐视频/同城/指定用户的作品/直播广场"

        override fun target() = "评论专区"

    },
    FRIENDS_LIST{
        override fun drawableRes() = R.mipmap.ic_home_friends

        override fun bgColor() = Color.parseColor("#37E5FF")

        override fun title() = "好友列表"

        override fun description() = "好友列表互动/文字/图片"

        override fun target() = "我的好友列表"

    },
    MESSAGES{
        override fun drawableRes() = R.mipmap.ic_home_messages

        override fun bgColor() = Color.parseColor("#2B7EFF")

        override fun title() = "消息列表"

        override fun description() = "发消息"

        override fun target() = "消息列表"

    },
    TRAIN{
        override fun drawableRes() = R.mipmap.ic_home_grey

        override fun bgColor() = Color.parseColor("#FA5856")

        override fun title() = "养号"

        override fun description() = "推荐专区养号"

        override fun target() = "首页推荐列表"

    },
    PARISE{
        override fun drawableRes() = R.mipmap.ic_home_parise

        override fun bgColor() = Color.parseColor("#F464BA")

        override fun title() = "点赞"

        override fun description() = "视频点赞"

        override fun target() = "视频点赞"

    },
    PRIVATE_MESSAGES{
        override fun drawableRes() = R.mipmap.ic_home_private_message

        override fun bgColor() = Color.parseColor("#37E5FF")

        override fun title() = "私信"

        override fun description() = "私信首页推荐视频"

        override fun target() = "首页推荐视频"

    },
    SAME_CITY_USER{
        override fun drawableRes() = R.mipmap.ic_home_same_city

        override fun bgColor() = Color.parseColor("#3b75ff")

        override fun title() = "同城用户"

        override fun description() = "私/关同城用户"

        override fun target() = "同城页面第一个视频"

    },
    BATCH_STAR{
        override fun drawableRes() = R.mipmap.ic_home_batch_star

        override fun bgColor() = Color.parseColor("#FA5856")

        override fun title() = "批量取关/回关"

        override fun description() = "我的抖友,批量关注即取消关注"

        override fun target() = "我的粉丝列表或我的关注列表"

    };



    @DrawableRes
    abstract fun drawableRes():Int
    @ColorInt
    abstract fun bgColor():Int
    abstract fun title():String
    abstract fun description():String
    abstract fun target():String
}