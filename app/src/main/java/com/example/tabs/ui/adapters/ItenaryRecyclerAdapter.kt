package com.example.tabs.ui.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.tabs.R
import com.example.tabs.data.responses.Itenary
import com.example.tabs.ui.home.HomeViewModel
import java.util.*


class ItenaryRecyclerAdapter(
    val viewModel: HomeViewModel,
    val arrayList: List<Itenary>,
    val context: Context
) : RecyclerView.Adapter<ItenaryRecyclerAdapter.NotesViewHolder>() {
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
        if (arrayList.isEmpty()) {
            Toast.makeText(
                context,
                "Sorry! Cant find a plan with the budget and time, try changing those values",
                Toast.LENGTH_LONG
            ).show()
        } else {

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
        fun bind(itenary: Itenary) {
            var titleView: TextView = binding.findViewById(R.id.textView)
            var image: ImageView = binding.findViewById(R.id.imageView)
            var card: CardView = binding.findViewById(R.id.main_card)
            var category: TextView = binding.findViewById(R.id.tv_category)
            var description: TextView = binding.findViewById(R.id.tv_description)
            var cost: TextView = binding.findViewById(R.id.tv_place_cost)
            var time: TextView = binding.findViewById(R.id.tv_travel_time)
            var btnMap: Button = binding.findViewById(R.id.btn_maps)
            var time_spent:Float=0F
            if (itenary.type == "place") {
                titleView.setText(itenary.response.name)
                titleView.setTextSize(18f)
                category.setText(itenary.response.category)
                description.setText(itenary.response.description)
               try {
                   if (itenary.response.price.toInt() == 0) {
                       cost.setText("Free")
                   } else
                       cost.setText("Rs. ${itenary.response.price}")
               }catch (exception: Exception){

               }

                try{
                 time_spent=itenary.response.time_spent.toFloat()


                }catch (exception: Exception){
                    time_spent= 1.0F
                }
                time.setText("${time_spent} Hr")

                Log.d("Cost and time", "" + itenary.response.price + itenary.response.time_spent)
                category.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12F)

                image.setImageResource(R.drawable.ic_place)
                btnMap.setOnClickListener {
                    val urlAddress =
                        "http://maps.google.com/maps?q=" + itenary.response.latitude.toString() + "," + itenary.response.longitude.toString() + "(" + itenary.response.name + ")&iwloc=A&hl=es"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlAddress))
                    context.startActivity(intent)

                }
            } else if (itenary.type == "drive") {
                category.setText("Drive for ${Math.round(itenary.response.driving_time * 10.0) / 10.0} hours")
                category.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
                image.setImageResource(R.drawable.ic_car_foreground)
                titleView.visibility = View.INVISIBLE
                cost.visibility = View.GONE
                time.visibility = View.GONE
                btnMap.visibility = View.GONE
                description.visibility = View.GONE
            }
        }
    }
}