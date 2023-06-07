package com.mentallys.chatView.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mentallys.chatView.databinding.*
import com.mentallys.chatView.model.ChatMessage
import com.mentallys.chatView.util.DateFormatter
import com.mentallys.chatView.widget.ChatView.Companion.TYPE_DATE
import com.mentallys.chatView.widget.ChatView.Companion.TYPE_IMAGE
import com.mentallys.chatView.widget.ChatView.Companion.TYPE_TEXT
import com.mentallys.chatView.widget.ChatView.Companion.TYPE_VIDEO
import java.util.*

class SimpleChatAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var chatMessage = mutableListOf<ChatMessage>()

    private var onChatImageClickListener: ((ChatMessage) -> Unit)? = null
    private var onChatVideoClickListener: ((ChatMessage) -> Unit)? = null
    private var onChatUserImageClickListener: ((ChatMessage) -> Unit)? = null
    private var onChatUsernameClickListener: ((ChatMessage) -> Unit)? = null
    private var onMessageClickListener:((ChatMessage)->Unit)? = null
    private val layoutManager: RecyclerView.LayoutManager? = null

    companion object {
        const val TYPE_TEXT_RIGHT = 0
        const val TYPE_TEXT_LEFT = 1
        const val TYPE_IMAGE_RIGHT = 2
        const val TYPE_IMAGE_LEFT = 3
        const val TYPE_VIDEO_RIGHT = 4
        const val TYPE_VIDEO_LEFT = 5
        const val TYPE_DATE_CENTER = 6
    }

    inner class SimpleChatDiffUtil(
        private val oldList: List<ChatMessage>,
        private val newList: List<ChatMessage>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_TEXT_RIGHT -> {
                TypeTextSend(
                    SendTextMessageItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            TYPE_TEXT_LEFT -> {
                TypeTextReceive(
                    ReceiveTextMessageItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            TYPE_DATE_CENTER -> {
                DateHeader(
                    DateHeaderItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            TYPE_IMAGE_RIGHT -> {
                TypeImageSend(
                    SendImageMessageItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            TYPE_IMAGE_LEFT -> {
                TypeImageReceive(
                    ReceiveImageMessageItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            TYPE_VIDEO_RIGHT -> {
                TypeVideoSend(
                    SendVideoMessageItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                TypeVideoReceive(
                    ReceiveVideoMessageItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(chatMessage[position].view_type){
            TYPE_TEXT_RIGHT->(holder as TypeTextSend).bind(position)
            TYPE_TEXT_LEFT->(holder as TypeTextReceive).bind(position)
            TYPE_IMAGE_RIGHT->(holder as TypeImageSend).bind(position)
            TYPE_IMAGE_LEFT->(holder as TypeImageReceive).bind(position)
            TYPE_VIDEO_RIGHT->(holder as TypeVideoSend).bind(position)
            TYPE_VIDEO_LEFT->(holder as TypeVideoReceive).bind(position)
            TYPE_DATE_CENTER->(holder as DateHeader).bind(position)
        }
    }

    override fun getItemCount(): Int {
        return chatMessage.size
    }
    override fun getItemViewType(position: Int): Int {
        val isFromMe = chatMessage[position].is_from_me
        val viewType =  when (chatMessage[position].message_type) {
            TYPE_TEXT -> {
                if (isFromMe)
                    0
                else
                    1
            }
            TYPE_IMAGE -> {
                if (isFromMe)
                    2
                else
                    3
            }
            TYPE_VIDEO -> {
                if (isFromMe)
                    4
                else
                    5
            }
            TYPE_DATE -> {
                if (isFromMe)
                    6
                else
                    6
            }
            else -> throw RuntimeException("MessageType in ChatMessage() is not added")
        }
        chatMessage[position].view_type = viewType
        return viewType
    }
    inner class DateHeader(private val binding: DateHeaderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {

                var date = chatMessage[position].createdAt

                messageText.text = DateFormatter.format(date, "d MMMM yyyy")

                messageText.setOnClickListener {
                    onMessageClickListener?.invoke(chatMessage[position])
                }
            }
        }
    }
    inner class TypeTextSend(private val binding: SendTextMessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {

                txtMessage.text = chatMessage[position].message

                txtDate.text = DateFormatter.format(chatMessage[position].createdAt, "HH:mm")

                txtMessage.setOnClickListener {
                    onMessageClickListener?.invoke(chatMessage[position])
                }
            }
        }
    }

    inner class TypeTextReceive(private val binding: ReceiveTextMessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {

                txtMessage.text = chatMessage[position].message

                txtDate.text = DateFormatter.format(chatMessage[position].createdAt, "HH:mm")

                txtMessage.setOnClickListener {
                    onMessageClickListener?.invoke(chatMessage[position])
                }

            }
        }
    }

    inner class TypeImageSend(private val binding: SendImageMessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {

                txtDate.text = chatMessage[position].createdAt.toString()

                Glide.with(context)
                    .load(chatMessage[position].message)
                    .into(imgMessage)

                imgMessage.setOnClickListener {
                    onChatImageClickListener?.invoke(chatMessage[position])
                }
            }
        }
    }

    inner class TypeImageReceive(private val binding: ReceiveImageMessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {

                txtDate.text = DateFormatter.format(chatMessage[position].createdAt, "HH:mm")

                Glide.with(context)
                    .load(chatMessage[position].message)
                    .dontAnimate()
                    .into(imgMessage)

                imgMessage.setOnClickListener {
                    onChatImageClickListener?.invoke(chatMessage[position])
                }
            }
        }
    }

    inner class TypeVideoSend(private val binding: SendVideoMessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {

                txtDate.text = DateFormatter.format(chatMessage[position].createdAt, "HH:mm")

                Glide.with(context)
                    .load(chatMessage[position].message)
                    .into(imgMessage)


                imgMessage.setOnClickListener {
                    onChatImageClickListener?.invoke(chatMessage[position])
                }
            }
        }
    }

    inner class TypeVideoReceive(private val binding: ReceiveVideoMessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {

                txtUsername.text = chatMessage[position].username
                txtDate.text = chatMessage[position].createdAt.toString()

                Glide.with(context)
                    .load(chatMessage[position].profile_url)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imgProfile)

                Glide.with(context)
                    .load(chatMessage[position].message)
                    .into(imgMessage)

                imgMessage.setOnClickListener {
                    onChatImageClickListener?.invoke(chatMessage[position])
                }

                imgProfile.setOnClickListener {
                    onChatUserImageClickListener?.invoke(chatMessage[position])
                }

                txtUsername.setOnClickListener {
                    onChatUsernameClickListener?.invoke(chatMessage[position])
                }
            }
        }
    }

    fun setOnChatAddAttachmentClickListener(listener: (ChatMessage) -> Unit) {
        onChatImageClickListener = listener
    }
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

    fun setOnMessageClickListener(listener: (ChatMessage) -> Unit) {
        onMessageClickListener = listener
    }

    fun addData(newList: List<ChatMessage>){
        val diffResult = DiffUtil.calculateDiff(SimpleChatDiffUtil(chatMessage,newList))
        Collections.reverse(newList)
        chatMessage.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    fun addData(chatMessage: ChatMessage){
        this.chatMessage.add(chatMessage)
        notifyItemInserted(this.chatMessage.size-1)
    }

    /*
     * PUBLIC METHODS
     * */
    fun addToEnd(message: ChatMessage){

        var oldSize = chatMessage.size
        val isNewMessageToday = !isPreviousSameDate(chatMessage.size - 1, message.createdAt)
        if (isNewMessageToday) {

            chatMessage.add(chatMessage.size, ChatMessage(
                UUID.randomUUID().toString(),
                "",
                "",
                "",
                true,
                message.createdAt,
                TYPE_DATE,
            )
            )
        }
        chatMessage.add(chatMessage.size, message)
        notifyItemRangeInserted(oldSize, chatMessage.size - oldSize)
    }

    private fun isPreviousSameDate(position: Int, dateToCompare: Date?): Boolean {
        if (chatMessage.size == 0 || chatMessage.size <= position) return false
        return if (chatMessage[position].message_type != TYPE_DATE) {
            val previousPositionDate = chatMessage[position].createdAt
            DateFormatter.isSameDay(dateToCompare, previousPositionDate)
        } else false
    }

    fun addListMessages(messages: List<ChatMessage>, reverse: Boolean) {
        if (messages.isEmpty()) return
        if (reverse) Collections.reverse(messages)
        if (chatMessage.isNotEmpty()) {
            val lastItemPosition: Int = chatMessage.size - 1
            val lastItem = chatMessage[lastItemPosition].createdAt
            if (DateFormatter.isSameDay(messages[0].createdAt, lastItem)) {
                chatMessage.removeAt(lastItemPosition)
                notifyItemRemoved(lastItemPosition)
            }
        }
        var oldSize = chatMessage.size
        generateDateHeaders(messages)
        Collections.reverse(chatMessage)
        notifyItemRangeInserted(oldSize, chatMessage.size - oldSize)
    }

    protected fun generateDateHeaders(messages: List<ChatMessage>) {
        for (i in messages.indices) {
            val message: ChatMessage = messages[i]
            this.chatMessage.add(message)
            if (messages.size > i + 1) {
                val nextMessage = messages[i + 1]
                if (!DateFormatter.isSameDay(message.createdAt, nextMessage.createdAt)) {
                    var messageCteated = ChatMessage(
                        UUID.randomUUID().toString(),
                        "",
                        "",
                        "",
                        true,
                        message.createdAt,
                        TYPE_DATE,
                    )
                    this.chatMessage.add(messageCteated)
                }
            } else {
               var messageCteated = ChatMessage(
                    UUID.randomUUID().toString(),
                    "",
                    "",
                    "",
                    true,
                   message.createdAt,
                   TYPE_DATE,
                )
                this.chatMessage.add(messageCteated)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun remove(chatMessage: ChatMessage){
        this.chatMessage.remove(chatMessage)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun remove(position: Int){
        chatMessage.removeAt(position)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearMessages(){
        chatMessage.clear()
        notifyDataSetChanged()
    }

}