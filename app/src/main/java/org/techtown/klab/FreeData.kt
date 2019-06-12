package org.techtown.klab

data class FreeData(
    var title:String,
    var name:String,
    var date:String,
    var maintext:String,
    var comment:ArrayList<Comment>) {
    constructor() : this("0","0", "0", "0", ArrayList())
}