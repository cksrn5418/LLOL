package org.techtown.klab

import android.graphics.Bitmap
import android.graphics.BitmapFactory

data class MyTalent
    (var userid:String,
     var name:String,
     var title:String,
     var category:String,
     var people_amount:String,
     var maintext:String,
     var startyear:String,
     var startmonth:String,
     var startday:String,
     var datedetail:String,
     var addresstext:String,
     var latitude:String,
     var longitude:String,
     var cost:String,
     var openchat:String,
     var count:String,
     var profileimg:Bitmap? = null,
     var image:Bitmap? = null) {
    //데이터를 저장할 class이며,
    // 원소, 생성자, get, set 함수까지 끝
}