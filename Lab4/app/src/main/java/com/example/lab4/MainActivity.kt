package com.example.lab4
import android.app.Activity
import android.os.Bundle
import android.widget.*
import android.content.Intent

class MainActivity : Activity() {
private val handler = android.os.Handler(android.os.Looper.getMainLooper())
 private var storedMillis = 0L
 private var startedAt = 0L
 private var isTimerRunning = false
 private fun elapsedMillis() = storedMillis + if(isTimerRunning) android.os.SystemClock.elapsedRealtime()-startedAt else 0L
 private val ticker = object : Runnable { override fun run() { val s=elapsedMillis()/1000; findViewById<TextView>(R.id.time).text=String.format("%02d:%02d",s/60,s%60); handler.postDelayed(this,200) } }
 override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); setContentView(R.layout.activity_main)
 storedMillis=savedInstanceState?.getLong("stored")?:0L; startedAt=savedInstanceState?.getLong("started")?:0L; isTimerRunning=savedInstanceState?.getBoolean("running")?:false
 findViewById<Button>(R.id.start).setOnClickListener { if(!isTimerRunning){startedAt=android.os.SystemClock.elapsedRealtime();isTimerRunning=true} }
 findViewById<Button>(R.id.pause).setOnClickListener { storedMillis=elapsedMillis();isTimerRunning=false }
 findViewById<Button>(R.id.reset).setOnClickListener { storedMillis=0;isTimerRunning=false }
 }
 override fun onStart(){super.onStart();handler.post(ticker)}
 override fun onStop(){handler.removeCallbacks(ticker);super.onStop()}
 override fun onSaveInstanceState(outState:Bundle){outState.putLong("stored",storedMillis);outState.putLong("started",startedAt);outState.putBoolean("running",isTimerRunning);super.onSaveInstanceState(outState)}
}
