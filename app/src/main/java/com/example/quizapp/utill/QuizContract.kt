package com.example.quizapp.utill

import android.provider.BaseColumns

open class KBaseColumns  {
    val _ID = "_id"
}

class QuizContract private constructor(){


    class CategoriesTable private constructor():BaseColumns{
        companion object: KBaseColumns() {
            const val TABLE_NAME = "quiz_categories"
            const val COLUMN_NAME = "name"
        }
    }


    class QuestionsTable private constructor(): BaseColumns {
        companion object : KBaseColumns() {
            const val TABLE_NAME = "quiz_questions"
            const val COLUMN_QUESTION = "question"
            const val COLUMN_OPTION1 = "option1"
            const val COLUMN_OPTION2 = "option2"
            const val COLUMN_OPTION3 = "option3"
            const val COLUMN_ANSWER_NR = "answer_nr"
            const val COLUMN_CATEGORY_ID = "category_id"

        }
    }
}