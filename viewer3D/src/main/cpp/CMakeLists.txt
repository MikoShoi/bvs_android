cmake_minimum_required(VERSION 3.4.1)

set(EXTERNAL_LIBS_DIR ${CMAKE_SOURCE_DIR}/external_libs)

add_library(lib_assimp SHARED IMPORTED)
set_target_properties(lib_assimp PROPERTIES IMPORTED_LOCATION
    ${EXTERNAL_LIBS_DIR}/assimp/lib/${ANDROID_ABI}/libassimp.so)

add_library(native-lib SHARED
            native-lib.cpp
            ModelLoader.cpp
            ModelData.cpp)

target_include_directories(native-lib PRIVATE
                           ${EXTERNAL_LIBS_DIR}/assimp/include)

target_link_libraries(native-lib
                      android
                      lib_assimp
                      log)
