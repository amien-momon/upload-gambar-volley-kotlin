
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import kotlin.collections.HashMap


class Bukti_Activity : AppCompatActivity() {

    lateinit var foto :ImageView
    lateinit var file : Button
    lateinit var upload : Button
    var bitmap: Bitmap? = null
    lateinit var UPLOAD_URL : String = "http://localhost/file/Bukti.php"
    



    companion object {
        private const val IMAGE_PICK_CODE = 999
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bukti_)

        file = findViewById(R.id.file)
        upload = findViewById(R.id.upload)
        foto = findViewById(R.id.bukti)
        et_username = findViewById(R.id.username)


        //botton upload
        upload.setOnClickListener{
                upload()
        }
        
        //membuka gallery dan memilih gambar
        file.setOnClickListener{
            launchGallery()
        }

    }
    
    
    //membuka gallery dan menggambil gambar
    private fun launchGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)

    }
    
    
            //menampilkan gambar yang dipilih
            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null)
            {
                val path = data.getData()
                try
                {
                    val inputStream = path?.let { contentResolver.openInputStream(it) }
                    bitmap = BitmapFactory.decodeStream(inputStream)
                    foto.setImageBitmap(bitmap)
                    foto.visibility = View.VISIBLE
                    upload.visibility = View.VISIBLE
                }
                catch (e: IOException) {
                    e.printStackTrace()
                }
                Toast.makeText(this, "Foto terpilih", Toast.LENGTH_SHORT).show()
            }
            super.onActivityResult(requestCode, resultCode, data)
        }



    private fun upload() {

        val stringRequest = object : StringRequest(
            Request.Method.POST,
                UPLOAD_URL,
                Response.Listener<String> { response ->
                    try {
                        val obj = JSONObject(response)
                        if (!obj.getBoolean("error")){
                            Toast.makeText(
                                applicationContext,
                                obj.getString("massage"),
                                Toast.LENGTH_LONG
                            ).show()
                            Handler().postDelayed({
                                startActivity(Intent(this, Pesanan_Activity::class.java))
                                finish()
                            },900)
                        }else{
                            Toast.makeText(
                                applicationContext,
                                obj.getString("massage"),
                                Toast.LENGTH_LONG
                            ).show()
                        }


                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                },
                Response.ErrorListener { volleyError ->
                    Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG)
                        .show()
                }) {

                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    val gambar = bitmap?.let { imgToString(it) }
                    if (gambar != null) {
                        params.put("foto",gambar)
                    }
                    return params
                }

            }

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add<String>(stringRequest)

        }


    //Kompres bitmap jpeg
    //Base64 merubahnya menjadi string 
    private fun imgToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val bytes = baos.toByteArray()
        return Base64.encodeToString(bytes,Base64.DEFAULT)
    }



}
