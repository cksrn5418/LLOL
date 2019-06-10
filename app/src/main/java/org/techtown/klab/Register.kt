package org.techtown.klab

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.AlertDialog
import android.widget.ArrayAdapter
import android.widget.Spinner
import kotlinx.android.synthetic.main.activity_register.*

class Register : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        Init()
    }

    fun Init(){
        var age = ArrayList<String>()
        for(i in 1..100)
            age.add(i.toString())
        register_agespinner.adapter = ArrayAdapter<String>(applicationContext, R.layout.support_simple_spinner_dropdown_item, age)
        register_agespinner.gravity = Spinner.TEXT_ALIGNMENT_CENTER
        register_image.setImageBitmap(GlobalApplication.user.profile)
        register_name.text = GlobalApplication.user.nickname

        register_radiogroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.register_hobby -> {
                    register_health.isChecked = false
                    register_study.isChecked = false
                    GlobalApplication.user.flag = 1
                }
                R.id.register_health -> {
                    register_hobby.isChecked = false
                    register_study.isChecked = false
                    GlobalApplication.user.flag = 2
                }
                R.id.register_study -> {
                    register_hobby.isChecked = false
                    register_health.isChecked = false
                    GlobalApplication.user.flag = 3
                }
            }
        }

        lateinit var builder : AlertDialog.Builder

        register_btn.setOnClickListener {
            builder = AlertDialog.Builder(this)
            builder.setCancelable(false)
            if(register_radiogroup.checkedRadioButtonId == -1) {
                builder.setTitle("회원가입 실패!").setMessage("관심 분야에 하나라도 체크하셔야 합니다.")
                    .setPositiveButton("확인") { dialog, which ->
                        dialog.dismiss()
                    }
            }else{
                builder.setTitle("회원가입 성공!").setMessage("회원가입에 성공하였습니다! 메인 화면으로 이동합니다.")
                    .setPositiveButton("확인") { dialog, which ->
                        GlobalApplication.user.age = register_agespinner.selectedItem.toString()
                        GlobalApplication.user.phonenum = register_phonenum.text.toString()
                        dialog.dismiss()
                        val nextIntent = Intent(this, MainActivity::class.java)
                        startActivity(nextIntent)
                    }
            }
            builder.show()
        }
    }
}
