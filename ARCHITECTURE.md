**Hierarchy diagram:**

https://drive.google.com/file/d/1asMJJfmhjXVysWA2iL5xV9gVdO8HuDO0/view?usp=sharing

**Design patterns and dataflow**.

Our app is based on the "MVVM-pattern". This pattern consists of three components; Model, View and
Viewmodel, where each component has its own responsibilities. For dataflow, we follow udf
(unidirectional dataflow). The ui passes events (like button clicks or textfield changes) to the
viewmodel which then handles the business logic. The viewmodel passes the states to the ui. This
ensures state encapsulation which means that the states will only be updated in one place and there 
is only one source of truth. For the code, we primarily use Stateflow (asStateFlow() and
collectAsState())

**Ui layer:**

Master UI is a Compose function with an horizontal pager which contains all the different screens,
as well as the bottom bar. Each screen has its viewmodel, which is declared in MainActivity. We
chose to hoist the viewmodels to MainActivity, to ensure that a new viewmodel is not created each
and every time the user navigates. Most of the compose functions take lambda functions and variables
as arguments (instead of the viewmodel). This is to hoist the ui state to the highest ui level where
all the "children" needs to update according to the ui state. This makes the compose functions more
testable, reusable and independent.

Each screen is entirely independent of each-other. If one fails, the others do not. If, for example,
the weather forecast api fails, the waring screen will still show the warnings fetched from the
metAlerts api. Or if the device is disconnected from the internet, the user can still change
the settings, as this does not require internet. This is to follow the principle of low coupling.To
also follow the principle of high cohesion, we have made sure that each component is only
responsible for its own tasks. For example, one screen does not manage the api calls for another.

**Data layer:**

In the data layer, we have made a class called DataHolder. Dataholder is a single source of truth
for all the data for a location. For example, if the user want to see the weather for Bergen, a
DataHolder object will be created, and manage each of the api calls. The DataHolder object contains
location data (name, coordinates etc.), as well as all data from the apis. It also has a companion
object called Favourites, which is a list of DataHolder object. The reason for this is that both
the favourite screen and the home screen needs access to the data, which will avoid unnecessary
api calls. It also simplifies the viewmodels. 

To avoid high coupling, we have made sure to make the data from each api independent of eachother.
One api failing will not affect the others, even though its all stored in one place. If for example
the sunrise api fails, the home screen will still show the weather data. The only exception is that
the gpt api is dependent on both the weather and the warning api, as the gpt is supposed to give
information and tips based on the weather and warnings. The favourite screen will also be able to
show the name of each location if the api fails.

For storing long term memory, we are using shared preferences. For managing the data, we have
created an object called PreferenceManager. This objects holds all function for saving and fetching
the data. The data we chose to store are all settings and the location data for the favourite list.

**Api-level:**

We have chosen API-level 28. We wanted a balance between being compatible with many devices, while
still potentially being able to use modern android functionality. We prioritized the latter the
most, because we did not want to be constrained by lack of features. However, we also wanted a
stable version of android. The newer ones can tend to be a bit unreliable as they have not been
tested as much. API-level 28 felt like a reasonable choice. It makes the app compatible with
90,3% of all Android devices. It was launched in 2018 as Android 9 (Pie). It meets Google Play
stores' minimum requirements for app-updates