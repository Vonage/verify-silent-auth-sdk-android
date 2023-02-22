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
package com.vonage.silentauth.network

import android.os.Build
import android.util.Log
import com.vonage.silentauth.BuildConfig

class TraceCollector private constructor() {
    private val debugInfo = DebugInfo()
    private val trace by lazy { StringBuilder() }

    private var isTraceEnabled = false

    @Synchronized
    fun shouldLogDebugInfoToConsole(value: Boolean) {
        debugInfo.enableConsole(value)
    }

    @Synchronized
    fun startTrace() {
        if (!isTraceEnabled) {
            debugInfo.enableCollection(true)
            trace.clear()
            isTraceEnabled = true
            trace.append("${DateUtils.now()}: ${deviceInfo()}")
        } else {
            Log.e("TraceCollector", "startTrace - Trace is already enabled")
        }
    }

    @Synchronized
    fun stopTrace() {
        isTraceEnabled = false
        trace.clear()
        debugInfo.enableCollection(false)
        debugInfo.clear()
    }

    fun getTrace(): TraceInfo {
        return TraceInfo(trace.toString(), debugInfo)
    }

    @Synchronized
    fun addTrace(log: String) {
        if (isTraceEnabled) trace.append(log + "\n")
    }

    @Synchronized
    fun addDebug(priority: Int, tag: String, log: String) {
        debugInfo.addLog(priority, tag, log)
    }

    fun getDebugInfo(): DebugInfo {
        return debugInfo
    }

    companion object {
        val instance: TraceCollector by lazy { TraceCollector() }
    }
}

data class TraceInfo(val trace: String, val debugInfo: DebugInfo)

class DebugInfo {
    private val bufferMap by lazy { mutableMapOf<String, String>() }
    private var consoleLogsEnabled = true
    private var collectionEnabled = false

    private val dateUtils: DateUtils by lazy {
        DateUtils()
    }

    @Synchronized
    fun enableCollection(value: Boolean) {
        if (value) {
            bufferMap[DateUtils.now()] = deviceInfo()
        }
        collectionEnabled = value
    }

    @Synchronized
    fun enableConsole(value: Boolean) {
        consoleLogsEnabled = value
    }

    @Synchronized
    fun clear() {
        bufferMap.clear()
    }

    @Synchronized
    fun addLog(priority: Int, tag: String, msg: String) {

        if (collectionEnabled) {
            bufferMap[DateUtils.now()] = "$tag - $msg"
        }

        if (consoleLogsEnabled) {
            when (priority) {
                2 -> Log.v(tag, msg) // VERBOSE
                3 -> Log.d(tag, msg) // DEBUG
                4 -> Log.i(tag, msg) // INFO
                5 -> Log.w(tag, msg) // WARN
                6 -> Log.e(tag, msg) // ERROR
                else -> { // Fall back to // DEBUG
                    Log.d(tag, msg)
                }
            }
        }
    }

    override fun toString(): String {
        var buffer = StringBuffer()
        val sorted = bufferMap.toSortedMap()
        val skeys = sorted.keys
        for (key in skeys) {
            buffer.append("$key: ${sorted[key]}\n")
        }
        return buffer.toString()
    }
}

fun userAgent(): String {
    return "tru-sdk-android" + "/" + BuildConfig.VERSION_NAME + "/" + Build.VERSION.SDK_INT + " " + "Android" + "/" + Build.VERSION.RELEASE + " " + Build.MANUFACTURER + "/" + Build.MODEL
}

fun deviceInfo(): String {
    val manufacturer = Build.MANUFACTURER
    val model = Build.MODEL
    val version = Build.VERSION.SDK_INT
    val versionRelease = Build.VERSION.RELEASE
    return "DeviceInfo: $manufacturer, $model, $version, $versionRelease \n User-Agent: ${userAgent()}\n"
}
