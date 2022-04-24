package com.example.connect.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.connect.R
import kotlinx.android.synthetic.main.activity_edit_user_profile.*

class EditUserProfileActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user_profile)

        return_posts.setOnClickListener {
            onBackPressed()
        }
    }
}