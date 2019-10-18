package br.com.renatoarg.marvelrx.view.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import br.com.renatoarg.exoplayer.Meditation
import br.com.renatoarg.exoplayer.PlayerActivity
import br.com.renatoarg.exoplayer.R
import kotlinx.android.synthetic.main.row_results.view.*
import timber.log.Timber

class MeditationsAdapter(private var results:List<Meditation>, private var context: Context) : RecyclerView.Adapter<MeditationsAdapter.ViewHolder>() {

    private var mResults: List<Meditation> = results
    private val mContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Timber.d("onCreateViewHolder:")
        val view = LayoutInflater.from(mContext).inflate(R.layout.row_results, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        Timber.d("getItemCount:")
        return mResults.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Timber.d("onBindViewHolder:")
        holder.name.text = mResults[position].name
        holder.row.setOnClickListener {
            val intent = Intent(mContext, PlayerActivity::class.java)
            intent.putExtra("uri", mResults[position].uri)
            startActivity(mContext, intent, null)
        }
    }

    fun updateList(results: List<Meditation>) {
        Timber.d("updateList:")
        this.mResults = results
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.tv_name
        val row = itemView.cl_row_results
    }
}