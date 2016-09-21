### Capstone-Project
####App Name: WannaJoin
This app would be for parents who want an easy and informal way of letting their kids’ friends, classmates, etc., know of their plans and also be aware of others’ plans without the overhead of calls, emails, etc. It would enable on the spur of the moment group play/activities.

####UI Mockups
Please see Capstone_Stage1.pdf

####Installation Instructions
1. Create a Firebase project in the Firebase console “https://firebase.google.com/“
2. Click Add Firebase to the Android app and follow setup steps. 
3. When prompted, enter app's package name “com.village.wannajoin” and SHA-1 hash of the signing certificate 
4. Download a google-services.json file and copy this into the project’s app folder.
5. Go to Google API console “https://console.developers.google.com/“
6. Select the project you created earlier via Firebase console
7. Enable following APIs (a) Google Place API for Android (b) Google Map API for Android
8. Go to Credentials section and copy Android API key
9. Now paste Android API key from step 8 into AndroidManifest.xml file 
<meta-data
   android:name="com.google.android.geo.API_KEY"
   android:value="Enter_your_api_key" />
10. Go to Firebase console’s Auth section. Enable Google and Facebook login.
11. You should be able to install and run the app now. 




