# AAD-Preparation

Associate Android Developer Study Guide   
https://developers.google.com/training/certification/associate-android-developer/study-guide/   

# Navigation
- [Material design]         
- [Localisation]      
- [Device compatibility]        
- [Lifecycle]       
- [App components]      
- [Working in the background]      
- [Notification]      
- [Accessibility Features]        
- [Day/Night Mode]      
- [Styles]       
- [Jetpack]    
- [Debugging]  
- [Test]  
- [Others]      
- [Kotlin]

# Material design
Add the material dependency in build.gradle
```gradle
implementation 'com.google.android.material:material:1.0.0'
```

- ```Toast``` vs ```Snackbar```       

|             | Toast                                                                | Snackbar            |
|-------------|----------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------|
| Overview    | A toast provides simple feedback about an operation in a small popup | Snackbars provide lightweight feedback about an operation                                                                                         |
| Interaction | Toasts automatically disappear after a timeout                       | Snackbar could either automatically disappear after a timeout, or be manually closed                                                           |
| More info   |                                                                      | 1. Having a CoordinatorLayout in your view hierarchy allows Snackbar to enable certain features 2. Snackbars can contain an action such as "undo"|
| Sample code | [BaseActivity]                                                       | [BaseActivity]      |


[Read more - Toasts](https://developer.android.com/guide/topics/ui/notifiers/toasts)       
[Read more - Snackbar](https://developer.android.com/reference/android/support/design/widget/Snackbar)       


## Layouts
- AppBarLayout -> [Any activity in this app      
- BottomSheet -> [ContentProviderFragment]      
- Chip -> [NotificationActivity]      
- ConstraintLayout -> [rv_album_item]      
- FloatingActionButton -> [MainActivity]      
- MaterialButton -> [BackgroundServiceFragment]      
- MaterialCardView -> [BackgroundActivity]      
- NavigationView -> [MainActivity]      
- RadioGroup -> [BackgroundActivity]      
- Seekbar (sliders) -> [BackgroundActivity]      
- TabLayout -> [AppComponentsActivity]      
- TextInputLayout, TextInputEditText -> [NotificationActivity]      
- RecyclerView (grid) -> [ArtistsFragment]   
- RecyclerView (linear) -> [AlbumsFragment]   
- RecyclerView (drag + swipe) -> [PlaylistFragment], [PlaylistAdapter], [RecyclerViewItemTouchHelper]    
- RecyclerView (SelectionTracker) -> [ArtistsFragment], [ArtistAdapter], [ArtistItemKeyProvider], [ArtistItemDetailsLookup]    
  ```SelectionTracker``` is another to do ```setOnClickListener```, which is more powerful while multiple items need selecting   
- BottomNavigationView -> [UIComponentsActivity]    
- TabLayout + ViewPager -> [MusicFragment], [UIComponentsActivity]    
- SearchView -> [SearchSongsActivity], [AndroidManifest]     

### SearchView
Believe if or not, a SearchView could be far more complicated than you've expected.   
Before we get started, let's take a look at some features of SearchView:   
- Search Interface    
- Query suggestion (either recent searches or custom suggestions)    
- Query history   
- Searchable configuration (E.g. voice search)    

[Read more](https://developer.android.com/guide/topics/search)    

First thing first, you need to create either Search Dialog (a SearchView inside NavigationView) or Search Widget (your custom search view, which could be an EditText placed anywhere in your layout).     

Secondly, think about how you handle the search view. Here is my workflow:        
1. Start the ```SearchSongsActivity```       
2. User types something in ```SearchView``` and press enter        
3. Start the ```SearchSongsActivity``` again        
4. Handle searches in ```onNewIntent()```      

You need the following:      
1. Add tags to your result activity in AndroidManifest.xml
```xml
<activity
    android:name=".activities.SearchSongsActivity"
    android:launchMode="singleTop">

    <intent-filter>
        <action android:name="android.intent.action.SEARCH" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
    </intent-filter>

    <intent-filter>
        <action android:name="com.google.android.gms.actions.SEARCH_ACTION" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
    </intent-filter>

     <meta-data
        android:name="android.app.searchable"
        android:resource="@xml/searchable" />
</activity>
```

2. In xml/searchable.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<searchable xmlns:android="http://schemas.android.com/apk/res/android"
    android:hint="@string/search_hint"
    android:label="@string/app_name"
    android:searchSuggestAuthority="com.catherine.materialdesignapp.providers.SearchSuggestionProvider"
    android:searchSuggestSelection=" ?"
    android:voiceSearchMode="showVoiceSearchButton|launchRecognizer" />
```

> ```android:searchSuggestAuthority"```: (Optional) Refer to your search content provider     
> ```android:searchSuggestSelection"```: (Optional) Pop up search suggestions, " ?" means query    
> ```android:voiceSearchMode"```: (Optional) To enable voice search    
> Read more search configuration here: https://developer.android.com/guide/topics/search/searchable-config       


(Optional) Create your search suggestion content provider       
```Java
public class SearchSuggestionProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "com.catherine.materialdesignapp.providers.SearchSuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES | DATABASE_MODE_2LINES;

    public SearchSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
```

And register your content provider in AndroidManifest.xml
```xml
<provider
    android:name=".providers.SearchSuggestionProvider"
    android:authorities="com.catherine.materialdesignapp.providers.SearchSuggestionProvider" />
```

3. Handle intents and create a search icon in your activity
```Java
public class SearchSongsActivity extends AppCompatActivity {  
    private final static String TAG = SearchSongsActivity.class.getSimpleName();  
    private SearchManager searchManager;
    private SearchView searchView;

  /**
     * In this case, this onNewIntent will be called while
     * user finishes searching, this activity will be relaunch.
     * <p>
     * Because
     * 1. this activity launches in single top/task/instance mode
     * 2. ACTION_SEARCH is defined in intent-filter
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent == null)
            return;
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            // Handle the scenario that user submitted searches:
            // 1. Fill in what user just typed in SearchView automatically
            // 2. Dismiss search suggestions
            // 3. Query
            // 4. Save queries
            searchView.setQuery(query, false);
            searchView.clearFocus();
            query(query);
            saveQueries(query);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.searchable_menu, menu);

        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:

                // clear query history
                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                        SearchSuggestionProvider.AUTHORITY, SearchSuggestionProvider.MODE);
                suggestions.clearHistory();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void query(String text) {
        // do something
    }

    // save queries while you've defined a search content provider.
    private void saveQueries(String text) {
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                SearchSuggestionProvider.AUTHORITY, SearchSuggestionProvider.MODE);
        suggestions.saveRecentQuery(text, null);
    }
}
```

4. In menu/ui_components_menu.xml    
```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <item
        android:id="@+id/action_search"
        android:actionLayout="@layout/searchview_layout"
        android:icon="@drawable/ic_search_black_24dp"
        android:title="@string/action_search"
        app:actionViewClass="androidx.appcompat.widget.SearchView"
        app:showAsAction="ifRoom|collapseActionView" />
</menu>
```
> ```app:showAsAction="collapseActionView"```: Click the search icon and stretch the view   
> ```setIconified(false)```: Always show the search field       


## Custom Layouts
Build a custom view from scratch:       
1. [Slider](https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/components/Slider.java)        
2. [attrs](https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/res/values/attrs.xml)      
3. [BackgroundServiceFragment], [fragment_background_service](https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/res/layout/fragment_background_service.xml)         


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

## Create your own resources for specific needs
E.g. 
Language and region: ```en```, ```en-rUS```     
Layout Direction: ```ldrtl```, ```ldltr```      
HDR: ```highdr```, ```lowdr```      
Screen Orientation: ```port```, ```land```      
UI mode: ```car```, ```television```, ```watch```...        
Nigh mode: ```night```, ```notnight```      
DPI: ```ldpi```, ```mdpi```...      
Platform version: ```v21```, ```v26```...       
...     

Go to https://developer.android.com/guide/topics/resources/providing-resources.html#AlternativeResources and check **table 2**


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
![screenshot](https://raw.githubusercontent.com/Catherine22/AAD-Preparation/master/AAD-Preparation/screenshots/image-asset.png)  

```res``` directory example: [res]     

[Read more](https://developer.android.com/guide/practices/screens_support)        

## To support right-to-left layouts
1. Target API 17 or higher       
2. In Manifest,     
```xml
<application
    android:supportsRtl="true">
</application>
```

# Lifecycle
[Read more](https://developer.android.com/guide/components/activities/activity-lifecycle)


## Saving UI states
1. ```ViewModel```      
2. ```onSaveInstanceState()```      
To test ```savedInstanceState```, have ```Do not keep activities``` selected on system Settings page to test ```onSaveInstanceState``` and ```onRestoreInstanceState```       
Code: [LifecycleActivity]       
3. Persistent in local storage for complex or large data       

[Read more](https://developer.android.com/topic/libraries/architecture/saving-states.html)     


## Monitor lifecycle events via ```Lifecycle``` class in two ways:        
1. Implement ```LifecycleObserver```        
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
        
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d(TAG, "landscape");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d(TAG, "portrait");
        }
    }
```
Code: [BaseActivity]        
[Read more](https://blog.csdn.net/zhaokaiqiang1992/article/details/19921703)        

## PictureInPicture and Multi-window        
Picture in picture mode is a special case of multi-window mode.     


# App components
There are four different types of app components:       
- Activity      
- Services      
- Broadcast receivers       
- Content providers     

[Read more](https://developer.android.com/guide/components/fundamentals)

## Activity

### 4 Launch Modes
```xml
<activity
    android:name=".SingleTaskActivity"
    android:launchMode="singleTask">
```

Let's say we have 4 activities: A, B, C and D       

1. standard     
- Default mode, it pushes new activities on the top of the stack.     
- Example (how activities work in the stack): (bottom) A-B-B-D-A-C-C (top)      

2. singleTop        
- No duplicate activities on the top, but there could be same activities in the stack.     
- Example (how activities work in the stack): (bottom) A-B-A-C-D-C (top)        
- Let's say C is the top activity, and you try to launch C again. Then this C won't be created, instead, ```onNewIntent()``` will be called in existed C.       

3. singleTask
- No duplicate activities in the stack.      
- Example (how activities work in the stack): (bottom) A-B-C-D (top)        
- Let's say C is in the stack, and you try to launch C again. Then this C won't be created, instead, ```onNewIntent()``` will be called in existed C.       
- Don't forget to set taskAffinity (```android:taskAffinity="your packageName"```) to map the activity to app.

4. singleInstance
- ```singleTask``` + ```taskAffinity``` is more recommended.
- No duplicate activities in the system, which means if there is A running in App1, then we lunch A in App2, this running A in App1 will be killed.       
- Example (how activities work in the stack): (bottom) A-B-C-D (top)        
- Let's say C is in the stack, and you try to launch C again. Then this C won't be created, instead, ```onNewIntent()``` will be called in existed C.       
- You could set taskAffinity (```android:taskAffinity="your packageName"```) as well


NOTICE: You might need to handle both ```onCreate()``` and ```onNewIntent()``` lifecycle events in ```singleTop```

## Fragment
1. To build a multi-pane UI   
2. To reuse fragments in multiple activities.   
3. A fragment must always be hosted in an activity and the fragment's lifecycle is directly affected by the host activity's lifecycle.    


Code (Activity + Fragment): [UIComponentsActivity]   
Code (ViewPager + Fragment): [MusicFragment]    
[Read more](https://developer.android.com/guide/components/fragments)

## Services
- Two services in Android - background service and foreground service     
- Background services are no longer working since Android Oreo, you are suppose to use ```JobScheduler``` instead.      
- Foreground services and JobScheduler are alternatives to run app in the background, but ```notification``` is required while running a foreground service.      
- ```JobScheduler``` is only available on devices running API 21+. Fore devices running API 14+ including devices without Google Play serivces, ```WorkManager``` let you schedule background tasks that need guaranteed completion,  whether or not the app process is running.

| Android API level | background service | foreground service | job scheduler |
| -- | -- | -- | -- |
| ≤ 25 | O | X | X |
| ≥ 26 | X | O | O |


### ```JobScheduler``` TIPS:
1. Because setting this property is not compatible with persisted jobs, doing so will throw an IllegalArgumentException when ```JobInfo.Builder.build()``` is called.   
2. ```jobScheduler.cancel(JOB_ID)``` or ```jobScheduler.cancelAll()``` only works while jobs haven't started. For example, a job is scheduled to start in 5 seconds (```setMinimumLatency(5000)```), ```cancel()``` works right before the job actually runs.    
3. Don't forget to finish jobs if the task is done. (```jobFinished(jobParameters, false)```)

Code: [BackgroundServiceFragment], [MusicPlayerService], [MusicPlayerJobScheduler], [AndroidManifest]       
[Read more](https://developer.android.com/guide/components/services)        

## Broadcast receiver
You could either register receivers by dynamically extending ```BroadcastReceiver``` or statically declaring an implementation with the ```<receiver>``` tag in the AndroidManifest.xml.        

NOTICE, since Android 8.0 (API level 26), some receivers cannot be defined in Manifest, check the list [here](https://developer.android.com/guide/components/broadcast-exceptions.html)        

In most cases, you should follow the stops:     
1. Check available receivers [here](https://chromium.googlesource.com/android_tools/+/refs/heads/master/sdk/platforms/android-28/data/broadcast_actions.txt)        
2. Create your own receivers        
Code: [ScreenOnReceiver], [AirplaneModeChangedReceiver]     
3. Let services or jobs to filter system intents        
Code: [AirplaneModeService], [AirplaneModeJobScheduler], [ScreenOnOffService], [ScreenOnOffJobScheduler]        
4. Ask for permission if needed     
5. Register services or schedule jobs (Android 5.0+)        

### Implicit broadcast exceptions
These broadcasts can continue to register receivers in manifest.     


Code: [SystemBroadcastReceiverFragment]       
[Read more](https://developer.android.com/reference/android/content/BroadcastReceiver)       

## Content Provider
Create your own content providers to share data with other applications or access existing content providers in another applications.       

### System content providers
**In order to get the uri path, we are going to have a look at android source code.**       
1. Go to https://android.googlesource.com/platform/packages/providers/ContactsProvider/+/master/ and pick out needed providers      
2. Search ```<provider>``` tag in [AndroidManifest](https://android.googlesource.com/platform/packages/providers/ContactsProvider/+/master/AndroidManifest.xml), e.g.       
```xml
<provider 
    android:name="CallLogProvider"
    android:authorities="call_log"
    android:syncable="false" android:multiprocess="false"
    android:exported="true"
    android:readPermission="android.permission.READ_CALL_LOG"
    android:writePermission="android.permission.WRITE_CALL_LOG">
</provider>
```
To read call logs, ask for ```READ_CALL_LOG``` permission.      
3. Now we have the host name (```android:authorities```), then go to [CallLogProvider], which refers to ```android:name```, and get the table name        
4. Search ```UriMatcher``` in [CallLogProvider], you will find a bunch of ```sURIMatcher.addURI()```.       
```Java
static {
    sURIMatcher.addURI(CallLog.AUTHORITY, "calls", CALLS);
    sURIMatcher.addURI(CallLog.AUTHORITY, "calls/#", CALLS_ID);
    sURIMatcher.addURI(CallLog.AUTHORITY, "calls/filter/*", CALLS_FILTER);
    // Shadow provider only supports "/calls".
    sURIMatcher.addURI(CallLog.SHADOW_AUTHORITY, "calls", CALLS);
}
```

5. The completely path would be: scheme (content://) + table_name (authorities) + path (defined uriMatcher)      
```Java
private final static Uri callLogsDB = Uri.parse("content://call_log/calls");
```
6. Fetch call logs      
You are able to query call logs, register observers to listen to fetch events like incoming calls, outgoing calls, etc.     
Check all fields you can query: [CallLogProvider], [CallLog](https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/provider/CallLog.java)       

Code: [CursorLoaderActivity]

### User-defined content providers
A content provider uri should be ```scheme + authority + table + [id] + [filter]```. E.g. ```content://com.catherine.myapp/member/1/name```     

ContactsProvider + CallLogs code: [ContentProviderFragment]

### Define your own content provider
code: [AlbumsProvider]      

Query examples:     
1. Query all of Taylor Swift's albums and return ```title``` and ```url```
```Kotlin
val ALBUM_URI = Uri.parse("content://com.catherine.materialdesignapp.providers.AlbumsProvider/albums")
var cursor: Cursor? = null
try {
    cursor = contentResolver.query(
            ALBUM_URI,
            arrayOf("title, url"),
            "artist = ?",
            arrayOf("Taylor Swift"),
            null)
    Log.d(TAG, "cursor items:" + cursor?.count)
    if (cursor != null)
        while (cursor.moveToNext()) {
            for (i in 0 until cursor.columnCount) {
                Log.d(TAG, "${cursor.getColumnName(i)}: ${cursor.getString(i)}")
            }
        }
} catch (e: Exception) {
    e.printStackTrace()
} finally {
    cursor?.close()
}
```

2. Query the ```_id = 3``` item
```Kotlin
val ALBUM_URI = Uri.parse("content://com.catherine.materialdesignapp.providers.AlbumsProvider/albums/3")
var cursor: Cursor? = null
try {
    cursor = contentResolver.query(
            ALBUM_URI,
            null,
            null,
            null,
            null)
    Log.d(TAG, "cursor items:" + cursor?.count)
    if (cursor != null)
        while (cursor.moveToNext()) {
            for (i in 0 until cursor.columnCount) {
                Log.d(TAG, "${cursor.getColumnName(i)}: ${cursor.getString(i)}")
            }
        }
} catch (e: Exception) {
    e.printStackTrace()
} finally {
    cursor?.close()
}
```

3. Insert a new item
```Kotlin
val values = ContentValues()
values.put("title", "Reputation")
values.put("url", "https://www.amazon.com/reputation-Taylor-Swift/dp/B0754BBDF1/ref=ntt_mus_dp_dpt_1")
values.put("image", "https://images-na.ssl-images-amazon.com/images/I/81Ii6u2neLL._SX522_.jpg")
values.put("thumbnail_image", "https://images-na.ssl-images-amazon.com/images/I/71PcXiIap4L.jpg")
values.put("artist", "Taylor Swift")
val songList = arrayListOf(
    "...Ready for It?",
    "End Game",
    "I Did Something Bad",
    "Don't Blame Me",
    "Delicate",
    "Look What You Made Me Do"
)
values.put(
    "songs", fromStringList(songList)
)
try {
    contentResolver.insert(
        ALBUM_URI,
        values
    )
} catch (e: Exception){
    e.printStackTrace()
}
```
```Kotlin
private fun fromStringList(value: List<String>?): String {
    if (value == null || value.isEmpty()) {
        return JSONArray().toString()
    }
    val ja = JSONArray()
    for (i in value.indices) {
        ja.put(value[i])
    }
    return ja.toString()
}
```

4. Update "Speak Now"
```Kotlin
var values = ContentValues()
var songList = arrayListOf("Mine", "Innocent", "Speak Now")
values.put("songs", "'" + fromStringList(songList) + "'")
try {
    contentResolver.update(
        ALBUM_URI,
        values,
        "title = ?",
        arrayOf(
            "Speak Now"
        )
    )
} catch (e: Exception){
    e.printStackTrace()
}
```

5. Delete the ```_id = 3``` item
```Kotlin
val ALBUM_ID_URI = Uri.parse("content://com.catherine.materialdesignapp.providers.AlbumsProvider/albums/3")
try {
    contentResolver.delete(ALBUM_ID_URI, null, null)
} catch (e: Exception){
    e.printStackTrace()
}
```

6. Delete Taylor Swift's Reputation album
```Kotlin
try {
    contentResolver.delete(ALBUM_URI, "artist = ? AND title = ?", arrayOf("Taylor Swift", "Reputation"))
} catch (e: Exception) {
    e.printStackTrace()
}
```


# Working in the background
Tasks on a background thread using ```AsyncTask``` (for short or interruptible tasks) or ```AsyncTaskLoader``` (for tasks that are high-priority, or tasks that need to report back to the user or UI).   

## AsyncTask
- run on UI thread: ```onPreExecute```, ```onProgressUpdate``` and ```onPostExecute```    
- update progress to UI via ```publishProgress```, handle data in ```onProgressUpdate```   
- ```WeakReference```   
- ```executeOnExecutor```      

Code: [BackgroundActivity], [SleepTask]   

## AsyncTaskLoader    
When you want the data to be available even if the device configuration changes, use ```loaders```    
This ```getLoaderManager()``` or ```getSupportLoaderManager()``` is deprecated since Android P. Instead, we use ```ViewModels``` and ```LiveData```   

- Call ```getLoaderManager()``` or ```getSupportLoaderManager()``` depends on whether you use ```Support Library```   
- ```initLoader``` is supposed to be called in ```onCreate()```   
- Call ```restartLoader``` is equivalent to ```initLoader```, but ```initLoader``` only works at the first time.    
- ```onLoadFinished``` is run on background thread, this may cause memory leak if updating UI here - use at your own risk.    

Code: [BackgroundActivity], [SleepTaskLoader]   

## ViewModels and LiveData

# Notification
Three style of notifications:   
1. Standard notification    
2. Notification with actions (one or two buttons)   
3. Replying notification

## Notification dots (Badge)
Long click app icons on Android O+ devices, notification badge will pop up.    

## Notification Channels
Classify notifications by channels in the Settings app on Android O+ devices.    


Code: [NotificationActivity]    
[Read more](https://codelabs.developers.google.com/codelabs/android-training-notifications/index.html?index=..%2F..android-training#0)

# Accessibility Features
1. Set ContentDescription       
2. Make the views focusable     


# Day/Night Mode
1. Define your own style in styles.xml, notice your style must extend whatever styles contain ".DayNight" keyword.      
```xml
<style name="AppTheme.NoActionBar" parent="Theme.MaterialComponents.DayNight.NoActionBar">
    <item name="windowActionBar">false</item>
    <item name="windowNoTitle">true</item>
    <item name="colorPrimary">@color/colorPrimary</item>
    <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
    <item name="colorAccent">@color/colorAccent</item>
</style>
```
or
```xml
<style name="AppTheme" parent="AppTheme.DayNight">
    <item name="colorPrimary">@color/colorPrimary</item>
    <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
    <item name="colorAccent">@color/colorAccent</item>
</style>
```

2.
In Manifest, update the theme
```xml
android:theme="@style/AppTheme.NoActionBar"
```

3. Initialise night mode programmatically if you want
```Java
public class MainActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initNightMode();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // We set the theme, immediately in the Activity’s onCreate()
    private void initNightMode() {
        Storage storage = new Storage(this);
        int nightMode = storage.retrieveInt(Storage.NIGHT_MODE);
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
```

4. Switch day/night mode programmatically if you want
```Java
SharedPreferences sharedPreferences = getSharedPreferences("main", Context.MODE_PRIVATE);
button.setOnClickListener(
    v -> {
        int nightMode = AppCompatDelegate.getDefaultNightMode();
        if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            button.setTitle(getString(R.string.action_night_mode));
            sharedPreferences.edit().putInt("night_mode", AppCompatDelegate.MODE_NIGHT_NO).apply();
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            item.setTitle(getString(R.string.action_day_mode));
            sharedPreferences.edit().putInt("night_mode", AppCompatDelegate.MODE_NIGHT_YES).apply();
        }
        // Recreate the activity for the theme change to take effect.
        recreate();
    }
);
```

5. Notice, in step 4, the ```recreate()``` will recreate the Activity only, which means, other activities won't update simultaneously. They are still Day Mode. To update the whole app to Night Mode, there are two points:        
    - Create your own Night Mode resources if needed. E.g. ```layout-night```       
    - Manage options menu by your base activity     

Code: [BaseActivity]
[Read more](https://www.youtube.com/watch?v=1by5J7c5Vz4)       

# Styles
To inherit styles from Android support library by using ```parent```
```xml
<style name="GreenText" parent="TextAppearance.AppCompat">
    <item name="android:textColor">#00FF00</item>
</style>
```

And to inherit user-defined styles, you could use a dot notation
```xml
<style name="GreenText.Large">
    <item name="android:textSize">22dp</item>
</style>
```

Code: [styles](https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/res/values/styles.xml)      


# Jetpack
[Video](https://www.youtube.com/watch?v=7p22cSzniBM)        
[Guide](https://developer.android.com/jetpack/docs/guide)

## Room
A robust SQLite object mapping library. 

## WorkManager
WorkManager providers APIs for deferrable, one-off and recurring background tasks that need guaranteed execution 

## ViewModel
ViewModel is constructed when app configuration changes such as screen rotation

## LiveData
LiveData is an observable data holder for the data is meant to be shown on screens.

## Paging
Paging library integrates directly with endless data

## Navigation
Navigation library simplifies implementation of complex but common navigation requirements.

Go to [google tutorial](https://codelabs.developers.google.com/codelabs/android-training-livedata-viewmodel/index.html?index=..%2F..android-training#0) to see how ```Room``` + ```ViewModel``` + ```LiveData``` works.     

Code(Room + ViewModel + LiveData): [PlaylistFragment], [AlbumsFragment], [ArtistsFragment] and [jetpack](https://github.com/Catherine22/AAD-Preparation/tree/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/jetpack)      


## Migrate to AndroidX
1. In order to activate AndroidX, add two flags in gradle.properties    
```
android.useAndroidX=true
android.enableJetifier=true
```

2. Remove android support libraries ```com.android.support...``` and ```android.arch...```, all the changes must be implemented to both classes and layouts.    

3. Update test options in build.gradle    
```gradle
android {
  defaultConfig {
          testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
      }
  testOptions {
          execution 'ANDROIDX_TEST_ORCHESTRATOR'
      }
}

dependencies {
    androidTestImplementation 'androidx.test:runner:1.1.1'
    androidTestUtil 'androidx.test:orchestrator:1.1.1'
}
```


[Read More](https://developer.android.com/jetpack/androidx/migrate)

# Debugging

1. Set the debuggable variable (Some 3rd party dependencies might use this variable as well)        
```gradle
android {
    buildTypes {
        debug {
            debuggable true
        }
        release {
            debuggable false
        }
    }
}
```
2. Add breakpoints if needed (Click the Debug button instead of the Run button)        
    - ```step over```: go to next line of code      
    - ```step into```: go into the function     

3. Add watchpoints for C/C++ code       
First thing first, install LLDB or LLDB navigator won't appear on Android Studio.       
Then debug C/C++ code as if debugging Java/Kotlin.     


[Read More](https://developer.android.com/studio/debug/)

# Test
To create modules in apps and publish which as individual APKs, you need ```Android Bundle```       

## Android Bundle
Min SDK version: Android 5.0+ (API level 21)      

**Dynamic delivery**: An android bundle, which contains features that you suppose the user won't use at install, is basically a bunch of code and resources. Users download bundles on demand.     

APKs you will publish:     
1. ```Base APK``` is where contains basic functionality and must-have resources at first download and install.      
2. ```Configuration APKs``` are modules for specific screen density, CPU architecture or language. Google Play automatically generates configuration APKs for you.       
3. (Optional) ```Dynamic feature APKs``` provide modularize features as the user requests

[Read More](https://developer.android.com/studio/projects/dynamic-delivery)

## Getting started
1. Install Android Studio Canary 14             
2. In AndroidManifest.xml
```xml
<application
    android:extractNativeLibs="false"
/>
```

3. In build.gradle
```gradle
android {
    bundle {
        language {
            enableSplit = true
        }
        density {
            enableSplit = true
        }
        abi {
            enableSplit = true
        }
    }

        dependencies {
        // dynamic delivery
        implementation "com.google.android.play:core:${rootProject.ext.playcore}"
        // bundle's dependencies
        implementation 'com.android.support:customtabs:28.0.0'
    }

    // bundles
    dynamicFeatures = [":bbc_news"]
}
```

4. Create new Dynamic Feature Module (Edit -> New Module -> Dynamic Feature Module) and Fill in bbc_news as module name      
5. Create NewsPageActivity in bbc_news module       
```Java
public class NewsPageActivity extends AppCompatActivity {
    private final static String TAG = NewsPageActivity.class.getSimpleName();
    private final static String BBC_NEWS_URL = "https://www.bbc.com/news";
    private final static int CUSTOM_TABS_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_page);

        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                .setToolbarColor(getResources().getColor(R.color.colorPrimary))
                .build();
        customTabsIntent.intent.setData(Uri.parse(BBC_NEWS_URL));
        startActivityForResult(customTabsIntent.intent, CUSTOM_TABS_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CUSTOM_TABS_REQUEST_CODE) {
            finish();
        }
    }
}
```

6. Update AndroidManifest.xml in bbc_news module       
```xml
<application>
        <activity android:name=".NewsPageActivity">
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />
            </intent-filter>
        </activity>
    </application>
```

7. Install and launch this bbc_news module 
See [DynamicDeliveryActivity], [bbc_news module]     

Code: [DynamicDeliveryActivity], [bbc_news module], [tour_guide module], [assets module], [open_weather module]     
[Google sample code](https://github.com/googlesamples/android-dynamic-features)     
[Read More](https://developer.android.com/training/testing/fundamentals)        

8. bundletool (Generate signed Android bundles)    
Before you publish your Android bundles and APK to Google Play, you need ```bundletool```, a command line tool, to test those bundles locally.     
Download [bundletool](https://github.com/google/bundletool/releases) and move to AAD-Preparation/       
 
- In Android Studio, go to Build -> Generate Signed Bundle/APK... -> generate a .aab file
- Generate a set of APKs         
```sh
java -jar ../bundletool-all-0.9.0.jar build-apks --bundle=app/debug/app-debug.aab  --output=app/debug/MaterialDesign.apks
```

- Deploy APKs to a connected device     
```sh
java -jar ../bundletool-all-0.9.0.jar install-apks --apks=app/debug/MaterialDesign.apks
```

- Unzip and see all APKs if you like     
```sh
mkdir app/debug/apks
unzip app/debug/MaterialDesign.apks -d app/debug/apks
```

> Sign APKs: [apksigner](https://developer.android.com/studio/command-line/apksigner)      
> Sign .jar files, .aar files or Android bundles: [jarsigner](https://docs.oracle.com/javase/8/docs/technotes/tools/windows/jarsigner.html)      

[Read more](https://developer.android.com/studio/command-line/bundletool)       

## Fundamentals of Tests
1. Tests give you rapid feedback on failures, which are spotted earlier on development cycle are far easier to fix than once that have gone live.       
2. You are able to maintain a stable velocity throughout the lifetime of your project.      
3. Avoid the boom bust cycle of crunch feature time and the aggregation of technical debt.      

## Testing pyramid
1. (small tests) Unit test      
    - Validate the functionality and contracts of each class within your app        
    - Run on your own local machine      
    - fast and focus        
    - 70%       
2. (medium tests) Integration test      
    - Interaction between a view and view model and run on particular screens, DAOs and multi-fragment tests        
    - Run on real devices or emulators       
    - 20%       
3. (large tests) End-to-end test        
    - validate end-to-end workflows that guide users through multiple modules and features      
    - Run on real devices or emulators       
    - 10%       

![screenshot](https://raw.githubusercontent.com/Catherine22/AAD-Preparation/master/AAD-Preparation/screenshots/tests.png)       

[Blog](https://testing.googleblog.com/2010/12/test-sizes.html)      
[Video](https://www.youtube.com/watch?v=pK7W5npkhho) 

## TDD, Test Driven Development
![screenshot](https://raw.githubusercontent.com/Catherine22/AAD-Preparation/master/AAD-Preparation/screenshots/TDD.png)  

## Principles of Unit Testing
1. Through: Test failure conditions, invalid inputs and boundary conditions.     
2. Repeatable: Return the same results every time.      
3. Focused: Focus one specific aspect of the code.        
4. Verifies behaviour: Avoid writting too many assumption in the actual implementation of your code.        
5. Fast     
6. Concise      

## Device
You could run your unit tests on any of them:       
1. Real devices     
2. Emulator     
3. Simulator. E.g. Roboletric       

> Robolectric supports testing on JVM-powered development machines      

## Instrumented test 
```Espresso```        
- [NavigationDrawer unit test code]       
- [Options menu unit test code]      

```AndroidJUnit4```       
- [RecyclerView unit test code]     

## Robolectric      

## Mockito      



[Google sample](https://github.com/googlesamples/android-testing) 
[Google testing blog](https://testing.googleblog.com/)      
[Google doc](https://developer.android.com/training/testing)        

# Others
## Generate SSL client certificates via openssl
E.g. https://openweathermap.org/api     

1. Show SSL certificates        
```sh
openssl s_client -connect openweathermap.org:443 -showcerts
```

2. Create a .pem file and paste string from ```-----BEGIN CERTIFICATE-----``` to ```-----END CERTIFICATE-----```        
```sh
echo -n | openssl s_client -connect openweathermap.org:443 | \ sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > openweathermap.crt
```

> Convert .pem file to .crt
```sh
openssl x509 -outform der -in openweathermap.pem -out openweathermap.crt
```

## Debug
1. Check pem online     
https://www.sslshopper.com/certificate-decoder.html     

2. Pin SSL online
https://www.ssllabs.com/ssltest/index.html

3. Debug certificates
```sh
openssl s_client -debug -connect w openweathermap.org:443
```
search ```Verify return code```     

4. Pin certificates
```sh
openssl s_client -servername openweathermap.org -connect openweathermap.org:443 | openssl x509 -pubkey -noout | openssl pkey -pubin -outform der | openssl dgst -sha256 -binary | openssl enc -base64
```


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
9. [Sealed Class], [Enum Classes]             
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




[Material design]:<https://github.com/Catherine22/AAD-Preparation#material-design>
[Localisation]:<https://github.com/Catherine22/AAD-Preparation#Localisation>
[Device compatibility]:<https://github.com/Catherine22/AAD-Preparation#device-compatibility>
[Lifecycle]:<https://github.com/Catherine22/AAD-Preparation#lifecycle>
[App components]:<https://github.com/Catherine22/AAD-Preparation#app-components>
[Working in the background]:<https://github.com/Catherine22/AAD-Preparation#working-in-the-background>
[Notification]:<https://github.com/Catherine22/AAD-Preparation#notification>
[Accessibility Features]:<https://github.com/Catherine22/AAD-Preparation#accessibility-features>
[Day/Night Mode]:<https://github.com/Catherine22/AAD-Preparation#daynight-mode>
[Styles]:<https://github.com/Catherine22/AAD-Preparation#styles>
[Jetpack]:<https://github.com/Catherine22/AAD-Preparation#jetpack>
[Debugging]:<https://github.com/Catherine22/AAD-Preparation#debugging>
[Test]:<https://github.com/Catherine22/AAD-Preparation#test>
[Kotlin]:<https://github.com/Catherine22/AAD-Preparation#kotlin>

[res]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/res/>
[BaseActivity]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/activities/BaseActivity.java>
[MainActivity]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/activities/MainActivity.java>
[LifecycleActivity]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/activities/LifecycleActivity.java>
[LifecycleObserverImpl]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/utils/LifecycleObserverImpl.java>
[AppComponentsActivity]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/activities/AppComponentsActivity.java>
[MusicPlayerService]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/services/MusicPlayerService.java>
[MusicPlayerJobScheduler]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/services/MusicPlayerJobScheduler.java>
[AndroidManifest]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/AndroidManifest.xml>
[InternetConnectivityReceiver]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/receivers/InternetConnectivityReceiver.java>
[AirplaneModeChangedReceiver]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/receivers/AirplaneModeChangedReceiver.java>
[ScreenOnReceiver]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/receivers/ScreenOnReceiver.java>
[NetworkHealthService]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/services/NetworkHealthService.java>
[NetworkHealthJobScheduler]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/services/NetworkHealthJobScheduler.java>
[AirplaneModeService]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/services/AirplaneModeService.java>
[AirplaneModeJobScheduler]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/services/AirplaneModeJobScheduler.java>
[ScreenOnOffService]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/services/ScreenOnOffService.java>
[ScreenOnOffJobScheduler]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/services/ScreenOnOffJobScheduler.java>
[BackgroundActivity]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/activities/BackgroundActivity.java>
[SleepTask]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/tasks/SleepTask.java>
[SleepTaskLoader]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/tasks/SleepTaskLoader.java>
[NotificationActivity]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/activities/NotificationActivity.java>
[ContentProviderFragment]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/fragments/ContentProviderFragment.java>
[AlbumsProvider]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/providers/AlbumsProvider.java>
[CursorLoaderActivity]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/activities/CursorLoaderActivity.java>
[BackgroundServiceFragment]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/fragments/BackgroundServiceFragment.java>
[rv_album_item]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/res/layout/rv_album_item.xml>
[PlaylistAdapter]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/adapters/PlaylistAdapter.java>
[PlaylistFragment]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/fragments/PlaylistFragment.java>
[AlbumsFragment]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/fragments/AlbumsFragment.java>
[ArtistsFragment]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/fragments/ArtistsFragment.java>
[ArtistAdapter]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/adapters/ArtistAdapter.java>
[ArtistItemKeyProvider]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/components/ArtistItemKeyProvider.java>
[ArtistItemDetailsLookup]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/components/ArtistItemDetailsLookup.java>
[RecyclerViewItemTouchHelper]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/components/RecyclerViewItemTouchHelper.java>
[MusicFragment]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/fragments/MusicFragment.java>
[SystemBroadcastReceiverFragment]<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/fragments/SystemBroadcastReceiverFragment.java>
[UIComponentsActivity]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/activities/UIComponentsActivity.java>
[SearchSongsActivity]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/activities/SearchSongsActivity.java>
[DynamicDeliveryActivity]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/java/com/catherine/materialdesignapp/activities/DynamicDeliveryActivity.java>
[bbc_news module]:<https://github.com/Catherine22/AAD-Preparation/tree/master/AAD-Preparation/bbc_news>
[tour_guide module]:<https://github.com/Catherine22/AAD-Preparation/tree/master/AAD-Preparation/tour_guide>
[open_weather module]:<https://github.com/Catherine22/AAD-Preparation/tree/master/AAD-Preparation/open_weather>
[assets module]:<https://github.com/Catherine22/AAD-Preparation/tree/master/AAD-Preparation/assets>
[AndroidManifest]:<https://github.com/Catherine22/AAD-Preparation/blob/master/AAD-Preparation/app/src/main/AndroidManifest.xml>
[NavigationDrawer unit test code]:<https://github.com/Catherine22/AAD-Preparation/tree/master/AAD-Preparation/app/src/androidTest/java/com/catherine/materialdesignapp/DrawerNavigatorTest.java>
[RecyclerView unit test code]:<https://github.com/Catherine22/AAD-Preparation/tree/master/AAD-Preparation/app/src/androidTest/java/com/catherine/materialdesignapp/BackgroundActivityTest.java>
[Options menu unit test code]:<https://github.com/Catherine22/AAD-Preparation/tree/master/AAD-Preparation/app/src/androidTest/java/com/catherine/materialdesignapp/DayNightModeTest.java>



[Basic Types]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/BasicTypes.kt>
[Control Flow]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/ControlFlow.kt>
[Returns and Jumps]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/ReturnsAndJumps.kt>
[Classes and Inheritance]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/MyClass.kt>
[Properties and Fields]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/PropertiesAndFields.kt>
[Visibility Modifiers]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/package1/VisibilityModifiers.kt>
[BaseClass]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/BaseClass.kt>
[BaseClassExtensions]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/package1/BaseClassExtensions.kt>
[Data Class]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/DataClass.kt>
[User]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/User.kt>
[Employee]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/Employee.kt>
[Nested and Inner Classes]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/InnerClassExample.kt>
[Sealed Class]:<https://github.com/Catherine22/AAD-Preparation/blob/master/KotlinFromScratch/src/SealedClass.kt>
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

[CallLogProvider]:<https://android.googlesource.com/platform/packages/providers/ContactsProvider/+/master/src/com/android/providers/contacts/CallLogProvider.java>
[Grid and keyline shapes]:<https://material.io/design/iconography/#grid-keyline-shapes>
