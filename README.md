# 一个简易的以彩云天气API为基础的天气预报系统
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
- **采用API接口**:  https://api.caiyunapp.com/v2.6/$Token/101.6656,39.2072/realtime
- **Token**: Y5H90JK8UASwnmel
- **本地存储**: Room
- **响应式编程**: LiveData, ViewModel, Coroutine
- **UI组件**: Material , RecyclerView

## 项目架构（MVVM） (代码实现: UI层 仓库层 ViewModel层)

### 1.ui(view视图层 , viewModel层)
- **作用**: 与用户交互 显示内容 
- **包含**:
            -**place** 显示地方
            -**weather** 显示当前天气
            -**ViewModel** UI层和Repository通信的桥梁 管理UI相关数据 习惯上写在一起     
            -**RecyclerView** 列表的逻辑实现以及子项布局         
- **特点**: 不包含任何业务逻辑 通过观察ViewModel的数据来更新UI展示内容 并将用户操作传递给ViewModel

### 2.logic(model数据模型层 , repository仓库层)
- **作用**: 负责数据的获取 处理 存储 
- **包含**: 
            - **dao**: 本地数据源 写给RoomDatabase的接口
            - **model**： 数据模型 定义数据结构 包括网络请求返回的json解析对象 和数据库的Entity
            - **network**： 网络数据源 用API从网络获取数据
            - **Repository**: 核心层 仓库层 判断从本地还是网络数据源获取数据 本地有缓存就本地 以上所有代码都为了实现仓库层对网络信息的获取