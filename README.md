# devutils v1.0版本 由三个工具类组成，分别为CrashUtils、DeviceUtils、LocationUtils#

**CrashUtils主要是手机应用崩溃日志，使用的时候需要注意以下：**</br>
  1.在项目的application里面进行初始化，代码如下</br>
  
    
    CrashUtils crashUtil=CrashUtils.getInstance();
    crashUtils.init(this);
    
    
  2.直接生成json字符串格式的crash日志，方便进行上传到服务器</br></br>
  
 **DeviceUtils主要的拿取手机的基本信息，安装应用信息，电话本通讯录，使用的时候需要注意以下：**</br>
    1.需要有必备的一些权限，如电话本，电话状态等等</br>
    2.getDeviceInfo获取设备基本信息，如手机品牌，手机型号。。。返回格式为json</br>
    3.getAllInfo获取设备基本信息包括用户安装的app应用，如包名，应用名称等等</br>
    4.getInstallApps获取用户安装的app应用</br>
    5.getAppIcon通过应用包名，获取应用logo，返回类型为Drawable</br>
    6.getContact获取手机通讯录，返回类型为json字符串</br></br>
    
  **LocationUtils主要是进行定位服务，集成的是高德地图，使用注意以下：**</br>
    1.要在高德地图开发者平台申请帐号以及创建应用，注意的是sha1值跟包名，得到appkey</br>
    2.在清单文件里面进行配置，代码如下：</br>
    
        
        <meta-data
            android:name="com.amap.api.v2.apikey"
            android:value="你申请的key" />
        <!-- 定位需要的服务 -->
        <service android:name="com.amap.api.location.APSService" />
       
        
    >3.在application里面进行初始化，代码如下：</br>
    
       
        LocationUtils.initLocation(this);
       
        
    >4.在Activity里面调取定位，该Activity要实现LocationUtils.LocationListener这个接口</br>
    
       
        LocationUtils.getLocation(this);
        
        
        ....</br>
        
        定位结果返回，结果为json字符串格式</br>
        
      
        @Override</br>
         public void onReceiveLocation(String location)</br>
          {</br>
            tv.setText(location);</br>
             LocationUtils.stopLoacation();</br>
           }</br>
        
