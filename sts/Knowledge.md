# Vue

## router

1.URL映射，将组件和URL对应起来，可以基于URL进行局部页面的处理
2.支持嵌套
3.可以在路由前后设置守卫，如权限验证，数据加载等等

## Vuex

1.Vuex是Vue.js生态圈中的一个状态管理工具,它采用集中式存储管理应用的所有组件的状态，并以相应的规则保证状态以一种可预测的方式发生变化。Vuex 通过使用单一状态树，让你可以直接定位任何一个状态片段，同时允许你更好地维护和调试你的应用。

1. `node_modules`: 这个文件夹包含了项目的所有依赖包，这些依赖是通过 npm（Node Package Manager）或 yarn 包管理器安装的。
2. `@public`: 这可能是一个别名，用于指向构建过程中生成的静态资源文件夹，通常是 `public` 文件夹。
3. `favicon.ico`: 网站的图标，通常显示在浏览器的标签页上。
4. `index.html`: 是应用程序的主 HTML 文件，作为所有 Vue 组件的入口点。
5. `src`: 源代码文件夹，包含了应用程序的主要 JavaScript、Vue、CSS 和资产文件。
6. `assets`: 存放静态资源文件，如图片、样式表、JavaScript 文件等。
7. `components`: 存放 Vue 组件的文件夹，组件是 Vue 应用的构建块。
8. `HomeView.vue`: 一个 Vue 单文件组件，通常用作应用程序的主页视图。
9. `UserLogin.vue`: 一个 Vue 单文件组件，用于用户登录界面。
10. `api.js`: 一个 JavaScript 文件，可能包含应用程序的 API 调用和数据处理逻辑。
11. `App.vue`: Vue 应用的根组件，作为所有其他组件的顶层容器。
12. `main.js`: 主 JavaScript 文件，用于初始化和运行 Vue 应用。
13. `router.js`: Vue 路由器配置文件，用于定义应用程序的页面路由。
14. `store.js`: Vuex 状态管理库的配置文件，用于在 Vue 应用中维护中央状态树。
15. `.gitignore`: 一个 Git 配置文件，定义了哪些文件或文件夹应该被 Git 忽略，不加入版本控制。
16. `babel.config.js`: Babel 配置文件，用于定义 JavaScript 代码转译的规则。
17. `jsconfig.json`: 配置 JavaScript 开发环境的文件，通常用于指定项目的源代码文件夹和排除文件夹。
18. `Knowledge.md`: 一个 Markdown 文件，可能包含项目的知识库或文档。
19. `package-lock.json`: npm 包锁定文件，记录了项目依赖的确切版本。
20. `package.json`: 项目的配置文件，定义了项目的依赖、脚本、版本等信息。
21. `README.md`: 项目的说明文件，通常包含项目的介绍、安装和使用说明。
22. `vue.config.js`: Vue CLI 项目的配置文件，用于自定义构建和开发过程。

#####  Axios

是一个HTTP请求库，用于在浏览器和Node.js中发送AJAX请求。

##### Token

在用户成功登录后，服务器会生成一个用于验证用户身份的令牌（token），并将其发送回客户端。这个令牌可以是一个字符串，通常是一个长随机字符串，用于标识用户身份和授权用户访问受保护的资源。

#####  HTTP请求方法
GET：用于请求从服务器检索特定资源。GET 请求应该只检索数据，并且是幂等的，意味着多次执行相同的 GET 请求，应能得到相同的结果，且不应改变服务器状态。

POST：用于向服务器提交数据以创建新资源。POST 请求通常用于表单提交，并且是非幂等的，因为多次发送相同的 POST 请求可能会在服务器上产生多个资源。

#####  POJO
POJO（Plain Old Java Object）是一个简单的 Java 对象，它是普通的 Java 类，不遵循特定的 Java 架构或模型，也不依赖于特定的 Java AP
##### JPA
JPA（Java Persistence API）是一个 Java 持久化规范，它定义了一组用于对象关系映射（ORM）的接口，允许开发者以面向对象的方式与数据库交互。JPA 注解是 Java 5.0 引入的特性，用于在 Java 类中以声明方式配置 JPA 的持久化信息。

@Entity：用于声明一个类是一个实体，即它映射到数据库中的一个表。
@Table：用于指定实体类对应的数据库表的名称。
@Id：用于声明实体类中的某个属性是主键。
@GeneratedValue：用于指定主键的生成策略（如自增、序列等）。
@Column：用于指定实体类属性与数据库表列的映射关系。
@ManyToOne / @OneToMany / @OneToOne / @ManyToMany：用于声明实体类之间的关联关系。
@JoinColumn：用于指定多对一或一对多关系中的外键列。
@Embedded：用于将一个实体类的属性内嵌到另一个实体类的属性中。
@Transient：用于声明实体类中的某个属性不是持久化的，即它不映射到数据库表的列。
@NamedQuery：用于定义一个预命名的查询，可以在应用程序中重复使用。

##### Bean

在Spring Boot中，"bean"是一个由Spring框架管理的Java对象。在Spring中，应用程序的组件（如服务、控制器、数据访问对象等）被称为bean。这些bean由Spring容器负责创建、装配和管理。

Spring Boot通过自动配置和约定大于配置的原则简化了Spring应用程序的开发。因此，在Spring Boot中，你可以使用各种注解（如@Component、@Service、@Repository等）将类标记为bean

