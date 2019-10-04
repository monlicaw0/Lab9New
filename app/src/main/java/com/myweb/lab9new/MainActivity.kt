package com.myweb.lab9new

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    var studentList  = arrayListOf<Student>()
    val createClient = StudentAPI.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recycler_view.layoutManager = LinearLayoutManager(applicationContext) as RecyclerView.LayoutManager?
        recycler_view.itemAnimator = DefaultItemAnimator() as RecyclerView.ItemAnimator?
        recycler_view.addItemDecoration(DividerItemDecoration(recycler_view.getContext(), DividerItemDecoration.VERTICAL))

        recycler_view.addOnItemTouchListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                Toast.makeText(applicationContext,"You click on : "+studentList[position].std_id,
                    Toast.LENGTH_SHORT).show()
                val std  = studentList[position]
                val intent = Intent(applicationContext, EditStudentsActivity::class.java)
                intent.putExtra("mId", std.std_id)
                intent.putExtra("mName", std.std_name)
                intent.putExtra("mAge", std.std_age.toString())
                startActivity(intent)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        callStudentData()
    }

    fun clickSearch(v: View){
        studentList.clear();
        createClient.retrieveStudentID(edt_search.text.toString())
            .enqueue(object : Callback<Student> {

                override fun onResponse(call: Call<Student>, response: Response<Student>) {
                    studentList.add(Student(response.body()?.std_id.toString(),response.body()?.std_name.toString(),
                        response.body()?.std_age.toString().toInt()))
                    //// Set Data to RecyclerRecyclerView
                    recycler_view.adapter = EditStudentsAdapter(studentList,applicationContext)
                }
                override fun onFailure(call: Call<Student>, t: Throwable) = t.printStackTrace()
            })
    }

    fun callStudentData(){
        studentList.clear();
        createClient.retrieveStudent()
            .enqueue(object : Callback<List<Student>> {
                override fun onResponse(call: Call<List<Student>>, response: Response<List<Student>>) {
                    response.body()?.forEach {
                        studentList.add(Student(it.std_id, it.std_name,it.std_age))
                    }
                    //// Set Data to RecyclerRecyclerView
                    recycler_view.adapter = EditStudentsAdapter(studentList,applicationContext)
                }
                override fun onFailure(call: Call<List<Student>>, t: Throwable) = t.printStackTrace()
            })
    }
}

interface OnItemClickListener {
    fun onItemClicked(position: Int, view: View)
}
fun RecyclerView.addOnItemTouchListener(onClickListener: OnItemClickListener) {
    this.addOnChildAttachStateChangeListener(object: RecyclerView.OnChildAttachStateChangeListener {

        override fun onChildViewDetachedFromWindow(view: View) {
            view?.setOnClickListener(null)
        }

        override fun onChildViewAttachedToWindow(view: View) {
            view?.setOnClickListener {
                val holder = getChildViewHolder(view)
                onClickListener.onItemClicked(holder.adapterPosition, view)
            }
        }
    })
}
