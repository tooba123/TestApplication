# TestApplication
In this Android Application, Users can do the following:

-View weather info of different cities located in different Countries , including Beijing, Berlin, Cardiff, Edinburgh, london and Nottingham.
-See the battery level of their device and clock which displays their current location time.
-View all other users installed apps info in device.

# Inner Working
-Application is built with the help of using MVP architecture.

-Weather Info is received from webserver. In case if decice is not connected with Internet or Internet is unavailable,
 then local cache is also used to retrieve the information.
 
-LiveData is also used to send latest data to the view as soon data updates are made or information is received from
 webserver or Local cache.
