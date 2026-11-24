package com.example.lab5
import android.app.Activity
import android.os.Bundle
import android.widget.*
import android.content.Intent

class MainActivity : Activity() {
private lateinit var db: UserDatabase
 private var deadline=0L
 private val handler=android.os.Handler(android.os.Looper.getMainLooper())
 private val ticker=object:Runnable { override fun run(){val left=((deadline-android.os.SystemClock.elapsedRealtime()+999)/1000).coerceAtLeast(0);findViewById<TextView>(R.id.time).text=left.toString();findViewById<Button>(R.id.enter).isEnabled=left>0;findViewById<Button>(R.id.register).isEnabled=left>0;handler.postDelayed(this,200)}}
 override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);setContentView(R.layout.activity_main);db=UserDatabase(this);deadline=savedInstanceState?.getLong("deadline")?: (android.os.SystemClock.elapsedRealtime()+60000)
 fun value(id:Int)=findViewById<EditText>(id).text.toString().trim()
 fun status(s:String){findViewById<TextView>(R.id.status).text=s}
 findViewById<Button>(R.id.register).setOnClickListener{if(android.os.SystemClock.elapsedRealtime()<deadline)status(if(db.register(value(R.id.name),value(R.id.login),value(R.id.password)))"Зареєстровано" else "Перевірте дані")}
 findViewById<Button>(R.id.enter).setOnClickListener{if(android.os.SystemClock.elapsedRealtime()<deadline)status(db.authenticate(value(R.id.login),value(R.id.password))?.let{"Вітаю, $it"}?:"Невірний логін або пароль")}
 findViewById<Button>(R.id.restart).setOnClickListener{deadline=android.os.SystemClock.elapsedRealtime()+60000}
 }
 override fun onStart(){super.onStart();handler.post(ticker)}
 override fun onStop(){handler.removeCallbacks(ticker);super.onStop()}
 override fun onSaveInstanceState(outState:Bundle){outState.putLong("deadline",deadline);super.onSaveInstanceState(outState)}
}
class UserDatabase(context:android.content.Context):android.database.sqlite.SQLiteOpenHelper(context,"users.db",null,1){
 override fun onCreate(db:android.database.sqlite.SQLiteDatabase){db.execSQL("CREATE TABLE users(id INTEGER PRIMARY KEY,name TEXT NOT NULL,login TEXT UNIQUE NOT NULL,salt TEXT NOT NULL,password_hash TEXT NOT NULL)")}
 override fun onUpgrade(db:android.database.sqlite.SQLiteDatabase,oldVersion:Int,newVersion:Int){}
 private fun hash(password:String,salt:ByteArray):String {val spec=javax.crypto.spec.PBEKeySpec(password.toCharArray(),salt,120000,256);return android.util.Base64.encodeToString(javax.crypto.SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).encoded,android.util.Base64.NO_WRAP)}
 fun register(name:String,login:String,password:String):Boolean{if(name.isBlank()||login.isBlank()||password.isBlank())return false;val salt=ByteArray(16);java.security.SecureRandom().nextBytes(salt);val values=android.content.ContentValues().apply{put("name",name);put("login",login);put("salt",android.util.Base64.encodeToString(salt,android.util.Base64.NO_WRAP));put("password_hash",hash(password,salt))};return writableDatabase.insert("users",null,values)!=-1L}
 fun authenticate(login:String,password:String):String? { readableDatabase.query("users",arrayOf("name","salt","password_hash"),"login=?",arrayOf(login),null,null,null).use{if(!it.moveToFirst())return null;val candidate=hash(password,android.util.Base64.decode(it.getString(1),android.util.Base64.NO_WRAP));return if(java.security.MessageDigest.isEqual(candidate.toByteArray(),it.getString(2).toByteArray()))it.getString(0) else null} }
}
