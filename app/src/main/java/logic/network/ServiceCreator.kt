package logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
TODO: 写一个完整的Retrofit类
        1.建立baseURL
        2.加入转化工厂Gson
        3.用Builder创建完之后, 写创建api接口实现类对象的create方法(用retrofit的create去实现)
        4.写内联函数防止泛型擦除
*/

object ServiceCreator {
    private const val BASE_URL =  "https://api.caiyunapp.com/v2.6"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    fun <T> create(apiService: Class<T>): T = retrofit.create(apiService)
    inline fun <reified T> create(): T = create(T::class.java)

    //因为有泛型擦除 所以要写内联函数让他变成一个入口 从而可以真正工作
    //inline 配 reified 破解泛型擦除就这个套路就对了
    //能用T::class.java这种写法 是因为没有泛型擦除了 上面Class<T>是因为根本不知道你是什么东西 只能告诉你哦我传了个类
    //最后使用: val ...  =  ServiceCreator.create<参数类>() 括号里自动传入参数类的::class.java

    /*
      学习笔记: T作为泛型的使用只是发挥占位符 / 对象的作用(因为存在泛型擦除) T不是一个类 Class<T>才是一个类

    * 那么为什么会返回T呢 因为引用数据类型 通常数据量大,结构复杂(干的活重),
      他们的值存储在对应的堆内存中 变量本身只指向对象的地址/引用(存储在栈中), 所以函数在返回引用数据类型时,返回的是对象的地址,通常就说返回了这个对象

    *还有基本数据类型, 他们干的活轻,设计的目的是高效和轻量, 值直接存储在栈内存中, 返回时就是返回的值本身

    */


}