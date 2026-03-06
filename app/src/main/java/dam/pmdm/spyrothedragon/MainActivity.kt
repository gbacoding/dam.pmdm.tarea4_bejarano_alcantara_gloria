package dam.pmdm.spyrothedragon

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import dam.pmdm.spyrothedragon.databinding.ActivityMainBinding

// --- IMPORTS ---
import android.media.MediaPlayer
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.graphics.drawable.AnimationDrawable
import android.view.GestureDetector
import android.view.animation.*
import android.graphics.Rect
import android.view.MotionEvent
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var navController: NavController? = null

    // Variables Multimedia
    private var mediaPlayerVideo: MediaPlayer? = null
    private var musicPlayer: MediaPlayer? = null
    private var contadorClicksMundos = 0
    private var ultimoClickMundos: Long = 0

    // Detector para el Easter Egg de Ripto
    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment: Fragment? = supportFragmentManager.findFragmentById(R.id.navHostFragment)
        navHostFragment?.let {
            navController = NavHostFragment.findNavController(it)
            NavigationUI.setupWithNavController(binding.navView, navController!!)
            NavigationUI.setupActionBarWithNavController(this, navController!!)
        }

        binding.navView.setOnItemSelectedListener { selectedBottomMenu(it) }


        //  GESTURE DETECTOR (RIPTO)

        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(e: MotionEvent) {
                if (navController?.currentDestination?.id == R.id.navigation_characters) {
                    val rv = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerViewCharacters)
                    val canvas = findViewById<RiptoMagicView>(R.id.riptoCanvas)

                    for (i in 0 until (rv?.childCount ?: 0)) {
                        val item = rv?.getChildAt(i) ?: continue
                        val tvName = item.findViewById<TextView>(R.id.name)

                        if (tvName?.text?.toString()?.contains("Ripto", true) == true) {
                            val cardLoc = IntArray(2)
                            item.getLocationOnScreen(cardLoc)
                            val cardRect = Rect(cardLoc[0], cardLoc[1], cardLoc[0] + item.width, cardLoc[1] + item.height)

                            if (cardRect.contains(e.rawX.toInt(), e.rawY.toInt())) {
                                // Buscamos la imagen para precisar el cetro
                                val imageView = item.findViewById<View>(R.id.image)
                                val imgLoc = IntArray(2)
                                imageView.getLocationOnScreen(imgLoc)

                                val canvasLoc = IntArray(2)
                                canvas?.getLocationOnScreen(canvasLoc)

                                // AJUSTE AL CETRO: 20% ancho (izquierda), 35% alto (arriba)
                                val relativeX = (imgLoc[0] + imageView.width * 0.20f) - canvasLoc[0]
                                val relativeY = (imgLoc[1] + imageView.height * 0.35f) - canvasLoc[1]

                                // Llamada limpia con 2 parámetros
                                canvas?.animarCetro(relativeX, relativeY)
                                break
                            }
                        }
                    }
                }
            }
        })

        // --- LÓGICA DE LA GUÍA ---
        val prefs = getSharedPreferences("ConfigApp", MODE_PRIVATE)
        val guiaYaCompletada = prefs.getBoolean("guiaCompletada", false)

        if (!guiaYaCompletada) {
            binding.guiaContainer.visibility = View.VISIBLE
            val p1 = binding.includeGuia
            p1.layoutGuiaPantalla1.visibility = View.VISIBLE
            iniciarMusicaFondo()
            reproducirVideo(p1.surfaceFire)
            binding.btnOmitirGuiaGlobal.setOnClickListener {
                reproducirEfecto(R.raw.skip_sound)
                finalizarTodoLaGuia(prefs)
            }
            p1.btnComenzarGuia.setOnClickListener {
                reproducirEfecto(R.raw.next_sound)
                p1.layoutGuiaPantalla1.visibility = View.GONE
                liberarVideo()
                val p2 = binding.includeGuiaP2
                p2.layoutGuiaPantalla2.visibility = View.VISIBLE
                p2.imgBocadilloAnimadoP2.setBackgroundResource(R.drawable.animacion_bocadillo_pantalla2)
                p2.imgBocadilloAnimadoP2.translationX = 800f
                p2.imgBocadilloAnimadoP2.animate().translationX(0f).setDuration(600).start()
                (p2.imgBocadilloAnimadoP2.background as? AnimationDrawable)?.start()
            }
            binding.includeGuiaP2.btnEntendidoGuia.setOnClickListener {
                reproducirEfecto(R.raw.next_sound)
                binding.includeGuiaP2.layoutGuiaPantalla2.visibility = View.GONE
                binding.navView.selectedItemId = R.id.nav_worlds
                val p3 = binding.includeGuiaP3
                p3.layoutGuiaPantalla3.visibility = View.VISIBLE
                p3.imgBocadilloAnimadoP3.setBackgroundResource(R.drawable.animacion_bocadillo_pantalla2)
                p3.imgBocadilloAnimadoP3.scaleX = 0f
                p3.imgBocadilloAnimadoP3.scaleY = 0f
                p3.imgBocadilloAnimadoP3.animate().scaleX(1f).scaleY(1f).setDuration(700).setInterpolator(OvershootInterpolator()).start()
                (p3.imgBocadilloAnimadoP3.background as? AnimationDrawable)?.start()
            }
            binding.includeGuiaP3.btnEntendidoGuiaP3.setOnClickListener {
                reproducirEfecto(R.raw.next_sound)
                binding.includeGuiaP3.layoutGuiaPantalla3.visibility = View.GONE
                binding.navView.selectedItemId = R.id.nav_collectibles
                val p4 = binding.includeGuiaP4
                p4.layoutGuiaPantalla4.visibility = View.VISIBLE
                p4.imgBocadilloAnimadoP4.setBackgroundResource(R.drawable.animacion_bocadillo_invertido_pantalla4)
                p4.imgBocadilloAnimadoP4.translationY = -700f
                p4.imgBocadilloAnimadoP4.animate().translationY(0f).setDuration(900).setInterpolator(BounceInterpolator()).start()
                (p4.imgBocadilloAnimadoP4.background as? AnimationDrawable)?.start()
            }
            binding.includeGuiaP4.btnFinalizarGuia.setOnClickListener {
                reproducirEfecto(R.raw.next_sound)
                binding.includeGuiaP4.layoutGuiaPantalla4.visibility = View.GONE
                val p5 = binding.includeGuiaP5
                p5.layoutGuiaPantalla5.visibility = View.VISIBLE
                p5.imgBocadilloAnimadoP5.setBackgroundResource(R.drawable.animacion_bocadillo_pantalla5)
                p5.imgBocadilloAnimadoP5.scaleX = 0.2f
                p5.imgBocadilloAnimadoP5.scaleY = 0.2f
                p5.imgBocadilloAnimadoP5.animate().scaleX(1f).scaleY(1f).setDuration(600).start()
                (p5.imgBocadilloAnimadoP5.background as? AnimationDrawable)?.start()
            }
            binding.includeGuiaP5.btnFinalizarTodo.setOnClickListener {
                reproducirEfecto(R.raw.next_sound)
                binding.includeGuiaP5.layoutGuiaPantalla5.visibility = View.GONE
                val p6 = binding.includeGuiaP6
                p6.layoutGuiaPantalla6.visibility = View.VISIBLE
                p6.layoutGuiaPantalla6.alpha = 0f
                p6.layoutGuiaPantalla6.rotation = -180f
                p6.layoutGuiaPantalla6.animate().rotation(0f).alpha(1f).setDuration(800).start()
            }
            binding.includeGuiaP6.btnFinalizarGuiaTotal.setOnClickListener {
                reproducirEfecto(R.raw.end_guide_sound)
                finalizarTodoLaGuia(prefs)
            }
        } else {
            binding.guiaContainer.visibility = View.GONE
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(ev)
        if (ev.action == MotionEvent.ACTION_DOWN) {
            if (navController?.currentDestination?.id == R.id.navigation_worlds) {
                val tiempoActual = System.currentTimeMillis()
                if (tiempoActual - ultimoClickMundos < 800) {
                    contadorClicksMundos++
                } else { contadorClicksMundos = 1 }
                ultimoClickMundos = tiempoActual
                if (contadorClicksMundos == 3) {
                    contadorClicksMundos = 0
                    activarVideoEasterEggMundos()
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun finalizarTodoLaGuia(prefs: android.content.SharedPreferences) {
        pararMusicaFondo()
        binding.guiaContainer.visibility = View.GONE
        prefs.edit().putBoolean("guiaCompletada", true).apply()
        navController?.navigate(R.id.navigation_characters)
    }

    private fun iniciarMusicaFondo() {
        try {
            if (musicPlayer == null) {
                musicPlayer = MediaPlayer.create(this, R.raw.background_music)
                musicPlayer?.isLooping = true
                musicPlayer?.setVolume(0.4f, 0.4f)
                musicPlayer?.start()
            }
        } catch (e: Exception) { e.printStackTrace() }
    }

    private fun pararMusicaFondo() {
        musicPlayer?.let { if (it.isPlaying) it.stop(); it.release() }
        musicPlayer = null
    }

    private fun reproducirEfecto(resId: Int) {
        try {
            // Creamos el reproductor con el contexto de la actividad
            val mp = MediaPlayer.create(this, resId)
            mp?.let {
                it.setVolume(1.0f, 1.0f)
                it.setOnCompletionListener { player ->
                    player.release()
                }
                it.start()
            }
        } catch (e: Exception) {
            android.util.Log.e("AUDIO_ERROR", "Error al reproducir el efecto: ${e.message}")
        }
    }

    private fun reproducirVideo(sv: SurfaceView) {
        sv.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(h: SurfaceHolder) {
                try {
                    mediaPlayerVideo = MediaPlayer.create(this@MainActivity, R.raw.fuego_loop)
                    mediaPlayerVideo?.setDisplay(h)
                    mediaPlayerVideo?.isLooping = true
                    mediaPlayerVideo?.setVolume(0f, 0f)
                    mediaPlayerVideo?.start()
                } catch (e: Exception) { e.printStackTrace() }
            }
            override fun surfaceChanged(h: SurfaceHolder, f: Int, w: Int, hi: Int) {}
            override fun surfaceDestroyed(h: SurfaceHolder) { liberarVideo() }
        })
    }

    private fun liberarVideo() {
        mediaPlayerVideo?.let { if (it.isPlaying) it.stop(); it.release() }
        mediaPlayerVideo = null
    }

    private fun activarVideoEasterEggMundos() {
        val container = findViewById<android.widget.FrameLayout>(R.id.easterEggVideoContainer)
        val videoView = findViewById<android.widget.VideoView>(R.id.videoViewEasterEgg)
        if (container != null && videoView != null) {
            container.visibility = View.VISIBLE
            val videoPath = "android.resource://${packageName}/${R.raw.video_easter_egg1}"
            videoView.setVideoPath(videoPath)
            videoView.setOnCompletionListener { container.visibility = View.GONE }
            videoView.start()
        }
    }

    private fun selectedBottomMenu(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_characters -> navController?.navigate(R.id.navigation_characters)
            R.id.nav_worlds -> navController?.navigate(R.id.navigation_worlds)
            else -> navController?.navigate(R.id.navigation_collectibles)
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.about_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_info) {
            showInfoDialog()
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun showInfoDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.title_about)
            .setMessage(R.string.text_about)
            .setPositiveButton(R.string.accept, null)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        liberarVideo()
        pararMusicaFondo()
    }
}