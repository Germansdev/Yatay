package com.islayatay

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.islayatay.databinding.ActivityHomeBinding


enum class ProviderType {
    BASIC,
    GOOGLE,
    FACEBOOK
}

class HomeActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

       binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Setup recupero los datos con un bundle
        val bundle: Bundle? = intent.extras
        val email: String? = bundle?.getString("email")
        val provider: String? = bundle?.getString("provider")
        setup(email?:"", provider?:"")

        // Guardado de datos y asi recuperar en futuras sesiones
        val prefs: SharedPreferences.Editor = getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE).edit()
        prefs.putString("email",email)
        prefs.putString("provider", provider)
        prefs.apply()

        /**Remote Config
        binding.alertaTextView3.visibility= View.INVISIBLE
        Firebase.remoteConfig.fetchAndActivate().addOnCompleteListener{
            if(it.isSuccessful){
                val showAlertMessageText = Firebase.remoteConfig.getBoolean("alerta_message")

                if(showAlertMessageText){
                    binding.alertaTextView3.visibility=View.VISIBLE
                }

            }
        }*/


    }

    private fun setup(email: String, provider: String) {
        title = "Inicio"
        binding.emailTextView.text = email
        binding.providerTextView.text = provider
        binding.cerrarSesionBtn.setOnClickListener {

            //Borrado de datos
            val prefs: SharedPreferences.Editor = getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()


           //facebook:
            if (provider==ProviderType.FACEBOOK.name){
                LoginManager.getInstance().logOut()
            }

            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }

        binding.saveBtn.setOnClickListener {
        db.collection("users").document(email).set(
            hashMapOf("provider" to provider,
            "address" to binding.addressEditText.text.toString(),
            "phone" to binding.phoneEditText.text.toString())
        )
        }

        binding.getBtn.setOnClickListener {

        }
        binding.deleteBtn.setOnClickListener {

        }



    }

 /**   private fun showAuth(email: String, provider: ProviderType) {
        val authIntent = Intent(this, AuthActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(authIntent)
    }
 */

}

