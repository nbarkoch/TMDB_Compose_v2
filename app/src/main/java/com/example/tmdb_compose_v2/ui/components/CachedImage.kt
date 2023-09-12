package com.example.tmdb_compose_v2.ui.components

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.URL

class ImageCacheManager(context: Context) {

    companion object {
        const val EXPIRATION_TIME = 24 * 60 * 60 * 1000
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("image_cache", Context.MODE_PRIVATE)

    private fun getCacheTimestamp(url: String): Long {
        // Retrieve the timestamp for the given URL, default to 0 if not found
        return sharedPreferences.getLong("$url.timestamp", 0)
    }

    private fun encodeBitmapToString(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val imageBytes = outputStream.toByteArray()
        return android.util.Base64.encodeToString(imageBytes, android.util.Base64.DEFAULT)
    }


    private fun decodeStringToBitmap(imageString: String): Bitmap? {
        val imageBytes = android.util.Base64.decode(imageString, android.util.Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }


    private fun saveToCache(url: String, bitmap: Bitmap) {
        // Save the image data as a Base64-encoded string
        val imageString = encodeBitmapToString(bitmap)

        // Save the image data to SharedPreferences
        sharedPreferences.edit {
            putString(url, imageString)

            // Store the timestamp for the image
            putLong("$url.timestamp", System.currentTimeMillis())
        }
    }

    private fun getFromCache(url: String): Bitmap? {
        // Retrieve the cached image data as a Base64-encoded string
        val imageString = sharedPreferences.getString(url, null)

        // If image data exists, decode it into a Bitmap
        return imageString?.let { image ->
            decodeStringToBitmap(image)
        }
    }



    private fun isCacheValid(url: String): Boolean {
        val currentTime = System.currentTimeMillis()
        val cachedTime = getCacheTimestamp(url)
        val expirationTime = cachedTime + (EXPIRATION_TIME)

        return currentTime <= expirationTime
    }

    private suspend fun fetchImageFromNetwork(url: String): Bitmap? {
        try {
            return withContext(Dispatchers.IO) {
                val inputStream: InputStream = URL(url).openStream()
                val downloadedBitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()

                // Save the downloaded image to the cache
                saveToCache(url, downloadedBitmap)

                downloadedBitmap
            }
        } catch (e: Exception) {
            // any exceptions will return null
            e.printStackTrace()
            return null
        }
    }

    suspend fun loadImage(url: String): Bitmap? {
        val cachedBitmap = getFromCache(url)
        return if (cachedBitmap != null && isCacheValid(url)) {
            // Use the cached image
            cachedBitmap
        } else {
            // Fetch the image from the network and save it to the cache
            val downloadedBitmap = fetchImageFromNetwork(url)
            downloadedBitmap?.run {
                saveToCache(url, downloadedBitmap)
            }
            downloadedBitmap
        }
    }

}

/**
 * Custom composable that manage the caching in the level of sharedPreferences
 * (Since I didn't find any solution that satisfy the requirement,)
 * Well-known libraries such as Coil that use Compose apparently don't support it explicitly.
 *
 * We would of course prefer to use a library that is maintained and that works more efficiently,
 * but within the framework of this project we were required to do a certain thing, so I did it this way
 * */
@Composable
fun CachedImage(
    url: String, contentDescription: String?,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null
) {
    val localContext = LocalContext.current
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val imageCacheManager = remember { ImageCacheManager(localContext) }

    LaunchedEffect(url) {
        imageBitmap = imageCacheManager.loadImage(url)
    }

    Image(
        bitmap = imageBitmap?.asImageBitmap() ?: ImageBitmap(5, 5),
        contentDescription = contentDescription,
        modifier = modifier,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter
    )
}