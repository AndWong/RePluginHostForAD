先简单的描述一下在广告方面遇到的问题.
开发一款App有了一定的用户量之后通常会想接入第三方广告来实现变现,
然而在很多市场不让这类带广告的App上架,除非接的是他们家的广告.

在这里我只能呵呵了.这点困难就想难倒我们.

那接下来ShowTime.怎么做呢?
没错,就是插件化.
以广点通广告为例
这里我使用的是360开源的[RePlugin](https://github.com/Qihoo360/RePlugin),具体介绍和使用方法请看官方文档.

一.[RePlugin插件接入指南](https://github.com/Qihoo360/RePlugin/wiki/%E6%8F%92%E4%BB%B6%E6%8E%A5%E5%85%A5%E6%8C%87%E5%8D%97)
第 1 步：添加 RePlugin Plugin Gradle 依赖
在项目根目录的 build.gradle（注意：不是 app/build.gradle） 中添加 replugin-plugin-gradle 依赖：
```
 buildscript {
    dependencies {
        classpath 'com.qihoo360.replugin:replugin-plugin-gradle:2.2.0'
        ...
    }
}
```
第 2 步：添加 RePlugin Plugin Library 依赖
在 app/build.gradle 中应用 replugin-plugin-gradle 插件，并添加 replugin-plugin-lib 依赖:
```
apply plugin: 'replugin-plugin-gradle'

dependencies {
    compile 'com.qihoo360.replugin:replugin-plugin-lib:2.2.0'
    ...
}
```
接下来您就可以像正常接入广告那样，开发插件。生成出来的是APK，既可以“安装到设备”，又可以“作为插件”使用。

二.[RePlugin主程序接入指南](https://github.com/Qihoo360/RePlugin/wiki/%E4%B8%BB%E7%A8%8B%E5%BA%8F%E6%8E%A5%E5%85%A5%E6%8C%87%E5%8D%97)
第 1 步：添加 RePlugin Host Gradle 依赖
在项目根目录的 build.gradle（注意：不是 app/build.gradle） 中添加 replugin-host-gradle 依赖：
```
buildscript {
    dependencies {
        classpath 'com.qihoo360.replugin:replugin-host-gradle:2.2.0'
        ...
    }
}
```
第 2 步：添加 RePlugin Host Library 依赖
在 app/build.gradle 中应用 replugin-host-gradle 插件，并添加 replugin-host-lib 依赖:
```
apply plugin: 'replugin-host-gradle'
repluginHostConfig {
    useAppCompat = true
}
dependencies {
    ...
    compile 'com.qihoo360.replugin:replugin-host-lib:2.1.7'
}
```
第 3 步：配置 Application 类
```
public class App extends Application{
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        RePlugin.App.attachBaseContext(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RePlugin.App.onCreate();
    }
}
```
三.[宿主App](https://github.com/AndWong/RePluginHostForAD/tree/master/app)调用[插件广告](https://github.com/AndWong/RePluginHostForAD/tree/master/pluginApp)
1.编译插件广告,将生成的xx.apk包重命名xx.jar
   将 xx.jar放到宿主App的 assets/plugins 目录下 , Replugin将会自动获取该内置插件

2.处理广点通开屏广告
由于广点通开屏广告的展示点击都由SDK封装处理了.
我们这里采用的方式是,由宿主跳转到插件的闪屏页,在插件中完成请求,展示,点击结束后回到宿主的主页面.
(1)宿主跳转到插件Activity
```
    try {
        String config = AssetsUtils.readText(MainActivity.this, "ad_config.json");
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("adPlugin", "com.plugin.ad.LogoActivity"));
        intent.putExtra("EXTRA_CONFIG", config);
        intent.putExtra("EXTRA_POI", POI_FIRST);
        RePlugin.startActivity(MainActivity.this, intent);
    } catch (Throwable e) {
        e.printStackTrace();
    }
```
(2)插件开屏广告请求处理,就按正常的广告逻辑走

(3)插件回到宿主的主页面
```
 private void intoMainPage() {
        //TODO 打开宿主应用
        Intent intent = new Intent();
        intent.setClassName("com.wifi.robot", "com.wifi.robot.ui.SecondActivity");
        startActivity(intent);
        finish();
    }
```
(4)宿主的清单文件中添加必要配置,否则广告无反应
```
  <!-- 广点通广告 -->
        <service
            android:name="com.qq.e.comm.DownloadService"
            android:exported="false" />
        <activity
            android:name="com.qq.e.ads.ADActivity"
            android:configChanges="keyboard|keyboardHidden|orientation|screenSize" />
```
注意 : 尽量使宿主和插件的包名一致,已避免广告无收益

3.处理广点通原生广告
广点通原生广告不同于开屏广告,其展示曝光和点击曝光都由自己处理.
我们只能通过反射的方案去请求广告

(1)在插件中先对广告请求做一层封装
```
package com.plugin.ad.managers;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.plugin.ad.listeners.ILoadListener;
import com.plugin.ad.models.ADModel;
import com.plugin.ad.models.ModelManager;
import com.plugin.ad.utils.JsonUtil;

/**
 * 广告请求管理类
 * Created by wong on 17-8-16.
 */
public class LoadManager {
    /**
     * 初始化
     *
     * @param config
     */
    private static void onInit(String config) {
        ADModel adModel = JsonUtil.getInstance().fromJson(config, ADModel.class);
        ModelManager.operationNativeConfig(adModel.place_ids);
        ModelManager.putAdKey("gdt", adModel.gdt_key);
    }

    /**
     * 请求原生广告
     *
     * @param context
     * @param config
     * @param poi
     * @param listener
     */
    public static void requestNativeAD(Context context, String config, int poi, final ILoadListener listener) {
        onInit(config);
        LoadGdtManager.loadAD(context,poi,listener);
    }
}

```
```
 /**
     * 请求广告
     *
     * @param context  必须是该插件的Context
     * @param poi
     * @param listener
     */
    public static void loadAD(Context context, final int poi, final ILoadListener listener) {
        NativeAD nativeAD = new NativeAD(context, getGdtKey(), getGdtId(poi), new NativeAD.NativeAdListener() {
            @Override
            public void onADLoaded(List<NativeADDataRef> list) {
                if (null != list && !list.isEmpty()) {
                    listener.gdtNativeSuccess(poi, list.get(0));
                } else {
                    listener.failure("Empty");
                }
            }

            @Override
            public void onNoAD(int i) {
                listener.failure("onNoAD->" + i);
            }

            @Override
            public void onADStatusChanged(NativeADDataRef nativeADDataRef) {
            }

            @Override
            public void onADError(NativeADDataRef nativeADDataRef, int i) {

            }
        });
        nativeAD.loadAD(1);
    }
```
(2)宿主中反射LoadManager的requestNativeAD()方法
a.拿到插件的ClassLoader
```
     ClassLoader classLoader = RePlugin.fetchClassLoader("adPlugin");
```
b.取得需要反射的类
```
    Class<?> methodClass = classLoader.loadClass("com.plugin.ad.managers.LoadManager");
```
c.由于请求广告的requestNativeAD()方法中有一个参数是接口.
(这里得使用动态代理)
取得被代理接口
```
            /**
             * 被代理的接口
             */
            Class<?> callBackClass = classLoader.loadClass("com.plugin.ad.listeners.ILoadListener");
            /**
             * 这个是动态代理
             * callBackClass : 需要被代理的接口的Class
             * proxListener : 返回的是这个被代理接口的实例
             */
            Object proxListener = LoadCallBackProx.getInstance(classLoader, callBackClass);
```
d.接下来就是反射请求接口了
```
            /**
             * callBackClass : 被代理的接口的Class
             * proxListener : 被代理接口的实例
             */
            Method load = methodClass.getDeclaredMethod("requestNativeAD", new Class[]{Context.class, String.class, int.class, callBackClass});
            load.invoke(null, RePlugin.fetchContext("adPlugin"), config, poi, proxListener);
```
注意传入的Context必须是插件的Context
e.在动态代理中取得回调
```
public class LoadCallBackProx implements InvocationHandler {
    /**
     * 这里的话直接去获取对象,
     * 把这个接口的字节码对象数组扔进来就可以了
     */
    public static Object getInstance(ClassLoader classLoader, Class<?> interfaces) {
        return Proxy.newProxyInstance(classLoader, new Class[]{interfaces}, new LoadCallBackProx());
    }

    /**
     * @param o
     * @param method  : 具体的方法名称
     * @param objects : 被代理类的回调方法参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        //判断是什么方法被调用了嘛
        String methodName = method.getName();
        if ("gdtNativeSuccess".equals(methodName)) {
            int poi = (Integer) objects[0];
            Object object = objects[1];
            ReAdFactory.putNativeAD(poi, object);
            EventHelper.post(new ReNativeAdEvent(poi, object, true));
        } else if (methodName.equals("failure")) {
            EventHelper.post(new ReNativeAdEvent(false));
        }
        return null;
    }
}
```
这里我使用了EventBus将回调的广告传到请求的界面中
```

    /**
     * 原生信息流
     * 插件广告请求
     *
     * @param event
     */
    @Subscribe
    public void onEventReADEvent(ReNativeAdEvent event) {
        if (event.isSucc) {
            //成功
            try {
                Object object = ReAdFactory.getNativeAD(POI_SECOND);
                //展示
                String title = (String) object.getClass().getMethod("getTitle").invoke(object);
                String iconUrl = (String) object.getClass().getMethod("getIconUrl").invoke(object);
                //反射调用曝光接口
                object.getClass().getMethod("onExposured", new Class[]{View.class}).invoke(object, findViewById(R.id.activity_main));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //失败
        }
    }
```
点击曝光的反射
```
    /**
     * 点击原生信息流广告
     */
    private void clickNativeAD() {
        try {
            Object object = ReAdFactory.getNativeAD(POI_SECOND);
            //调用点击曝光接口
            object.getClass().getMethod("onClicked", new Class[]{View.class}).invoke(object, findViewById(R.id.activity_main));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
```
四.最后,第一次写文章,欢迎点评

宿主App : https://github.com/AndWong/RePluginHostForAD/tree/master/app

插件App : https://github.com/AndWong/RePluginHostForAD/tree/master/pluginApp
