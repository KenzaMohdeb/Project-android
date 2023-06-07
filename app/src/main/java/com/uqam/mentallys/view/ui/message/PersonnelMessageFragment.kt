package com.uqam.mentallys.view.ui.message

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.azure.android.communication.chat.ChatClient
import com.azure.android.communication.chat.ChatClientBuilder
import com.azure.android.communication.chat.ChatThreadClient
import com.azure.android.communication.chat.ChatThreadClientBuilder
import com.azure.android.communication.chat.models.*
import com.azure.android.communication.common.CommunicationTokenCredential
import com.azure.android.communication.common.CommunicationUserIdentifier
import com.azure.android.core.http.policy.UserAgentPolicy
import com.mentallys.chatView.widget.ChatView
import com.uqam.mentallys.BuildConfig
import com.uqam.mentallys.R
import com.uqam.mentallys.data.responses.LoginUserInfo
import com.uqam.mentallys.databinding.FragmentPersonnelMessageBinding
import com.uqam.mentallys.model.ChatParticipantInfo
import com.uqam.mentallys.network.Resource
import com.uqam.mentallys.utils.handleApiError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.internal.UTC
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class PersonnelMessageFragment : Fragment(R.layout.fragment_personnel_message),
    ListChatParticipantAdapter.OnItemClickListener {

    private val invitedInfoviewModel: SharedChatUserInfo by activityViewModels()
    private val chatViewModel by viewModels<ChatViewModel>()
    private lateinit var chatClient: ChatClient
    private lateinit var chatThreadClient: ChatThreadClient
    private lateinit var currentUser: LoginUserInfo
    private lateinit var userID: String
    private lateinit var userToken: String
    private lateinit var threadId: String
    private lateinit var userName: String
    private lateinit var endPoint: String
    private lateinit var communicationInvitedUserId: String
    private val topic = "Chat Communication"
    private lateinit var binding: FragmentPersonnelMessageBinding
    private lateinit var chatView : ChatView
    private val listChatParticipantAdapter = ListChatParticipantAdapter(this)
    private val applicationID = "ACS-Chat"
    private val sdkName = "azure-communication-com.azure.android.communication.chat"
    private val sdkVersion = "1.0.0"

    private val REQUEST_PICK_PICTURE = 1035

    var imagePickLauncher: ActivityResultLauncher<Intent>? = null
    private var imageUri: Uri? = null
    private var postPath: String? = null
    private var contentSentType: String = "Text"
    var photoFile: File? = null
    private var listChatThreadClient = mutableListOf<ChatParticipantInfo>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPersonnelMessageBinding.inflate(layoutInflater)
        userToken= ""
        userName= ""
        this.threadId= ""
        userID= ""
        userName=""

        this.chatView = binding.chatView

        this.endPoint = BuildConfig.END_POINT_URL

        communicationInvitedUserId = invitedInfoviewModel.getUserInfo()?.CommunicationUserId!!

        binding.apply {

            chatView.setOnAttachmentSendListener {
                //user clicked on the Attachment link
                //ask permission to access to the user gallery
                askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_PICK_PICTURE)
            }

            recyclerViewChatParticipants.apply {
                adapter = listChatParticipantAdapter
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL, false
                )
                setHasFixedSize(true)
            }

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCurrentUser()
        showParticipantList()
        checkExistingThreadIdChatRoom()
        createNewChatThreadId()
        getListChatThreadClient()
        getExistingChatRoomThreadId()
        displayChatMessages()
        getImageFromUserGallery()
    }
    private fun setCurrentUser()
    {
        lifecycleScope.launch {
            currentUser = chatViewModel.getUserInfo().first()
            userID = currentUser.CommunicationUserId!!
            userToken = currentUser.AccessToken!!
            userName = currentUser.fullName!!
            createChatClient(endPoint,userToken)
           // binding.progressbar.visible(true)
        }
    }
    /**
     * to check an Existing ThreadId for a Chat Room
     * for the selected Professional
     */
    private fun checkExistingThreadIdChatRoom() {
        chatViewModel.chatSessionInfo.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        userID = it.value.communicationUserId
                        userToken = it.value.accessToken
                        userName = it.value.fullName

                        //save the updated current user
                        chatViewModel.saveUserInfo(it.value.communicationUserId,
                            it.value.accessToken,
                            it.value.accessTokenExpiresOn
                        )

                        createChatClient(endPoint,userToken)
                        //binding.progressbar.visible(true)
                    }
                }
                is Resource.Failure -> handleApiError(it) {
                    Toast.makeText(context, it.errorBody.toString(), Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(context, "System error", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun createChatClient(url:String, token:String) {

            this.chatClient = ChatClientBuilder()
                .endpoint(url)
                .credential(CommunicationTokenCredential(token))
                .addPolicy(
                    UserAgentPolicy(
                        applicationID,
                        sdkName, sdkVersion
                    )
                )
                .buildClient()

        chatViewModel.getListChatThreadClient(this.chatClient)
    }
    /**
     * chatClient will be created to receive the messages
     */
    private fun getListChatThreadClient() {
        chatViewModel.listChatThreadClient.observe(viewLifecycleOwner) {
            if(it.errorMessage == "401"){

                chatViewModel.refreshAccessTokenAsync()
            }
            else if (it.listChatClient.size > 0) {
                chatViewModel.getExistingChatRoomThreadId(it, communicationInvitedUserId, userID)
            }
            else{
                chatViewModel.createNewChatThreadId(this.chatClient, userID, userName, topic)
            }
        }
    }
    private fun setChatParticipantList(listChatParticipantInfo: List<ChatParticipantInfo>){
        listChatParticipantAdapter.submitList(listChatParticipantInfo)
        listChatParticipantAdapter.notifyDataSetChanged()
    }

    private fun showParticipantList() {
        invitedInfoviewModel.participantListVisible.observe(viewLifecycleOwner){
            if(it){
                binding.chatSection.visibility = View.VISIBLE
                binding.chatView.visibility = View.GONE
            }
        }
    }

    /**
     * chatAsyncClient will be created to receive the messages
     */
    private fun getExistingChatRoomThreadId() {
        chatViewModel.existingChatThreadId.observe(viewLifecycleOwner) {

            if(it.isNullOrEmpty() || it[0].communicationId != this.communicationInvitedUserId) {
                this.listChatThreadClient = it as MutableList<ChatParticipantInfo>
                chatViewModel.createNewChatThreadId(this.chatClient, userID, userName, topic)
            }
            else{
                this.threadId = it[0].threadId
                setChatParticipantList(it)
                this.chatThreadClient = createChatThreadClient(endPoint, userToken, this.threadId)
                chatViewModel.getChatMessages(this.chatThreadClient, userID)
                setChatSessionRoom()
            }
        }
    }

    private fun createNewChatThreadId() {
        chatViewModel.threadId.observe(viewLifecycleOwner) {

                if (it.isNotEmpty()) {
                    this.threadId = it

                    this.chatThreadClient = createChatThreadClient(this.endPoint,this.userToken, it)

                    val invitedParticipant = ChatParticipant()
                    invitedParticipant.communicationIdentifier =
                        CommunicationUserIdentifier(invitedInfoviewModel.list.value?.CommunicationUserId)
                    invitedParticipant.displayName = invitedInfoviewModel.list.value?.fullUserName

                    addParticipant(invitedParticipant)

            }
        }
    }

    /**
     * to set the chat user session
     */
    private fun setChatSessionRoom() {
        if(this.threadId.isNotEmpty()) {
            subscribeToMessageReceived()
            sendMessageSetup()
        }
        binding.progressbar.visibility = View.GONE
    }

    /*
 * chatThreadAsyncClient will be created
 * to send messages to the participants
 */
    private fun createChatThreadClient(url:String,
                                       token:String,
                                       threadId:String):ChatThreadClient {
        return  ChatThreadClientBuilder()
            .endpoint(url)
            .credential(CommunicationTokenCredential(token))
            .addPolicy(UserAgentPolicy(applicationID, sdkName, sdkVersion))
            .chatThreadId(threadId)
            .buildClient()
    }
    private fun subscribeToMessageReceived() {
        this.chatClient.startRealtimeNotifications(userToken, context)
        this.chatClient.addEventHandler(
            ChatEventType.CHAT_MESSAGE_RECEIVED
        ) { payload: ChatEvent? ->
            realTimeNotificationCallBack(payload)
        }
    }

    private fun realTimeNotificationCallBack(payload: ChatEvent?) {
        val chatMessageReceivedEvent = payload as ChatMessageReceivedEvent?
        if (chatMessageReceivedEvent != null) {
            var isSender = false
            var contentType = ChatView.TYPE_TEXT
            val dateFormat = setChatDataFormat()
            val dataTime = dateFormat.parse(
                chatMessageReceivedEvent.createdOn.toLocalDateTime().toString()
            )!!
            val authorID =
                (chatMessageReceivedEvent.sender as CommunicationUserIdentifier).id.toString()
            if (authorID == userID)
                isSender = true
            if (contentSentType == "Image")
                contentType = ChatView.TYPE_IMAGE

            activity?.runOnUiThread {
                val message = com.mentallys.chatView.model.ChatMessage(
                    chatMessageReceivedEvent.id,
                    chatMessageReceivedEvent.content.toString(),
                    chatMessageReceivedEvent.senderDisplayName,
                    "",
                    isSender,
                    dataTime,
                    contentType
                )
                this.chatView.addToEnd(message)

            }
        }
    }

    private fun displayChatMessages() {
        chatViewModel.chatMessages.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                if(it.isNotEmpty()) {
                    activity?.runOnUiThread {
                        chatView.addListMessages(it, false)
                    }
                }
            }
        }
    }
    private fun sendMessageSetup() {

        this.chatView.setOnMessageSendListener {
            contentSentType="Text"
            val chatMessageOptions = SendChatMessageOptions()
                .setType(ChatMessageType.TEXT)
                .setContent(it)
                .setSenderDisplayName(userName)
            this.chatThreadClient.sendMessage(chatMessageOptions).id
        }
    }

    private fun getImageFromUserGallery() {
        imagePickLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == AppCompatActivity.RESULT_OK && result.data != null) {

                    //Uri of the uploaded image
                    imageUri = result.data?.data

                    //get the path of the image on the memory
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = activity?.contentResolver
                        ?.query(imageUri!!, filePathColumn, null, null, null)
                    assert(cursor != null)
                    cursor!!.moveToFirst()
                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    postPath = cursor.getString(columnIndex)
                    cursor.close()

                    //define the file to upload
                    photoFile = File(postPath!!)
                    postPath = photoFile!!.path.toString()
                    contentSentType = "Image"
                    val chatMessageOptions = SendChatMessageOptions()
                        .setType(ChatMessageType.TEXT)
                        .setContent(postPath)
                        .setSenderDisplayName(userName)
                    this.chatThreadClient.sendMessage(chatMessageOptions).id

                } else { // Result was a failure
                    Toast.makeText(context, "Error taking picture", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun askForPermission(permission: String, requestCode: Int) {
        if (context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    permission
                )
            } !== PackageManager.PERMISSION_GRANTED
        ) {
            // Should we show an explanation?
            if (activity?.let {
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        it, permission
                    )
                } == true
            ) {
                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(permission),
                    requestCode
                )
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(permission),
                    requestCode
                )
            }
        } else {
            when (requestCode) {
                REQUEST_PICK_PICTURE -> dispatchImageIntent()
                else -> Toast.makeText(
                    context,
                    getString(R.string.askForPermission_msg),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    /*
    to pick an image from the user gallery
     */
    private fun dispatchImageIntent() {
        val pickImageIntent = Intent(
            Intent.ACTION_GET_CONTENT,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        pickImageIntent.type = "image/*"
        if (pickImageIntent.resolveActivity(requireContext().packageManager) != null) {
            imagePickLauncher?.launch(
                pickImageIntent
            )
        }
    }
    private fun addParticipant(invitedParticipant: ChatParticipant) {

        val dataTime = Calendar.getInstance().time
        val participant = ChatParticipant()
            .setCommunicationIdentifier(invitedParticipant.communicationIdentifier)
            .setDisplayName(invitedParticipant.displayName)

        this.chatThreadClient.addParticipant(participant)
        this.listChatThreadClient.add(0,ChatParticipantInfo(this.threadId,
            (invitedParticipant.communicationIdentifier as CommunicationUserIdentifier).id,
            invitedParticipant.displayName,"", dataTime,"last message"))

        setChatParticipantList(this.listChatThreadClient)
        setChatSessionRoom()
    }

    private fun setChatDataFormat(): SimpleDateFormat {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm")
        formatter.timeZone = UTC
        return formatter
    }

    override fun onItemClick(resource: ChatParticipantInfo) {

        invitedInfoviewModel.showParticipantList(false)

        binding.chatSection.visibility = View.GONE
        binding.chatView.visibility = View.VISIBLE

        if(resource.threadId != this.threadId) {

            this.threadId = resource.threadId

            binding.progressbar.visibility = View.VISIBLE
            invitedInfoviewModel.setChatParticipantInfo(resource.communicationId)

            this.chatView.clearMessages()
            this.chatThreadClient =
                createChatThreadClient(this.endPoint, this.userToken, resource.threadId)
            chatViewModel.getChatMessages(this.chatThreadClient, this.userID)
            setChatSessionRoom()
        }

    }
}


