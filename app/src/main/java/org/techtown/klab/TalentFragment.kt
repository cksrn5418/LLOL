package org.techtown.klab

import android.content.Context
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_talent.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TalentFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TalentFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class TalentFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_talent, container, false)
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

        var list: ArrayList<MyTalent>

        if(GlobalApplication.user.flag == "0"){
            talent_hobbybtn.performClick()
        }else if(GlobalApplication.user.flag == "1"){
            talent_healthbtn.performClick()
        }else if(GlobalApplication.user.flag == "2"){
            talent_studybtn.performClick()
        }

        talent_hobbybtn.setOnClickListener {
            list = GlobalApplication.hobby_list
            var adapter = MyTalentAdapter(context!!, R.layout.talent_row, list)
            talent_listview.adapter = adapter
        }
        talent_healthbtn.setOnClickListener {
            list = GlobalApplication.health_list
            var adapter = MyTalentAdapter(context!!, R.layout.talent_row, list)
            talent_listview.adapter = adapter
        }
        talent_studybtn.setOnClickListener {
            list = GlobalApplication.study_list
            var adapter = MyTalentAdapter(context!!, R.layout.talent_row, list)
            talent_listview.adapter = adapter
        }

        talent_searchbtn.setOnClickListener {
            list = ArrayList()
            for(i in 0 until GlobalApplication.recommend_list.size) {
                if (GlobalApplication.recommend_list[i].title.matches(Regex(".*" + talent_search.text.toString() +".*"))){
                    list.add(GlobalApplication.recommend_list[i])
                }
            }
            var adapter = MyTalentAdapter(context!!, R.layout.talent_row, list)
            talent_listview.adapter = adapter
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
         * @return A new instance of fragment TalentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TalentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
