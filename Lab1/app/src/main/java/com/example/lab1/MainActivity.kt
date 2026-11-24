package com.example.lab1
import android.app.Activity
import android.os.Bundle
import android.widget.*
import android.content.Intent

class MainActivity : Activity() {
override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); setContentView(R.layout.activity_main); findViewById<TextView>(R.id.greetingText).text = "Вітаю у моєму додатку!" }
}
