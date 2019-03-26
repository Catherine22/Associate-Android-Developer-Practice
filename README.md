# AAD-Preparation

# Navigation
- [Material design]         
- [Localization]      
- [Device compatibility]        
- [Lifecycle]       
- [App components]      
- [Jetpack]       


# Material design
## ```Toast``` vs ```Snackbar```       
|             | Toast                                                                | Snackbar            |
|-------------|----------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------|
| Overview    | A toast provides simple feedback about an operation in a small popup | Snackbars provide lightweight feedback about an operation                                                                                         |
| Interaction | Toasts automatically disappear after a timeout                       | Snackbar could either automatically disappear after a timeout, or be manually closed                                                           |
| More info   |                                                                      | 1. Having a CoordinatorLayout in your view hierarchy allows Snackbar to enable certain features 2. Snackbars can contain an action such as "undo"|
| Sample code | [BaseActivity]                                                       | [BaseActivity]      |


[Read more](https://developer.android.com/guide/topics/ui/notifiers/toasts)       
[Read more](https://developer.android.com/reference/android/support/design/widget/Snackbar)       


# Localization
List all resource directories you should take care of:       
1. animator/     
2. anim/     
3. color/        
4. drawable/     
5. mipmap/       
6. layout/       
7. menu/     
8. raw/      
9. values/       
10. xml/     
11. font/       


# Device compatibility
1. Create alternative UI resources such as layouts, drawables and mipmaps     
2. Set layout files with the following rules:       
    - Avoid hard-coded layout sizes by using ```wrap_content```, ```match_parent``` and ```layout_weight```, etc        
    - Prefer ```ConstraintLayout```     
    - Redraw views when window configuration changes (multi-window mode or screen rotation)        
3. Define alternative layouts for specific screen sizes. E.g. ```layout-w600dp``` and ```layout-w600dp-land``` for 7” tablets and 7” tablets in landscape representative        
4. Create stretchable nine-patch bitmaps        
5. Build a dynamic UI with fragments        
6. Test on all screen sizes     

ConstraintLayout example: []      
Fragments example: []     

[Read more](https://developer.android.com/training/multiscreen/screensizes)


## Pixel densities
Pixel density is how many pixels within a physical area of the screen, ```dpi``` is the basic unit.       

**dpi**: Dots per inch      
**resolution**: The total number of pixels on a screen      
**dp or dip**: Instead of px (pixel), measure UI with dp (density-independent pixels) on mobile devices       


## UI Spec
|               | ldpi     | mdpi     | hdpi     | xhdpi    | xxhdpi     | xxxhdpi    |
|---------------|----------|----------|----------|----------|------------|------------|
| Scaling ratio | 0.75x    | 1x       | 1.5x     | 2x       | 3x         | 4x         |
| Dpi           | ~120dpi  | ~160dpi  | ~240dpi  | 320dpi   | 480dpi     | 640dip     |
| App icon size | 36x36 px | 48x48 px | 72x72 px | 96x96 px | 144x144 px | 192x192 px |

> nodpi: bitmaps in nodpi drawables look larger in xhdpi devices whereas it seems smaller on mdpi devices.      
> anydpi: These bitmaps in anydpi have priority when no bitmaps are found in other drawable directories. For instance, we have ```drawable-hdpi/banner.9.png``` and ```drawable-anydpi/banner.xml```, ```banner.9.png``` will be used on hdpi devices and ```banner.xml``` will be seen on other devices.       

To see more details by automatically importing icons with Android Studio Image Asset tools and have a look at [Grid and keyline shapes]        
![screenshot](https://raw.githubusercontent.com/Catherine22/AAD-Preparation/master/screenshots/image-asset.png)  

```res``` directory example: [res]     

[Read more](https://developer.android.com/guide/practices/screens_support)        


# Lifecycle
[Read more](https://developer.android.com/guide/components/activities/activity-lifecycle)


## Saving UI states
1. ```ViewModel```      
2. ```onSaveInstanceState()```      
Have ```Do not keep activities``` be selected on system Settings page to test ```onSaveInstanceState``` and ```onRestoreInstanceState```       
Code: [LifecycleActivity]       
3. Persistent in local storage for complex or large data       

[Read more](https://developer.android.com/topic/libraries/architecture/saving-states.html)     


## Monitor lifecycle events via ```Lifecycle``` class in two ways:        
1. Implement both ```LifecycleObserver``` and ```LifecycleOwner```      
Code: [LifecycleActivity], [LifecycleObserverImpl]

2. Associate with Jetpack       

[Read more](https://developer.android.com/topic/libraries/architecture/lifecycle.html#java)

## Handle configuration changes     
1. Enable activities to handle configuration changes like screen rotation and keyboard availability change
```xml
<activity android:name=".MyActivity"
          android:configChanges="orientation|keyboardHidden"
          android:label="@string/app_name">
```

2. In activities        
```Java
 @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // do something 
    }
```
Code: [BaseActivity]        
[Read more](https://blog.csdn.net/zhaokaiqiang1992/article/details/19921703)        
[Read more](https://blog.csdn.net/zhaokaiqiang1992/article/details/19921703)        

# App components
There are four different types of app components:       
- Activity      
- Services      
- Broadcast receivers       
- Content providers     

[Read more](https://developer.android.com/guide/components/fundamentals)

## Activity

> Activity vs Fragment

## Services
Two services in Android - background service and foreground service     
Background services are no longer working since Android Oreo, you are suppose to use ```JobScheduler``` instead.      
Foreground services and JobScheduler are alternatives to run app in the background, but ```notification``` is required while running a foreground service.      

| Android API level | background service | foreground service | job scheduler |
| -- | -- | -- | -- |
| ≤ 25 | O | X | X |
| ≥ 26 | X | O | O |

Code: [AppComponentsActivity], [MusicPlayerService], [MusicPlayerJobScheduler], [AndroidManifest]       
[Read more](https://developer.android.com/guide/components/services)        


# Jetpack

[Material design]:<:<https://github.com/Catherine22/AAD-Preparation/blob/master/README.md#material-design>
[Localization]:<https://github.com/Catherine22/AAD-Preparation/blob/master/README.md#localization>
[Device compatibility]:<https://github.com/Catherine22/AAD-Preparation/blob/master/README.md#device-compatibility>
[res]:<https://github.com/Catherine22/AAD-Preparation/blob/master/app/src/main/res/
[Lifecycle]:<<https://github.com/Catherine22/AAD-Preparation/blob/master/README.md#lifecycle>
[Jetpack]:<<https://github.com/Catherine22/AAD-Preparation/blob/master/README.md#jetpack>

[BaseActivity]:<https://github.com/Catherine22/AAD-Preparation/blob/master/app/src/main/java/com/catherine/materialdesignapp/activities/BaseActivity.java>
[MainActivity]:<https://github.com/Catherine22/AAD-Preparation/blob/master/app/src/main/java/com/catherine/materialdesignapp/activities/MainActivity.java>
[LifecycleActivity]:<https://github.com/Catherine22/AAD-Preparation/blob/master/app/src/main/java/com/catherine/materialdesignapp/activities/LifecycleActivity.java>
[LifecycleObserverImpl]:<https://github.com/Catherine22/AAD-Preparation/blob/master/app/src/main/java/com/catherine/materialdesignapp/activities/LifecycleObserverImpl.java>
[MusicPlayerService]:<https://github.com/Catherine22/AAD-Preparation/blob/master/app/src/main/java/com/catherine/materialdesignapp/activities/MusicPlayerService.java>
[MusicPlayerJobScheduler]:<https://github.com/Catherine22/AAD-Preparation/blob/master/app/src/main/java/com/catherine/materialdesignapp/activities/MusicPlayerJobScheduler.java>
[AndroidManifest]:<https://github.com/Catherine22/AAD-Preparation/blob/master/app/src/main/AndroidManifest.xml>

[Grid and keyline shapes]:<https://material.io/design/iconography/#grid-keyline-shapes>


