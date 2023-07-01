package com.abisayo.chemfootball.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abisayo.chemfootball.CoursesRepository


class CoursesViewModel : ViewModel() {

    private val repository : CoursesRepository
    private val _allCourses = MutableLiveData<List<Courses>>()
    val allCourses : LiveData<List<Courses>> = _allCourses

    init {
        repository = CoursesRepository().getInstance()
        repository.loadCourses(_allCourses)
    }
}
