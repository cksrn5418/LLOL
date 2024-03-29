package org.techtown.klab

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.ActionBar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Button
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.custom_bar.view.*
import kotlinx.android.synthetic.main.fragment_main.*


class MainActivity : AppCompatActivity(), MainFragment.OnFragmentInteractionListener, TalentWriteFragment.OnFragmentInteractionListener, TalentReadFragment.OnFragmentInteractionListener, TalentFragment.OnFragmentInteractionListener, FreeFragment.OnFragmentInteractionListener, FreeWriteFragment.OnFragmentInteractionListener, FreeReadFragment.OnFragmentInteractionListener, MypageFragment.OnFragmentInteractionListener {

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun replaceFragment(fragment:Fragment){
        var fragmentManager = supportFragmentManager
        var fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.replace(R.id.fragment, fragment)
        fragmentTransaction.commit()
    }

    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    private val fragmentManager = supportFragmentManager
    private lateinit var transaction: FragmentTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GlobalApplication.navigation = navigation
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        Init()
        dialog()
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.bottom_home -> {
                GlobalApplication.actionbar.customView.title.text = "홈"
                GlobalApplication.builder.setMessage("추천 재능 목록이 나옵니다.\n\n오른쪽 버튼을 통해 강의를 거리순, 인기순으로 확인할 수 있습니다.\n\n오른쪽 아래 펜모양 버튼을 누르면 재능 등록이 가능합니다.")
                main_write.show()
                replaceFragment(MainFragment())
            }
            R.id.bottom_list -> {
                GlobalApplication.actionbar.customView.title.text = "재능목록"
                GlobalApplication.builder.setMessage("위 검색창을 통해 재능을 검색할 수 있습니다.\n\n카테고리별로 강의를 확인할 수 있습니다.")
                main_write.hide()
                replaceFragment(TalentFragment())
            }
            R.id.bottom_free -> {
                GlobalApplication.actionbar.customView.title.text = "게시판"
                GlobalApplication.builder.setMessage("다른 사용자들과 자유롭게 이야기할 수 있습니다.")
                main_write.hide()
                replaceFragment(FreeFragment())
            }
            R.id.bottom_my -> {
                GlobalApplication.actionbar.customView.title.text = "마이페이지"
                GlobalApplication.builder.setMessage("내 정보 확인 및 수정이 가능하고 수강중인 재능 및 재능기부 목록을 볼 수 있습니다.")
                main_write.hide()
                replaceFragment(MypageFragment())
            }
        }
        true
    }

    fun Init(){
        main_write.setOnClickListener{
            replaceFragment(TalentWriteFragment())
        }

        GlobalApplication.actionbar = this.supportActionBar!!
        GlobalApplication.actionbar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        GlobalApplication.actionbar.setCustomView(R.layout.custom_bar)
        GlobalApplication.actionbar.customView.title.text = "홈"

        GlobalApplication.Dataset()

        transaction = fragmentManager.beginTransaction()
        transaction.add(R.id.fragment, MainFragment())
        transaction.commit()
        GlobalApplication.actionbar.customView.findViewById<Button>(R.id.question).setOnClickListener {
            GlobalApplication.builder.show()
        }
    }

    fun dialog(){
        GlobalApplication.builder = AlertDialog.Builder(this)
        GlobalApplication.builder.setTitle("도움말")
        GlobalApplication.builder.setMessage("추천 재능 목록이 나옵니다.\n\n오른쪽 버튼을 통해 강의를 거리순, 인기순으로 확인할 수 있습니다.\n\n오른쪽 아래 펜모양 버튼을 누르면 재능 등록이 가능합니다.")
        GlobalApplication.builder.setPositiveButton("확인") { dialog, which ->
        }
    }
}
