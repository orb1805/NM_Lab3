package e.roman.nm_lab3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var lagrangePolynomialBtn: Button
    private lateinit var newtonPolynomialBtn: Button
    private lateinit var cubicSplineBtn: Button
    private lateinit var leastSquaresBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lagrangePolynomialBtn = findViewById(R.id.btn_lagrange_polynomial)
        newtonPolynomialBtn = findViewById(R.id.btn_newton_polynomial)
        cubicSplineBtn = findViewById(R.id.btn_cubic_spline)
        leastSquaresBtn = findViewById(R.id.btn_least_squares)
        lagrangePolynomialBtn.setOnClickListener { startActivity(Intent(this, LagrangePolynomialActivity::class.java)) }
        newtonPolynomialBtn.setOnClickListener { startActivity(Intent(this, NewtonPolynomialActivity::class.java)) }
        cubicSplineBtn.setOnClickListener{ startActivity(Intent(this, CubicSplineActivity::class.java)) }
        leastSquaresBtn.setOnClickListener{ startActivity(Intent(this, LeastSquaresActivity::class.java)) }
    }
}