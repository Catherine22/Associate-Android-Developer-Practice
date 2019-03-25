# AAD-Preparation

# Navigation
- (Toasts vs Snackbar)[]         
- (Localization)[]      


# ```Toast``` vs ```Snackbar```       
|             | Toast                                                                | Snackbar            |
|-------------|----------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------|
| Overview    | A toast provides simple feedback about an operation in a small popup | Snackbars provide lightweight feedback about an operation                                                                                         |
| Interaction | Toasts automatically disappear after a timeout                       | Snackbar could either automatically disappear after a timeout, or be manually closed                                                           |
| More info   |                                                                      | 1. Having a CoordinatorLayout in your view hierarchy allows Snackbar to enable certain features 2. Snackbars can contain an action such as "undo"|
| Sample code | [MainActivity],[MainActivity]                                        | [MainActivity]      |


[doc](https://developer.android.com/guide/topics/ui/notifiers/toasts)       
[doc](https://developer.android.com/reference/android/support/design/widget/Snackbar)       


# Localization
Resource directories example:       
1. animator     
2. anim     
3. color        
4. drawable     
5. mipmap       
6. layout       
7. memu     
8. raw      
9. values       
10. xml     
11. font        

## drawable


# Device compatibility
1. Create alternative UI resources such as layouts, drawables and mipmaps     
2. Set layout files with the following rules:       
    - Avoid hard-coded layout sizes by using ```wrap_content```, ```match_parent``` and ```layout_weight```, etc        
    - Prefer ```ConstraintLayout```     
    - Redraw views when window configuration changes (multi-window mode or screen rotation)        
3. Define alternative layouts for specific screen sizes. E.g. ```layout-w600dp``` and ```layout-w600dp-land``` for 7” tablets and 7” tablets in landscape representative        
4. Build a dynamic UI with fragments        

ConstraintLayout example: []()      
Fragments example: []()     

[doc](https://developer.android.com/training/multiscreen/screensizes)

## Pixel densities
Pixel density is how many pixels within a physical area of the screen, **dpi** is the basic unit.       
**dpi**: Dots per inch      
**resolution**: The total number of pixels on a screen      
**dp or dip**: Instead of px (pixel), measure UI with dp (density-independent pixels) on mobile devices       


[doc](https://developer.android.com/guide/practices/screens_support)        






[MainActivity]:<https://github.com/Catherine22/AAD-Preparation/blob/master/app/src/main/java/com/catherine/materialdesignapp/MainActivity.java>