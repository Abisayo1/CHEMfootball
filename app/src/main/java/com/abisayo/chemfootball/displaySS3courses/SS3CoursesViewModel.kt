package com.abisayo.chemfootball.displaySS3courses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abisayo.chemfootball.models.Courses

class SS3CoursesViewModel : ViewModel() {

    private val repository : SS3CoursesRepo
    private val _allCourses = MutableLiveData<List<Courses>>()
    val allCourses : LiveData<List<Courses>> = _allCourses

    init {
        repository = SS3CoursesRepo().getInstance()
        repository.loadCourses(_allCourses)
    }
}