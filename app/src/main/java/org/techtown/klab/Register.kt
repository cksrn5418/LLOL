package org.techtown.klab

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.AlertDialog
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_talent_write.*
import java.lang.Exception

class Register : AppCompatActivity(), OnMapReadyCallback {

    var latitude:String = ""
    var longitude:String = ""
    var address:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        Init()
    }

    fun Init(){

        val i = intent
        var flag = i.getBooleanExtra("register", true)

        if(!flag){
            register_btn.text = "수정"
        }

        var age = ArrayList<String>()
        for(i in 1..100)
            age.add(i.toString())
        register_agespinner.adapter = ArrayAdapter<String>(applicationContext, R.layout.support_simple_spinner_dropdown_item, age)
        register_agespinner.gravity = Spinner.TEXT_ALIGNMENT_CENTER
        register_name.text = GlobalApplication.user.nickname

        register_radiogroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.register_hobby -> {
                    register_health.isChecked = false
                    register_study.isChecked = false
                    GlobalApplication.user.flag = 1.toString()
                }
                R.id.register_health -> {
                    register_hobby.isChecked = false
                    register_study.isChecked = false
                    GlobalApplication.user.flag = 2.toString()
                }
                R.id.register_study -> {
                    register_hobby.isChecked = false
                    register_health.isChecked = false
                    GlobalApplication.user.flag = 3.toString()
                }
            }
        }

        var mapFragment = supportFragmentManager.findFragmentById(R.id.register_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        lateinit var builder : AlertDialog.Builder

        register_btn.setOnClickListener {
            builder = AlertDialog.Builder(this)
            builder.setCancelable(false)
            if(register_radiogroup.checkedRadioButtonId == -1 || register_maploc.text.toString() == "") {
                builder.setTitle("회원가입 실패").setMessage("주소와 관심분야는 필수 항목입니다!")
                    .setPositiveButton("확인") { dialog, which ->
                        dialog.dismiss()
                    }
            }else{
                if(flag) {
                    builder.setTitle("회원가입 성공").setMessage("회원가입에 성공하였습니다! 메인 화면으로 이동합니다!")
                        .setPositiveButton("확인") { dialog, which ->
                            dialog.dismiss()

                            GlobalApplication.user.phonenum = register_phonenum.text.toString()
                            GlobalApplication.user.age = register_agespinner.selectedItem.toString()
                            GlobalApplication.user.latitude = latitude
                            GlobalApplication.user.longitude = longitude

                            var database = FirebaseDatabase.getInstance()
                            var myRef = database.getReference("Users")
                            myRef.child(GlobalApplication.user.id).setValue(GlobalApplication.user)

                            val nextIntent = Intent(this, MainActivity::class.java)
                            startActivity(nextIntent)
                        }
                }
                else{
                    builder.setTitle("수정 완료").setMessage("회원정보 수정 성공하였습니다! 메인 화면으로 이동합니다!")
                        .setPositiveButton("확인") { dialog, which ->
                            dialog.dismiss()

                            GlobalApplication.user.phonenum = register_phonenum.text.toString()
                            GlobalApplication.user.age = register_agespinner.selectedItem.toString()
                            GlobalApplication.user.latitude = latitude
                            GlobalApplication.user.longitude = longitude

                            var database = FirebaseDatabase.getInstance()
                            var myRef = database.getReference("Users")
                            myRef.child(GlobalApplication.user.id).setValue(GlobalApplication.user)

                            val nextIntent = Intent(this, MainActivity::class.java)
                            startActivity(nextIntent)
                        }
                }
            }
            builder.show()
        }
    }
    override fun onMapReady(googlemap: GoogleMap?) {
        var mMap = googlemap
        var geocoder = Geocoder(this)

        register_searchbtn.setOnClickListener {
            var str = register_searchloc.text.toString()
            lateinit var addressList:List<Address>

            if(str!= "") {
                try {
                    // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
                    addressList = geocoder.getFromLocationName(str, 10); // 최대 검색 결과 개수
                } catch (e: Exception) {
                    Log.v("Error : ", e.stackTrace.toString())
                }

                Log.v("AddressList[0]", addressList[0].toString())
                // 콤마를 기준으로 split
                var splitStr = addressList.get(0).toString().split(",");
                address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1, splitStr[0].length - 2); // 주소
                Log.v("address : ", address)
                register_maploc.text = "주소 : " + address

                latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
                longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도
                Log.v("latitude : ", latitude)
                Log.v("longitude : ", longitude)

                // 좌표(위도, 경도) 생성
                var point = LatLng(latitude.toDouble(), longitude.toDouble())
                // 마커 생성
                var mOptions2 = MarkerOptions()
                mOptions2.title("검색 결과")
                mOptions2.snippet(address)
                mOptions2.position(point)
                // 마커 추가
                mMap?.addMarker(mOptions2)
                // 해당 좌표로 화면 줌
                mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15F))
            }else{
                var builder = AlertDialog.Builder(this)
                builder.setTitle("에러!").setMessage("주소나 위치를 입력하셔야됩니다").setPositiveButton("확인"
                ) { dialog, which ->
                }.show()
            }
        }
    }
}
