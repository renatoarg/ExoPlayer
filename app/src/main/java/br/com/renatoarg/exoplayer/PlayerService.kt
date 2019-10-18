package br.com.renatoarg.exoplayer

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast

class PlayerService : Service() {

    override fun onBind(p0: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        // Send a notification that service is started
        Toast.makeText(this@PlayerService, "service started", Toast.LENGTH_SHORT).show()


        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this@PlayerService, "service destroyed", Toast.LENGTH_SHORT).show()
    }

}