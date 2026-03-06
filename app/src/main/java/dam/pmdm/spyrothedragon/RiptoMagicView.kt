package dam.pmdm.spyrothedragon

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

class RiptoMagicView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var centerX = 0f
    private var centerY = 0f
    private var radius = 0f
    private var isAnimating = false

    // Pincel para el brillo
    private val paintBrillo = Paint().apply {
        color = ContextCompat.getColor(context, R.color.purple_light)
        style = Paint.Style.FILL
        isAntiAlias = true
        // Añadimos un desenfoque para que parezca magia real
        maskFilter = BlurMaskFilter(30f, BlurMaskFilter.Blur.NORMAL)
    }

    // Pincel para el núcleo
    private val paintNucleo = Paint().apply {
        color = ContextCompat.getColor(context, R.color.yellow)
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    fun animarCetro(x: Float, y: Float) {
        this.centerX = x
        this.centerY = y
        this.visibility = VISIBLE

        // Animación del radio: De 0 a 120 (más grande ahora)
        val animator = ValueAnimator.ofFloat(0f, 120f)
        animator.duration = 800
        animator.addUpdateListener {
            radius = it.animatedValue as Float
            invalidate() // Redibuja
        }

        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) { isAnimating = true }
            override fun onAnimationEnd(p0: Animator) {
                isAnimating = false
                visibility = GONE // Se oculta al terminar
            }
            override fun onAnimationCancel(p0: Animator) {}
            override fun onAnimationRepeat(p0: Animator) {}
        })

        animator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isAnimating) {
            // Dibujamos el halo exterior morado
            canvas.drawCircle(centerX, centerY, radius, paintBrillo)
            // Dibujamos el núcleo amarillo (más pequeño)
            canvas.drawCircle(centerX, centerY, radius * 0.4f, paintNucleo)
        }
    }
}