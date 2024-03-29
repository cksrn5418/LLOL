package org.techtown.klab

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_main.*
import android.widget.BaseAdapter
import android.widget.Button
import kotlin.math.sqrt


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MainFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MainFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_main, container, false)
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

    fun calcDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val EARTH_R: Double
        val Rad: Double
        val radLat1: Double
        val radLat2: Double
        val radDist: Double
        var distance: Double
        val ret: Double

        EARTH_R = 6371000.0
        Rad = Math.PI / 180
        radLat1 = Rad * lat1
        radLat2 = Rad * lat2
        radDist = Rad * (lon1 - lon2)

        distance = Math.sin(radLat1) * Math.sin(radLat2)
        distance = distance + Math.cos(radLat1) * Math.cos(radLat2) * Math.cos(radDist)
        ret = EARTH_R * Math.acos(distance)

        return ret
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        actionbar!!.title = "홈"

        var spinnerArray = ArrayList<String>()
        spinnerArray.add("인기순")
        spinnerArray.add("거리순")
        var spinner_adapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, spinnerArray)
        main_spinner.adapter = spinner_adapter

        main_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position == 0){
                    GlobalApplication.recommend_list.sortByDescending {
                        it.count.toInt()
                    }
                    var adapter = MyTalentAdapter(context!!, R.layout.talent_row, GlobalApplication.recommend_list)
                    main_listview.adapter = adapter
                }
                else if(position == 1){
                    GlobalApplication.recommend_list.sortBy {
                        calcDistance(it.latitude.toDouble(), it.longitude.toDouble(),  GlobalApplication.user.latitude.toDouble(),  GlobalApplication.user.longitude.toDouble())
                    }
                    var adapter = MyTalentAdapter(context!!, R.layout.talent_row, GlobalApplication.recommend_list)
                    main_listview.adapter = adapter
                }
            }

        }
        super.onActivityCreated(savedInstanceState)
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
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
