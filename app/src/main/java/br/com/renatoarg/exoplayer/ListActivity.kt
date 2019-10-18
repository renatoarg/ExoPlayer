package br.com.renatoarg.exoplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.renatoarg.marvelrx.view.adapters.MeditationsAdapter
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity() {



    private var adapter = MeditationsAdapter(ArrayList(), this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val arrayList = ArrayList<Meditation>()
        var meditation1 = Meditation("Meditation 1", "https://s3.amazonaws.com/wakingup-dev/courses/mp3s/000/000/038/original/img-609b54eb53f.mp3")
        var meditation2 = Meditation("Meditation 2","https://s3.amazonaws.com/wakingup-dev/courses/mp3s/000/000/017/original/img-0090d5cae29.mp3")
        var meditation3 = Meditation("Meditation 3","https://s3.amazonaws.com/wakingup-dev/courses/mp3s/000/000/020/original/img-3c6483d2a8d.mp3")
        arrayList.add(meditation1)
        arrayList.add(meditation2)
        arrayList.add(meditation3)
        adapter.updateList(arrayList)

        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        recyclerView.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))
        recyclerView.adapter = adapter
    }
}
