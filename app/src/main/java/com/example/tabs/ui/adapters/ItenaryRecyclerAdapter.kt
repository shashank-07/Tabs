package com.example.tabs.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.tabs.R
import com.example.tabs.data.responses.Itenary
import com.example.tabs.ui.home.HomeViewModel

class ItenaryRecyclerAdapter(
    val viewModel: HomeViewModel,
    val arrayList: List<Itenary>,
    val context: Context
): RecyclerView.Adapter<ItenaryRecyclerAdapter.NotesViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ItenaryRecyclerAdapter.NotesViewHolder {
        var root = LayoutInflater.from(parent.context).inflate(R.layout.places_card, parent, false)
        return NotesViewHolder(root)
    }

    override fun onBindViewHolder(holder: ItenaryRecyclerAdapter.NotesViewHolder, position: Int) {

        holder.bind(arrayList[position])

    }

    override fun getItemCount(): Int {
        if(arrayList.isEmpty()){
            Toast.makeText(
                context,
                "Sorry! Cant find a plan with the budget and time, try changing those values",
                Toast.LENGTH_LONG
            ).show()
        }else{

        }
        return arrayList.size
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    inner class NotesViewHolder(private val binding: View) : RecyclerView.ViewHolder(binding) {
        fun bind(itenary: Itenary){
            var titleView:TextView=binding.findViewById(R.id.textView)
            var image : ImageView =binding.findViewById(R.id.imageView)
            var card:CardView=binding.findViewById(R.id.main_card)
            var constraint:ConstraintLayout=binding.findViewById(R.id.main_constraint)
            if(itenary.type=="place"){
                titleView.setText(itenary.response.name)
                constraint.setBackgroundColor(binding.resources.getColor(R.color.teal_200))
                titleView.setTextSize(18f)

            }else if(itenary.type=="drive"){
                titleView.setText("Drive for ${Math.round(itenary.response.driving_time * 10.0) / 10.0} hours")
                image.setImageResource(R.drawable.ic_car_foreground)
                card.cardElevation=0f

            }


        }

    }

}