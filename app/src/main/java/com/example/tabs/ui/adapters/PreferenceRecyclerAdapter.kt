package com.example.tabs.ui.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.tabs.R
import com.example.tabs.data.responses.Preference
import com.example.tabs.ui.register.RegisterViewModel

class PreferenceRecyclerAdapter(
    val viewModel: RegisterViewModel,
    val arrayList: List<Preference>,
    val context: Context
) : RecyclerView.Adapter<PreferenceRecyclerAdapter.NotesViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PreferenceRecyclerAdapter.NotesViewHolder {
        var root =
            LayoutInflater.from(parent.context).inflate(R.layout.preference_card, parent, false)
        return NotesViewHolder(root)
    }

    override fun onBindViewHolder(
        holder: PreferenceRecyclerAdapter.NotesViewHolder,
        position: Int
    ) {
        holder.bind(arrayList.get(position))
    }

    override fun getItemCount(): Int {
        if (arrayList.isEmpty()) {
            Toast.makeText(context, "List is empty", Toast.LENGTH_LONG).show()
        }
        return arrayList.size
    }


    inner class NotesViewHolder(private val binding: View) : RecyclerView.ViewHolder(binding) {
        fun bind(preference: Preference) {
            var titleView: TextView = binding.findViewById(R.id.title)
            var card: CardView = binding.findViewById(R.id.pref_card)
            var constraint: ConstraintLayout=binding.findViewById(R.id.constraint_card)
            if (!preference.selected) {
                constraint.setBackgroundColor(binding.resources.getColor(R.color.dark_grey))
                card.setBackgroundColor(binding.resources.getColor(R.color.dark_grey))

                titleView.setTextColor(binding.resources.getColor(R.color.light_grey))
                card.cardElevation=0f

            } else {
                constraint.setBackgroundColor(binding.resources.getColor(R.color.purple_200))
                card.setBackgroundColor(binding.resources.getColor(R.color.purple_200))

                titleView.setTextColor(Color.WHITE)

                card.cardElevation=30f
            }
            titleView.text = preference.group
            titleView.setOnClickListener {
                viewModel.remove(preference.group)
            }
        }
    }
}