package com.example.quizapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.R.id.edit
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.ArrayAdapter
import com.example.quizapp.data.QuizDBHelper
import com.example.quizapp.model.Category


class MainActivity : AppCompatActivity() {

    companion object {
        val SHARED_PREFS = "sharedPrefs"
        val KEY_HIGHSCORE = "keyHighscore"
        val EXTRA_CATEGORY_ID = "extraCategoryID"
        val EXTRA_CATEGORY_NAME = "extraCategoryName"
    }

    private var highscore: Int = 0

    private var REQUEST_CODE_QUIZ = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadCategories()
        loadHighscore()
        button_start_quiz.setOnClickListener {
            startQuiz()
        }
    }


    private fun loadCategories() {
        var dbHelper = QuizDBHelper.getInstance(this)
        var categories = dbHelper.getAllCategories()

        var adapterCategories = ArrayAdapter<Category>(this,android.R.layout.simple_spinner_dropdown_item,categories)
        spinner_category.adapter = adapterCategories

    }

    private fun startQuiz() {
        var selectedCategory =  spinner_category.selectedItem as Category
        var categoryID = selectedCategory.id
        var categoryName  = selectedCategory.name

        Log.d("INTENT",selectedCategory.toString())

        var intent = Intent(this,QuizActivity::class.java)
        intent.putExtra(EXTRA_CATEGORY_ID, categoryID)
        intent.putExtra(EXTRA_CATEGORY_NAME, categoryName)
        startActivityForResult(intent,REQUEST_CODE_QUIZ)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_QUIZ) {
            if (resultCode == Activity.RESULT_OK) {
                val score = data!!.getIntExtra(QuizActivity.EXTRA_SCORE, 0)
                if (score > highscore) {
                    updateHighscore(score)
                }
            }else{
                Log.d("Result ERROR","ERROR")
            }
        }else
            Log.d("Result ERROR","ERROR")
    }

    private fun loadHighscore() {
        val prefs = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        highscore = prefs.getInt(KEY_HIGHSCORE, 0)
        text_view_highscore.text = "Highscore: $highscore"
    }

    private fun updateHighscore(highscoreNew: Int) {
        highscore = highscoreNew
        text_view_highscore.text = "Highscore: $highscore"

        val prefs = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt(KEY_HIGHSCORE, highscore)
        editor.apply()
    }

}
