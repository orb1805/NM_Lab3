package e.roman.nm_lab3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlin.math.abs
import kotlin.math.sign

class NewtonPolynomialActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var solveBtn: Button
    private lateinit var xEt: MutableList<EditText>
    private lateinit var xFixedEt: EditText
    private lateinit var lXTv: TextView
    private lateinit var xFixed1Tv: TextView
    private lateinit var xFixed2Tv: TextView
    private lateinit var error1Tv: TextView
    private lateinit var error2Tv: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newton_polynomial)

        solveBtn = findViewById(R.id.btn_solve)
        solveBtn.setOnClickListener(this)
        xEt = mutableListOf(findViewById(R.id.et_x1), findViewById(R.id.et_x2), findViewById(R.id.et_x3), findViewById(R.id.et_x4))
        xFixedEt = findViewById(R.id.et_x_fixed)
        lXTv = findViewById(R.id.tv_l_x)
        xFixed1Tv = findViewById(R.id.tv_fixed_l1)
        xFixed2Tv = findViewById(R.id.tv_fixed_l2)
        error1Tv = findViewById(R.id.tv_error1)
        error2Tv = findViewById(R.id.tv_error2)
    }

    override fun onClick(p0: View?) {
        val x = mutableListOf(
            xEt[0].text.toString().toFloat(),
            xEt[1].text.toString().toFloat(),
            xEt[2].text.toString().toFloat(),
            xEt[3].text.toString().toFloat()
        )
        val xFixed = xFixedEt.text.toString().toFloat()
        var p = f(x[0])
        var tmp: Float
        var tmpX: MutableList<Float>
        var strP = "\n${p.toString().substring(0, 4)}"
        var tmpStr: String
        var tmpF: Float
        for (i in 0 until x.lastIndex) {
            tmpStr = ""
            tmp = 1f
            tmpX = mutableListOf()
            for (j in 0 .. i) {
                tmpStr = "$tmpStr(x-${x[j]})"
                tmp *= xFixed - x[j]
                tmpX.add(x[j])
            }
            tmpX.add(x[i + 1])
            tmpF = f(tmpX)
            p += tmp * tmpF
            strP = if (sign(tmpF) == 1f)
                "$strP\n+${tmpF.toString().substring(0, 3)}$tmpStr"
            else
                "$strP\n-${(-tmpF).toString().substring(0, 3)}$tmpStr"
        }
        lXTv.text = "P(x)$strP"
        xFixed1Tv.text = "P($xFixed)"
        xFixed2Tv.text = "Error"
        error1Tv.text = "$p"
        error2Tv.text = "${abs(p - f(xFixed))}"
    }
}