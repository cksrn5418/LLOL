package org.techtown.klab

import android.content.res.ColorStateList
import android.drm.DrmStore
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.ActionBar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Button
import kotlinx.android.synthetic.main.custom_bar.view.*


class MainActivity : AppCompatActivity(), MainFragment.OnFragmentInteractionListener, TalentWriteFragment.OnFragmentInteractionListener, TalentFragment.OnFragmentInteractionListener, FreeFragment.OnFragmentInteractionListener, FreeWriteFragment.OnFragmentInteractionListener, FreeReadFragment.OnFragmentInteractionListener, MypageFragment.OnFragmentInteractionListener {

    lateinit var builder:AlertDialog.Builder

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
    lateinit var actionbar:ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        GlobalApplication.Data_Setting()
        Init()
        dialog()
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.bottom_home -> {
                actionbar.customView.title.text = "홈"
                builder.setMessage("추천 재능 목록이 나옵니다!!")
                main_write.show()
                replaceFragment(MainFragment())
            }
            R.id.bottom_list -> {
                actionbar.customView.title.text = "재능목록"
                builder.setMessage("검색 옵션에 맞는 재능 목록이 나옵니다!!")
                main_write.hide()
                replaceFragment(TalentFragment())
            }
            R.id.bottom_free -> {
                actionbar.customView.title.text = "게시판"
                builder.setMessage("다른 사용자들과 자유롭게 이야기할 수 있습니다!!")
                main_write.hide()
                replaceFragment(FreeFragment())
            }
            R.id.bottom_my -> {
                actionbar.customView.title.text = "마이페이지"
                builder.setMessage("내 정보와 수강중인 재능 및 재능기부 목록 등을 볼 수 있습니다!!")
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
        actionbar = this.supportActionBar!!
        actionbar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionbar.setCustomView(R.layout.custom_bar)
        actionbar.customView.title.text = "홈"
        transaction = fragmentManager.beginTransaction()
        transaction.add(R.id.fragment, MainFragment())
        transaction.commit()
        actionbar.customView.findViewById<Button>(R.id.question).setOnClickListener {
            builder.show()
        }
    }
    fun dialog(){
        builder = AlertDialog.Builder(this)
        builder.setTitle("도움말")
        builder.setMessage("추천 재능 목록이 나옵니다!!")
        builder.setPositiveButton("확인") { dialog, which ->
        }
    }
}
