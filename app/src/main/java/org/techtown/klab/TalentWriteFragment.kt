package org.techtown.klab

import android.content.Context
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.fragment_talent_write.*
import android.R
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Path
import android.graphics.drawable.BitmapDrawable
import android.location.Address
import android.support.v4.app.FragmentManager
import android.support.v7.widget.AlertDialogLayout
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.DatePicker
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.*
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.Exception
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TalentWriteFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TalentWriteFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class TalentWriteFragment : Fragment(), OnMapReadyCallback {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    var latitude:String = ""
    var longitude:String = ""
    var address:String = ""
    var mYear:Int = -1
    var mMonth:Int = -1
    var mDay:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(org.techtown.klab.R.layout.fragment_talent_write, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TalentWriteFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TalentWriteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        var mMap = googleMap
        var geocoder = Geocoder(view!!.context)

        mMap!!.setOnMapClickListener { point ->
            val mOptions = MarkerOptions()
            // 마커 타이틀
            mOptions.title("마커 좌표")
            val latitude = point!!.latitude // 위도
            val longitude = point!!.longitude // 경도
            // 마커의 스니펫(간단한 텍스트) 설정
            mOptions.snippet("$latitude, $longitude")
            // LatLng: 위도 경도 쌍을 나타냄
            mOptions.position(LatLng(latitude, longitude))
            // 마커(핀) 추가
            googleMap!!.addMarker(mOptions)
        }

        talentwrite_searchbtn.setOnClickListener {
            var str = talentwrite_searchloc.text.toString()
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
                talentwrite_maploc.text = "주소 : " + address

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
                mMap.addMarker(mOptions2)
                // 해당 좌표로 화면 줌
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15F))
            }else{
                var builder = AlertDialog.Builder(context)
                builder.setTitle("에러!").setMessage("주소나 위치를 입력하셔야됩니다").setPositiveButton("확인"
                ) { dialog, which ->
                }.show()
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var mapFragment = childFragmentManager.findFragmentById(org.techtown.klab.R.id.talentwrite_map) as SupportMapFragment
        mapFragment?.getMapAsync(this)

        var list1:ArrayList<String> = ArrayList()
        var list2:ArrayList<String> = ArrayList()

        list1.add("취미활동")
        list1.add("건강 및 운동")
        list1.add("교육")

        for(i in 0..100){
            list2.add(i.toString() + "명")
        }

        talentwrite_category.adapter = ArrayAdapter<String>(context, org.techtown.klab.R.layout.support_simple_spinner_dropdown_item, list1)
        talentwrite_people.adapter = ArrayAdapter<String>(context, org.techtown.klab.R.layout.support_simple_spinner_dropdown_item, list2)

        talentwrite_imguploadbtn.setOnClickListener {
            var intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, 1)
        }

        talentwrite_datebtn.setOnClickListener {
            val cal = GregorianCalendar()
            mYear = cal.get(Calendar.YEAR)
            mMonth = cal.get(Calendar.MONTH)
            mDay = cal.get(Calendar.DAY_OF_MONTH)
            var dialog = DatePickerDialog(context, mDateSetListener, mYear, mMonth, mDay)
            dialog.show()
        }

        talentwrite_cancel.setOnClickListener {
            replaceFragment(MainFragment())
        }

        talentwrite_save.setOnClickListener{
            var builder = AlertDialog.Builder(context)
            if(talentwrite_title.text.toString() == "" || talentwrite_people.selectedItemPosition == 0 || address == ""){
                builder.setTitle("실패").setMessage("필수 입력 항목에 내용을 채워주세요").setPositiveButton("확인") { dialog, which -> }
            }
            else {
                builder.setTitle("성공").setMessage("재능 등록이 완료되었습니다.").setPositiveButton("확인") { dialog, which -> }

                var img = (talentwrite_img.drawable as BitmapDrawable).bitmap

                GlobalApplication.recommend_list.add(
                    MyTalent(
                        GlobalApplication.user.id,
                        GlobalApplication.user.nickname,
                        talentwrite_title.text.toString(),
                        talentwrite_category.selectedItemPosition.toString(),
                        talentwrite_people.selectedItemPosition.toString(),
                        talentwrite_maintext.toString(),
                        mYear.toString(),
                        mMonth.toString(),
                        mDay.toString(),
                        talentwrite_datedetail.text.toString(),
                        address,
                        latitude,
                        longitude,
                        talentwrite_cost.toString(),
                        talentwrite_openchat.text.toString(),
                        0.toString(),
                        GlobalApplication.user.profile,
                        GlobalApplication.Bitmap_to_String(img)
                    )
                )
            }
            builder.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    talentwrite_img.setImageURI(data!!.data)
                } catch (e:Exception) {
                    Log.v("Error", e.printStackTrace().toString())
                }
            }
        }
    }

    fun replaceFragment(fragment:Fragment){
        var fragmentManager = fragmentManager
        var fragmentTransaction = fragmentManager!!.beginTransaction()
//        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.replace(org.techtown.klab.R.id.fragment, fragment)
        fragmentTransaction.commit()
    }

    var mDateSetListener = DatePickerDialog.OnDateSetListener() { datePicker: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
        mYear = year
        mMonth = monthOfYear
        mDay = dayOfMonth

        talentwrite_datebtn.text = mYear.toString() + "년 " + mMonth.toString() + "월 " + mDay.toString() + "일"
    }
}
