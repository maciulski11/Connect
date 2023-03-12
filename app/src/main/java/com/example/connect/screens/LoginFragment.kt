package com.example.connect.screens

import android.content.Intent
import android.util.Log
import androidx.navigation.fragment.findNavController
import com.example.connect.R
import com.example.connect.activity.MainActivity
import com.example.connect.base.BaseFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*

class LoginFragment : BaseFragment() {

    override val layout: Int = R.layout.fragment_login

    private companion object {
        private const val RC_GOOGLE_SIGN_IN = 9001
        private const val RESULT_CANCELD = 1
    }

    private val DEBUG = "DE_BUG"
    private val fbAuth = FirebaseAuth.getInstance()
    private var googleSignInClient: GoogleSignInClient? = null

    private var flags: Int = 0

    override fun subscribeUi() {

        setOnClckRegister()
        setOnClickLogin()
        uzytkownikJestJuzZalogowanyDoApki()
        googleSignIN()
    }

    private fun setOnClckRegister() {
        przeniesRejestracja.setOnClickListener {
            findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment().actionId)
        }
    }


    private fun setOnClickLogin() {
        loginButton.setOnClickListener {
            val email = logowanie.text.toString()
            val password = hasloInput.text.toString()

            if (email == "" || password == "") {
                return@setOnClickListener
            } else {

                //sprawdzamy czy takie dane sa w naszej bazie danych
                fbAuth.signInWithEmailAndPassword(
                    email,
                    password
                ) //->nasze dane z ET wysylamy do naszej funkcji
                    .addOnSuccessListener { authRes ->  //jezeli logowanie sie powiodlo
                        if (authRes != null) {
                            val intent = Intent(requireContext(), MainActivity::class.java)
                            startActivity(intent)
                           }
                    }
                    .addOnFailureListener { exception -> //jezeli cos pojdzie nie tak
                        Snackbar.make(requireView(), "Cos poszlo nie tak", Snackbar.LENGTH_SHORT)
                            .show()
                        Log.d(DEBUG, exception.message.toString())
                    }
            }
        }

    }

    // tworzymy ta funkcje ktora sprawdzi czy jestemy zalogowani po to zeby za kazdym wejsciem
    //nie musieli sie logowac ponownie:
    private fun uzytkownikJestJuzZalogowanyDoApki() {

        //spr czy jest zalogowany i to moze byc nullem dlatego korzystamy z let
        fbAuth.currentUser?.let { auth -> //jezeli nie jest nullem to odpalamy glowna aktywnosc
            //curentUser -> spr czy uzytkownik jest zalogowany
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
                //   .apply {
                //    flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    //flagi maja zadanie ze po wejsciu juz do glownej aktywnosci maja wyczyscic aktywnosc
                    //logowania zeby nie dalo sie do tego cofnac tylko wyjdiemy z apki
              //  }
        }
    }

    private fun googleSignIN() {

        sign_in_button_google.setOnClickListener {

            // Configure Google Sign In
//            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build()

//            googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

            signIn()
        }

    }

    private fun signIn() {
        val signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = fbAuth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user == null) {
            Log.w("lipa", "Google sign in failed")
            return
        } else {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)//przenosimy uzytkownika po kliknieciu w nasz przycisk

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("lipa", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("lipa", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        fbAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("lipa", "signInWithCredential:success")
                    val user = fbAuth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("lipa", "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    override fun unsubscribeUi() {
    }
}