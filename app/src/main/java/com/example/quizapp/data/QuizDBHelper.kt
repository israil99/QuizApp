package com.example.quizapp.data

import com.example.quizapp.utill.QuizContract.QuestionsTable
import android.content.Context
import android.database.sqlite.SQLiteDatabase;
import com.example.quizapp.model.Category
import com.example.quizapp.model.Question
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper
import com.example.quizapp.utill.QuizContract.CategoriesTable



class QuizDBHelper(context: Context) : SQLiteAssetHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {

    private lateinit var db: SQLiteDatabase


    fun getAllQuestions(): MutableList<Question> {
        val questionList = ArrayList<Question>()
        db = readableDatabase
        val c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null)

        if (c.moveToFirst()) {
            do {
                val question = Question()
                question.id = (c.getInt(c.getColumnIndex(QuestionsTable._ID)))
                question.question = c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION))
                question.option1 = c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1))
                question.option2 = c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2))
                question.option3 = c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3))
                question.answerNr = c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR))
                question.categoryID = c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID))
                questionList.add(question)
            } while (c.moveToNext())
        }

        c.close()
        return questionList
    }

    fun getAllCategories(): MutableList<Category> {
        val categoryList = ArrayList<Category>()
        db = readableDatabase
        val c = db.rawQuery("SELECT * FROM " + CategoriesTable.TABLE_NAME, null)

        if (c.moveToFirst()) {
            do {
                val category = Category()
                category.id = (c.getInt(c.getColumnIndex(CategoriesTable._ID)))
                category.name = (c.getString(c.getColumnIndex(CategoriesTable.COLUMN_NAME)))
                categoryList.add(category)
            } while (c.moveToNext())
        }

        c.close()
        return categoryList
    }

    fun getQuestions(categoryID: Int): MutableList<Question> {
        val questionList = ArrayList<Question>()
        db = readableDatabase

        val selection = QuestionsTable.COLUMN_CATEGORY_ID + " = ? "
        val selectionArgs = arrayOf(categoryID.toString())

        val c = db.query(
            QuestionsTable.TABLE_NAME,
            null,
            selection,
            selectionArgs, null, null, null
        )

        if (c.moveToFirst()) {
            do {
                val question = Question()
                question.id = c.getInt(c.getColumnIndex(QuestionsTable._ID))
                question.question = c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION))
                question.option1 = c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1))
                question.option2 = c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2))
                question.option3 = c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3))
                question.answerNr = c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR))
                question.categoryID = c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY_ID))
                questionList.add(question)
            } while (c.moveToNext())
        }

        c.close()
        return questionList
    }



    companion object {
        private const val DATABASE_NAME = "MyAwesomeQuiz.db"
        private const val DATABASE_VERSION = 1


        private var instance: QuizDBHelper? = null
        @Synchronized
        fun getInstance(context: Context): QuizDBHelper {
            if (instance == null) {
                instance = QuizDBHelper(context.applicationContext)
            }
            return instance as QuizDBHelper
        }
    }
}