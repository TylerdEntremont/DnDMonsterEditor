This app is designed to allow dungeon and dragons dungeon masters to change the stats of monsters and get a new Challenge Rating.  
This will allow the monsters to be adjusted such that monsters who are traditionally of a strength level not consistent with the party 
but which are themetically appropriate can be used in such instances.



Libraries:
  OKHTTP and Retrofit: Libaries for making the API Calls, used because they are the standard
  RoomDatabase: Library for storing data in a database and making those calls, used because it is considered the standard
  Dagger and Hilt: Libraries for dependency injection, used because they are more versitile then Koin
 
Current State: The app is functional in all cases and provides useful feedback in most cases, has a visual warning on monsters who are 
spellcasters because dealing wiht spellcasters has not been implemented yet. Allows the saving and reacess of modified monsters, also 
allows these to be deleted when no longer needed

Future Work:
  Handle Spell Casters
  Add functionality to change CR and get new stats
  Add handling for special abilities effect on CR
