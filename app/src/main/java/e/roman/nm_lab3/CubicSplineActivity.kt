package e.roman.nm_lab3

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import kotlin.math.abs
import kotlin.math.ln
import kotlin.math.sin

class CubicSplineActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var layout: LinearLayout
    private lateinit var imView: ImageView
    private lateinit var canvas: Canvas
    private lateinit var bitmap: Bitmap
    private lateinit var paintBlack: Paint
    private lateinit var paintBlue: Paint

    private lateinit var solveBtn: Button
    private lateinit var xEt: MutableList<EditText>
    private lateinit var fEt: MutableList<EditText>
    private lateinit var xFixedEt: EditText
    private lateinit var resultTv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cubic_spline)

        layout = findViewById(R.id.layout)
        imView = findViewById(R.id.im_view)
        paintBlack = Paint()
        paintBlue = Paint()
        paintBlack.color = Color.BLACK
        paintBlue.color = Color.BLUE
        bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888)
        imView.setImageBitmap(bitmap)
        canvas = Canvas(bitmap)

        solveBtn = findViewById(R.id.btn_solve)
        solveBtn.setOnClickListener(this)
        xEt = mutableListOf(findViewById(R.id.et_x1), findViewById(R.id.et_x2), findViewById(R.id.et_x3), findViewById(R.id.et_x4), findViewById(R.id.et_x5))
        fEt = mutableListOf(findViewById(R.id.et_f1), findViewById(R.id.et_f2), findViewById(R.id.et_f3), findViewById(R.id.et_f4), findViewById(R.id.et_f5))
        xFixedEt = findViewById(R.id.et_x_fixed)
        resultTv = findViewById(R.id.tv_result)
    }

    override fun onClick(p0: View?) {
        val multer = 200f
        val sizeX = 200f
        val sizeY = 500f

        val x = mutableListOf(
            xEt[0].text.toString().toFloat(),
            xEt[1].text.toString().toFloat(),
            xEt[2].text.toString().toFloat(),
            xEt[3].text.toString().toFloat(),
            xEt[4].text.toString().toFloat()
        )
        val f = mutableListOf(
            fEt[0].text.toString().toFloat(),
            fEt[1].text.toString().toFloat(),
            fEt[2].text.toString().toFloat(),
            fEt[3].text.toString().toFloat(),
            fEt[4].text.toString().toFloat()
        )
        /*val x = mutableListOf(
            0.0f,
            1.7f,
            3.4f,
            5.1f,
            6.8f
        )
        val f = mutableListOf(
            0.0f,
            1.3038f,
            1.8439f,
            2.2583f,
            2.6077f
        )*/
        val xFixed = xFixedEt.text.toString().toFloat()
        val cSystem = mutableListOf(
            mutableListOf(
                0f,
                2 * (h(x, 1) + h(x, 2)),
                h(x, 2),
                3 * ((f[2] - f[1]) / h(x, 2) - (f[1] - f[0]) / h(x, 1))
            ),
            mutableListOf(
                h(x, 2),
                2 * (h(x, 2) + h(x, 3)),
                h(x, 3),
                3 * ((f[3] - f[2]) / h(x, 3) - (f[2] - f[1]) / h(x, 2))
            ),
            mutableListOf(
                h(x, 3),
                2 * (h(x, 3) + h(x, 4)),
                0f,
                3 * ((f[4] - f[3]) / h(x, 4) - (f[3] - f[2]) / h(x, 3))
            )
        )
        val c = triDiagonalSolve(cSystem)
        c.add(0, 0f)
        val a = mutableListOf(f[0], f[1], f[2], f[3])
        val b = mutableListOf(
            (f[1] - f[0]) / h(x, 1) - h(x, 1) * (c[1] + 2 * c[0]) / 3,
            (f[2] - f[1]) / h(x, 2) - h(x, 2) * (c[2] + 2 * c[1]) / 3,
            (f[3] - f[2]) / h(x, 3) - h(x, 3) * (c[3] + 2 * c[2]) / 3,
            (f[4] - f[3]) / h(x, 4) - h(x, 4) * c[3] * 2 / 3
        )
        val d = mutableListOf(
            (c[1] - c[0]) / (3 * h(x, 1)),
            (c[2] - c[1]) / (3 * h(x, 2)),
            (c[3] - c[2]) / (3 * h(x, 3)),
            -c[3] / (3 * h(x, 4))
        )
        canvas.drawColor(Color.WHITE)
        var xTmpPrev: Float
        var yTmpPrev: Float
        var xTmp: Float
        var yTmp: Float
        for (i in a.indices) {
            xTmpPrev = x[i]
            yTmpPrev = f[i]
            xTmp = x[i] + 0.01f
            yTmp = s(xTmp, x[i], a[i], b[i], c[i], d[i])
            while (xTmp < x[i + 1]) {
                canvas.drawLine(
                    xTmpPrev * multer + sizeX,
                    -yTmpPrev * multer + sizeY,
                    xTmp * multer + sizeX,
                    -yTmp * multer + sizeY,
                    paintBlack
                )
                xTmpPrev = xTmp
                yTmpPrev = yTmp
                xTmp += 0.01f
                yTmp = s(xTmp, x[i], a[i], b[i], c[i], d[i])
            }
            canvas.drawLine(
                xTmp * multer + sizeX,
                -yTmp * multer + sizeY,
                x[i + 1] * multer + sizeX,
                -f[i + 1] * multer + sizeY,
                paintBlack
            )
        }
        for (i in x.indices)
            canvas.drawCircle(
                x[i] * multer + sizeX,
                -f[i] * multer + sizeY,
                10f,
                paintBlue
            )
        paintBlue.color = Color.RED
        canvas.drawCircle(
            xFixed * multer + sizeX,
            -s(xFixed, x[1], a[1], b[1], c[1], d[1]) * multer + sizeY,
            10f,
            paintBlue
        )
        paintBlue.color = Color.BLUE
        imView.setImageBitmap(bitmap)
        resultTv.text = "S($xFixed) = ${s(xFixed, x[1], a[1], b[1], c[1], d[1])}"

        //ответ
    }
}