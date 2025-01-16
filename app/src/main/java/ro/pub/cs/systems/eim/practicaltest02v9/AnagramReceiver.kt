package ro.pub.cs.systems.eim.practicaltest02v9

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AnagramReceiver(private val callback: (List<String>) -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val anagrams = intent?.getStringArrayListExtra("anagrams") ?: emptyList<String>()
        callback(anagrams)
    }
}
