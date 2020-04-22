/**
 * Copyright (c) 2017-2019 woodser
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

#include <jni.h>

#ifndef _Included_monero_cpp_bridge_MoneroUtilsNative
#define _Included_monero_cpp_bridge_MoneroUtilsNative

// TODO: this causes warning
std::string jstring2string(JNIEnv *env, jstring jStr);

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jbyteArray JNICALL Java_monero_common_MoneroUtils_jsonToBinaryJni(JNIEnv *, jclass, jstring);

JNIEXPORT jstring JNICALL Java_monero_common_MoneroUtils_binaryToJsonJni(JNIEnv *, jclass, jbyteArray);

JNIEXPORT jstring JNICALL Java_monero_common_MoneroUtils_binaryBlocksToJsonJni(JNIEnv *, jclass, jbyteArray);

JNIEXPORT void JNICALL Java_monero_common_MoneroUtils_initLoggingJni(JNIEnv *, jclass, jstring jpath, jboolean);

JNIEXPORT void JNICALL Java_monero_common_MoneroUtils_setLogLevelJni(JNIEnv *, jclass, jint);

#ifdef __cplusplus
}
#endif
#endif
