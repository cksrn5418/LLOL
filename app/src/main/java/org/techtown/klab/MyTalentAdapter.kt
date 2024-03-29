package org.techtown.klab

import android.app.Activity
import android.support.v4.app.Fragment
import android.content.Context
import android.media.Image
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.custom_bar.view.*


class MyTalentAdapter(context: Context, val resource:Int, var list:ArrayList<MyTalent>)
    :ArrayAdapter<MyTalent>(context,resource,list)
{

    // ctrl + O = 오버라이딩 가능한 함수 목록
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var v:View? = convertView

        if(v== null){ //만들어진 view가 없다면
            val vi = context.applicationContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            v = vi.inflate(resource, null)

        }

        val p = list.get(position)
        v!!.findViewById<TextView>(R.id.row_profilename).text = p.name
        v!!.findViewById<TextView>(R.id.row_title).text = p.title
        v!!.findViewById<TextView>(R.id.row_amount).text = p.people_amount.toString()
        if(p.image != null){
            v!!.findViewById<ImageView>(R.id.row_image).setImageBitmap(GlobalApplication.String_to_Bitmap(p.image!!))
        }
        if(p.profileimg != null){
            v!!.findViewById<ImageView>(R.id.row_profileimg).setImageBitmap(GlobalApplication.String_to_Bitmap(p.profileimg!!))
        }

        v.setOnClickListener {
            GlobalApplication.selected_lecture = p
            var fragmentManager = (context as FragmentActivity).supportFragmentManager
            var fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right)
            GlobalApplication.actionbar.customView.title.text = "재능 확인"
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.fragment, TalentReadFragment())
            fragmentTransaction.commit()
        }

        return v // 만들었으니 만든거 return
        //return super.getView(position, convertView, parent)
    }
}