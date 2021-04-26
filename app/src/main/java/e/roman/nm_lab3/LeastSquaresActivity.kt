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

class LeastSquaresActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var imView: ImageView
    private lateinit var canvas: Canvas
    private lateinit var bitmap: Bitmap
    private lateinit var paintBlack: Paint

    private lateinit var solveBtn: Button
    private lateinit var xEt: MutableList<EditText>
    private lateinit var fEt: MutableList<EditText>
    private lateinit var xFixedEt: EditText
    private lateinit var resultTv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_least_squares)

        imView = findViewById(R.id.im_view)
        paintBlack = Paint()
        //paintBlack.color = Color.RED
        bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888)
        imView.setImageBitmap(bitmap)
        canvas = Canvas(bitmap)

        solveBtn = findViewById(R.id.btn_solve)
        solveBtn.setOnClickListener(this)
        xEt = mutableListOf(findViewById(R.id.et_x1), findViewById(R.id.et_x2), findViewById(R.id.et_x3), findViewById(R.id.et_x4), findViewById(R.id.et_x5), findViewById(R.id.et_x6))
        fEt = mutableListOf(findViewById(R.id.et_f1), findViewById(R.id.et_f2), findViewById(R.id.et_f3), findViewById(R.id.et_f4), findViewById(R.id.et_f5), findViewById(R.id.et_f6))
        xFixedEt = findViewById(R.id.et_x_fixed)
        resultTv = findViewById(R.id.tv_result)
    }

    override fun onClick(p0: View?) {
        val multer = 200f
        val sizeX = 200f
        val sizeY = 500f
        var res: String

        paintBlack.color = Color.RED
        val x = mutableListOf(
            xEt[0].text.toString().toFloat(),
            xEt[1].text.toString().toFloat(),
            xEt[2].text.toString().toFloat(),
            xEt[3].text.toString().toFloat(),
            xEt[4].text.toString().toFloat(),
            xEt[5].text.toString().toFloat()
        )
        val y = mutableListOf(
            fEt[0].text.toString().toFloat(),
            fEt[1].text.toString().toFloat(),
            fEt[2].text.toString().toFloat(),
            fEt[3].text.toString().toFloat(),
            fEt[4].text.toString().toFloat(),
            fEt[5].text.toString().toFloat()
        )
        /*val x = mutableListOf(
            0.0f,
            0.2f,
            0.4f,
            0.6f,
            0.8f,
            1.0f
        )
        val y = mutableListOf(
            1.0f,
            1.0032f,
            1.0512f,
            1.2592f,
            1.8192f,
            3.0f
        )*/
        var aSystem = Matrix(2, 2, 0f)
        aSystem.data[0][0] = x.size.toFloat()
        aSystem.data[0][1] = x.sum()
        aSystem.data[1][0] = aSystem.data[0][1]
        var tmp = 0f
        for (i in x)
            tmp += i * i
        aSystem.data[1][1] = tmp
        Log.d("check-ls", aSystem.data.toString())
        tmp = 0f
        for (i in x.indices)
            tmp += x[i] * y[i]
        var b = mutableListOf(y.sum(), tmp)
        var a = luSolve(aSystem, b)
        Log.d("check-ls", a.toString())
        canvas.drawColor(Color.WHITE)
        var xTmpPrev = x[0]
        var xTmp = x[0] + 0.01f
        while (xTmp < x.last()) {
            canvas.drawLine(
                xTmpPrev * multer + sizeX,
                -f(xTmpPrev, a) * multer + sizeY,
                xTmp * multer + sizeX,
                -f(xTmp, a) * multer + sizeY,
                paintBlack
            )
            Log.d("check-ls", "x: $xTmp")
            Log.d("check-ls", "y: ${f(xTmp, a)}")
            xTmpPrev = xTmp
            xTmp += 0.01f
        }
        var error = 0f
        for (i in x.indices)
            error += (f(x[i], a) - y[i]) * (f(x[i], a) - y[i])
        //Log.d("LS-check", error.toString()).
        res = "Error:\n$error"

        aSystem = Matrix(3, 3, 0f)
        aSystem.data[0][0] = x.size.toFloat()
        tmp = x.sum()
        aSystem.data[0][1] = tmp
        aSystem.data[1][0] = tmp
        tmp = 0f
        for (i in x)
            tmp += i * i
        aSystem.data[0][2] = tmp
        aSystem.data[1][1] = tmp
        aSystem.data[2][0] = tmp
        tmp = 0f
        for (i in x)
            tmp += i * i * i
        aSystem.data[1][2] = tmp
        aSystem.data[2][1] = tmp
        tmp = 0f
        for (i in x)
            tmp += i * i * i * i
        aSystem.data[2][2] = tmp
        b = mutableListOf(y.sum())
        tmp = 0f
        for (i in x.indices)
            tmp += x[i] * y[i]
        b.add(tmp)
        tmp = 0f
        for (i in x.indices)
            tmp += x[i] * x[i] * y[i]
        b.add(tmp)
        a = luSolve(aSystem, b)
        paintBlack.color = Color.BLUE
        xTmpPrev = x[0]
        xTmp = x[0] + 0.01f
        while (xTmp < x.last()) {
            canvas.drawLine(
                xTmpPrev * multer + sizeX,
                -f(xTmpPrev, a) * multer + sizeY,
                xTmp * multer + sizeX,
                -f(xTmp, a) * multer + sizeY,
                paintBlack
            )
            xTmpPrev = xTmp
            xTmp += 0.01f
        }
        error = 0f
        for (i in x.indices)
            error += (f(x[i], a) - y[i]) * (f(x[i], a) - y[i])
        res = "$res\n$error"
        resultTv.text = res
        //Log.d("LS-check", error.toString())

        paintBlack.color = Color.BLACK
        for (i in x.indices)
            canvas.drawCircle(
                x[i] * multer + sizeX,
                -y[i] * multer + sizeY,
                5f,
                paintBlack
            )
    }
}