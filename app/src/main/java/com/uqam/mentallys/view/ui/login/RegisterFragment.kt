package com.uqam.mentallys.view.ui.login


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.squareup.picasso.Picasso
import com.uqam.mentallys.R
import com.uqam.mentallys.databinding.FragmentRegisterBinding
import com.uqam.mentallys.network.Resource
import com.uqam.mentallys.utils.enable
import com.uqam.mentallys.utils.handleApiError
import com.uqam.mentallys.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File


@Suppress("IMPLICIT_BOXING_IN_IDENTITY_EQUALS")
@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {
    private val REQUEST_CAMERA_CAPTURE = 1034
    private val REQUEST_PICK_PICTURE = 1035
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<AuthViewModel>()
    var navController: NavController?=null
    var cameraResultLauncher: ActivityResultLauncher<Intent>? = null
    var imagePickLauncher: ActivityResultLauncher<Intent>? = null
    private var imageUri: Uri? = null
    private var postPath: String? = null
    val APP_TAG = "MyCustomApp"
    val photoFileName = "photo.jpg"
    var photoFile: File? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRegisterBinding.bind(view)
        navController = Navigation.findNavController(view)
        binding.progressbar.visible(false)
        binding.editTextPassword.addTextChangedListener {
            val email = binding.editTextEmailAddress.text.toString().trim()
            binding.buttonRegister.enable(email.isNotEmpty() && it.toString().isNotEmpty())
        }
        observerLoginInfo()

        //Within the onCreate method
        imagePickLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
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

                Picasso.get().load(imageUri)
                    .resize(300, 300)
                    .centerCrop()
                    .into(binding.imgPreview)

            } else { // Result was a failure
                Toast.makeText(context, "Error taking picture", Toast.LENGTH_SHORT).show()
            }
        }
        //Within the onCreate method
        cameraResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == AppCompatActivity.RESULT_OK && result.data != null) {
                var takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                takenImage = Bitmap.createScaledBitmap(takenImage!!, 200, 200, true)
                binding.imgPreview.setImageBitmap(takenImage)

            } else { // Result was a failure
                Toast.makeText(context, "Error taking picture", Toast.LENGTH_SHORT).show()
            }
        }
        binding.buttonRegister.setOnClickListener {
            binding.progressbar.visibility = View.VISIBLE
            createAccount()
            // uploadFile()
        }
        binding.btnCamera.setOnClickListener {
            clickedCameraBtnImage(it)
        }
        binding.btnImage.setOnClickListener {
            clickedCameraBtnImage(it)
        }

        //if the account is already created
        binding.textViewLoginNow.setOnClickListener {
            navController!!.navigate(R.id.loginFragment)
        }
    }
    private fun observerLoginInfo() {
        viewModel.CreateUserResponse.observe(viewLifecycleOwner) {
            binding.progressbar.visibility = View.GONE
            when (it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        Toast.makeText(context, "Account has been created", Toast.LENGTH_LONG).show()
                        navController!!.navigate(R.id.loginFragment)
                    }
                }
                is Resource.Failure -> handleApiError(it) { Toast.makeText(context, it.errorBody.toString(), Toast.LENGTH_LONG).show()}
                else -> { Toast.makeText(context, "internal error", Toast.LENGTH_LONG).show() }
            }
        }
    }
    private fun createAccount() {
        val firstName = binding.firstName.text.toString().trim('"')
        val lastName = binding.lastName.text.toString().trim()
        val email = binding.editTextEmailAddress.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()
        val passwordConfirm = binding.editTextConfirmPassword.text.toString().trim()

        viewModel.createAccount(firstName, lastName,email, password, passwordConfirm, photoFile)
    }

    private fun clickedCameraBtnImage(v: View) {
        when (v.id) {
            R.id.btn_image -> askForPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_PICK_PICTURE
            )
            R.id.btn_camera -> askForPermission(
                Manifest.permission.CAMERA, REQUEST_CAMERA_CAPTURE
            )
            else -> {}
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
                REQUEST_CAMERA_CAPTURE -> dispatchTakePictureIntent()
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

    /*
    to use the camera to take a new image
     */
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName)

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (this.photoFile != null) {
            val fileProvider: Uri? =
                activity?.let {
                    FileProvider.getUriForFile(
                        it,
                        "com.codepath.fileprovider.Mentallys", photoFile!!)
                }
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            if (context?.let { takePictureIntent.resolveActivity(it.packageManager) } != null) {
                cameraResultLauncher?.launch(
                    takePictureIntent
                )
            }

        }
    }
    // Returns the File for a photo stored on disk given the fileName
    fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES) , APP_TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(APP_TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }
}