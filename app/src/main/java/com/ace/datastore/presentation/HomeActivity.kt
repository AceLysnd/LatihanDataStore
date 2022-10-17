package com.ace.datastore.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.ace.datastore.R
import com.ace.datastore.data.AccountDataStoreManager
import com.ace.datastore.databinding.ActivityHomeBinding
import com.ace.datastore.utils.ViewModelFactory
import com.bumptech.glide.Glide
import java.io.File

class HomeActivity : AppCompatActivity() {

    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var uri: Uri

    private lateinit var viewModel: MainViewModel
    private lateinit var pref: AccountDataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()

        binding.btnChoose.setOnClickListener{
            checkingPermissions()
        }

        setUsername()

    }

    private fun setUsername() {
        viewModel.getAccount().observe(this) {
            binding.tvUsername.text = it.username
        }
    }


    private fun setupViewModel() {
        pref = AccountDataStoreManager(this)
        viewModel =
            ViewModelProvider(this, ViewModelFactory(pref))[MainViewModel::class.java]
    }

    private fun checkingPermissions() {
        val REQUEST_CODE_PERMISSION = 200
        if (isGranted(
                this, Manifest.permission.CAMERA,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_CODE_PERMISSION,
            )
        ) {
            chooseImageDialog()
        }
    }

    private fun isGranted(
        homeActivity: HomeActivity,
        permission: String,
        permissions: Array<String>,
        request: Int
    ): Boolean {
        val permissionCheck = ActivityCompat.checkSelfPermission(homeActivity, permission)
        return if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(homeActivity, permission)) {
                showPermissionDeniedDialog()
            } else {
                ActivityCompat.requestPermissions(homeActivity, permissions, request)
            }
            false
        } else {
            true
        }
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permission Denied")
            .setMessage("Permission is denied, please allow from App Settings.")
            .setPositiveButton(
                "App Settings"
            ) { _, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("Package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            .show()
    }

    private fun chooseImageDialog() {
        AlertDialog.Builder(this)
            .setMessage("Pilih Gambar")
            .setPositiveButton("Gallery") {_, _ -> openGallery() }
            .setNegativeButton("Camera") {_, _ -> openCamera() }
            .show()
    }

    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            binding.ivImage.setImageURI(result)
        }

    private fun openGallery(){
        intent.type = "image/*"
        galleryResult.launch("image/*")
    }

    private val cameraResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { result ->
            if (result) {
                handleCameraImage(uri)
            }
        }

    private fun handleCameraImage(uri: Uri) {
        Glide.with(this).load(uri).into(binding.ivImage)
    }

    private fun openCamera() {
        val photoFile = File.createTempFile(
            "IMG_",
            ".jpg",
            this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )

        uri = FileProvider.getUriForFile(
            this,
            "${this.packageName}.provider",
            photoFile
        )
        cameraResult.launch(uri)
    }
}