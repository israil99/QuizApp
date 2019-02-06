package com.example.quizapp.model

class Category {

    var id: Int = 0
    var name: String? = null

    constructor() {}

    constructor(name: String) {
        this.name = name
    }

    companion object {
        val PROGRAMMING = 1
        val GEOGRAPHY = 2
        val MATH = 3
    }

    override fun toString(): String {
        return "$name"
    }
}