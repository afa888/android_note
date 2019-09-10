# Activity的四种启动模式应用场景

###一、Activity四种启动模式：
####(一)、基本描述
1.standard：标准模式：如果在mainfest中不设置就默认standard；standard就是新建一个Activity就在栈中新建一个activity实例；
2.singleTop：栈顶复用模式：与standard相比栈顶复用可以有效减少activity重复创建对资源的消耗，但是这要根据具体情况而定，不能一概而论；
3.singleTask：栈内单例模式，栈内只有一个activity实例，栈内已存activity实例，在其他activity中start这个activity，Android直接把这个实例上面其他activity实例踢出栈GC掉；
4.singleInstance :堆内单例：整个手机操作系统里面只有一个实例存在就是内存单例；

> 在singleTop、singleTask、singleInstance 中如果在应用内存在Activity实例，并且再次发生startActivity(Intent intent)回到Activity后,由于并不是重新创建Activity而是复用栈中的实例，因此Activity再获取焦点后并没调用onCreate、onStart，而是直接调用了onNewIntent(Intent intent)函数；

####(二)、Intent中标志位设置启动模式
在上文中的四种模式都是在mainfest的xml文件中进行配置的，GoogleAndroid团队同时提供另种级别更高的设置方式，即通过Intent.setFlags(int flags)设置启动模式；

1.FLAG_ACTIVITY_CLEAR_TOP : 等同于mainfest中配置的singleTask，没啥好讲的；
2.FLAG_ACTIVITY_SINGLE_TOP: 同样等同于mainfest中配置的singleTop;
3.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS: 其对应在AndroidManifest中的属性为android:excludeFromRecents=“true”,当用户按了“最近任务列表”时候,该Task不会出现在最近任务列表中，可达到隐藏应用的目的。
4.FLAG_ACTIVITY_NO_HISTORY: 对应在AndroidManifest中的属性为:android:noHistory=“true”，这个FLAG启动的Activity，一旦退出，它不会存在于栈中。
5.FLAG_ACTIVITY_NEW_TASK: 这个属性需要在被start的目标Activity在AndroidManifest.xml文件配置taskAffinity的值【必须和startActivity发其者Activity的包名不一样，如果是跳转另一个App的话可以taskAffinity可以省略】，则会在新标记的Affinity所存在的taskAffinity中压入这个Activity。

> 个人认为在上述Flag中FLAG_ACTIVITY_NEW_TASK是最为重要的一个flag，同时也需要注意的是网上有很多是瞎说的；而且也是个人唯一一个在实际开发中应用过的属性；先说说个人应用示例：
1.在Service中启动Activity；
2.App为系统Launcher时，跳转到微信无法退出时用到；
(四)、startActivity场景
Activity的启动模式的应用的设置是和它的开发场景有关系的，在App中打开新的Activity的基本上分为两种情况：

####(三)、startActivity场景
目标Activity是本应用中的Activity，即它的启动模式是可以直接在fanifest中配置或者默认为standard，任务栈也可以自己随意设置；
目标Activity是第三方App中的Activity，这个时候就需要先考虑打开新Activity的是和自己App放在同一任务栈中还是新的task中【这个是很重要的因为在Android的机制中：同一个任务栈中的activity的生命周期是和这个task相关联的[具体实例见下文]】，然后考虑Activity的启动模式； 所以Android提供了优先级更高的设置方式在Intent.setFlags(int flags),通过这setFlags就可以为打开第三方的App中Activity设置任务栈和启动模式了，具体设置就自己去看源码了。

###二、Activity四种启动模式常见使用场景：

二、Activity四种启动模式常见使用场景：
这也是面试中最为长见的面试题；当然也是个人工作经验和借鉴网友博文，如有错误纰漏尽请诸位批评指正；

LauchMode	| Instance
-------------------|-------------------              
standard	| 邮件、mainfest中没有配置就默认标准模式
singleTop	|登录页面、WXPayEntryActivity、WXEntryActivity 、推送通知栏
singleTask	|程序模块逻辑入口:主页面（Fragment的containerActivity）、WebView页面、扫一扫页面、电商中：购物界面，确认订单界面，付款界面
singleInstance	|系统Launcher、锁屏键、来电显示等系统应用
