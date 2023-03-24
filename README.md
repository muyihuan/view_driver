# 视图驱动器

设计文档：https://l4h83azj7r.feishu.cn/docx/KakjdwqxxoKjfixu8YgcHNiOnIt

## 一、目标
构建一个通用的View库/工具。
  1. View作为最终渲染的数据格式约定。
  2. 通过View来驱动数据加载、View渲染。
  3. 提供灵活的数据加载方式，支持注解、并发、缓存等。
  4. 可以满足个性化场景的加载渲染扩展。
  5. 可在编译期间检查用户编写是否存在问题。       
  6. 学习和使用成本低。
  
## 二、边界
#### 包含：
  1. 提供构建View驱动器的接口。
  2. 通过输入的View驱动加载数据，并进行View的构建、渲染。
#### 不包含：
  1. 定义View的内容和View的维护。
  2. 定义加载哪些Model数据以及Model加载的具体实现。

## 三、名词解释
  1. Model: 各业务系统内部的实体数据。
  2. View: 前端展示的内容或者页面都可抽象为视图，一个视图大多是由很多业务数据组拼转换出来。
  3. Model加载器: 通过接口或者本地方法获取Model数据。
  4. View渲染器: 根据加载好的Model数据，按照用户定义的规则赋值给View的内容，包含类型转换、脱敏、直接赋值等。
  5. View驱动器: 通过用户设置的View、Model加载器、View渲染器等进行Model数据的加载和View的构建。
  
## 四、待完善～
  1. 不支持Model的主键id既是主键也是外键id这种情况。
  2. 不支持不绑定getter但是View的子View存在两种查询方式的情况。
  3. 不支持继承关系的解析。
  4. 不支持注解方式。
  
  
