package ro.pub.cs.systems.eim.practicaltest02v9

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PracticalTest02MainActivityv9 : AppCompatActivity() {
    private lateinit var receiver: AnagramReceiver
    private lateinit var etWord: EditText
    private lateinit var etMinLength: EditText
    private lateinit var btnFetch: Button
    private lateinit var tvResults: TextView

    private val anagramService by lazy { createAnagramService() }

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practical_test02v9_main)

        etWord = findViewById(R.id.etWord)
        etMinLength = findViewById(R.id.etMinLength)
        btnFetch = findViewById(R.id.btnFetch)
        tvResults = findViewById(R.id.tvResults)

        // Configurăm receiver-ul pentru broadcast
        receiver = AnagramReceiver { anagrams ->
            runOnUiThread {
                tvResults.text = anagrams.joinToString(", ")
            }
        }

        val filter = IntentFilter("ANAGRAM_RESULT")
        registerReceiver(receiver, filter, RECEIVER_EXPORTED)

        // Butonul pentru inițierea cererii
        btnFetch.setOnClickListener {
            val word = etWord.text.toString()
            val minLength = etMinLength.text.toString().toIntOrNull() ?: 0

            if (word.isNotEmpty() && minLength > 0) {
                fetchAnagrams(word, minLength)
            } else {
                tvResults.text = "Invalid input. Please try again."
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun fetchAnagrams(word: String, minLength: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = anagramService.getAnagrams(word)
                if (response.isSuccessful) {
                    val anagramResponse = response.body()
                    val allAnagrams = anagramResponse?.all ?: emptyList()

                    // Logăm răspunsul complet
                    Log.d("AnagramApp", "Răspuns complet: $allAnagrams")

                    // Filtrăm anagramele pe baza dimensiunii
                    val filteredAnagrams = allAnagrams.filter { it.length >= minLength }
                    Log.d("AnagramApp", "Anagrame filtrate: $filteredAnagrams")

                    // Trimitem datele prin broadcast
                    sendAnagramBroadcast(filteredAnagrams)
                } else {
                    Log.e("AnagramApp", "Eroare: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("AnagramApp", "Eroare: ${e.message}")
            }
        }
    }

    private fun sendAnagramBroadcast(anagrams: List<String>) {
        val intent = Intent("ANAGRAM_RESULT")
        intent.putStringArrayListExtra("anagrams", ArrayList(anagrams))
        sendBroadcast(intent)
    }

    private fun createAnagramService(): AnagramService {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://www.anagramica.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(AnagramService::class.java)
    }
}
