package org.techtown.klab

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class FreeDataAdapter(context: Context, val resource:Int, var list:ArrayList<FreeData>)
    : ArrayAdapter<FreeData>(context,resource,list) {
    // ctrl + O = 오버라이딩 가능한 함수 목록
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var v: View? = convertView

        if(v== null){ //만들어진 view가 없다면
            val vi = context.applicationContext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            v = vi.inflate(resource, null)

        }

        val p = list.get(position)
        v!!.findViewById<TextView>(R.id.freerow_name).text = p.name
        v!!.findViewById<TextView>(R.id.freerow_title).text = p.title
        v!!.findViewById<TextView>(R.id.freerow_date).text = p.date

        return v // 만들었으니 만든거 return
        //return super.getView(position, convertView, parent)
    }
}