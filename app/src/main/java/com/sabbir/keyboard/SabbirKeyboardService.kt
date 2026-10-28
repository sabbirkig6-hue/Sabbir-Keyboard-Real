
package com.sabbir.keyboard

import android.inputmethodservice.InputMethodService
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputConnection
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class SabbirKeyboardService : InputMethodService() {

    private var language = 0
    private var symbols = false

    private val layouts = listOf(
        listOf(
            listOf("অ","আ","ই","ঈ","উ","ঊ","ঋ","এ","ঐ","ও"),
            listOf("ক","খ","গ","ঘ","ঙ","চ","ছ","জ","ঝ","ঞ"),
            listOf("ট","ঠ","ড","ঢ","ণ","ত","থ","দ","ধ","ন"),
            listOf("প","ফ","ব","ভ","ম","য","র","ল","শ","ষ"),
            listOf("স","হ","ড়","ঢ়","য়","ং","ঃ","ঁ")
        ),
        listOf(
            listOf("q","w","e","r","t","y","u","i","o","p"),
            listOf("a","s","d","f","g","h","j","k","l"),
            listOf("z","x","c","v","b","n","m")
        ),
        listOf(
            listOf("ض","ص","ث","ق","ف","غ","ع","ه","خ","ح"),
            listOf("ش","س","ي","ب","ل","ا","ت","ن","م"),
            listOf("ئ","ء","ؤ","ر","ى","ة","و","ز")
        ),
        listOf(
            listOf("ق","و","ع","ر","ت","ے","ء","ی","ہ","پ"),
            listOf("ا","س","د","ف","گ","ہ","ج","ک","ل"),
            listOf("ز","ش","چ","ط","ب","ن","م")
        )
    )

    private val languageNames =
        listOf("বাংলা", "English", "العربية", "اردو")

    override fun onCreateInputView(): View {
        return buildKeyboard()
    }

    private fun buildKeyboard(): View {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(4, 6, 4, 8)
            setBackgroundColor(Color.rgb(238, 245, 240))
        }

        val heading = TextView(this).apply {
            text = "🕌  سُبْحَانَ اللَّهِ   •   الْحَمْدُ لِلَّهِ"
            textSize = 17f
            gravity = Gravity.CENTER
            setTextColor(Color.rgb(20, 100, 60))
            setPadding(4, 10, 4, 10)
        }
        root.addView(heading)

        val toolbar = LinearLayout(this).apply {
            gravity = Gravity.CENTER
        }

        addButton(toolbar, "🕋") {
            language = (language + 1) % 4
            refreshKeyboard()
        }
        addButton(toolbar, languageNames[language]) {
            language = (language + 1) % 4
            refreshKeyboard()
        }
        addButton(toolbar, "😊") {
            currentInputConnection?.commitText("😀", 1)
        }
        addButton(toolbar, "🎙") {
            requestVoiceTyping()
        }
        root.addView(toolbar)

        if (symbols) {
            listOf(
                listOf("1","2","3","4","5","6","7","8","9","0"),
                listOf("@","#","$","%","&","-","+","(",")","/"),
                listOf("*","\"",":",";","!","?","'","⌫")
            ).forEach { row ->
                addKeyRow(root, row)
            }
        } else {
            layouts[language].forEach { row ->
                addKeyRow(root, row)
            }
        }

        val bottom = LinearLayout(this).apply {
            gravity = Gravity.CENTER
        }

        addButton(bottom, "?123") {
            symbols = !symbols
            refreshKeyboard()
        }
        addButton(bottom, "⇧") {}
        addButton(bottom, "🕋") {
            language = (language + 1) % 4
            refreshKeyboard()
        }
        addButton(bottom, "সাব্বির", 2) {
            currentInputConnection?.commitText(" ", 1)
        }
        addButton(bottom, "⌫") {
            val ic = currentInputConnection
            ic?.deleteSurroundingText(1, 0)
        }
        addButton(bottom, "↵") {
            currentInputConnection?.sendKeyEvent(
                android.view.KeyEvent(
                    android.view.KeyEvent.ACTION_DOWN,
                    android.view.KeyEvent.KEYCODE_ENTER
                )
            )
        }

        root.addView(bottom)
        return root
    }

    private fun addKeyRow(
        parent: LinearLayout,
        keys: List<String>
    ) {
        val row = LinearLayout(this).apply {
            gravity = Gravity.CENTER
        }

        keys.forEach { key ->
            addButton(row, key) {
                if (key == "⌫") {
                    currentInputConnection?.deleteSurroundingText(1, 0)
                } else {
                    currentInputConnection?.commitText(key, 1)
                }
            }
        }

        parent.addView(row)
    }

    private fun addButton(
        parent: LinearLayout,
        label: String,
        weight: Int = 1,
        action: () -> Unit
    ) {
        val button = TextView(this).apply {
            text = label
            textSize = if (label.length > 3) 13f else 19f
            gravity = Gravity.CENTER
            typeface = Typeface.DEFAULT
            setTextColor(Color.rgb(35, 55, 43))
            background = GradientDrawable().apply {
                setColor(Color.WHITE)
                cornerRadius = 9f
                setStroke(1, Color.rgb(211, 225, 215))
            }
            val p = (4 * resources.displayMetrics.density).toInt()
            setPadding(1, p, 1, p)
            isClickable = true
            isFocusable = true
            setOnClickListener { action() }
        }

        val params = LinearLayout.LayoutParams(
            0,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            weight.toFloat()
        )
        params.setMargins(2, 3, 2, 3)
        parent.addView(button, params)
    }

    private fun refreshKeyboard() {
        setInputView(buildKeyboard())
    }

    private fun requestVoiceTyping() {
        val ic = currentInputConnection
        if (ic != null) {
            ic.commitText("", 1)
        }
        // Voice recognition integration will be added separately.
    }
}
