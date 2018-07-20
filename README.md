# Geofence
An app that you could take advantage in tracking a person's frequent locations, an employee availability in office etc...!!!

Geofence Api:
* Geofence is one of the most popular google's services that we could use to monitor a person's frequent visisted areas, people's availability in some specified areas by plotting a virtual fence over physical geo-graphical areas.  

Using "Geofence Api", we can fetch following info via specific events:

1] GEOFENCE_TRANSITION_ENTER: This is fired when a person enters a Geofence.
2] GEOFENCE_TRANSITION_EXIT: This is fired when a person exists a Geofence.
3] INITIAL_TRIGGER_ENTER: This is fired initially once soon after app is launched if a person is already inside the Geofence.
4] INITIAL_TRIGGER_DWELL: This is fired when a person rests for specified duration inside a Geofence. 

Steps to procced through app:

Step1:

--> Download & open in Android studio

Step2:

--> Open GeoFenceConstants under utils package & customise geofence config according to your requirements

Step3:

--> Now install the app, you should see the co-ordinates displaying on the screen for every 5secs. Whenever you enter the geofence or exist the defined geofence that you configured in previous step, the app displays "You entered location now!" & "You exited location now!" respectively.   

Njoy Geofence :):)
