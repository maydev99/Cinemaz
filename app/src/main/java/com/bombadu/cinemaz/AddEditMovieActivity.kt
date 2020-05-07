package com.bombadu.cinemaz

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_edit_movie.*

class AddEditMovieActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NAME = "com.bombadu.cinemaz.EXTRA_NAME"
        const val EXTRA_REVIEWER = "com.bombadu.cinemaz.EXTRA_REVIEWER"
        const val EXTRA_RATING = "com.bombadu.cinemaz.EXTRA_RATING"
        const val EXTRA_SOURCE = "com.bombadu.cinemaz.EXTRA_SOURCE"
        const val EXTRA_ID = "com.bombadu.cinemaz.EXTRA_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_movie)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        rating_picker.minValue = 1
        rating_picker.maxValue = 10

        val intent = intent
        if (intent .hasExtra(EXTRA_ID)) {
            title = "Edit Movie"
            movie_name_edit_text.setText(intent.getStringExtra(EXTRA_NAME))
            source_edit_text.setText(intent.getStringExtra(EXTRA_SOURCE))
            reviewer_edit_text.setText(intent.getStringExtra(EXTRA_REVIEWER))
            rating_picker.value = intent.getIntExtra(EXTRA_RATING, 1)
        } else {
            title = "Add Record"
        }



    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_edit_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                saveMovie()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveMovie() {

        if (movie_name_edit_text.text.toString().trim().isBlank() || source_edit_text.text.toString().trim().isBlank()
            || reviewer_edit_text.text.toString().trim().isBlank()) {
            Toast.makeText(this, "Cannot insert empty movie information", Toast.LENGTH_SHORT).show()
            return
        }

        val myName = movie_name_edit_text.text.toString()
        val myRating = rating_picker.value
        val mySource = source_edit_text.text.toString()
        val myReviewer = reviewer_edit_text.text.toString()
        val data = Intent().apply {


            putExtra(EXTRA_NAME, myName)
            putExtra(EXTRA_SOURCE, mySource)
            putExtra(EXTRA_RATING, myRating)
            putExtra(EXTRA_REVIEWER, myReviewer)

            val id = intent.getIntExtra(EXTRA_ID, -1)
            if (id != -1) {
                putExtra(EXTRA_ID, id)
            }
        }
        setResult(Activity.RESULT_OK, data)
        finish()
    }
}
