# AAD-Preparation

# Navigation
- [Material design]         
- [Localisation]      
- [Device compatibility]        
- [Lifecycle]       
- [App components]      
- [Jetpack]       
- [Kotlin]


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


## BottomSheet

# Localisation
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
Have ```Do not keep activities``` selected on system Settings page to test ```onSaveInstanceState``` and ```onRestoreInstanceState```       
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

## Broadcast receiver
You could either register receivers by dynamically extending ```BroadcastReceiver``` or statically declaring an implementation with the ```<receiver>``` tag in the AndroidManifest.xml

Code: [NetworkHealthService], [NetworkHealthJobScheduler], [InternetConnectivityReceiver]        
[Read more](https://developer.android.com/reference/android/content/BroadcastReceiver)       

## Content Provider
Create your own content providers to share data with other applications or access existing content providers in another applications.       

### System content providers
**In order to get the uri path, we are going to have a look at android source code.**       
1. Go to https://android.googlesource.com/platform/packages/providers/ and pick out needed providers        
2. Search ```<provider>``` tag in AndroidManifest, e.g. in https://android.googlesource.com/platform/packages/providers/UserDictionaryProvider/+/refs/tags/android-9.0.0_r33/AndroidManifest.xml        
```xml
<provider android:name="CallLogProvider"
            android:authorities="call_log"
            android:syncable="false" android:multiprocess="false"
            android:exported="true"
            android:readPermission="android.permission.READ_CALL_LOG"
            android:writePermission="android.permission.WRITE_CALL_LOG">
        </provider>
```
3. Now we have host name (```android:authorities```), then, go to [CallLogProvider] (```android:name```) to get the table name        
4. Search "urimatcher" in [UserDictionaryProvider], and have a bunch of ```sURIMatcher.addURI()``` found. We figure out that one url is "content://call_log/calls"        
5. Grant permission: ```<uses-permission android:name="android.permission.READ_CALL_LOG" />```, ```<uses-permission android:name="android.permission.WRITE_CALL_LOG" />```

**CRUD - Create**

**CRUD - Read**

**CRUD - Update**

**CRUD - Delete**

### User-defined content providers
A content provider uri should be ```scheme + authority + table + [id] + [filter]```. E.g. ```content://com.catherine.myapp/member/1/name```     



# Jetpack

# Kotlin
The exam is only available in Java at this time (4/1/2019)      
[Read more](https://kotlinlang.org/docs/reference/)     

1. [Basic Types]    
    - Bitwise operators     
    - == vs ===     
    - Numbers       
    - Characters      
    - Strings         
    - Array     
2. [Control Flow]     
    - ```when```      
    - ```if```        
    - ```while```       
3. [Returns and Jumps]     
    - ```break```       
    - ```xxx@ for``` or ```xxx@ while```     
    - ```return```      
4. [Classes and Inheritance]      
    - Class with multiple constructors      
    - Inheritance       
    - ```interface```     
    - ```override```      
    - ```inner class```     
    - ```super@xxx.f()```       
    - ```abstract class```      
5. [Properties and Fields]       
    - getter and setter      
    - ```lateinit```        
    - ```::```      
6. [Visibility Modifiers]     
    - ```open```        
    - ```public```      
    - ```internal```        
    - ```protected```       
    - ```private```     
7. [BaseClass], [BaseClassExtensions]       
    - Extension functions       
    - Extension properties      
    - Companion objects (which is similar to ```static```)     
    - Call extension functions of the base class declared other class       
    - Call functions both declared in the base class and self class inside extension functions (check [BaseClassExtensions])      
8. [Data Class]     
    - Properties declared in the primary constructor or class body      
    - Copying    
    - E.g. [User], [Employee]      
9. [enum and sealed class]             
    - enum vs sealed class      
10. [Generics]      
    - Declaration-site variance     
    - Type projections      
11. [Nested and Inner Classes]      
    - Nested Class      
    - Inner Class       
    - Anonymous Inner Class [NOT YET]     
12. [Enum classes]      
    - Basic usage of enum classes       
    - Another way to initialise the enum     
    - Enum constants can also declare their own anonymous classes       
    - Print all values of enum class        
13. [Objects]       
    - Object expressions - class        
    - Object expressions - class + interface        
    - Anonymous objects     
    - Object declarations (Singleton)       
    - Companion objects     
14. [Type aliases]      
    - Shorten types declaration     
15. [Inline classes]      
    - An inline class must have a single property initialised in the primary constructor        
    - Inline classes cannot have properties with backing fields, ie, your code would be:     
    ```Kotlin
    val length: Int
      get() = s.length
    ```
    - Representation: Inline classes could be as inline, generic, interface or nullable   
    - Inline classes vs type aliases    
    - Enable inline classes in Gradle:    
    ```Gradle
    compileKotlin {
        kotlinOptions.freeCompilerArgs += ["-XXLanguage:+InlineClasses"]
    }
    ```
16. [Delegation]    
  - Implementation by delegation    
  - ```by```    
  ```Kotlin
  // The last "b" (from ": Base by b") is implemented by the "b" in "Derived(b: Base)"
  class Derived(b: Base) : Base by b
  ```
  - Overriding functions and variables is optional    
17. [Delegated Properties]    
  - Declare standard Delegates(```Lazy```, ```Observable``` and storing properties in a Map) via ```by```
  - Implement properties including standard delegates once for all    
  - Local delegated properties    
  -     
18. [Kotlin Singleton] vs [Java Singleton]    
19. [Functions]   
  - Basic functions   
  - Functions with default arguments    
  - Override functions    
  - Lambda    
  - variable number of arguments (```vararg```)    
  - Unit-returning functions    
  - ```infix fun```       
20. Projections   
  - in-projections    
  - out-projections   
  - star-projections    
21. [Higher-Order Functions and Lambdas]    
  - Higher-Order Functions is a function that takes functions as parameters, or returns a function.   
  - Compare callbacks in Java and Kotlin (SAM for Kotlin classes)    
  - Lambda functions    
  - Pass functions as arguments to another function (```this::func```)    
  - Passing a lambda to the last parameter    
  - Implement a function type as an interface (You can either override ```invoke()``` or run ```invoke()```)   
  - ```invoke()```    
  - ```::```    
22. Inline Functions    
  




[Material design]:<:<https://github.com/Catherine22/AAD-Preparation/blob/master/README.md#material-design>
[Localisation]:<https://github.com/Catherine22/AAD-Preparation/blob/master/README.md#Localisation>
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
[InternetConnectivityReceiver]:<https://github.com/Catherine22/AAD-Preparation/blob/master/app/src/main/java/com/catherine/materialdesignapp/receivers/InternetConnectivityReceiver.java>
[NetworkHealthService]:<https://github.com/Catherine22/AAD-Preparation/blob/master/app/src/main/java/com/catherine/materialdesignapp/activities/NetworkHealthService.java>
[NetworkHealthJobScheduler]:<https://github.com/Catherine22/AAD-Preparation/blob/master/app/src/main/java/com/catherine/materialdesignapp/activities/NetworkHealthJobScheduler.java>

[Basic Types]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/BasicTypes.kt>
[Control Flow]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/ControlFlow.kt>
[Returns and Jumps]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/ReturnsAndJumps.kt>
[Classes and Inheritance]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/MyClass.kt>
[Properties and Fields]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/PropertiesAndFields.kt>
[Visibility Modifiers]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/package1/VisibilityModifiers.kt>
[BaseClass]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/BaseClass.kt>
[BaseClassExtensions]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/BaseClassExtensions.kt>
[Data Class]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/DataClass.kt>
[User]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/User.kt>
[Employee]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/Employee.kt>
[enum and sealed class]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/SealedClass.kt>
[Nested and Inner Classes]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/InnerClassExample.kt>
[Enum Classes]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/EnumClasses.kt>
[Objects]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/Objects.kt>
[Type aliases]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/TypeAliases.kt>
[Inline classes]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/InlineClass.kt>
[Delegation]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/Delegation.kt>
[Delegated Properties]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/DelegatedProperties.kt>
[Kotlin Singleton]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/singleton_kotlin/>
[Java Singleton]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/singleton_java/>
[Functions]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/Functions.kt>
[Higher-Order Functions and Lambdas]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/Lambdas.kt>

[CallLogProvider]:<https://android.googlesource.com/platform/packages/providers/ContactsProvider/+/refs/tags/android-9.0.0_r34/src/com/android/providers/contacts/CallLogProvider.java>
[Grid and keyline shapes]:<https://material.io/design/iconography/#grid-keyline-shapes>
