## DiverseShow
`note:`蕴含技术和乐趣

## 分支模型
> **dev**：默认分支 + 开发分支；    
> **master**：主分支，日常开发不涉及master分支，切勿向master分支提交代码；    

## 代码操纵规范
> 注意api兼容，保证其他同学代码顺利运行；    
> 代码自由发挥，包括修改代码框架，允许出现多套base；    

## 代码结构
> **app**： 壳module，非必要不允许添加代码在这个module；    

> **components**：组件module，公共层Module，包括： 

>- base: 基础框架封装；    
>- common：公共层封装，例如 util、widget等，命名：common-util等；    
>- api层：其它业务module的api，命名：api-feature等；    
    ....    
    
> **features**：业务module，里面可以新建和操作多个module，命名：ds-feature等.   
