package org.techtown.klab

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.ActionBar
import android.widget.ArrayAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kakao.auth.KakaoSDK
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.fragment_free_write.*
import kotlinx.android.synthetic.main.fragment_main.*


class GlobalApplication : Application() {

    companion object {
        var instance: GlobalApplication? = null
        var hobby_list:ArrayList<MyTalent> = ArrayList()
        var health_list:ArrayList<MyTalent> = ArrayList()
        var study_list:ArrayList<MyTalent> = ArrayList()
        var free_list:ArrayList<FreeData> = ArrayList()
        var recommend_list:ArrayList<MyTalent> = ArrayList()
        var my_list:ArrayList<MyTalent> = ArrayList()
        var free_index:Int = -1
        var user:User = User()
        var selected_lecture = MyTalent()
        lateinit var actionbar: ActionBar

        @SuppressLint("NewApi")
        fun Bitmap_to_String(img: Bitmap) : String{
            val byteArrayOutputStream = ByteArrayOutputStream()
            img.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val imageBytes = byteArrayOutputStream.toByteArray()
            return Base64.getEncoder().encodeToString(imageBytes)
        }

        @SuppressLint("NewApi")
        fun String_to_Bitmap(str:String) : Bitmap{
            val imageByteArray = Base64.getDecoder().decode(str) //String을 바이트 Array로 Decode한다/
           return BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
        }

        fun Dataset(){
            var database = FirebaseDatabase.getInstance()
            var myRef = database.getReference("DB/Talents")

            myRef.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var my = ArrayList<MyTalent>()
                    var hobby = ArrayList<MyTalent>()
                    var health = ArrayList<MyTalent>()
                    var study = ArrayList<MyTalent>()
                    var recommend = ArrayList<MyTalent>()

                    for(data in dataSnapshot.children) {
                        val pdata = data.getValue(MyTalent::class.java)
                        if(pdata!!.userid == GlobalApplication.user.id)
                            my.add(pdata!!)
                        if(pdata!!.category == "0"){
                            hobby.add(pdata)
                        }else if(pdata!!.category == "1"){
                            health.add(pdata)
                        }else if(pdata!!.category == "2"){
                            study.add(pdata)
                        }
                        recommend.add(pdata!!)
                    }

                    GlobalApplication.my_list = my
                    GlobalApplication.hobby_list = hobby
                    GlobalApplication.health_list = health
                    GlobalApplication.study_list = study
                    GlobalApplication.recommend_list = recommend
                }
            })

            var database1 = FirebaseDatabase.getInstance()
            var myRef1 = database1.getReference("DB/Free")
            //var talents = myRef.push();//id 자동생성
            myRef1.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    free_list = ArrayList()
                    for(data in dataSnapshot.children) {
                        val pdata = data.getValue(FreeData::class.java)
                        free_list.add(pdata!!)
                    }
                }
            })
        }
    }


    fun getGlobalApplicationContext(): GlobalApplication {
        if (instance == null) {
            throw IllegalStateException("This Application does not inherit com.kakao.GlobalApplication")
        }
        return instance as GlobalApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        // Kakao Sdk 초기화
        KakaoSDK.init(KakaoSDKAdapter())

        var database = FirebaseDatabase.getInstance()
        var myRef = database.getReference("DB/Talents")

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var recommend = ArrayList<MyTalent>()

                for(data in dataSnapshot.children) {
                    val pdata = data.getValue(MyTalent::class.java)
                    recommend.add(pdata!!)
                }

                GlobalApplication.recommend_list = recommend
            }
        })
    }

    override fun onTerminate() {
        super.onTerminate()
        instance = null
    }
}