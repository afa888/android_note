#apk安装包优化

######1.清理无用资源
>
> 在我们应用版本的迭代中，肯定有废弃的代码和资源，我们要及时地清理，来减小App体积，下面给出几种清理的方法。
>
> ###### (1).使用Refactor->Remove unused Resource
>
> 这个一键清除的小功能不是特别的又用，因为资源是经过反射或字符拼接等方式获取，所以检查不完全，需要我们不断的实验。
>
> 
>
> ![img](https://upload-images.jianshu.io/upload_images/5748654-8e0439bf866e6a33.png?imageMogr2/auto-orient/strip|imageView2/2/w/408/format/webp)
>
> image2.png
>
###### (2).使用Lint工具
>
> lint工具还是很有用的，它给我我们需要优化的电，这个在介绍工具的文章已经讲过，下面我只给出需要注意的点：
>
> - 检测没有用的布局并且删除
> - 把未使用到的资源删除
> - 建议String.xml有一些没有用到的字符也删除掉
>
###### (3).开启shrinkResources去除无用资源
>
> 在build.gradle 里面配置shrinkResources true，在打包的时候会自动清除掉无用的资源，但经过实验发现打出的包并不会，而是会把部分无用资源用更小的东西代替掉。注意，这里的“无用”是指调用图片的所有父级函数最终是废弃代码，而shrinkResources true 只能去除没有任何父函数调用的情况.

```bash
    android {
        buildTypes {
            release {
                shrinkResources true
            }
        }
    }
```

>###### (4).删除无用的语言资源
>
> 大部分应用其实并不需要支持几十种语言的国际化支持。比如我们只是保存中文支持：

```bash
    android {
        defaultConfig {
            resConfigs "zh"
        }
    }
```

>###### (5).清理第三方库中冗余代码
>
> 对于第三方库，可能我们只是用到库中的一个功能，那么我们就可以导入源码，并且删除无关的代码，来减小体积。

###### 2.图片资源优化
>
> 图片是占用空间比较大的资源，这是我们要重点优化的地方。
>
> ###### (1).使用压缩过的图片
>
> 这个在前面关于图片压缩已经讲过，这里就不再累赘。
>
> ###### (2).只用一套图片
>
> 对于绝大对数APP来说，只需要取一套设计图就足够了。从内存占用和适配的角度考虑，这一套图建议放在xhdpi文件夹下；
>
> ###### (3).使用不带alpha值的jpg图片
>
> 对于非透明的大图，jpg将会比png的大小有显著的优势，虽然不是绝对的，但是通常会减小到一半都不止。
>
> ###### (4).使用tinypng有损压缩
>
> 支持上传PNG图片到官网上压缩，然后下载保存，在保持alpha通道的情况下对PNG的压缩可以达到1/3之内，而且用肉眼基本上分辨不出压缩的损失.
>
> ###### (5).使用webp格式
>
> webp支持透明度，压缩比比jpg更高但显示效果却不输于jpg,从Android 4.0+开始原生支持，但是不支持包含透明度，直到Android 4.2.1+才支持显示含透明度的webp，使用的时候要特别注意。
>
> ###### (6).使用svg
>
> 矢量图是由点与线组成,和位图不一样,它再放大也能保持清晰度，而且使用矢量图比位图设计方案能节约30～40%的空间，现在谷歌一直在强调扁平化方式，矢量图可很好的契合该设计理念。
>
> - 占用存储空间小
> - 无极拉伸不会出现锯齿，可以照顾不同尺寸的机型
> - Android Studio自带很多资源
>
> ###### (7).使用shape
>
> 特别是在扁平化盛行的当下，很多纯色的渐变的圆角的图片都可以用shape实现，代码灵活可控，省去了大量的背景图片。
>
> ###### (8).使用着色方案
>
> 相信你的工程里也有很多selector文件，也有很多相似的图片只是颜色不同，通过着色方案我们能大大减轻这样的工作量，减少这样的文件。
>
> ###### (9).对打包后的图片进行压缩
>
> 使用7zip压缩方式对图片进行压缩,建议使用微信的[AndResGuard](https://github.com/shwenzhang/AndResGuard)

###### 3.资源动态加载
>
> 资源可以动态加载，减小apk体积。
>
> ###### (1).在线化素材库
>
> 如果你的APP支持素材库(比如聊天表情库)的话，考虑在线加载模式，因为往往素材库都有不小的体积
>
> ###### (2).皮肤加载
>
> 有的app用到皮肤库，这是就可以使用动态加载。
>
> ###### (3).模块插件化
>
> 如果模块过得，apk体积过大，可以考虑插件化，来减少体积。

###### 4.lib库优化
>只提供对主流架构的支持，比如arm，对于mips和x86架构可以考虑不支持，这样可以大大减小APK的体积.
>
>1. lib/：包含特定于处理器软件层的编译代码。该目录包含了每种平台的子目录，像armeabi，armeabi-v7a， arm64-v8a，x86，x86_64，和mips。大多数情况下我们可以只用一种armeabi-v7a，后面会讲到原因。
>2. armeabi-v7主要不支持ARMv5(1998年诞生)和ARMv6(2001年诞生).目前这两款处理器的手机设备基本不在我公司的适配范围（市场占比太少）。
>   而许多基于 x86 的设备也可运行 armeabi-v7a 和 armeabi NDK 二进制文件。对于这些设备，主要 ABI 将是 x86，辅助 ABI 是 armeabi-v7a。
>   最后总结一点：如果适配版本高于4.1版本，当然，如果armeabi-v7a不是设备主要ABI，那么会在性能上造成一定的影响。

###### 5.7zip压缩资源
>
> 对于assets或者raw文件夹中的资源，可以使用7zip压缩，使用时进行解压。

###### 6.代码混淆
>
> 在gradle使用minifyEnabled进行Proguard混淆的配置.

```bash
    android {
        buildTypes {
            release {
                minifyEnabled true
            }
        }
    }
```

> ###### 为什么代码混淆可以让apk变小?
>
> 1）可以删除注释和不用的代码。
> 2）将java文件名改成短名
> 3）将方法名改成短名

###### 7.资源(res)混淆
>
> 资源混淆简单来说希望实现将res/drawable/icon,png变成res/drawable/a.png,或我们甚至可以将文件路径也同时混淆，改成r/s/a.png。
> 建议使用微信的[AndResGuard](https://github.com/shwenzhang/AndResGuard)

###### 8.使用微信AndResGuard
>
> 使用微信AndResGuard对资源混淆并且压缩图片res等资源

```csharp
    apply plugin: 'AndResGuard'
    buildscript {
        dependencies {
            classpath 'com.tencent.mm:AndResGuard-gradle-plugin:1.1.7'
        }
    }
    andResGuard {
        mappingFile = null
        use7zip = true
        useSign = true
        keepRoot = false
        // add <your_application_id>.R.drawable.icon into whitelist.
        // because the launcher will get thgge icon with his name
        def packageName = <your_application_id>
                whiteList = [
        //for your icon
        packageName + ".R.drawable.icon",
                //for fabric
                packageName + ".R.string.com.crashlytics.*",
                //for umeng update
                packageName + ".R.string.umeng*",
                packageName + ".R.string.UM*",
                packageName + ".R.string.tb_*",
                packageName + ".R.layout.umeng*",
                packageName + ".R.layout.tb_*",
                packageName + ".R.drawable.umeng*",
                packageName + ".R.drawable.tb_*",
                packageName + ".R.anim.umeng*",
                packageName + ".R.color.umeng*",
                packageName + ".R.color.tb_*",
                packageName + ".R.style.*UM*",
                packageName + ".R.style.umeng*",
                packageName + ".R.id.umeng*"
        ]
        compressFilePattern = [
        "*.png",
                "*.jpg",
                "*.jpeg",
                "*.gif",
                "resources.arsc"
        ]
        sevenzip {
            artifact = 'com.tencent.mm:SevenZip:1.1.7'
            //path = "/usr/local/bin/7za"
        }
    }
```

###### 9.Facebook的redex优化字节码
>
> redex是facebook发布的一款android字节码的优化工具.
> [redex](https://github.com/facebook/redex)