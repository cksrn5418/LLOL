package org.techtown.klab

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeResponseCallback
import com.kakao.usermgmt.response.model.UserProfile
import com.kakao.util.exception.KakaoException
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import android.content.pm.PackageManager
import android.content.pm.PackageInfo
import android.util.Base64
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase


class LoginActivitiy : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Init()
    }
    fun Init(){
        var database = FirebaseDatabase.getInstance()
        var myRef = database.getReference("message")
        myRef.setValue("Hello, World!")

        supportActionBar!!.hide()
        var callback = SessionCallback(this)
        Session.getCurrentSession().addCallback(callback)
    }

    class SessionCallback : ISessionCallback {

        var context:Activity

        constructor(context:Activity){
            this.context = context
        }

        override fun onSessionOpenFailed(exception: KakaoException?) {
            Log.v("SessionCallback :: ", "onSessionOpenFailed : " + exception.toString())
        }

        override fun onSessionOpened() {
            requestMe()
        }

        fun requestMe() {

            // 사용자정보 요청 결과에 대한 Callback
            UserManagement.getInstance().requestMe(object : MeResponseCallback() {
                // 세션 오픈 실패. 세션이 삭제된 경우,
                override fun onSessionClosed(errorResult: ErrorResult) {
                    Log.e("SessionCallback :: ", "onSessionClosed : " + errorResult.errorMessage)
                }

                // 회원이 아닌 경우,
                override fun onNotSignedUp() {
                    Log.e("SessionCallback :: ", "onNotSignedUp")
                }

                // 사용자정보 요청에 성공한 경우,
                override fun onSuccess(userProfile: UserProfile) {

                    Log.e("SessionCallback :: ", "onSuccess")

                    var bmp:Bitmap? = null
                    var flag = false

                    Thread(Runnable {
                        try {
                            var url = URL(userProfile.profileImagePath)
                            var con = url.openConnection() as HttpURLConnection
                            con.doInput
                            con.connect()
                            bmp = BitmapFactory.decodeStream(url.openStream())
                            con.disconnect()
                            flag = true
                        } catch (e: Exception) {
                            Log.v("Error : ", e.toString())
                        }
                    }).start()

                    while(!flag){}

                    GlobalApplication.user = User(userProfile.id.toString(), userProfile.nickname, GlobalApplication.Bitmap_to_String(bmp!!))

                    Log.v("로그 : ", userProfile.id.toString())

                    var builder = AlertDialog.Builder(context)
                    builder.setCancelable(false)
                    builder.setTitle("로그인 성공!").setMessage("로그인 성공하였습니다. 확인을 누르시면 다음 페이지로 넘어갑니다").setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()
                        val nextIntent = Intent(context, Register::class.java)
                        context.startActivity(nextIntent)
                    })
                    builder.show()
                }

                // 사용자 정보 요청 실패
                override fun onFailure(errorResult: ErrorResult?) {
                    Log.e("SessionCallback :: ", "onFailure : " + errorResult!!.errorMessage)
                }
            })
        }
    }
}
