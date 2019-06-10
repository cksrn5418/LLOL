package org.techtown.klab

import android.graphics.Bitmap

data class User(var id:String, var nickname:String, var profile:Bitmap, var phonenum:String = "", var age:String = "", var flag:Int = -1) {
}