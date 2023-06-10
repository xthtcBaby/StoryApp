package com.dicoding.storyapp.ui.customView

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.dicoding.storyapp.R
import com.google.android.material.textfield.TextInputLayout

class MyPasswordEditText : TextInputLayout {

    private lateinit var inputText: EditText

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()

    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    private fun init() {
        addOnEditTextAttachedListener{
            inputText = it.editText!!
            inputText.hint = context.getString(R.string.masukkan_password)
            inputText.textAlignment = View.TEXT_ALIGNMENT_VIEW_START

            inputText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // Nothing
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.length < 8 && s.length != 0) {
                        it.error = context.getString(R.string.password_req)
                    } else {
                        it.error = null
                    }
                }

                override fun afterTextChanged(s: Editable) {
                    // Nothing
                }

            })
        }
    }
}