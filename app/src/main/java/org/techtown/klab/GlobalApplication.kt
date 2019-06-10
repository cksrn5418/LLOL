package org.techtown.klab

import android.app.Application
import com.kakao.auth.KakaoSDK

class GlobalApplication : Application() {
    companion object {
        var instance: GlobalApplication? = null
        var hobby_list:ArrayList<MyTalent> = ArrayList()
        var health_list:ArrayList<MyTalent> = ArrayList()
        var study_list:ArrayList<MyTalent> = ArrayList()
        var free_list:ArrayList<FreeData> = ArrayList()
        var free_index:Int = -1
        lateinit var user:User

        fun Data_Setting(){
            hobby_list.add(MyTalent(100, "이윤성", "꽃꽃이 단기", 10, "공부하러오세요"))
            hobby_list.add(MyTalent(100, "오지연", "전국 식도락 탐험", 8, "공부하러오세요"))
            hobby_list.add(MyTalent(100, "이주형", "수제맥주 만들기", 10, "공부하러오세요"))
            hobby_list.add(MyTalent(100, "이찬구", "장기 한판?", 20, "공부하러오세요"))

            health_list.add(MyTalent(100, "이윤성", "스쿼트는 나에게 맡겨", 4, "사실운동못해요..^^"))
            health_list.add(MyTalent(100, "이윤성", "신앙기도, 간증", 4, "사실불교에요..^^"))
            health_list.add(MyTalent(100, "오지연", "줌바댄스 추러 오세요", 4, "사실춤못춰요..^^"))
            health_list.add(MyTalent(100, "이주형", "건강하게 술마시는 비법", 4, "사실술못마셔요..^^"))

            study_list.add(MyTalent(100, "오지연", "영어 회화", 2, "미쿸인입니다"))
            study_list.add(MyTalent(100, "이윤성", "올바른 글쓰기", 2, "한쿸인입니다"))
            study_list.add(MyTalent(100, "이찬구", "포토샵 교육", 2, "사람입니다"))

            free_list.add(FreeData("꽃꽃이 너무 재밌지 않아요?", "이윤자", "2019.04.23", "꽃꽃이꿀잼", ArrayList()))
            free_list.add(FreeData("하루에 운동 얼마나 하세요?", "이주똥", "2019.04.23", "꽃꽃이꿀잼", ArrayList()))
            free_list.add(FreeData("올해 무료건강검진 날짜 언제죠?", "오지순", "2019.04.20", "꽃꽃이꿀잼", ArrayList()))
            free_list.add(FreeData("요즘 공부가 너무 재밌어요", "이찬룡", "2019.04.10", "꽃꽃이꿀잼", ArrayList()))
            free_list.add(FreeData("금강산복지센터에서 무료취업교육하네요!", "배진자", "2019.04.01", "꽃꽃이꿀잼", ArrayList()))
        }
    }


    fun getGlobalApplicationContext(): GlobalApplication {
        if (instance == null) {
            throw IllegalStateException("This Application does not inherit com.kakao.GlobalApplication")
        }
        return instance as GlobalApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        // Kakao Sdk 초기화
        KakaoSDK.init(KakaoSDKAdapter())
    }

    override fun onTerminate() {
        super.onTerminate()
        instance = null
    }
}