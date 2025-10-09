# 一个简易的以彩云天气API为基础的天气预报系统(https://api.caiyunapp.com/v2/place?query=北京&token={token}&lang=zh_CN)
用于学习安卓开发

## 至少具备以下功能
可以搜索全球大多数国家的各个城市数据；
可以查看全球绝大多数城市的天气信息；
可以自由地切换城市，查看其他城市的天气；
可以手动刷新实时的天气。


## 技术栈
- **语言**: Kotlin
- **架构**: MVVM
- **网络**: Retrofit, OkHttp
- **本地存储**: Room
- **响应式编程**: LiveData, ViewModel, Coroutine
- **UI组件**: Material , RecyclerView

## 项目架构（MVVM）

### 1.ui(view视图层)
- **作用**: 与用户交互 显示内容
- **包含**:
            -**place** 显示地方
            -**weather** 显示当前天气
- **特点**: 不包含任何业务逻辑 通过观察ViewModel的数据来更新UI展示内容 并将用户操作传递给ViewModel

### 2.logic(model数据模型层)
- **作用**: 负责数据的获取 处理 存储 
- **包含**: 
            - **dao**: 本地数据源 写给RoomDatabase的接口
            - **model**： 数据模型 定义数据结构 包括网络请求返回的json解析对象 和数据库的Entity
            - **network**： 网络数据源 用API从网络获取数据 