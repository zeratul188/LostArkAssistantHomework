package com.lostark.lostarkassistanthomework.add

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.lostark.lostarkassistanthomework.settings.ChracterRecylerAdapter
import com.lostark.lostarkassistanthomework.LoadingDialog
import com.lostark.lostarkassistanthomework.R
import com.lostark.lostarkassistanthomework.checklist.RecyclerViewDecoration
import com.lostark.lostarkassistanthomework.checklist.rooms.HomeworkDatabase
import com.lostark.lostarkassistanthomework.objects.Chracter
import org.jsoup.Jsoup

class AutoFragment : Fragment() {
    lateinit var listAuto: RecyclerView
    lateinit var btnSearch: Button
    lateinit var btnAll: Button
    lateinit var edtName: EditText
    lateinit var txtContent: TextView

    val chracters = ArrayList<Chracter>()
    lateinit var chracterAdapter: ChracterRecylerAdapter

    val NOTIFYED = 1
    val ERRORED = 2
    val EMPTYED = 3

    fun setEditName(edtName: EditText) {
        this.edtName = edtName
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_auto, container, false)

        listAuto = view.findViewById(R.id.listAuto)
        btnSearch = view.findViewById(R.id.btnSearch)
        txtContent = view.findViewById(R.id.txtContent)
        btnAll = view.findViewById(R.id.btnAll)

        val loadingDialog = LoadingDialog(requireContext())
        loadingDialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        loadingDialog.setCancelable(false)

        val handler = ChracterHandler()

        btnSearch.setOnClickListener {
            chracters.clear()
            loadingDialog.show()
            txtContent.text = ""
            txtContent.visibility = View.VISIBLE
            val thread = CloringThread(edtName, requireContext(), loadingDialog, chracters, handler)
            thread.start()
        }

        btnAll.setOnClickListener {
            chracters.forEach { chracter ->
                chracter.isChecked = true
            }
            chracterAdapter.notifyDataSetChanged()
        }

        chracterAdapter = ChracterRecylerAdapter(chracters, requireContext(), txtContent)
        listAuto.adapter = chracterAdapter
        listAuto.addItemDecoration(RecyclerViewDecoration(0, 10))

        return view
    }

    inner class ChracterHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            when (msg.what) {
                NOTIFYED -> {
                    val count = chracters.size
                    txtContent.text = "총 ${count}개의 결과를 찾았습니다."
                    chracterAdapter.notifyDataSetChanged()
                }
                ERRORED -> {
                    txtContent.text = "정보를 불러오는데 문제가 발생하였습니다.\n\n- 인터넷 연결을 확인해주세요."
                }
                EMPTYED -> {
                    txtContent.text = "검색결과가 없습니다. 문제 발생 시 아래 방법을 시도하십시오\n\n- 캐릭터 이름을 다시 정확히 입력해주십시오.\n- 로스트아크 점검 시간을 피해주십시오.\n- 인터넷 연결을 확인해주세요.\n- 이미 모든 캐릭터를 추가하였습니다."
                }
                else -> {

                }
            }
        }
    }
}

class CloringThread(
    private val edtName: EditText,
    private val context: Context,
    private val loadingDialog: LoadingDialog,
    private val chracters: ArrayList<Chracter>,
    private val handler: Handler
) : Thread() {
    override fun run() {
        val homeworkDB = HomeworkDatabase.getInstance(context)!!
        val datas = homeworkDB.homeworkDao().getAll()
        try {
            var doc = Jsoup.connect("https://lostark.game.onstove.com/Profile/Character/"+edtName.text.toString()).get()

            var elements = doc.select("#expand-character-list > ul")
            for (element in elements) {
                val result = element.select("span > button > span")
                val lists = result.text().split(" ")
                for (name in lists) {
                    var single_doc = Jsoup.connect("https://lostark.game.onstove.com/Profile/Character/${name}").get()
                    var level_element = single_doc.select("#lostark-wrapper > div > main > div > div.profile-ingame > div.profile-info > div.level-info2 > div.level-info2__expedition > span:nth-child(2)")
                    var level_str = level_element.text()
                    level_str = level_str.replace("Lv.", "")
                    level_str = level_str.replace(",", "")
                    var level = level_str.toDouble()
                    var server_element = single_doc.select("#lostark-wrapper > div > main > div > div.profile-character-info > span.profile-character-info__server")
                    var server = server_element.text()
                    server = server.replace("@", "")
                    var job_element = single_doc.select("#lostark-wrapper > div > main > div > div.profile-character-info > img")
                    var job = job_element.attr("alt")
                    /*println("================================================================")
                    println("Name : ${name}\nLevel : ${level}\nServer : ${server}\nJob : ${job}")
                    println("================================================================")*/
                    val jobs = context.resources.getStringArray(R.array.job)
                    if (jobs.indexOf(job) != -1) {
                        var isOverlap = false
                        run {
                            datas.forEach { item ->
                                if (item.name == name) {
                                    isOverlap = true
                                    return@run
                                }
                            }
                        }
                        if (!isOverlap) {
                            chracters.add(Chracter(name, level, server, job, false))
                        }
                    }
                }
            }
            chracters.sort()
            loadingDialog.dismiss()
            var message = Message.obtain()
            if (!chracters.isEmpty()) {
                message.what = 1
            } else {
                message.what = 3
            }
            handler.sendMessage(message)
        } catch (e: Exception) {
            e.printStackTrace()
            loadingDialog.dismiss()
            var message = Message.obtain()
            message.what = 2
            handler.sendMessage(message)
        }
    }
}