package com.example.social

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        nameTextView.text = intent.getStringExtra("Name")
        emailTextView.text = intent.getStringExtra("Email")
        val picUrl = intent.getStringExtra("PicUrl")
        Glide
            .with(this)
            .load(picUrl)
            .circleCrop()
            .into(userImage)
        back_button.setOnClickListener {
            val intent1 = Intent(this,MainActivity::class.java)
            startActivity(intent1)
        }

    }
}