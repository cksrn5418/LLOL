package org.techtown.klab

data class FreeData(
    var title:String,
    var name:String,
    var date:String,
    var maintext:String,
    var comment:ArrayList<Comment>) {
}