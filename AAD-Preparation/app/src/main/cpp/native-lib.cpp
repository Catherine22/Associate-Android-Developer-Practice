#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_catherine_materialdesignapp_utils_CBridge_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}