package com.example.social

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.AbdAllahAbdElFattah13.linkedinsdk.ui.LinkedInUser
import com.AbdAllahAbdElFattah13.linkedinsdk.ui.linkedin_builder.LinkedInBuilder
import com.AbdAllahAbdElFattah13.linkedinsdk.ui.linkedin_builder.LinkedInFromActivityBuilder
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var callBackManager : CallbackManager

    lateinit var name :String
    lateinit var picUrl :String
    lateinit var email :String
    lateinit var id : String

    private var LINKEDIN_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        callBackManager = CallbackManager.Factory.create()

        facebookSignup.setPermissions(Arrays.asList("email", "public_profile"))

        linkedinSignup.setOnClickListener{
            LinkedInFromActivityBuilder.getInstance(this)
                .setClientID("78cbhep5vihrtg")
                .setClientSecret("PL4stChO0UlxyMzK")
                .setRedirectURI("https://anti-social-auth.com/login")
                .authenticate(LINKEDIN_REQUEST_CODE)
            showProgress()
        }

        facebookSignup.registerCallback(callBackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {

                val request = GraphRequest.newMeRequest(loginResult?.accessToken) { `object`,response->
                    try {
                        //here is the data that you want
                        name = `object`.getString("name")
                        email = `object`.getString("email")
                        id = `object`.getString("id")
                        Log.d("UserID",id)
                        picUrl = "https://graph.facebook.com/"+id+"/picture?type=normal"
                        startIntent()

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                val parameters = Bundle()
                parameters.putString("fields", "name,email,id,picture.type(large)")
                request.parameters = parameters
                request.executeAsync()

            }

            override fun onCancel() {
            }

            override fun onError(error: FacebookException?) {
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LINKEDIN_REQUEST_CODE && data != null) {
            if (resultCode == RESULT_OK) {
                //Successfully signed in
                val user: LinkedInUser? = data.getParcelableExtra("social_login")

                //acessing user info
                if (user != null) {
                    hideProgress()
                    name = user.firstName.toString()+" "+user.lastName.toString()
                    picUrl = user.profilePictureUrl.toString()
                    email = user.email.toString()
                    startIntent()

                }
            } else {
                if (data.getIntExtra("err_code", 0) == LinkedInBuilder.ERROR_USER_DENIED) {
                    //Handle : user denied access to account
                } else if (data.getIntExtra("err_code", 0) == LinkedInBuilder.ERROR_FAILED) {

                    //Handle : Error in API : see logcat output for details
                    data.getStringExtra("err_message")?.let { Log.e("LINKEDIN ERROR", it) }
                }
            }
        }
        else
        {
            callBackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun startIntent() {
        val intent = Intent(this, InfoActivity::class.java)
        intent.putExtra("Name", name)
        intent.putExtra("PicUrl", picUrl)
        intent.putExtra("Email", email)
        startActivity(intent)
    }

    private fun showProgress()
    {
        progressBar.visibility = View.VISIBLE
    }
    private fun hideProgress()
    {
        progressBar.visibility = View.GONE
    }
}