# Sets the minimum version of CMake required to build the native
# library. You should either keep the default value or only pass a
# value of 3.4.0 or lower.

cmake_minimum_required(VERSION 3.4.1)
#add_compile_options(-std=c++03)
# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds it for you.
# Gradle automatically packages shared libraries with your APK.

#include_directories(D:/Android/WorkSpaseForAS/TestLibjpeg/app/libs)
add_library( # Sets the name of the library.
             simple_gif

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             # Associated headers in the same location as their source
             # file are automatically included.
             #src/main/cpp/native-lib.cpp
             src/main/cpp/DataBlock.cpp
             src/main/cpp/BitWritingBlock.cpp
             src/main/cpp/GifDecoder.cpp
             src/main/cpp/GifEncoder.cpp
             src/main/cpp/GifFrame.cpp
             src/main/cpp/io_github_yylyingy_simplegif_GifDecodeInfoHandle.cpp
             src/main/cpp/io_github_yylyingy_simplegif_GifEncodeInfoHandle.cpp
             src/main/cpp/GCTGifEncoder.cpp
             src/main/cpp/LCTGifEncoder.cpp
             src/main/cpp/SimpleGCTEncoder.cpp
             src/main/cpp/BaseGifEncoder.cpp
             )

#include_directories(src/main/cpp/include)
#add_library( libjpeg SHARED IMPORTED )
#这句话是jpeg对应的so文件,so文件是放到ibs这个文件夹中(相对与cpp这个文件的位置)
#set_target_properties(libjpeg  PROPERTIES IMPORTED_LOCATION
#D:/Android/WorkSpaseForAS/TestLibjpeg/libjpeg/libs/${ANDROID_ABI}/libjpeg.so)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because system libraries are included in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib
              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )


# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in the
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       simple_gif

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib}
                       #jnigraphics这个是android下面的bitmap.h对应的库
                       jnigraphics)
