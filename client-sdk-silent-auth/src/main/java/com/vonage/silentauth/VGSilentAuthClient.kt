/*
 * MIT License
 * Copyright (C) 2022 4Auth Limited. All rights reserved
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.vonage.silentauth

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import com.vonage.silentauth.network.CellularNetworkManager
import com.vonage.silentauth.network.NetworkManager
import java.net.URL
import org.json.JSONObject

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class VGSilentAuthClient private constructor(networkManager: CellularNetworkManager) {
    private val networkManager: NetworkManager = networkManager

    /**
     * Open a given url after forcing the data connectivity on the device
     *
     * @param url The url to be open over a data cellular connectivity.
     * @param debug A flag to include or not the url trace in the response
     *
     */
    fun openWithDataCellular(@NonNull url: URL, debug: Boolean): JSONObject {
        Log.d("VGSilentAuthClient", "openWithDataCellular")
        val networkManager: NetworkManager = getCellularNetworkManager()
        return networkManager.openWithDataCellular(url, null, debug)
    }

    /**
     * Open a given url after forcing the data connectivity on the device
     *
     * @param url The url to be open over a data cellular connectivity.
     * @param accessToken Optional Access Token to be added in the Authorization header (Bearer).
     * @param debug A flag to include or not the url trace in the response
     *
     */
    fun openWithDataCellularAndAccessToken(@NonNull url: URL, accessToken: String?, debug: Boolean): JSONObject {
        Log.d("VGSilentAuthClient", "openWithDataCellularAndAccessToken")
        val networkManager: NetworkManager = getCellularNetworkManager()
        return networkManager.openWithDataCellular(url, accessToken, debug)
    }

    private fun getCellularNetworkManager(): NetworkManager {
        return networkManager
    }

    companion object {
        private const val TAG = "VGSilentAuthClient"
        private var instance: VGSilentAuthClient? = null
        private var currentContext: Context? = null

        @Synchronized
        fun initializeSdk(context: Context): VGSilentAuthClient {
            var currentInstance = instance
            if (null == currentInstance || currentContext != context) {
                val nm = CellularNetworkManager(context)
                currentContext = context
                currentInstance = VGSilentAuthClient(nm)
            }
            instance = currentInstance
            return currentInstance
        }

        @Synchronized
        fun getInstance(): VGSilentAuthClient {
            val currentInstance = instance
            checkNotNull(currentInstance) {
                VGSilentAuthClient::class.java.simpleName +
                        " is not initialized, call initializeSdk(...) first"
            }
            return currentInstance
        }
    }
}
