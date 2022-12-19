package com.lostark.lostarkassistanthomework.checklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lostark.lostarkassistanthomework.App
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.objects.Checklist
import com.lostark.lostarkassistanthomework.checklist.rooms.Homework
import com.lostark.lostarkassistanthomework.checklist.rooms.HomeworkDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeworkPagerAdapter(
    private val checklists: ArrayList<ArrayList<Checklist>>,
    private val homework: Homework,
    private val holder: ChracterRecylerAdapter.ViewHolder,
    private val fragment: ChecklistFragment,
    private val viewPager: ViewPager,
    private val bottomNavigationView: BottomNavigationView
) : PagerAdapter() {

    lateinit var listView: RecyclerView
    var homeworkDB: HomeworkDatabase = HomeworkDatabase.getInstance(App.context())!!
    //val heights = arrayListOf(0, 0)

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.fragment_list_day, container, false)
        val params = LinearLayout.LayoutParams(bottomNavigationView.measuredWidth, LinearLayout.LayoutParams.WRAP_CONTENT)
        view.layoutParams = params

        listView = view.findViewById(R.id.listView)

        var type = "null"
        when (position) {
            0 -> type = "일일"
            1 -> type = "주간"
        }
        val homeworkAdapter = HomeworkRecylerAdapter(checklists[position], App.context(), homework, homeworkDB, type, holder, fragment)
        listView.adapter = homeworkAdapter
        listView.addItemDecoration(RecyclerViewDecoration(10, 10))

        //heights[position] = view.height

        CoroutineScope(Dispatchers.Main).launch {
            delay(100)
            if (position == 0) {
                resized(view, homework)
            }
        }
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    override fun getCount(): Int = checklists.size



    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    private fun resized(view: View, homework: Homework) {
        if (view != null) {
            view.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            //val width = view.measuredWidth
            val height = view.measuredHeight
            val params = ConstraintLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
            params.topToBottom = bottomNavigationView.id
            viewPager.layoutParams = params
        }
    }

}