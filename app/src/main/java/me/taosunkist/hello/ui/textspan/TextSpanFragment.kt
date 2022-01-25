package me.taosunkist.hello.ui.textspan

import android.graphics.Color
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.mooveit.library.Fakeit
import me.taosunkist.hello.R
import me.taosunkist.hello.databinding.FragmentTextSpanBinding

class TextSpanFragment : Fragment() {

    private var _binding: FragmentTextSpanBinding? = null

    private val binding: FragmentTextSpanBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = FragmentTextSpanBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textContent =
            "Biden warns Putin of sanctions, aid for Ukraine military if Russia invades http://google.com is google website and http://youtube.com is youtube site"

        binding.normalSpanTextView.text = textContent
        createSpanStyle(binding.normalSpanTextView) {
            this.matchResultSequence = "(if)|(for)|(is)".toRegex().findAll(textContent)
        }

        val kickedNickname = Fakeit.pokemon().name()

        val content = getString(R.string.s_s_has_been_kicked_out_of_the_room_by_the_room_admin_1)

        val userNickName = getString(R.string.s_s_has_been_kicked_out_of_the_room_by_the_room_admin_2, Fakeit.name())

        val s = SpannableStringBuilder(kickedNickname + content + userNickName)

        val kickedUserClickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                Toast.makeText(context, "kickedUserClickableSpan", Toast.LENGTH_SHORT).show()
            }

            override fun updateDrawState(ds: TextPaint) {

                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.linkColor = Color.TRANSPARENT
            }
        }

        val userClickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                Toast.makeText(context, "userClickableSpan", Toast.LENGTH_SHORT).show()

            }

            override fun updateDrawState(ds: TextPaint) {

                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.linkColor = Color.TRANSPARENT
            }
        }

        s.setSpan(kickedUserClickableSpan, 0, kickedNickname.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        s.setSpan(userClickableSpan, s.length - userNickName.length, s.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        s.setSpan(ForegroundColorSpan(Color.parseColor("#88dd12")), 0, kickedNickname.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        s.setSpan(ForegroundColorSpan(Color.parseColor("#88dd12")),
            s.length - userNickName.length, s.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.normalSpanTextView2.highlightColor = Color.TRANSPARENT
        binding.normalSpanTextView2.movementMethod = LinkMovementMethod.getInstance()
        binding.normalSpanTextView2.text = s
    }
}