package com.example.connect.screens

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.connect.R
import com.example.connect.base.BaseFragment
import com.example.connect.data.Posts
import com.example.connect.view_model.RegisterViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment: BaseFragment() {

    override val layout: Int = R.layout.fragment_register

    private val DEBUG = "DE_BUG"
    private val fbAuth = FirebaseAuth.getInstance()
    private val regVM by viewModels<RegisterViewModel>()
    val posts = Posts()

    override fun subscribeUi() {
        setOnClckZarejestruj()
    }

    private fun setOnClckZarejestruj() {
        zarejestrujBT.setOnClickListener {
            val wpiszEmail = wpiszEmailET.text.toString()
            val wpiszHaslo = wpiszHasloET.text.toString()
            val powtorzHaslo = powtorzHasloET.text.toString()
            val wpiszImie = wpiszImiee.text.toString()

            if (wpiszHaslo == powtorzHaslo) {
                //tworzymy funkcje ktora doda nasz email i haslo do bazy
                fbAuth.createUserWithEmailAndPassword(wpiszEmail, wpiszHaslo)
                    .addOnSuccessListener { authRes ->
                        if (authRes.user != null) {
                            val user = com.example.connect.data.User(
                                authRes.user!!.uid,
                                authRes.user!!.email,
                                wpiszImie
                            )
                            regVM.createNewUser(user)
                            regVM.writeNewUserRealtimeFirebase(user)
                            findNavController()
                                .navigate(RegisterFragmentDirections.actionRegisterFragmentToEditProfileFragment2().actionId)
                        }
                    }
                    .addOnFailureListener { exception ->
                        Snackbar.make(requireView(), "Cos poszlo nie tak", Snackbar.LENGTH_SHORT)
                            .show()
                        Log.d(DEBUG, exception.message.toString())
                    }
            } else if (wpiszHaslo != powtorzHaslo) {
                Snackbar.make(requireView(), "Hasła są różne.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }


    override fun unsubscribeUi() {
    }
}