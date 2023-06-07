package com.mentallys.chatView.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.mentallys.chatView.R
import com.mentallys.chatView.adapter.SimpleChatAdapter
import com.mentallys.chatView.databinding.ChatViewWidgetBinding
import com.mentallys.chatView.model.ChatMessage


class ChatView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null
) : ConstraintLayout(context, attributeSet, 0) {

    companion object {
        const val TYPE_TEXT = 0
        const val TYPE_IMAGE = 1
        const val TYPE_VIDEO = 2
        const val TYPE_DATE = 3
    }


    private var _binding: ChatViewWidgetBinding? = null
    private val binding get() = _binding!!
    private var isMoreLayoutVisible = false
    private val simpleChatAdapter by lazy { SimpleChatAdapter(context) }
    private var onChatImageClickListener: ((ChatMessage) -> Unit)? = null
    private var onChatVideoClickListener: ((ChatMessage) -> Unit)? = null
    private var onChatUserImageClickListener: ((ChatMessage) -> Unit)? = null
    private var onChatUsernameClickListener: ((ChatMessage) -> Unit)? = null
    private var onSelectImageClickListener: (() -> Unit)? = null
    private var onSelectVideoClickListener: (() -> Unit)? = null
    private var onCameraClickListener: (() -> Unit)? = null
    private var onMessageSendListener: ((String) -> Unit)? = null
    private var onMessageClickListener: ((ChatMessage) -> Unit)? = null

    init {

        val layoutInflater = LayoutInflater.from(context)

        _binding = ChatViewWidgetBinding.inflate(layoutInflater, this, true)

        val a = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.ChatView,
            0, 0
        )

        setAttributes(a)

        a.recycle()

        setRecyclerView()

        setListeners()

       /* binding.btnAdd.setOnClickListener {
            if (isMoreLayoutVisible) {
                hideMoreLayout()
            }
            onSelectImageClickListener?.invoke()
        }*/

        binding.btnSend.setOnClickListener {
            if (isMoreLayoutVisible)
                hideMoreLayout()
            val message = binding.edtMessage.text.toString()
            binding.edtMessage.text.clear()
            if (message.isNotBlank() && message.isNotEmpty())
                onMessageSendListener?.invoke(message)
        }

        binding.edtMessage.setOnClickListener { hideMoreLayout() }
        binding.edtMessage.setOnEditorActionListener { _, _, _ ->
            if (isMoreLayoutVisible)
                hideMoreLayout()
            true
        }


    }

    private fun setAttributes(a: TypedArray) {
        setShowAddButton(a.getBoolean(R.styleable.ChatView_showAddButton, true))
        setAddButtonColor(
            a.getColor(
                R.styleable.ChatView_addButtonColor,
                Color.parseColor("#707070")
            )
        )
        setChatViewBackground(
            a.getColor(
                R.styleable.ChatView_chatViewBackgroundColor,
                context.getColor(android.R.color.transparent)
            )
        )
        setChatInputBackgroundColor(
            a.getColor(
                R.styleable.ChatView_chatInputBackgroundColor,
                Color.parseColor("#F2F2F2")
            )
        )
        setChatInputBackground(
            a.getInt(
                R.styleable.ChatView_chatInputBackground,
                R.drawable.chat_input_shape
            )
        )
        setHintTextColor(
            a.getColor(
                R.styleable.ChatView_hintTextColor,
                Color.parseColor("#707070")
            )
        )
        setTextColor(a.getColor(R.styleable.ChatView_textColor, Color.BLACK))
        setHint(a.getString(R.styleable.ChatView_hint))
        setSendButtonColor(
            a.getColor(
                R.styleable.ChatView_sendButtonColor,
                Color.parseColor("#F2F2F2")
            )
        )
        setShowImageButton(a.getBoolean(R.styleable.ChatView_showImageButton, true))
        setShowVideoButton(a.getBoolean(R.styleable.ChatView_showVideoButton, true))
        setShowCameraButton(a.getBoolean(R.styleable.ChatView_showCameraButton, true))
        setShowSenderLayout(a.getBoolean(R.styleable.ChatView_showSenderLayout, true))
    }

    fun setShowSenderLayout(boolean: Boolean) {
        if (boolean)
            binding.layoutChatInputHolder.visibility = VISIBLE
        else
            binding.layoutChatInputHolder.visibility = GONE
    }

    fun setShowCameraButton(boolean: Boolean) {
        if (boolean)
            binding.imgCamera.visibility = VISIBLE
        else
            binding.imgCamera.visibility = GONE
    }

    fun setShowVideoButton(boolean: Boolean) {
        if (boolean)
            binding.imgVideo.visibility = VISIBLE
        else
            binding.imgVideo.visibility = GONE
    }

    fun setShowImageButton(boolean: Boolean) {
        if (boolean)
            binding.imgImage.visibility = VISIBLE
        else
            binding.imgImage.visibility = GONE
    }

    fun setSendButtonColor(color: Int) {
        binding.btnSend.background.setTint(color)
    }

    fun setHint(string: String?) {
        val hint = string ?: "Écrire un message"
        binding.edtMessage.hint = hint
    }

    fun setTextColor(color: Int) {
        binding.edtMessage.setTextColor(color)
    }

    fun setHintTextColor(color: Int) {
        binding.edtMessage.setHintTextColor(color)
    }

    fun setChatInputBackground(int: Int) {
        binding.layoutChatInputHolder.setBackgroundResource(int)
        binding.edtMessage.setBackgroundResource(int)
    }

    fun setChatInputBackgroundColor(color: Int) {
        binding.edtMessage.background.setTint(color)
        binding.layoutChatInputHolder.background.setTint(color)
    }

    fun setAddButtonColor(color: Int) {
        binding.btnAdd.background.setTint(color)
    }

    fun setShowAddButton(boolean: Boolean) {
        if (boolean)
            binding.btnAdd.visibility = VISIBLE
        else
            binding.btnAdd.visibility = GONE
    }

    fun setChatViewBackground(color: Int) {
        binding.chatView.setBackgroundColor(color)
    }

    private fun showMoreLayout() {
        binding.btnAdd.setImageResource(R.drawable.ic_baseline_close_24)
        binding.moreLayout.visibility = VISIBLE
        isMoreLayoutVisible = true
    }

    private fun setListeners() {
        //adapter listeners
        simpleChatAdapter.setOnChatAddAttachmentClickListener { onChatImageClickListener?.invoke(it) }
        simpleChatAdapter.setOnChatImageClickListener { onChatImageClickListener?.invoke(it) }
        simpleChatAdapter.setOnChatUserImageClickListener { onChatUserImageClickListener?.invoke(it) }
        simpleChatAdapter.setOnChatUsernameClickListener { onChatUsernameClickListener?.invoke(it) }
        simpleChatAdapter.setOnChatVideoClickListener { onChatVideoClickListener?.invoke(it) }
        simpleChatAdapter.setOnMessageClickListener { onMessageClickListener?.invoke(it) }

        //ChatView listeners
        binding.btnAdd.setOnClickListener {
            hideMoreLayout()
            onSelectImageClickListener?.invoke()
        }

        binding.imgImage.setOnClickListener {
            hideMoreLayout()
            onSelectImageClickListener?.invoke()
        }
        binding.imgVideo.setOnClickListener {
            hideMoreLayout()
            onSelectVideoClickListener?.invoke()
        }
        binding.imgCamera.setOnClickListener {
            hideMoreLayout()
            onCameraClickListener?.invoke()
        }

    }

    private fun hideMoreLayout() {
        binding.btnAdd.setImageResource(R.drawable.attach_file)
        binding.moreLayout.visibility = INVISIBLE
        isMoreLayoutVisible = false
    }

    private fun setRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.stackFromEnd = true
        binding.rvChats.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = simpleChatAdapter
        }
    }

    fun addMessage(chatMessageList: ArrayList<ChatMessage>) {
        simpleChatAdapter.addData(chatMessageList)
    }

    fun addToEnd(message: ChatMessage){
        simpleChatAdapter.addToEnd(message)
        binding.rvChats.smoothScrollToPosition(simpleChatAdapter.itemCount)
    }
    fun addListMessages(messages: List<ChatMessage>, reverse: Boolean){
        simpleChatAdapter.addListMessages(messages, reverse)
    }

    fun addMessage(chatMessage: ChatMessage) {
        simpleChatAdapter.addData(chatMessage)
        binding.rvChats.smoothScrollToPosition(simpleChatAdapter.itemCount)
    }

    fun remove(chatMessage: ChatMessage) = simpleChatAdapter.remove(chatMessage)

    fun remove(position: Int) = simpleChatAdapter.remove(position)

    fun clearMessages() = simpleChatAdapter.clearMessages()

    fun setOnChatImageClickListener(listener: (ChatMessage) -> Unit) {
        onChatImageClickListener = listener
    }

    fun setOnChatVideoClickListener(listener: (ChatMessage) -> Unit) {
        onChatVideoClickListener = listener
    }

    fun setOnChatUserImageClickListener(listener: (ChatMessage) -> Unit) {
        onChatUserImageClickListener = listener
    }

    fun setOnChatUsernameClickListener(listener: (ChatMessage) -> Unit) {
        onChatUsernameClickListener = listener
    }

    fun setOnAttachmentSendListener(listener: () -> Unit) {
        onSelectImageClickListener = listener
    }

    fun setOnMessageSendListener(listener: (String) -> Unit) {
        onMessageSendListener = listener
    }

    fun setOnMessageClickListener(listener: (ChatMessage) -> Unit) {
        onMessageClickListener = listener
    }

    fun setOnSelectImageClickListener(listener: () -> Unit) {
        onSelectImageClickListener = listener
    }

    fun setOnSelectVideoClickListener(listener: () -> Unit) {
        onSelectVideoClickListener = listener
    }

    fun setOnSelectCameraClickListener(listener: () -> Unit) {
        onCameraClickListener = listener
    }

}