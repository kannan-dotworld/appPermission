package dev.dotworld.apppermission

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    companion object{
        val TAG =MainActivity.javaClass.simpleName    }
    private var permissionsRequired = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
    private val CAMERA_MIC_PERMISSION_REQUEST_CODE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (checkPermission()) {
            Log.d(TAG, "onCreate: all Permission done")
        } else {
            permissions()
        }


    }

    fun checkPermission(): Boolean {
        val camera = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        val phoneState =ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_PHONE_STATE
        )
        val storage = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val mic = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
        return camera == PackageManager.PERMISSION_GRANTED && phoneState == PackageManager.PERMISSION_GRANTED &&
                storage == PackageManager.PERMISSION_GRANTED && mic == PackageManager.PERMISSION_GRANTED

    }

    private fun permissions(){
if (
    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)||
    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO) ||
    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
    ActivityCompat.shouldShowRequestPermissionRationale(
        this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    ){
    Log.d(TAG, "permissions: if ")
    ActivityCompat.requestPermissions(this, permissionsRequired, CAMERA_MIC_PERMISSION_REQUEST_CODE)
}   else{
    Log.d(TAG, "permissions: else")
    ActivityCompat.requestPermissions(this, permissionsRequired, CAMERA_MIC_PERMISSION_REQUEST_CODE)
}
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_MIC_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    Log.d(TAG, "onRequestPermissionsResult: ")
                }   else if(ActivityCompat.shouldShowRequestPermissionRationale(this,permissionsRequired[0]) ||
            ActivityCompat.shouldShowRequestPermissionRationale(this,permissionsRequired[1]) ||
            ActivityCompat.shouldShowRequestPermissionRationale(this,permissionsRequired[2]) ||
            ActivityCompat.shouldShowRequestPermissionRationale(this,permissionsRequired[3])) {
                    Log.d(TAG, "onRequestPermissionsResult: else Permissions")
                   permissions()
                }
                else {
            Log.d(TAG, "onRequestPermissionsResult: setting page")
            val i = Intent()
            i.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            i.addCategory(Intent.CATEGORY_DEFAULT)
            i.data = Uri.parse("package:" + this.packageName)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("app need permission")
                    builder.setMessage("open settings?")
                    builder.setPositiveButton("Yes"){dialogInterface, which ->
                        Toast.makeText(applicationContext,"clicked yes",Toast.LENGTH_LONG).show()
                        startActivity(i)
                    }

                    builder.setNegativeButton("No"){dialogInterface, which ->
                        finish()
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
        }
            }

            else -> {
                Log.d(TAG, "onRequestPermissionsResult: Ignore all other requests")
            }
        }


    }



}
