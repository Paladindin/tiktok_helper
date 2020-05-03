package com.zwb.auto.utils

import android.content.Context
import android.widget.Toast

object ToastUtils {

    private var TOAST: Toast?=null

    fun showShort(context: Context, text:String){
        show(context, text, Toast.LENGTH_SHORT)
    }
    fun showShort(context: Context, resourceID:Int){
        show(context, resourceID, Toast.LENGTH_SHORT)
    }

    fun showLong(context:Context,text:String){
        show(context, text, Toast.LENGTH_SHORT)
    }

    fun showLong(context:Context,resourceID:Int){
        show(context, resourceID, Toast.LENGTH_SHORT)
    }

    fun show(context:Context,resourceID:Int,duration:Int){
        val text = context.resources.getString(resourceID)
        show(context, text, duration)
    }

    fun show(context:Context,text:String,duration: Int){
        TOAST = if(TOAST == null){
            Toast.makeText(context, text, duration)
        }else{
            TOAST?.cancel()
            Toast.makeText(context, text, duration)
        }
        TOAST?.show()
    }
}