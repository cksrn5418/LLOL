package org.techtown.klab

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.text.style.UnderlineSpan
import android.text.SpannableString
import android.R
import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.TextView
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_mypage.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MypageFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MypageFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MypageFragment : Fragment() {
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
        return inflater.inflate(org.techtown.klab.R.layout.fragment_mypage, container, false)
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val content = SpannableString("회원정보 수정")
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        mypage_userbtn.text = content
        mypage_image.setImageBitmap(GlobalApplication.String_to_Bitmap(GlobalApplication.user.profile))
        mypage_name.text = GlobalApplication.user.nickname

        mypage_list.adapter = MyTalentAdapter(context!!, org.techtown.klab.R.layout.talent_row, GlobalApplication.user.mytalent)

        mypage_userbtn.setOnClickListener {
            val nextIntent = Intent(context, Register::class.java)
            nextIntent.putExtra("register", false)
            startActivity(nextIntent)
        }

        mypage_mytalent.setOnClickListener {
            mypage_list.adapter = MyTalentAdapter(context!!, org.techtown.klab.R.layout.talent_row, GlobalApplication.user.mytalent)
        }

        mypage_talenting.setOnClickListener {
            var list:ArrayList<MyTalent> = ArrayList()
            for(i in 0 until GlobalApplication.recommend_list.size){
                if(GlobalApplication.recommend_list[i].userid == GlobalApplication.user.id){
                    list.add(GlobalApplication.recommend_list[i])
                }
            }
            mypage_list.adapter = MyTalentAdapter(context!!, org.techtown.klab.R.layout.talent_row, list)
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
         * @return A new instance of fragment MypageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MypageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
