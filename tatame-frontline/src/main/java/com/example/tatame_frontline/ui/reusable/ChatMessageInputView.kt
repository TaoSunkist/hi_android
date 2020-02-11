package com.example.tatame_frontline.ui.reusable

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatEditText
import com.example.tatame_component.utility.weak
import com.example.tatame_frontline.R

interface ChatMessageInputViewDelegate {
    fun chatMessageInputViewDidEnter(text: String)
    fun chatMessageInputViewEmojiButtonClicked()
    fun chatMessageInputViewMenuButtonClicked()
}

class ChatMessageInputView : FrameLayout {

    constructor(context: Context) : super(context) {
        commonInit(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        commonInit(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        commonInit(context)
    }

    private lateinit var editTextView: AppCompatEditText
    private lateinit var emojiButton: View
    private lateinit var menuButton: View

    var delegate: ChatMessageInputViewDelegate? by weak()

    private fun commonInit(context: Context) {
        View.inflate(context, R.layout.view_chat_message_input, this@ChatMessageInputView)
        editTextView = findViewById(R.id.view_chat_message_input_edit_text)
        emojiButton = findViewById<View>(R.id.view_chat_message_input_emoji_button).apply {
            setOnClickListener {
                emojiButtonClicked()
            }
        }
        menuButton = findViewById<View>(R.id.view_chat_message_input_menu_button).apply {
            setOnClickListener {
                menuButtonClicked()
            }
        }
        setupEditText()

        /* 如果还不做发表情之类的功能就uncomment下面的代码 */
        emojiButton.isEnabled = false
        menuButton.isEnabled = false
    }

    private fun setupEditText() {
        editTextView.setRawInputType(InputType.TYPE_CLASS_TEXT)
        editTextView.setImeActionLabel("发送", KeyEvent.KEYCODE_ENTER)
        editTextView.requestFocus()
        editTextView.setOnEditorActionListener { _, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                notifySend()
                return@setOnEditorActionListener true
            }
            if (actionId == KeyEvent.KEYCODE_ENTER) {
                notifySend()
                return@setOnEditorActionListener true
            }
            if (keyEvent != null) {
                if (keyEvent.action == KeyEvent.ACTION_DOWN && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                    notifySend()
                }
            }
            return@setOnEditorActionListener true
        }
    }

    private fun notifySend() {
        editTextView.text?.let {
            delegate?.chatMessageInputViewDidEnter(it.toString())
        }
        editTextView.setText("")
    }

    private fun emojiButtonClicked() {
        delegate?.chatMessageInputViewEmojiButtonClicked()
    }

    private fun menuButtonClicked() {
        delegate?.chatMessageInputViewMenuButtonClicked()
    }

}