package com.abisayo.chemfootball.displaySS2Courses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abisayo.chemfootball.CoursesRepository
import com.abisayo.chemfootball.models.Courses

class SS2CoursesViewModel : ViewModel() {

    private val repository : SS2CoursesRepo
    private val _allCourses = MutableLiveData<List<Courses>>()
    val allCourses : LiveData<List<Courses>> = _allCourses

    init {
        repository = SS2CoursesRepo().getInstance()
        repository.loadCourses(_allCourses)
    }
}