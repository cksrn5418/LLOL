package org.techtown.klab

import android.graphics.Bitmap
import com.google.firebase.database.Exclude
import com.google.firebase.database.PropertyName

data class User(
    @PropertyName("id") var id:String,
    @PropertyName("nickname") var nickname:String,
    @PropertyName("profile") var profile:String,
    @PropertyName("phonenum") var phonenum:String = "",
    @PropertyName("age") var age:String = "",
    @PropertyName("flag") var flag:String = (-1).toString()
)