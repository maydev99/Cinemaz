package com.bombadu.cinemaz

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.createBitmap
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bombadu.cinemaz.db.Movie
import com.bombadu.cinemaz.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.sort_order_dialog_layout.*

class MainActivity : AppCompatActivity() {

    lateinit var movieViewModel: MovieViewModel
    private val adapter = MovieAdapter()
    private var sortOrder = "rating"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadSharedPrefs()
        setupRecyclerView()
        getMovieList(sortOrder)




        floatingActionButton.setOnClickListener {
            startActivityForResult(
                Intent(this, AddEditMovieActivity::class.java),
                ADD_NOTE_REQUEST
            )
        }
    }

    private fun getMovieList(sortOrder: String) {
        if (sortOrder == "name") {
            movieViewModel.getAllMovies().observe(this,
                Observer { list ->
                    list?.let {
                        adapter.setMovies(it)
                        adapter.notifyDataSetChanged()
                    }
                })
        } else if (sortOrder == "rating") {
            movieViewModel.getAllMoviesByRating().observe(this,
                Observer { list ->
                    list?.let {
                        adapter.setMovies(it)
                        adapter.notifyDataSetChanged()
                    }
                })
        } else if (sortOrder == "reviewer") {
            movieViewModel.getAllMoviesByReviewer().observe(this,
                Observer { list ->
                    list?.let {
                        adapter.setMovies(it)
                        adapter.notifyDataSetChanged()
                    }
                })
        } else {
            movieViewModel.getAllMoviesBySource().observe(this,
                Observer { list ->
                    list?.let {
                        adapter.setMovies(it)
                        adapter.notifyDataSetChanged()
                    }
                })
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_NOTE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val newMovie = Movie(
                data.getStringExtra(AddEditMovieActivity.EXTRA_NAME),
                data.getIntExtra(AddEditMovieActivity.EXTRA_RATING, 10),
                data.getStringExtra(AddEditMovieActivity.EXTRA_REVIEWER),
                data.getStringExtra(AddEditMovieActivity.EXTRA_SOURCE)
            )

            movieViewModel.insertMovie(newMovie)
            makeAToast("Movie Saved")
        } else {
            makeAToast("Movie Not Saved")
        }

        if (requestCode == EDIT_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val id = data?.getIntExtra(AddEditMovieActivity.EXTRA_ID, -1)
            if (id == -1) {
                makeAToast("Note cannot be updated")
                return
            }

            val updateName = data?.getStringExtra(AddEditMovieActivity.EXTRA_NAME)
            val updateReviewer = data?.getStringExtra(AddEditMovieActivity.EXTRA_REVIEWER)
            val updateSource = data?.getStringExtra(AddEditMovieActivity.EXTRA_SOURCE)
            val updateRating = data?.getIntExtra(AddEditMovieActivity.EXTRA_RATING, 10)
            val updatedMovie = Movie(updateName!!, updateRating!!, updateReviewer!!, updateSource!!)
            if (id != null) {
                updatedMovie.id = id
            }

            movieViewModel.updateMovie(updatedMovie)
            makeAToast("Movie Updated")
        } else {
            makeAToast("Movie Not Saved")
        }
    }

    private fun setupRecyclerView() {
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
        recycler_view.adapter = adapter
        this.movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        if (sortOrder == "name") {
            movieViewModel.getAllMovies().observe(this, Observer { allMovies ->
                allMovies?.let { adapter.setMovies(it) }
            })
        } else if (sortOrder == "rating") {
            movieViewModel.getAllMoviesByRating().observe(this, Observer { allMovies ->
                allMovies?.let { adapter.setMovies(it) }
            })
        } else if (sortOrder == "reviewer") {
            movieViewModel.getAllMoviesByReviewer().observe(this, Observer { allMovies ->
                allMovies?.let { adapter.setMovies(it) }
            })
        } else {
            movieViewModel.getAllMoviesBySource().observe(this, Observer { allMovies ->
                allMovies?.let { adapter.setMovies(it) }
            })
        }





        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.getMovieAt(viewHolder.adapterPosition)
                    ?.let { movieViewModel.deleteMovie(it) }
                makeAToast("Note Deleted")
            }
        }).attachToRecyclerView(recycler_view)


        adapter.onItemClick = { pos, _ ->
            val myMovie = adapter.getMovieAt(pos)
            intent = Intent(this, AddEditMovieActivity::class.java)
            intent.putExtra(AddEditMovieActivity.EXTRA_NAME, myMovie!!.name)
            intent.putExtra(AddEditMovieActivity.EXTRA_REVIEWER, myMovie.reviewer)
            intent.putExtra(AddEditMovieActivity.EXTRA_RATING, myMovie.rating)
            intent.putExtra(AddEditMovieActivity.EXTRA_SOURCE, myMovie.source)
            intent.putExtra(AddEditMovieActivity.EXTRA_ID, myMovie.id)
            startActivityForResult(intent, EDIT_NOTE_REQUEST)
        }
    }

    companion object {
        private const val ADD_NOTE_REQUEST = 1
        private const val EDIT_NOTE_REQUEST = 2
    }

    private fun makeAToast(tMessage: String) {
        Toast.makeText(this, tMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_all -> {
                val deleteDialog = AlertDialog.Builder(this)
                deleteDialog.setTitle("Delete All Records")
                deleteDialog.setMessage("Are you sure?")
                deleteDialog.setIcon(R.mipmap.ic_launcher)
                deleteDialog.setPositiveButton("delete") { _, _ ->
                    movieViewModel.deleAllMovies()
                    makeAToast("All Data Deleted")
                }
                deleteDialog.setNegativeButton("cancel") { _, _ ->
                    //Closes Dialog
                }
                deleteDialog.show()

            }
        }


        when (item.itemId) {
            R.id.sort_dialog -> {
                val dialog = Dialog(this)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(true)
                dialog.setContentView(R.layout.sort_order_dialog_layout)
                dialog.title_button.setOnClickListener {
                    sortOrder = "name"
                    setupRecyclerView()
                    getMovieList(sortOrder)
                    saveSortOrder()
                    dialog.cancel()
                }
                dialog.rating_button.setOnClickListener {
                    sortOrder = "rating"
                    setupRecyclerView()
                    getMovieList(sortOrder)
                    saveSortOrder()
                    dialog.cancel()
                }

                dialog.reviewer_button.setOnClickListener {
                    sortOrder = "reviewer"
                    setupRecyclerView()
                    getMovieList(sortOrder)
                    saveSortOrder()
                    dialog.cancel()
                }

                dialog.source_button.setOnClickListener {
                    sortOrder = "source"
                    setupRecyclerView()
                    getMovieList(sortOrder)
                    saveSortOrder()
                    dialog.cancel()
                }

                dialog.show()

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveSortOrder() {
        val sharedPrefs: SharedPreferences = getSharedPreferences("prefs_key", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPrefs.edit()
        editor.putString("prefs_key", sortOrder)
        editor.apply()
    }

    private fun loadSharedPrefs() {
        val sharedPrefs: SharedPreferences = getSharedPreferences("prefs_key", Context.MODE_PRIVATE)
        sortOrder = sharedPrefs.getString("prefs_key", "name").toString()
    }


}
