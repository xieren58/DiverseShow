package com.four.common_util.coroutines
import kotlinx.coroutines.*


/**
 * ## 协程 Profile
 * 协程，指的是"用户线程"，即在软件层面实现"线程"的概念，以下的协程均指Kotlin协程。
 *
 * 协程指的是一个异步开发框架，并且仅在代码编译期对相关的代码进行了解释，并未在JVM上对该框架作出支持。
 * 协程实际上仍然基于线程池（也有Android的Main线程），它最大的作用其实是减少实际开发中的"接口回掉"。
 * 正确地使用协程也能够一定程度地提升线程利用率（后面再解释），以此可以提升一丢丢性能。
 * 作用：减少接口回掉，某些情况下提升一丢丢性能～
 *
 * ### 协程挂起
 * Example1: 主线程计算数据
 *  通常做法: 开子线程 -> 计算数据 -> 回掉结果 -> 得到结果
 *  协程做法：开子线程同时创建了协程 -> 计算数据 -> 得到结果
 *
 * 关键在于主线程不应该挂起来等待子线程的计算结果，但是协程可以随意挂起，挂起协程来等待结果，就可以去除回掉步骤。
 * 要点: 协程挂起不影响当前线程的继续运行！！！
 *
 * Example2: 线程休眠1s
 *  通常做法: 开子线程 -> sleep(1000) -> 线程休眠结束
 *  协程做法: 开子线程同时创建了协程 -> delay(1000) -> 协程休眠结束
 *
 *  要点：协程delay的时候，当前线程仍然可以继续工作！！！
 *
 */
fun main() {

    println("start..")
    coroutinesDemo()
    println("end..")

    //防止程序退出
    Thread.sleep(3000)
}

fun coroutinesDemo() {
    //两种创建协程的方法，launch和async

    //Default: CPU线程池 IO: IO线程池
    println("启动一个协程")
    GlobalScope.launch(Dispatchers.Default) {
        //一般情况下Default应该改为Main，主线程（main程序没有主线程）

        println("网络请求开始")
        //withContext是suspend修饰的，是挂起函数，会挂起当前的协程
        //withContext的作用是：挂起当前的协程，直到自己的代码块执行完成
        val data = withContext(Dispatchers.IO) {
            //模拟网络请求
            Thread.sleep(1000)
            "12345"
        }
        println("网络请求结束，得到数据=$data")


        val deferred = GlobalScope.async(Dispatchers.IO) {
            //模拟网络请求
            Thread.sleep(1000)
        }
        //挂起当前的协程（父），直到自己对应的协程（子）执行结束
        deferred.await()

        //为什么使用挂起函数或者子协程，而不直接进行网络请求？
        //因为网络请求是IO操作，会直接阻塞当前线程（这里是CPU线程），因此另外使用一个IO线程上的协程进行网络请求
        //日常生活中，这里的CPU线程往往是主线程，因此更需要开辟一个IO线程上的协程来进行网络请求
    }

    //请注意，上面的异步数据请求，一个接口回掉都没有，这就是协程最大的优势！
}