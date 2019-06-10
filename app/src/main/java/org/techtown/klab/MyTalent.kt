package org.techtown.klab

import android.graphics.Bitmap
import android.graphics.BitmapFactory

data class MyTalent(var userid:Int, var name:String, var title:String, var people_amount:Int, var maintext:String, var profileimg:Bitmap? = null, var image:Bitmap? = null) {
    //데이터를 저장할 class이며,
    // 원소, 생성자, get, set 함수까지 끝
}