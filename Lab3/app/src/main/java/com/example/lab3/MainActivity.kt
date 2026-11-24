package com.example.lab3
import android.app.Activity
import android.os.Bundle
import android.widget.*
import android.content.Intent

class MainActivity : Activity() {
override fun onCreate(savedInstanceState: Bundle?) {
 super.onCreate(savedInstanceState); setContentView(R.layout.activity_main)
 val input = findViewById<EditText>(R.id.message)
 findViewById<Button>(R.id.open).setOnClickListener { startActivity(Intent(this, DetailActivity::class.java).putExtra("message",input.text.toString())) }
 findViewById<Button>(R.id.share).setOnClickListener { startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(Intent.EXTRA_TEXT,input.text.toString()),"Поділитися")) }
 }
}
class DetailActivity : Activity() { override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); setContentView(R.layout.activity_detail); findViewById<TextView>(R.id.message).text = intent.getStringExtra("message") } }
