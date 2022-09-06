package com.udacity.asteroidradar.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.databinding.AsteroidListItemViewBinding


class AstroidsAdapter() :
    androidx.recyclerview.widget.ListAdapter<Asteroid, AstroidsAdapter.AsteroidViewHolder>(
        AsteroidDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {

        return AsteroidViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        holder.apply {
            itemView.setOnClickListener {
                onItemClickListener?.let { it(getItem(position)) }
            }
        }
        val item = getItem(position)
        holder.bind(item)
    }

    class AsteroidViewHolder private constructor(val binding: AsteroidListItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Asteroid) {
            binding.asteroid = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): AsteroidViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AsteroidListItemViewBinding.inflate(layoutInflater, parent, false)
                return AsteroidViewHolder(binding)
            }
        }
    }

    class AsteroidDiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(p0: Asteroid, p1: Asteroid): Boolean {
            return p0.id == p1.id
        }

        override fun areContentsTheSame(p0: Asteroid, p1: Asteroid): Boolean {
            return p0 == p1
        }
    }

    private var onItemClickListener: ((Asteroid) -> Unit)? = null

    fun setOnItemClickListener(listener: (Asteroid) -> Unit) {
        onItemClickListener = listener
    }

}