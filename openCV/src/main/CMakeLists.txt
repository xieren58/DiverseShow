cmake_minimum_required(VERSION 3.4.1)

#支持-std=gnu++11
set(CMAKE_VERBOSE_MAKEFILE on)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++14")
set(CMAKE_CXX_VISIBILITY_PRESET hidden)

include_directories(${CMAKE_SOURCE_DIR}/cpp/include)

#编译头文件
#定义全局 my_source_path 变量
file(GLOB my_source_path ${CMAKE_SOURCE_DIR}/cpp/*.cpp ${CMAKE_SOURCE_DIR}/cpp/*.c)
add_library(openCV SHARED ${my_source_path})

add_library(lib_opencv SHARED IMPORTED)
set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/jniLibs/${ANDROID_ABI}/libopencv_java4.so)

# 在ndk中查找log库 取别名log-lib
find_library(log-lib log)

# 在ndk中查找jnigraphics库 取别名jnigraphics-lib
# jnigraphics包含图形操作的库
find_library(jnigraphics-lib jnigraphics)

#设置 target 需要链接的库
target_link_libraries(
        openCV
        lib_opencv
        ${jnigraphics-lib}
        ${log-lib})
