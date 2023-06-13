package com.abisayo.chemfootball.models

data class Courses(var courseTitle: String ?= null, var note : String ?= null)

data class Scores(var studentName: String ?= null, var courseTitle: String ?= null, var studentScore : String ?= null, val userId: String?= null)
data class Questions(var questNum : Int? = null, var question: String ? = null, var option1 : String ?= null, var option2 : String? = null, var option3 : String ?= null, var option4 : String? = null)
data class Player(var name : String? = null, var trialNum: String ? = null, var score : String ?= null)

data class Multiplayer(
    var name: String?= null,
    var opponent_name : String?= null,
    var code: String?= null,
    var scoreStatus: String?= null,
    var my_score : String? = null,
    var opponent_score : String?=null,
    var trialNum: String? = null,
    var whoPlayFirst: String? = null
)
