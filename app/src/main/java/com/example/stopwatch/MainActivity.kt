package com.example.stopwatch

import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    val TAG : String = "mm"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         var cancel : Boolean = false

        var time_min: Int = 0
        var time_sec: Int = 0
        var c = 0
        var spref = getPreferences(Context.MODE_PRIVATE)
        var editor = spref.edit()
        start.setOnClickListener {

                  if (c==0) {
                      time_min = min.text.toString().toInt()
                      time_sec = sec.text.toString().toInt()
                      c = 1
                  }

            GlobalScope.launch(Dispatchers.Main) {

                while (time_min>=0) {
                    while (time_sec > 0) {
                          if (cancel==true)
                          { editor.putInt("m",time_min)
                              editor.putInt("s",time_sec)
                              editor.apply()
                              break
                          }

                        var x = withContext(Dispatchers.IO) {
                            wait()


                        }
                        if (cancel==true)
                        { editor.putInt("m",time_min)
                            editor.putInt("s",time_sec)
                            editor.apply()
                            break
                        }
                        time_sec -= x
                        time.text = time_min.toString() + " : " + time_sec.toString()


                    }
                    if (cancel==true)
                    { editor.putInt("m",time_min)
                        editor.putInt("s",time_sec)
                        editor.apply()
                        break
                    }

                    time_min--
                    time_sec = 60


                }

               if (time_min==0&&time_sec==0) time.text = "TIMEOUT"
            }

        }

        stop.setOnClickListener {

            cancel = true
        }


        resume.setOnClickListener {

            time_min = spref.getInt("m",0)
            time_sec = spref.getInt("s",0)

            cancel=false
            start.callOnClick()
        }

    }

    suspend fun wait() : Int
    {
        Thread.sleep(1000)
        return 1
    }


}
