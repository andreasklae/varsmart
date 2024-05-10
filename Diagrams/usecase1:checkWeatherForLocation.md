```mermaid
  sequenceDiagram
    actor U as User
    Participant App as MasterUi
    Participant WeatherScreen as WeatherScreen
    Participant favouriteScreen as favouriteScreen
    Participant Met as Met
    Participant LocationApi as LocationApi

    U ->>App: User opens app
    WeatherScreen ->> App: Display loading screen
    App->> U: Display loading screen 
    WeatherScreen ->> Met: fetchWeather(DefaultLocation)
    Met -->> WeatherScreen: weatherData
    WeatherScreen ->> App: Show weatherdata
    App->> U: Display weatherscreen with data
    U->>App: Navigate to favouriteScreen
    App->> favouriteScreen: load favouriteScreen
    favouriteScreen->> App: Show favouriteScreen
    App->>U: Display favourteScreen
    U->>App: Click on searchButton
    App->> favouriteScreen: Handle button click
    favouriteScreen->> App: Show searchbar
    App ->> U: Display searchBar
    U->>App: Search cityName
    App ->> favouriteScreen: Handle search
    favouriteScreen->>LocationApi:searchLocations(cityName)
    LocationApi-->>favouriteScreen: list of locations that match search
    favouriteScreen->> App: Show options
    App->> U: Show location list
    U->>App: Click on wanted location

    App->>favouriteScreen: Handle chosen option
    favouriteScreen->>WeatherScreen:setHomeLocation(wantedLocation)
    favouriteScreen ->> WeatherScreen: animateScrollToPage(WeatherScreen)
    WeatherScreen->> Met: fetchWeather(wantedtLocation)
    Met -->> WeatherScreen: weatherdata
    WeatherScreen ->> App:  Show weatherdata
    App ->>U: Display weatherscreen with data
