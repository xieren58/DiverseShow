## DiverseShow
`note:`蕴含技术和乐趣

## 分支模型
* **dev**：默认分支 + 开发分支；    
* **master**：主分支，日常开发不涉及master分支，切勿向master分支提交代码；    

## 代码操纵规范
* 注意api兼容，保证其他同学代码顺利运行；    
* 代码自由发挥，包括修改代码框架，允许出现多套base；    

## 代码结构
* **app**： 壳module，非必要不允许添加代码在这个module；    

* **components**：组件module，公共层Module，包括： 
>- base: 基础框架封装；    
>- common：公共层封装，例如 util、widget等，命名：common-util等；    
>- api层：其它业务module的api，命名：api-feature等；    
    ....    
    
* **features**：业务module，里面可以新建和操作多个module，命名：ds-xxx格式   
    
## 代码使用     
#### aarrun     
* 如果觉得aarrun出现问题，可以使用`includeProjects=all`依赖所有模块，这样就不会涉及aarrun；      
* 新建模块时，将build.gradle尾缀加一个kts，将其它业务模块的脚本复制过来就行；   
* 体验aarrun：   
>- 1. 在`includeProjects=all`的情况下，使用`./gradlew assembleDebugForAar`命令将所有的module打aar包，可在`rootDir/aarrun`下查看；    
>- 2. 此时就可以`includeProject=模块名字`，再sync now，在Android模式下你就会发现没有申明的模块都不见了；

#### Transform与字节码修改
* 简单封装了一个使用ASM库的通用AsmTransform，支持增量编译
* 使用AsmTransform实现字节码修改将自己的逻辑使用ClassVisitor注入即可
