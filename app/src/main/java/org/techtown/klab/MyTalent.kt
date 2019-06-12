package org.techtown.klab

import android.graphics.Bitmap
import android.graphics.BitmapFactory

data class MyTalent
    (var userid:String,
     var name:String,
     var phonenum:String,
     var title:String,
     var category:String,
     var people_amount:String,
     var maintext:String,
     var dueyear:String,
     var duemonth:String,
     var dueday:String,
     var addresstext:String,
     var latitude:String,
     var longitude:String,
     var count:String,
     var profileimg:String,
     var image:String) {
 constructor():this(0.toString(), 0.toString(), 0.toString(), 0.toString(), 0.toString(), 0.toString(), 0.toString(), 0.toString(), 0.toString(), 0.toString(), 0.toString(), 0.toString(), 0.toString(), 0.toString(), 0.toString(), 0.toString())
    //데이터를 저장할 class이며,
    // 원소, 생성자, get, set 함수까지 끝
}