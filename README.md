# Code Smell Detector


The below table specifies the types of smells that are detected by this tool **"CodeSmellDetector"** along with a brief description of each smell and the detection strategy. All the below code smell are taken from [http://www.modelrefactoring.org/smell_catalog/]().




## Bulk Data Transfer On Slow Network(Network)
<!-- Transfer means? uploading or downloading? 
-->**Description:**  It occurs when transferring data over a slower network connection. This will consume much more power then over a fast connection.

**Happens:** When Developers do not check the network connection when transferring data.

**Detection** 

1. Looking for a class that implements `DownloadCallback` which indcates the class dealing with the internet to send data. 
2. Or a class or a method having a object of  `HttpsURLConnection`. 
3. `HttpPost` or `HttpClient` or `openConnection()`

After detecting what listed above, check if they haev the follwoing:

1. `ConnectivityManager.TYPE_WIFI` 
2. `ConnectivityManager.TYPE_MOBILE`
3. `isNetworkRoaming()` 

if they having that mean the develper checking the network type before sending data 
consider as code smell.

This example of uploading picture to server[ https://androidexample.com/Upload_File_To_Server_-_Android_Example/index.php?view=article_discription&aid=83]()
<!--need example here 
after i run the tool, i will look for the 
-->
## Dropped Data (UI)
**Description:**  The user can input or edit data in an Activity or Fragment. Then another activity pops up (incoming phone call) and the input is lost. In other words,when user is being intrepted by onther application for example phone call while the user editting data. This action will lose the data.

**Happens:** When develpers do not implement `onSaveInstanceState()`, which is a Bundle object containing the activity's previously saved state, and ensure the user data is being save while he/she is editing data. The data  can be retreaive by using `onRestoreInstanceState(Bundle)`

**Detection** 

Looking for  `(EditText) findViewById(R.id."id")` in java activity to find variable name in order to determine  if it is being used in `onSaveInstanceState() `. if not it is code smell. 

To know which activty layert name search for ` setContentView(R.layout."Name")` and check the parment to know what the name of it. 
<!--* **Xml file Layout:**
	1. AppCompatEditTextinstead 
	2.  EditText
	3.  TextInputEditText
* **Class Activity**
	1. 	TextView 
	1. 	AppCompatEditText
	1. 	TextInputEditText-->


## Early Resource Binding
**Description:**  It occurs when physical, energy-consuming resources of an Android device are requested too early more energy is consumed.
GPS component of an Android device is requested in the `'onCreate()'` method already.
Thus, the GPS physical component consumes energy while the user isn't interacting with any map since nothing is visible yet.

>  If I want to get the user location before I populate the content is that smell?.
 
**Happens:** When developers requeste GPS component `onCreate()` methods before nothing is showing to the user yet. This practice consumes energy while the user isn't interacting with any map.

**Detection** Check if `onCreate()` methods  has  `requestLocationUpdates`  method which method of `LocationManager` class.

How to request user location [https://developer.android.com/guide/topics/location/strategies.html]()

## Interrupting From Background
**Description:**  It occurs when  activities start from BroadcastRecievers or Services that work in the background. Imagine a user that writes a SMS and gets interrupted by another apps activity that just started. The worse: this annoying app catches some of the input.

**Happens:** When Developers start activities on BroadcastRecievers or Services. 

**Detection:** 

Check for the following :

1. BroadcastRecievers:
	1. class inherited from `BroadcastReceiver`.
	2. And `onReceive` method overridde.
	3. Or class has a object `BroadcastReceiver()` and the 2 of the list is exist and `registerReceiver` method.
	
	More infirmation about : [https://androidexample.com/Introduction_To_Broadcast_Receiver_Basics/index.php?view=article_discription&aid=60
]()

If `onReceive` has `startActivity()`. has code smell.

2. Services
	1. class inherited from `Service `.
	2. And `onStartCommand() ` method overridde and `onBind()`.
	3. Or class has a object `Intent()` and passed througf `startService(intent)`.
	
If `onStartCommand ` has instraction to start activity `startActivity()` consider it as smelly.



More definition [https://android.stackexchange.com/questions/46096/whats-the-difference-between-a-service-and-a-broadcast-receiver
]()

## Nested Layout
**Description:**  Layouts with elements that have the attribute weight set must be computed twice. While each new element requires initialisation, layout and drawing parsing deep nested LinearLayouts will also increase the computation time exponentially.


**Happens:** N/A

**Detection:** N/A


## Not Descriptive UI
**Description:** Every UI element should have content description what it is for, but so element does not. The element that do not have description is code smell.

**Happens:** When Developers do not add description for element that do not have description.

**Detection**  There two places have to be checked if the element has description or not:
Each elements in xml layout file has to be checked if one element does not have `android:contentDescription`, get the of element and check in the java class if that `id` is exist in class and `setContentDescription()`  used .

more information about descrtiopns:[ https://developer.android.com/training/accessibility/accessible-app.html ]()

## Overdrawn Pixel
<!-- Transfer means? uploading or downloading? 
-->**Description:**  The layout of an Android UI is generally build using XML. So you can stack several layers to form complex user interfaces. Containers and basic elements may have attributes to describe their background, text, border, etc. In this case it might be possible to overdraw a pixel. This means for example the root container has a black background and contains several other elements that havve another (non-transparent) background color. And on top of these are buttons, text-fields that are also colored. This means that a pixel must be drawn several times which is a costly process.

**Happens:** N/A

**Detection** N/A

## Prohibited Data Transfer
**Description:** It is not checked if the user has disabled background data transmission before transmitting. 

**Happens:** When  develpers do not Check background data transfer before they are transmitting data.

**Detection** Check each class is using internet request `HttpPost` or `HttpClient` or `HttpsURLConnection`. if the class has  `getBackgroundDataSetting ()` or `getActiveNetworkInfo() `. if they dont have that it is code smell. 


 ~ This can be detected when the tool detecting `Bulk Data Transfer On Slow Network(Network)`
> Should we check if the androidManifest has permesion to acees internet?

## Set Config Changes
**Description:** It is considered a smell to set the attribute `android:configChanges` in the Android manifest. This attribute defines what configuration changes the app has to handle manually (else the system will take care of persisting input data between config changes in the activities ui elements). Handling them manually can cause memory bugs (leaving resources in memory). 

**Happens:** when develpers handel persisting input data between config changes in the activities ui elements which may cause memory bugs.

**Detection** 
> check the androidManifest and the source code. working on it 

## Tracking Hardware Id
**Description:**  

**Happens:** 

**Detection** 

## Uncached Views
**Description:**  

**Happens:** 

**Detection** 

## Uncontrolled Focus Order
**Description:**  

**Happens:** 

**Detection** 

## Unnecessary Permission
**Description:** 

**Happens:** 

**Detection** 


## Untouchable
**Description:**  

**Happens:** 

**Detection** 



