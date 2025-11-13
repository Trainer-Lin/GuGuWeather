# GuGuWeather 基于恶俗企鹅的安卓天气预报APP

基于彩云天气API开发的轻量型安卓天气预报应用，采用 MVVM 架构模式，专为安卓开发学习者设计。应用支持全球城市天气查询、多维度气象数据展示，同时整合主流安卓技术栈，帮助开发者快速掌握企业级应用的分层开发思想。

## 🌟 功能特点

- 🌍 **全球城市搜索**：支持检索全球绝大多数国家/地区的城市数据，兼容中英文城市名称模糊查询，结果实时展示。
- 📊 **全面天气信息**：
    - 实时天气：温度、湿度、风向、风速、气压、能见度等核心数据；
    - 每日预报：未来7天天气趋势、最高/最低温、降水概率、日出日落时间；
    - 天气状态：通过图标直观展示晴、雨、雪、多云等天气类型。
- 🔄 **城市管理与切换**：支持添加多个常用城市，在天气详情页一键切换，本地持久化存储城市列表。
- 🔄 **手动刷新机制**：下拉刷新或点击刷新按钮，实时拉取最新气象数据，保证信息时效性。
- 🎨 **Material Design 风格**：遵循 Material Design 设计规范，界面简洁美观，适配不同屏幕尺寸。

## 🛠️ 技术栈

| 技术类别       | 具体技术/框架                     | 核心作用                                                                 |
|----------------|----------------------------------|--------------------------------------------------------------------------|
| 开发语言       | Kotlin                           | 安卓官方推荐语言，空安全、协程等特性提升开发效率                         |
| 架构模式       | MVVM（Model-View-ViewModel）     | 数据与UI解耦，提高代码可维护性和测试性                                   |
| 网络请求       | Retrofit + OkHttp                | 封装网络请求，处理API调用、响应拦截、错误处理                             |
| 本地存储       | Room                             | 轻量级ORM框架，实现城市列表、天气缓存的数据持久化                         |
| 响应式编程     | LiveData + ViewModel + Coroutine | 处理异步任务（网络请求、数据库操作），实现UI数据联动和生命周期感知         |
| UI组件         | Material Design、RecyclerView    | 构建美观交互界面，RecyclerView实现城市列表高效渲染                         |
| 天气数据来源   | 彩云天气API（Token：`Y5H90JK8UASwnmel`） | 提供全球城市搜索、实时/每日天气数据接口                                   |

## 🏗️ 项目架构（MVVM）

项目严格遵循 MVVM 分层思想，各层职责单一、依赖清晰，便于扩展和维护。

### 1. UI层（View + ViewModel）
- **核心职责**：用户交互、数据展示、操作转发，不包含业务逻辑。
- **组成模块**：
    - **View 组件**：
        - `MainActivity`：应用主入口，管理Fragment切换；
        - `PlaceFragment`：城市搜索界面，包含搜索框、RecyclerView城市列表；
        - `WeatherActivity`：天气详情展示界面，包含实时天气、每日预报、刷新控件；
        - 适配器：`PlaceAdapter`（城市列表RecyclerView适配器）、`DailyWeatherAdapter`（每日预报适配器）；
        - 全局配置：`SunnyWeatherApplication`（初始化Room数据库、Retrofit等）。
    - **ViewModel 组件**：
        - `PlaceViewModel`：管理城市搜索、城市列表数据，处理搜索逻辑；
        - `WeatherViewModel`：管理天气数据请求、缓存，转发刷新操作；
        - 核心作用：作为UI层与Repository的通信桥梁，通过LiveData向UI层暴露可观察数据。

### 2. Logic层（Model + Repository）
- **核心职责**：数据获取、处理、存储，封装业务逻辑。
- **组成模块**：
    - **Model 层**：
        - `dao`：`PlaceDao`（Room数据库操作接口，定义城市数据的增删改查方法）；
        - `model`：数据模型类，分为两类：
            - 网络模型：`PlaceResponse`（城市搜索API响应解析类）、`RealtimeResponse`（实时天气响应类）、`DailyResponse`（每日天气响应类）；
            - 实体类：`Place`（城市实体，Room数据库表映射）、`Weather`（天气实体，整合实时+每日数据）、`Sky`（天气状态枚举，映射图标与描述）；
        - `network`：网络请求封装：
            - `ServiceCreator`：Retrofit实例创建工具，配置BaseUrl、OkHttp客户端；
            - `PlaceService`/`WeatherService`：API接口定义，通过Retrofit注解声明请求方式；
            - `SunnyWeatherNetwork`：网络请求工具类，封装API调用方法。
    - **Repository 层**：
        - `PlaceRepository`：城市数据仓库，决策从本地Room缓存或网络API获取数据；
        - `WeatherRepository`：天气数据仓库，处理天气数据的请求、缓存更新逻辑；
        - 核心作用：为ViewModel提供统一的数据访问接口，屏蔽数据来源细节（本地/网络）。

## 🚀 快速开始

### 1. 开发环境要求
- 开发工具：Android Studio Arctic Fox 及以上版本；
- 最低SDK版本：Android 5.0（API Level 21）；
- 目标SDK版本：Android 13（API Level 33）；
- 构建工具：Gradle 7.0+。

### 2. 项目导入与配置
1. 克隆或下载项目源码到本地；
2. 用Android Studio打开项目，等待Gradle同步完成（自动下载依赖）；
3. 彩云天气API Token已内置（`Y5H90JK8UASwnmel`），无需额外配置；若Token失效，可替换为个人Token（修改 `network/ServiceCreator.kt` 中的API请求参数）。

### 3. 运行项目
1. 连接安卓设备或启动模拟器（建议API Level 24+）；
2. 点击Android Studio的「Run」按钮，等待应用安装并启动；
3. 操作流程：打开应用 → 搜索城市（如“北京”“New York”）→ 选择城市 → 查看天气 → 下拉刷新或切换城市。

## 📚 学习价值

- 掌握 **MVVM架构** 的实际落地：理解View、ViewModel、Model、Repository的职责与交互流程；
- 熟练使用 **Retrofit + OkHttp**：学习网络请求封装、API接口定义、JSON数据解析；
- 实践 **Room数据库**：掌握数据持久化、DAO接口设计、本地缓存策略；
- 理解 **响应式编程**：通过LiveData + ViewModel + Coroutine处理异步任务，实现UI与数据联动；
- 熟悉 **Material Design**：学习RecyclerView、下拉刷新、卡片布局等组件的使用；
- 培养 **分层开发思想**：体会代码解耦、职责单一的设计原则，为复杂应用开发打下基础。

## 📝 注意事项
1. 彩云天气API有请求频率限制，开发测试时避免频繁刷新；
2. 若城市搜索无结果，可能是API不支持该城市，可尝试搜索热门城市；
3. 应用需授予「网络权限」，否则无法获取天气数据；
4. 本地缓存仅保留最近查询的城市和天气数据，重启应用后仍可查看。

## 🔍 扩展方向（进阶学习）
- 增加「定位功能」：通过GPS获取当前城市，自动查询天气；
- 添加「小时预报」：调用彩云天气小时预报API，展示未来24小时天气；
- 优化UI交互：添加天气动画、夜间模式、温度趋势图表；
- 增加「数据缓存过期策略」：设置天气数据缓存时间，过期自动刷新；
- 集成「权限申请框架」：如EasyPermissions，优化权限申请体验。