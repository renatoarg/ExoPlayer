package br.com.renatoarg.exoplayer.view

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import br.com.renatoarg.exoplayer.R
import br.com.renatoarg.exoplayer.model.MEDITATIONS.MEDITATION
import br.com.renatoarg.exoplayer.model.Meditation
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.meditation_row.view.*

class MeditationsAdapter(private val context:Context, meditations:ArrayList<Meditation>) : RecyclerView.Adapter<MeditationsAdapter.ViewHolder>() {

    private var mMeditations = meditations
    private var meditationsAdapterInterface = context as MeditationsAdapterInterface

    interface MeditationsAdapterInterface {
        fun onMeditationRowClicked(meditation:Meditation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.meditation_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mMeditations.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = mMeditations[position].title
        holder.description.text = mMeditations[position].description
        Picasso.get().load(mMeditations[position].image_url).into(holder.image)
        holder.row.setOnClickListener {
            meditationsAdapterInterface.onMeditationRowClicked(mMeditations[position])
        }
    }

    fun update(meditations: ArrayList<Meditation>) {
        mMeditations = meditations
        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val row: ConstraintLayout = itemView.cl_meditation_row
        val title: TextView = itemView.tv_title
        val description: TextView = itemView.tv_description
        var image: ImageView = itemView.iv_image
    }

}
