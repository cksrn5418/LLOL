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
import android.widget.Toast
import com.google.firebase.database.*
import com.kakao.usermgmt.LoginButton
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivitiy : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Init()
    }
    
    fun Init(){
        supportActionBar!!.hide()
        var callback = SessionCallback(this)
        Session.getCurrentSession().addCallback(callback)

        login_kakao.setOnClickListener {
            it.isClickable = false
            Log.v("안열려?", "ㅇㅇ")
        }
    }

    class SessionCallback : ISessionCallback {

        var context:Activity
        lateinit var btn:LoginButton

        constructor(context:Activity){
            this.context = context
        }

        override fun onSessionOpenFailed(exception: KakaoException?) {
            Log.v("SessionCallback :: ", "onSessionOpenFailed : " + exception.toString())
        }

        override fun onSessionOpened() {
            btn = context.findViewById(R.id.login_kakao)
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

                    var database = FirebaseDatabase.getInstance()
                    var myRef = database.getReference("Users")

                    myRef.addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                            var builder = AlertDialog.Builder(context)
                            builder.setCancelable(false)
                            builder.setTitle("로그인 실패!").setMessage("회원 정보가 없습니다. 회원가입창으로 이동합니다.").setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                                dialog.dismiss()
                                GlobalApplication.user.id = userProfile.id.toString()
                                GlobalApplication.user.profile = GlobalApplication.Bitmap_to_String(bmp!!)
                                GlobalApplication.user.nickname = userProfile.nickname
                                GlobalApplication.user.flag = (-1).toString()

                                val nextIntent = Intent(context, Register::class.java)
                                context.startActivity(nextIntent)
                            })
                            for(data in dataSnapshot.children){
                                val pdata = data.getValue(User::class.java)

                                if(pdata!!.id == userProfile.id.toString()) {
                                    GlobalApplication.user = pdata

                                    builder.setTitle("로그인 성공!").setMessage("로그인 성공하였습니다. 확인을 누르시면 다음 페이지로 넘어갑니다").setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                                        dialog.dismiss()
                                        val nextIntent = Intent(context, MainActivity::class.java)
                                        nextIntent.putExtra("register", true)
                                        context.startActivity(nextIntent)
                                    })
                                }
                            }
                            builder.show()
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }
                    })
                }
                // 사용자 정보 요청 실패
                override fun onFailure(errorResult: ErrorResult?) {
                    Log.e("SessionCallback :: ", "onFailure : " + errorResult!!.errorMessage)
                }
            })

            btn.isClickable = true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == 123) {
            if (resultCode == Activity.RESULT_OK) {
                val pass = data?.getStringExtra("pass")
                Toast.makeText(this, pass, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
