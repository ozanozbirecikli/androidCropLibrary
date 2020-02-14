package com.example.crop_library

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException


class MainActivity : AppCompatActivity() {

    lateinit var logo_image: ImageView
    lateinit var removeText: TextView

    private val GalleryPermission = 2
    private val CameraPermission = 1
    val SELECT_FILE = 1
    var CAPTURE_IMAGE = 2

    private val mSourceUri: Uri? = null
    private val mCompressFormat = CompressFormat.JPEG


    lateinit var cropImage: CropImageView

    private lateinit var CropIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        defineView()
    }

    fun defineView() {
        logo_image = findViewById(R.id.logo_image)

        cropImage = findViewById(R.id.cropImageView)

        cropImage.setCropShape(CropImageView.CropShape.OVAL)
        cropImage.setScaleType(CropImageView.ScaleType.FIT_CENTER)
        cropImage.setAutoZoomEnabled(true)


        removeText = findViewById(R.id.remove)

        logo_image.setOnClickListener {
            //            selectImage()

            startCropImageActivity()
        }

        removeText.setOnClickListener {
            logo_image.setImageResource(R.drawable.logoaddicon)
        }


    }


    private fun selectImage() {
        var options = arrayOf("Fotoğraf Çek", "Galeriden Yükle", "İptal")
        var builder = AlertDialog.Builder(this)
        builder.setTitle("Fotoğraf Yükle")
        builder.setItems(options, object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                if (options[p1] == "Fotoğraf Çek") {
                    cameraIntent()
                } else if (options[p1].equals("Galeriden Yükle")) {
                    galleryIntent()
                } else if (options[p1].equals("İptal")) {
                    p0?.dismiss()
                }
            }
        })
        builder.show()
    }


    private fun cameraIntent() {
        val tPermissonArray = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
/*
        if (ContextCompat.checkSelfPermission(
                baseContext!!,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                baseContext!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                baseContext!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //requestPermissions(new String[]{Manifest.permission.CAMERA}, CameraPermission);
            requestPermissions(tPermissonArray, CameraPermission)
        } else {*/
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAPTURE_IMAGE)
//        }
    }

    private fun galleryIntent() {
        /*if (ContextCompat.checkSelfPermission(
                baseContext!!,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val tPermissonArray =
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(tPermissonArray, GalleryPermission)
        } else*/

        val i = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        i.type = "image/*"

        startActivityForResult(i, SELECT_FILE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {

//            Uri uri = Uri.parse(data)
/*
            if (requestCode == CAPTURE_IMAGE) {
                onCaptureImageResult(data!!)
            } else if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data!!)

            }*/ if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
                val imageUri = CropImage.getPickImageResultUri(this, data!!)
                startCropActivity(imageUri)

                val cropped: Bitmap = cropImageView.getCroppedImage()
                logo_image.setImageBitmap(cropped)
//                logo_image.setImageURI(imageUri)
            } else if (requestCode == CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE) {
                CropImage.startPickImageActivity(this)

                val cropped: Bitmap = cropImageView.getCroppedImage()
                logo_image.setImageBitmap(cropped)

            } else {


            }
        }
    }

    private fun onCaptureImageResult(data: Intent) {
        val bitmap = data.extras!!.get("data") as Bitmap?
//        openCropActivity()
        logo_image.setImageBitmap(bitmap)


    }

    @Throws(IOException::class)
    private fun onSelectFromGalleryResult(data: Intent) {
/*
        val selectedImage = data.data
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

        val cursor = contentResolver.query(
            selectedImage!!,
            filePathColumn, null, null, null
        )
        cursor!!.moveToFirst()

        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        val picturePath = cursor.getString(columnIndex)
        cursor.close()
        val bitmap = BitmapFactory.decodeFile(picturePath)
        Log.e("", "")
        logo_image.setImageBitmap(BitmapFactory.decodeFile(picturePath))
*/


//        logo_image.setImageURI(data?.data)

/*
        cropImage.crop(data?.data)
            .execute(object : CropCallback {
                override fun onSuccess(cropped: Bitmap) {
                    cropImage.save(cropped)
                        .execute(data?.data, mSaveCallback)
                }

                override fun onError(e: Throwable) {}
            })
*/

        //upload(imageFile)
    }

    private fun startCropImageActivity() {
        CropImage.activity()
            .start(this)
    }

    private fun startCropActivity(uri: Uri) {

        CropImage.activity(uri)

            .start(this)

    }

    @SuppressLint("NewApi")
    fun onSelectImageClick(view: View?) {
        CropImage.startPickImageActivity(this)
    }


/*
    private fun openCropActivity(Uri sourceUri, Uri destinationUri) {
        UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(5f, 5f)
            .withMaxResultSize(400, 150)
            .start(this)

    }*/

}
