package com.example.lab2
import android.app.Activity
import android.os.Bundle
import android.widget.*
import android.content.Intent

class MainActivity : Activity() {
override fun onCreate(savedInstanceState: Bundle?) {
 super.onCreate(savedInstanceState); setContentView(R.layout.activity_main)
 val spinner = findViewById<Spinner>(R.id.spinner)
 val result = findViewById<TextView>(R.id.result)
 val cities = resources.getStringArray(R.array.cities)
 spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cities)
 spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
 override fun onNothingSelected(parent: AdapterView<*>?) {}
 override fun onItemSelected(parent: AdapterView<*>?, v: android.view.View?, position: Int, id: Long) {
 result.text = getString(R.string.selected, cities[position])
 findViewById<LinearLayout>(R.id.root).setBackgroundColor(if(position==0) 0xFFE3F2FD.toInt() else 0xFFFFF3E0.toInt())
 Toast.makeText(this@MainActivity,cities[position],Toast.LENGTH_SHORT).show()
 }}
 var changed = false
 findViewById<Button>(R.id.color).setOnClickListener { changed = !changed; findViewById<LinearLayout>(R.id.root).setBackgroundColor(if(changed) 0xFFD1C4E9.toInt() else 0xFFFFFFFF.toInt()) }
 }
 override fun onCreateOptionsMenu(menu: android.view.Menu): Boolean { menu.add("Українська"); menu.add("English"); return true }
 override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean { val locale = java.util.Locale(if(item.title=="English") "en" else "uk"); val config = resources.configuration; config.setLocale(locale); resources.updateConfiguration(config, resources.displayMetrics); recreate(); return true }
}
