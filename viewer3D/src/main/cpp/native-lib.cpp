#include <jni.h>
#include <string>
#include "ModelLoader.h"

#include <android/log.h>
#define printf(...) __android_log_print(ANDROID_LOG_DEBUG, "TAG", __VA_ARGS__);

extern "C"
JNIEXPORT jstring JNICALL
Java_com_model_Model_hiCpp(JNIEnv *env, jobject /* this */)
{
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jfloatArray JNICALL
Java_com_model_Model_getVertexData(JNIEnv *env, jobject, jstring path)
{
    try
    {
        std::string modelPath = env->GetStringUTFChars(path, NULL);

        std::vector<float> vertexData       = ModelLoader()
                .load(modelPath)
                .getModelData().posAndNorms;
        const unsigned int vertexDataSize   = vertexData.size();

        // create array that will be passed back to Java
        jfloatArray array = env->NewFloatArray(vertexDataSize);
        if(array == NULL)
        {
            std::__throw_runtime_error("Can not return vertex data");
        }

        env->SetFloatArrayRegion( array, 0, vertexDataSize,(jfloat*) vertexData.data() );

        return array;
    }
    catch (std::exception e)
    {
        env->ThrowNew ( env->FindClass("java/lang/Error"), e.what() );
    }
}

//== TODO: fix error handle