package com.zwb.auto.bean

/**
 * Description:
 * Author: zwb
 * Date: 2020/4/6
 */
enum class OperationDetail {
    OPERATE_TARGET {
        override fun title() = "目标对象"

        override fun type(): List<TYPE> {
            return mutableListOf(TYPE.MINE,TYPE.SPECIAL_USER)
        }

    },
    OPERATE_AREA {
        override fun title() = "操作范围"

        override fun type(): List<TYPE> {
            return mutableListOf(TYPE.FANS_LIST,TYPE.STARS_LIST)
        }

    },
    OPERATE_TYPE_COMMON {
        override fun title() = "操作类型"

        override fun type(): List<TYPE> {
            return mutableListOf(TYPE.STAR,TYPE.MESSAGE,TYPE.STAR_AND_MESSAGE)
        }
    },

    /**
     * 评论专区
     */
    OPERATE_AREA_COMMENT {
        override fun title() = "操作对象"

        override fun type(): List<TYPE> {
            return mutableListOf(TYPE.RECOMMEND_VDIEO,TYPE.WORKS_OF_USER,TYPE.LIVE_SQUARE,TYPE.SAME_CITY_LIST)
        }

    },

    /**
     * 好友列表
     */
    OPERATE_TYPE_FRIENDS_LIST {
        override fun title() = "操作类型"

        override fun type(): List<TYPE> {
            return mutableListOf(TYPE.WORD,TYPE.PICTURE,TYPE.WORD_AND_PICTURE)
        }
    },

    /**
     * 点赞专区
     */
    OPERATE_AREA_PARISE {
        override fun title() = "操作类型"

        override fun type(): List<TYPE> {
            return mutableListOf(TYPE.HOME_RECOMMEND_VDIEO,TYPE.PERSONAL_ALL_VIDEO,TYPE.SAME_CITY_VIDEO,TYPE.CURRENT_WORK_COMMENTER,TYPE.LIVE)
        }
    },

    /**
     * 批量关注/取消关注
     */
    OPERATE_TYPE_BATCH_STAR {
        override fun title() = "操作类型"

        override fun type(): List<TYPE> {
            return mutableListOf(TYPE.BATCH_STAR,TYPE.BATCH_UNSTAR)
        }
    };



    abstract fun title(): String
    abstract fun type(): List<TYPE>

    enum class TYPE {

        MINE {
            override fun desc() = "我的"
        },
        SPECIAL_USER {
            override fun desc() = "指定用户"
        },


        FANS_LIST {
            override fun desc() = "粉丝列表"
        },
        STARS_LIST {
            override fun desc() = "关注用户"
        },


        STAR {
            override fun desc() = "关注"
        },
        MESSAGE {
            override fun desc() = "私信"
        },
        STAR_AND_MESSAGE {
            override fun desc() = "关注+私信"
        },


        RECOMMEND_VDIEO{
            override fun desc() = "推荐视频"
        },
        WORKS_OF_USER{
            override fun desc() = "指定用户的所有作品"
        },
        LIVE_SQUARE{
            override fun desc() = "直播广场"
        },
        SAME_CITY_LIST{
            override fun desc() = "同城列表"
        },


        WORD{
            override fun desc() = "发文字"
        },
        PICTURE{
            override fun desc() = "发图片"
        },
        WORD_AND_PICTURE{
            override fun desc() = "发图文"
        },


        HOME_RECOMMEND_VDIEO{
            override fun desc() = "首页推荐视频"
        },
        PERSONAL_ALL_VIDEO{
            override fun desc() = "指定用户所有作品"
        },
        SAME_CITY_VIDEO{
            override fun desc() = "同城列表"
        },
        CURRENT_WORK_COMMENTER{
            override fun desc() = "当前作品的评论列表"
        },
        LIVE{
            override fun desc() = "直播间"
        },


        BATCH_STAR{
            override fun desc() = "批量关注"
        },
        BATCH_UNSTAR{
            override fun desc() = "批量取消关注"
        };


        abstract fun desc(): String
    }
}