cmake_minimum_required(VERSION 3.4.1)

#输出详细信息
set(CMAKE_VERBOSE_MAKEFILE on)

#设置库目录
set(LIBRARY_DIRS “${CMAKE_SOURCE_DIR}/src/main/jniLibs”)

#包含头文件目录
include_directories(src/main/cpp/include)

#添加 opencv 的动态库

add_library( libopencv_java3 SHARED IMPORTED )

#指定库路径

set_target_properties( libopencv_java3 IMPORTED_LOCATION “${LIBRARY_DIRS}/${ANDROID_ABI}/libopencv_java3.so” )

#设置源文件目录
aux_source_directory(src/main/cpp/source SOURCE_DIR)

add_library( native-lib SHARED ${SOURCE_DIR} )

find_library( log-lib log )

target_link_libraries( native-lib libopencv_java3 ${log-lib} )