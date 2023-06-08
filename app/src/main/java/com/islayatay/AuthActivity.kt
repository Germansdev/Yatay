package com.islayatay

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.islayatay.databinding.ActivityAuthBinding


class AuthActivity : AppCompatActivity() {

    private val GOOGLE_SIGN_IN = 100

    private val callbackManager = CallbackManager.Factory.create()

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        /**Splash IMPLEMENTAR SPLASH EN UN THEME E INCOCAR AQUI
        Thread.sleep(200)
        setTheme(SplashTheme)*/


        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setContentView(R.layout.activity_auth)

        //Analytics Event
        val analytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Integracion de Firebase completa")
        analytics.logEvent("InitScreen", bundle)

        /**Remote Config
        val configSettings: FirebaseRemoteConfigSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 60
        }
        val firebaseConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        firebaseConfig.setConfigSettingsAsync(configSettings)
        firebaseConfig.setDefaultsAsync(mapOf("alerta_message" to false))
        */

        //Setup
        setup()
        session()
    }

    override fun onStart() {
        super.onStart()
        binding.LinearVertResult.visibility = View.VISIBLE
    }

    private fun session() {
        val prefs: SharedPreferences? = getSharedPreferences(
            getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs?.getString("email", null)
        val provider = prefs?.getString("provider", null)

        if (email != null && provider != null) {
            binding.LinearVertResult.visibility = View.INVISIBLE
            showHome(email, ProviderType.valueOf(provider))
        }
    }

    private fun setup() {
        title = "Autenticación"

        binding.registrarBtn.setOnClickListener {
            if (binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()) {

                FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(
                        binding.emailEditText.text.toString(),
                        binding.passwordEditText.text.toString()
                    ).addOnCompleteListener {

                        if (it.isSuccessful) {
                            showHome(
                                it.result?.user?.email
                                    ?: "esto seria un string vacio porque no hay mail",
                                ProviderType.BASIC
                            )

                        } else {
                            showAlert()
                            //lo siguiente lo hizo gerdev
                            //var apply = apply { delay(40000) }
                            //throw RuntimeException("Test Crash") // Force a crash
                        }
                    }
            }
        }
        binding.accederBtn?.setOnClickListener {
            if (binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()) {

                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(
                        binding.emailEditText.text.toString(),
                        binding.passwordEditText.text.toString()
                    ).addOnCompleteListener {

                        if (it.isSuccessful) {
                            showHome(
                                it.result?.user?.email?: "",
                                ProviderType.BASIC
                            )

                        } else {
                            showAlert()

                        }
                    }
            }
        }

        binding.googleButon?.setOnClickListener {
            //Configuracion
            val googleConf = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient = GoogleSignIn.getClient(this, googleConf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)

        }

        //Facebook:
        binding.facebookbtn?.setOnClickListener {

            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))


            //configuracin
            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult) {
                        result?.let {
                            val token: AccessToken = it.accessToken

                            val credential = FacebookAuthProvider.getCredential(token.token)

                            FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {

                                if (it.isSuccessful){
                                    showHome(it.result?.user?.email?:"", ProviderType.FACEBOOK)
                                }else{
                                    showAlert()
                                    //lo siguiente lo hizo gerdev
                                    //var apply = apply { delay(40000) }
                                    //throw RuntimeException("Test Crash") // Force a crash
                                }
                            }

                        }

                    }

                    override fun onCancel() {

                    }

                    override fun onError(error: FacebookException) {
                        showAlert()
                    }
                })
        }


    }


    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando el usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email: String, provider: ProviderType) {
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        callbackManager.onActivityResult(requestCode, resultCode, data)

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)

                if (account != null){
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {

                        if (it.isSuccessful){
                            showHome(account.email ?:"", ProviderType.GOOGLE)
                        }else{
                            showAlert()
                            //lo siguiente lo hizo gerdev
                            //var apply = apply { delay(40000) }
                            //throw RuntimeException("Test Crash") // Force a crash
                        }
                    }

                }
            }catch (e: ApiException){
                showAlert()
                //Esto lo siguiente lo hizo gerdev
                //var apply = apply { delay(40000) }
                //throw RuntimeException("Test Crash") // Force a crash
            }

        }
    }
}

