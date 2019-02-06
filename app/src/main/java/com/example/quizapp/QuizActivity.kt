package com.example.quizapp

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import com.example.quizapp.data.QuizDBHelper
import com.example.quizapp.model.Question
import kotlinx.android.synthetic.main.activity_quiz.*
import java.util.*

class QuizActivity : AppCompatActivity() {


    private lateinit var questionList :MutableList<Question>
    private lateinit var textColorDefault:ColorStateList
    private var questionCounter:Int = 0
    private var questionCountTotal:Int = 0
    private lateinit var currentQuestion:Question

    private var score:Int = 0
    private var answered:Boolean = false
    private var backPressedTime: Long = 0


    companion object {
        const val EXTRA_SCORE = "extraScore"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        textColorDefault = radio_button1.textColors

        var intent = intent
        var categoryID = intent.getIntExtra(MainActivity.EXTRA_CATEGORY_ID,0)
        var categoryName  = intent.getStringExtra(MainActivity.EXTRA_CATEGORY_NAME)

        text_view_category.text = "Category: $categoryName"



        val dbHelper = QuizDBHelper.getInstance(this)
        questionList = dbHelper.getQuestions(categoryID)


        questionCountTotal = questionList.size
        questionList.shuffle()


        showNextQuestion()

        button_confirm_next.setOnClickListener {
            if(!answered){
                if (radio_button1.isChecked||radio_button2.isChecked||radio_button3.isChecked){
                    checkAnswer()
                }else{
                    Toast.makeText(this,"Please select the answer",Toast.LENGTH_SHORT).show()

                }
            }else{
                showNextQuestion()
            }
        }
    }

    private fun checkAnswer() {
        answered = true
       var rbSelected= findViewById<RadioButton>(radio_group.checkedRadioButtonId)
        var answerNr = radio_group.indexOfChild(rbSelected)+1
        if (answerNr==currentQuestion.answerNr){
            score++
            text_view_score.text = "Score: $score"

        }
        showSolution()
    }

    private fun showSolution() {
        radio_button1.setTextColor(Color.RED)
        radio_button2.setTextColor(Color.RED)
        radio_button3.setTextColor(Color.RED)

        when (currentQuestion.answerNr) {
            1 -> {
                radio_button1.setTextColor(Color.GREEN)
                text_view_question.text = "Answer 1 is correct"
            }
            2 -> {
                radio_button2.setTextColor(Color.GREEN)
                text_view_question.text = "Answer 2 is correct"
            }
            3 -> {
                radio_button3.setTextColor(Color.GREEN)
                text_view_question.text = "Answer 3 is correct"
            }
        }

        if (questionCounter < questionCountTotal) {
            button_confirm_next.text = "Next"
        } else {
            button_confirm_next.text = "Finish"
        }
    }

    private fun showNextQuestion() {

        radio_button1.setTextColor(textColorDefault)
        radio_button2.setTextColor(textColorDefault)
        radio_button3.setTextColor(textColorDefault)
        radio_group.clearCheck()

        if(questionCounter<questionCountTotal){
            currentQuestion = questionList[questionCounter]

            text_view_question.text = currentQuestion.question
            radio_button1.text = currentQuestion.option1
            radio_button2.text = currentQuestion.option2
            radio_button3.text = currentQuestion.option3

            questionCounter++
            text_view_question_count.text = "Question: $questionCounter / $questionCountTotal"
            answered = false
            button_confirm_next.text = "Confirm"
        }else{
            finishQuiz()
        }


    }

    private fun finishQuiz() {
        var resultIntent = Intent()
        resultIntent.putExtra(Companion.EXTRA_SCORE,score)
        setResult(Activity.RESULT_OK,resultIntent)
        finish()
    }

    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishQuiz()
        } else {
            Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show()
        }

        backPressedTime = System.currentTimeMillis()
    }

}
