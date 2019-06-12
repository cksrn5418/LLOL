package org.techtown.klab

import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import kotlinx.android.synthetic.main.fragment_free_read.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FreeReadFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FreeReadFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class FreeReadFragment : Fragment() {
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

    fun setListViewHeightBasedOnChildren(listView:ListView) {
        var listAdapter: ListAdapter? = listView.adapter
            ?: // pre-condition
            return

        var totalHeight = 0

        var desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (i in 0 until listAdapter!!.count) {
            var listItem = listAdapter.getView(i, null, listView);
            //listItem.measure(0, 0);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.measuredHeight;
        }
        var params = listView.layoutParams

        params.height = totalHeight
        listView.layoutParams = params;

        listView.requestLayout()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var position = GlobalApplication.free_index
        freeread_title.text = "제목 : " + GlobalApplication.free_list[position].title
        freeread_maintext.text = GlobalApplication.free_list[position].maintext
        freeread_comment.adapter = CommentAdapter(context!!, R.layout.comment_row, GlobalApplication.free_list[position].comment)
        setListViewHeightBasedOnChildren(freeread_comment)

        freeread_commentbtn.setOnClickListener {
            GlobalApplication.free_list[position].comment.add(0, Comment(GlobalApplication.user.profile, GlobalApplication.user.nickname, freeread_writecomment.text.toString()))
            freeread_writecomment.setText("")
            freeread_comment.adapter = CommentAdapter(context!!, R.layout.comment_row, GlobalApplication.free_list[position].comment)
            setListViewHeightBasedOnChildren(freeread_comment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_free_read, container, false)
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
         * @return A new instance of fragment FreeReadFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FreeReadFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
