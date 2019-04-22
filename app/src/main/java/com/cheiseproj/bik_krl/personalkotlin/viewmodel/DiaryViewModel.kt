package com.cheiseproj.bik_krl.personalkotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import com.cheiseproj.bik_krl.personalkotlin.data.db.entity.CategoryEntity
import com.cheiseproj.bik_krl.personalkotlin.data.db.entity.DiaryEntity
import com.cheiseproj.bik_krl.personalkotlin.data.repository.DiaryRepository
import com.cheiseproj.bik_krl.personalkotlin.di.module.OBSERVER_ON
import com.cheiseproj.bik_krl.personalkotlin.di.module.SUBSCRIBER_ON
import io.reactivex.Scheduler
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class DiaryViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository,
    @param:Named(SUBSCRIBER_ON) private val subscriberOn:Scheduler,
    @param:Named(OBSERVER_ON) private val observerOn:Scheduler

) :BaseViewModel(){
    val userDiary:MutableLiveData<List<DiaryEntity>> = MutableLiveData()
    val diaryCategory:MutableLiveData<List<CategoryEntity>> = MutableLiveData()
    val galleryLiveImage:MutableLiveData<ArrayList<String>> = MutableLiveData()
    val diaryId:MutableLiveData<Int> = MutableLiveData()

    fun getUserDiary(userId:Int){
        disposable.addAll(diaryRepository.getCurrentUserDiary(userId)
            .subscribeOn(subscriberOn)
            .observeOn(observerOn)
            .doOnSubscribe {  }
            .doOnComplete {  }
            .doOnError {  }
            .subscribe { userDiary.value = it })
    }

    fun getDiaryCategory(){
        disposable.addAll(diaryRepository.getDiaryCategory()
            .subscribeOn(subscriberOn)
            .observeOn(observerOn)
            .subscribe{diaryCategory.value = it})
    }

    fun insertUserDiary(diaryEntity: DiaryEntity){
        disposable.addAll(diaryRepository.insertUserDiary(diaryEntity)
            .subscribeOn(subscriberOn)
            .observeOn(observerOn)
            .doOnError { error -> Timber.i(error,"Data Insert Failed") }
            .subscribe {id -> diaryId.value = id.toInt(); Timber.i("Diary Inserted Successfully, Diary Id: $id");})
    }

    fun insertUserPhotos(galleryPhotos: ArrayList<String>?,diaryId:Int) {
        galleryPhotos?.let {p -> disposable.addAll(diaryRepository.insertUserDiaryPhoto(p,0,diaryId)
            .subscribeOn(subscriberOn)
            .observeOn(observerOn)
            .doOnError { error -> Timber.i(error,"Photos Insert Failed") }
            .subscribe {photoId -> Timber.i("Photos Inserted Successfully, Ids: $photoId") }) }
    }


}