package com.bombadu.cinemaz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bombadu.cinemaz.db.Movie

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieHolder>() {
    private var movies: List<Movie> = ArrayList()
    private var itemClickCallback: ItemClickCallback? = null
    var onItemClick: ((pos: Int, view: View) -> Unit)? = null


    internal interface  ItemClickCallback {
        fun onItemClick(p: Int)
    }

    internal fun setItemClickCallback (inItemClickCallback: ItemClickCallback) {
        this.itemClickCallback = inItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieAdapter.MovieHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_card, parent, false)
        return MovieHolder(itemView)
    }

    override fun getItemCount(): Int {
        return  movies.size

    }

    fun getMovieAt(position: Int) : Movie? {
        return movies[position]
    }

    fun setMovies(movie: List<Movie>) {
        this.movies = movie
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MovieAdapter.MovieHolder, position: Int) {
        val currentMovie = movies[position]
        holder.textViewName.text = currentMovie.name
        holder.textViewRating.text = "Rating: ${currentMovie.rating}"
        holder.textViewReviewer.text = "Reviewer: ${currentMovie.reviewer}"
        holder.textViewSource.text = "Watch on: ${currentMovie.source}"
    }

    inner class MovieHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var textViewName: TextView = itemView.findViewById(R.id.nameTextView)
        var textViewRating: TextView = itemView.findViewById(R.id.ratingTextView)
        var textViewSource: TextView = itemView.findViewById(R.id.sourceTextView)
        var textViewReviewer: TextView = itemView.findViewById(R.id.reviewerTextView)
        override fun onClick(v: View?) {
            if (v != null) {
                onItemClick?.invoke(adapterPosition, v)
            }
        }

        init {
            itemView.setOnClickListener(this)
        }

    }

}