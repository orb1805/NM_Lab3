package e.roman.nm_lab3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import kotlin.concurrent.fixedRateTimer
import kotlin.math.abs
import kotlin.math.sign

class LagrangePolynomialActivity : AppCompatActivity(), View.OnClickListener {

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
        setContentView(R.layout.activity_lagrange_polynomial)

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
        var l = 0f
        var strL = ""
        var tmp: Float
        var tmpStr: String
        var multer: Float
        for (i in x.indices) {
            tmp = 1f
            multer = 1f
            tmpStr = ""
            for (j in x.indices) {
                if (i != j) {
                    multer /= (x[i] - x[j])
                    tmp *= (xFixed - x[j])
                    tmpStr = "$tmpStr(x-${x[j]})"
                }
            }
            multer *= f(x[i])
            strL = if (sign(multer) == 1f)
                "$strL\n+${multer.toString().substring(0, 3)}$tmpStr+"
            else {
                strL = strL.removeRange(strL.lastIndex, strL.length)
                "$strL-\n-${(-multer).toString().substring(0, 3)}$tmpStr+"
            }
            l += tmp * multer
        }
        strL = strL.removeRange(strL.lastIndex, strL.length)
        lXTv.text = "L(x)$strL"
        xFixed1Tv.text = "L($xFixed)"
        xFixed2Tv.text = "Error"
        error1Tv.text = "$l"
        error2Tv.text = "${abs(f(xFixed) - l)}"
    }
}