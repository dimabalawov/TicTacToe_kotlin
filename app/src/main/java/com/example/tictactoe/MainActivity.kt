package com.example.tictactoe

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var activePlayer = 1
    private var gameActive = true
    private var gameState = IntArray(9) { 0 }

    private val winningPositions = arrayOf(
        intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8),
        intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8),
        intArrayOf(0, 4, 8), intArrayOf(2, 4, 6)
    )

    private lateinit var statusTextView: TextView
    private val buttonsList = ArrayList<Button>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootLayout = LinearLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setBackgroundColor(Color.parseColor("#202020"))
        }

        statusTextView = TextView(this).apply {
            text = "Ход игрока: X"
            textSize = 24f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 50)
        }
        rootLayout.addView(statusTextView)

        val gridLayout = GridLayout(this).apply {
            rowCount = 3
            columnCount = 3
            alignmentMode = GridLayout.ALIGN_BOUNDS
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER
            }
        }

        val dpToPx = resources.displayMetrics.density
        val btnSize = (90 * dpToPx).toInt()
        val marginSize = (4 * dpToPx).toInt()

        for (i in 0..8) {
            val btn = Button(this).apply {
                tag = i.toString()
                textSize = 32f
                setTextColor(Color.WHITE)
                setBackgroundColor(Color.DKGRAY)

                val params = GridLayout.LayoutParams()
                params.width = btnSize
                params.height = btnSize
                params.setMargins(marginSize, marginSize, marginSize, marginSize)
                layoutParams = params

                // ИСПРАВЛЕНИЕ ЗДЕСЬ: используем 'it' вместо 'this'
                setOnClickListener { onSpotPressed(it) }
            }
            buttonsList.add(btn)
            gridLayout.addView(btn)
        }
        rootLayout.addView(gridLayout)

        val resetButton = Button(this).apply {
            text = "Заново"
            textSize = 20f
            setBackgroundColor(Color.parseColor("#FF9800"))
            setTextColor(Color.BLACK)

            val params = LinearLayout.LayoutParams(
                (200 * dpToPx).toInt(),
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.topMargin = (40 * dpToPx).toInt()
            params.gravity = Gravity.CENTER
            layoutParams = params

            setOnClickListener { resetGame() }
        }
        rootLayout.addView(resetButton)

        setContentView(rootLayout)
    }

    private fun onSpotPressed(view: View) {
        val btn = view as Button
        val tappedTag = btn.tag.toString().toInt()

        if (gameState[tappedTag] != 0 || !gameActive) {
            return
        }

        gameState[tappedTag] = activePlayer

        if (activePlayer == 1) {
            btn.text = "X"
            btn.setTextColor(Color.CYAN)
            activePlayer = 2
            statusTextView.text = "Ход игрока: O"
        } else {
            btn.text = "O"
            btn.setTextColor(Color.MAGENTA)
            activePlayer = 1
            statusTextView.text = "Ход игрока: X"
        }

        checkForWin()
    }

    private fun checkForWin() {
        for (pos in winningPositions) {
            if (gameState[pos[0]] == gameState[pos[1]] &&
                gameState[pos[1]] == gameState[pos[2]] &&
                gameState[pos[0]] != 0
            ) {
                gameActive = false
                val winner = if (gameState[pos[0]] == 1) "X" else "O"
                statusTextView.text = "Победил $winner!"
                Toast.makeText(this, "Победитель: $winner", Toast.LENGTH_LONG).show()
                return
            }
        }

        if (!gameState.contains(0)) {
            statusTextView.text = "Ничья!"
            gameActive = false
        }
    }

    private fun resetGame() {
        activePlayer = 1
        gameActive = true
        gameState = IntArray(9) { 0 }
        statusTextView.text = "Ход игрока: X"

        for (btn in buttonsList) {
            btn.text = ""
        }
    }
}