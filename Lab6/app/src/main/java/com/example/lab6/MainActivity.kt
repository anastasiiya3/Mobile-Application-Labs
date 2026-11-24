package com.example.lab6
import android.app.Activity
import android.os.Bundle
import android.widget.*
import android.content.Intent

class MainActivity : Activity() {
private val auth by lazy { com.google.firebase.auth.FirebaseAuth.getInstance() }
 private val db by lazy { com.google.firebase.firestore.FirebaseFirestore.getInstance() }
 private var role="guest"
 private var deadline=0L
 private var books=listOf<com.google.firebase.firestore.DocumentSnapshot>()
 private val handler=android.os.Handler(android.os.Looper.getMainLooper())
 private val tick=object:Runnable{override fun run(){val left=((deadline-android.os.SystemClock.elapsedRealtime()+999)/1000).coerceAtLeast(0);findViewById<TextView>(R.id.time).text=left.toString();findViewById<Button>(R.id.login).isEnabled=left>0;handler.postDelayed(this,250)}}
 override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);setContentView(R.layout.activity_main);deadline=savedInstanceState?.getLong("deadline")?:android.os.SystemClock.elapsedRealtime()+60000
 val categories=findViewById<Spinner>(R.id.categories);categories.adapter=ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,arrayOf("Усі","Фантастика","Наука","Історія"))
 categories.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{override fun onNothingSelected(p:AdapterView<*>?){};override fun onItemSelected(p:AdapterView<*>?,v:android.view.View?,pos:Int,id:Long){load()}}
 findViewById<Button>(R.id.login).setOnClickListener{if(android.os.SystemClock.elapsedRealtime()<deadline){val email=findViewById<EditText>(R.id.email).text.toString();val pass=findViewById<EditText>(R.id.password).text.toString();if(email.isNotBlank()&&pass.isNotBlank())auth.signInWithEmailAndPassword(email,pass).addOnSuccessListener{refreshRole()}.addOnFailureListener{toast(it.message?:"Помилка входу")}}}
 findViewById<Button>(R.id.logout).setOnClickListener{auth.signOut();deadline=android.os.SystemClock.elapsedRealtime()+60000;refreshRole()}
 findViewById<ListView>(R.id.books).setOnItemClickListener{_,_,pos,_->startActivity(Intent(this,BookActivity::class.java).putExtra("id",books[pos].id))}
 findViewById<Button>(R.id.add).setOnClickListener{if(role=="admin")addBook()}
 refreshRole()
 }
 private fun toast(s:String){Toast.makeText(this,s,Toast.LENGTH_SHORT).show()}
 private fun refreshRole(){val user=auth.currentUser;role=if(user==null)"guest" else "user";showRole();if(user!=null)db.collection("users").document(user.uid).get().addOnSuccessListener{if(auth.currentUser?.uid==user.uid){role=if(it.getString("role")=="admin")"admin" else "user";showRole()}}}
 private fun showRole(){findViewById<TextView>(R.id.role).text=role;findViewById<Button>(R.id.add).isEnabled=role=="admin"}
 private fun load(){val category=findViewById<Spinner>(R.id.categories).selectedItem?.toString()?:"Усі";val q=if(category=="Усі")db.collection("books") else db.collection("books").whereEqualTo("category",category);q.get().addOnSuccessListener{books=it.documents;findViewById<ListView>(R.id.books).adapter=object:ArrayAdapter<com.google.firebase.firestore.DocumentSnapshot>(this,android.R.layout.simple_list_item_1,books){override fun getView(position:Int,convertView:android.view.View?,parent:android.view.ViewGroup):android.view.View{val item=books[position];val row=LinearLayout(this@MainActivity).apply{orientation=LinearLayout.HORIZONTAL};val image=ImageView(this@MainActivity);row.addView(image,LinearLayout.LayoutParams(120,160));item.getString("cover")?.takeIf{it.startsWith("https://")}?.let{com.squareup.picasso.Picasso.get().load(it).into(image)};row.addView(TextView(this@MainActivity).apply{text=listOf(item.getString("title"),item.getString("author"),item.getLong("pages")?.toString()).joinToString(" — ")});return row}}}.addOnFailureListener{toast(it.message?:"Помилка завантаження")}}
 private fun addBook(){val fields=listOf("title","author","pages","category","description","cover");val form=LinearLayout(this).apply{orientation=LinearLayout.VERTICAL};val inputs=fields.map{field->EditText(this).apply{hint=field;form.addView(this)}};android.app.AlertDialog.Builder(this).setTitle("Нова книга").setView(form).setPositiveButton("Зберегти"){_,_->val values=fields.zip(inputs.map{it.text.toString()}).toMap();val pages=values["pages"]?.toLongOrNull();if(pages!=null&&pages>0&&!values["title"].isNullOrBlank()){val data=values.toMutableMap<String,Any>();data["pages"]=pages;db.collection("books").add(data).addOnSuccessListener{load()}.addOnFailureListener{toast(it.message?:"Помилка")}}}.setNegativeButton("Скасувати",null).show()}
 override fun onStart(){super.onStart();handler.post(tick)}
 override fun onStop(){handler.removeCallbacks(tick);super.onStop()}
 override fun onSaveInstanceState(outState:Bundle){outState.putLong("deadline",deadline);super.onSaveInstanceState(outState)}
}
class BookActivity:Activity(){override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);val text=TextView(this).apply{setPadding(24,24,24,24);textSize=20f};setContentView(text);val id=intent.getStringExtra("id")?:return;com.google.firebase.firestore.FirebaseFirestore.getInstance().collection("books").document(id).get().addOnSuccessListener{text.text=listOf(it.getString("title"),it.getString("description")).joinToString("\n\n")}}}
