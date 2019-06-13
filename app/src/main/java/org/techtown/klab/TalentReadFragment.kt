package org.techtown.klab

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_talent_read.*
import java.lang.Exception
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_bar.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TalentReadFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TalentReadFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class TalentReadFragment : Fragment(), OnMapReadyCallback {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

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
        return inflater.inflate(R.layout.fragment_talent_read, container, false)
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var mapFragment = childFragmentManager.findFragmentById(org.techtown.klab.R.id.talentread_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        var flag = false

        for(i in 0 until GlobalApplication.user.mytalent.size){
            if(GlobalApplication.selected_lecture.title == GlobalApplication.user.mytalent[i].title){
                flag = true
            }
        }

        if(flag){
            talentread_save.text = "이미 신청됨"
            talentread_save.isEnabled = false
            talentread_save.backgroundTintList = context!!.resources.getColorStateList(R.color.Already)
        }


        talentread_save.setOnClickListener {
            val phoneNo = GlobalApplication.selected_lecture.phonenum
            val sms = "[재능시장] 재능시장어플에서 \"" + GlobalApplication.selected_lecture.title + "\"을(를) 보고 연락드렸습니다!"

            try {
                //전송
                var builder = AlertDialog.Builder(context)
                builder.setTitle("신청 확인").setMessage("확인을 누르시면 데이터가 저장되며 강의 제공자에게 문자가 발송됩니다.").setPositiveButton("확인", DialogInterface.OnClickListener { dialog, which ->
                    val message = Uri.parse("sms:$phoneNo")
                    val messageIntent = Intent(
                        Intent.ACTION_SENDTO, message
                    )
                    messageIntent.putExtra("sms_body", sms)
                    startActivity(messageIntent)
                    Toast.makeText(context, "전송하였습니다", Toast.LENGTH_SHORT).show()

                    GlobalApplication.user.mytalent.add(GlobalApplication.selected_lecture)

                    var database = FirebaseDatabase.getInstance()
                    var myRef = database.getReference("Users")
                    myRef.child(GlobalApplication.user.id).setValue(GlobalApplication.user)

                }).setNegativeButton("취소", DialogInterface.OnClickListener { dialog, which ->
                    Toast.makeText(context, "취소하였습니다", Toast.LENGTH_SHORT).show()
                }).show()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        talentread_cancel.setOnClickListener {
            var fragmentManager = fragmentManager
            var fragmentTransaction = fragmentManager!!.beginTransaction()
            GlobalApplication.actionbar.customView.title.text = "홈"
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.fragment, MainFragment())
            fragmentTransaction.commit()
        }

        if(GlobalApplication.selected_lecture.category == "0")
            talentread_category.text = "카테고리 : 취미활동"
        else if(GlobalApplication.selected_lecture.category == "1")
            talentread_category.text = "카테고리 : 건강 및 운동"
        else if(GlobalApplication.selected_lecture.category == "2")
            talentread_category.text = "카테고리 : 교육"

        talentread_duedate.text = "신청마감일자 : " + GlobalApplication.selected_lecture.dueyear + "년 " + (GlobalApplication.selected_lecture.duemonth.toInt() + 1) + "월 " + GlobalApplication.selected_lecture.dueday + "일"

        talentread_img.setImageBitmap(GlobalApplication.String_to_Bitmap(GlobalApplication.selected_lecture.image))

        talentread_maintext.text = GlobalApplication.selected_lecture.maintext

        talentread_people.text = "인원 : " + GlobalApplication.selected_lecture.people_amount + "명"

        talentread_title.text = GlobalApplication.selected_lecture.title

        talentread_searchloc.text = "교육장소 : " + GlobalApplication.selected_lecture.addresstext
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TalentReadFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TalentReadFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onMapReady(googlemap: GoogleMap?) {
        var mMap = googlemap

        var point = LatLng(GlobalApplication.selected_lecture.latitude.toDouble()
            , GlobalApplication.selected_lecture.longitude.toDouble())
        var mOptions = MarkerOptions()
        mOptions.title("교육 장소")
        mOptions.snippet(GlobalApplication.selected_lecture.addresstext)
        mOptions.position(point)
        // 마커 추가
        mMap?.addMarker(mOptions)

        var point1 = LatLng(GlobalApplication.user.latitude.toDouble()
            , GlobalApplication.user.longitude.toDouble())
        var mOptions1 = MarkerOptions()
        mOptions1.title("내가 등록한 위치")
        mOptions1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        mOptions1.position(point1)
        // 마커 추가
        mMap?.addMarker(mOptions1)

        // 해당 좌표로 화면 줌
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15F))
    }
}
