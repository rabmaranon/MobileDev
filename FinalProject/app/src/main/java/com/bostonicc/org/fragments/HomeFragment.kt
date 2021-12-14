package com.bostonicc.org.fragments


import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.net.http.SslError
import android.provider.MediaStore
import android.util.Log
import android.webkit.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bostonicc.org.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.webkit.WebChromeClient
import android.webkit.WebChromeClient.CustomViewCallback
import android.content.pm.PackageManager
import android.os.*
import android.view.*
import kotlin.system.exitProcess


class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        url = arguments?.getString("url").toString();
    }

    private lateinit var webView: WebView

    private val TAG = "HomeFragment"
    private var mCM: String? = null
    private var mUM: ValueCallback<Uri>? = null
    private var mUMA: ValueCallback<Array<Uri>>? = null
    private val FCR = 1


    lateinit var pb: ProgressBar
    var url = ""

    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    lateinit var customViewCallback: CustomViewCallback
    lateinit var mCustomView: View
    lateinit var mWebChromeClient: WebChromeClient
    lateinit var mWebViewClient: MyBrowser

    var isFirstTime = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        pb = root.findViewById(R.id.progressBar)
        swipeRefreshLayout = root.findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener { webView.reload() }
        webView = root.findViewById(R.id.webView) as WebView
        mWebViewClient = MyBrowser()
        webView.webViewClient = mWebViewClient
        val url = url

        webView.settings.loadsImagesAutomatically = true
        webView.settings.javaScriptEnabled = true
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
//        webView.webChromeClient = mWebChromeClient
        webView.settings.domStorageEnabled = true
        webView.settings.loadsImagesAutomatically = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        webView.settings.builtInZoomControls = true
        webView.settings.displayZoomControls = false
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true

//        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

//        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.isScrollbarFadingEnabled = false

        val settings = webView.getSettings()
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.databaseEnabled = true

        settings.javaScriptCanOpenWindowsAutomatically = true

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            webView.settings.databasePath =
                "/data/data/" + webView.getContext().packageName + "/databases/"
        }
        val databasePath: String =
            activity?.applicationContext?.getDir("database", Context.MODE_PRIVATE)?.path.toString()
        settings.databasePath = databasePath

        webView.settings.setGeolocationDatabasePath(activity?.filesDir?.path)



        var canClick = true
        webView.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {

                if (canClick){
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        activity?.finish()
                        exitProcess(0)
                    }
                }

                canClick = false
                Handler(Looper.getMainLooper()).postDelayed({
                    //Do something after 100ms
                }, 500)
                return@OnKeyListener true
            }
            false
        })

        webView.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }


        webView.webChromeClient = object : WebChromeClient() {
            //For Android 3.0+
            override fun onGeolocationPermissionsShowPrompt(
                origin: String,
                callback: GeolocationPermissions.Callback
            ) {
                callback.invoke(origin, true, false)
            }

            fun openFileChooser(uploadMsg: ValueCallback<Uri>?) {
                mUM = uploadMsg
                val i = Intent(Intent.ACTION_GET_CONTENT)
                i.addCategory(Intent.CATEGORY_OPENABLE)
                i.type = "*/*"
                activity?.startActivityForResult(
                    Intent.createChooser(i, "File Chooser"),
                    FCR
                )
            }


            //For Android 5.0+
            override fun onShowFileChooser(
                webView: WebView, filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                if (mUMA != null) {
                    mUMA!!.onReceiveValue(null)
                }
                mUMA = filePathCallback
                var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (activity?.getPackageManager()
                        ?.let { takePictureIntent!!.resolveActivity(it) } != null
                ) {
                    var photoFile: File? = null
                    try {
                        photoFile = createImageFile()
                        takePictureIntent?.putExtra("PhotoPath", mCM)
                    } catch (ex: IOException) {
                        Log.e(TAG, "Image file creation failed", ex)
                    }
                    if (photoFile != null) {
                        mCM = "file:" + photoFile.getAbsolutePath()
                        takePictureIntent?.putExtra(
                            MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile)
                        )
                    } else {
                        takePictureIntent = null
                    }
                }
                val contentSelectionIntent = Intent(Intent.ACTION_GET_CONTENT)
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE)
                contentSelectionIntent.type = "*/*"
                val intentArray: Array<Intent?>
                intentArray = takePictureIntent?.let { arrayOf(it) } ?: arrayOfNulls(0)
                val chooserIntent = Intent(Intent.ACTION_CHOOSER)
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser")
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
                startActivityForResult(chooserIntent, FCR)
                return true
            }
        }


        return root
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (Build.VERSION.SDK_INT >= 21) {
            var results: Array<Uri>? = null
            //Check if response is positive
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == FCR) {
                    if (null == mUMA) {
                        return
                    }
                    if (intent == null || intent.data == null) {
                        //Capture Photo if no image available
                        if (mCM != null) {
                            results = arrayOf(Uri.parse(mCM))
                        }
                    } else {
                        val dataString = intent.dataString
                        if (dataString != null) {
                            results = arrayOf(Uri.parse(dataString))
                        }
                    }
                }
            }
            mUMA!!.onReceiveValue(results)
            mUMA = null
        } else {
            if (requestCode == FCR) {
                if (null == mUM) return
                val result = if (intent == null || resultCode != RESULT_OK) null else intent.data
                mUM?.onReceiveValue(result)
//                mUM.onReceiveValue(result)
                mUM = null
            }
        }
    }

    var isRunning = true

    inner class MyBrowser : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.startsWith("mailto:")) {
                val i = Intent(Intent.ACTION_SENDTO, Uri.parse(url))
                startActivity(i)
                return true
            }
            if (url.startsWith("tel:")) {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse(url))
                startActivity(intent)
                view.reload()
                return true
            }

            if (!url.startsWith("https://www.bostonicc.org/")) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(browserIntent)
                return true
            }
            Log.i("Loaded", url)
            pb.visibility = View.VISIBLE
            view.loadUrl(url)
            return true
        }

        override fun onReceivedError(
            view: WebView,
            errorCode: Int,
            description: String,
            failingUrl: String
        ) {
            Log.e("error", "Error: $description")
            pb.visibility = View.INVISIBLE
            swipeRefreshLayout.isRefreshing = false;
        }

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            // for SSLErrorHandler
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setMessage("Error, Invalid SSL certificate found")
            builder.setPositiveButton("continue",
                DialogInterface.OnClickListener { dialog, which -> handler.proceed() })
            builder.setNegativeButton("cancel",
                DialogInterface.OnClickListener { dialog, which -> handler.cancel() })
            val dialog: AlertDialog = builder.create()
            dialog.show()

            swipeRefreshLayout.setRefreshing(false);
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            pb.setVisibility(View.INVISIBLE)
            swipeRefreshLayout.setRefreshing(false);
        }

//        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
//            super.onPageStarted(view, url, favicon)
//
//        }
    }

    override fun onResume() {
        super.onResume()

        if (isFirstTime){
            webView.loadUrl(url)
            isFirstTime = false
        }
        webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        webView.onPause()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        @SuppressLint("SimpleDateFormat") val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "img_" + timeStamp + "_"
        val storageDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }


}