```mermaid
    sequenceDiagram
      actor U as User
      Participant App as MasterUi
      Participant WeatherScreen as WeatherScreen
      Participant favouriteScreen as favouriteScreen


      App->> U: Display WeatherScreen 
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


      alt Checks weather first
          U->>App: Click on wanted location
          App->>favouriteScreen: Handle chosen option
          favouriteScreen->>WeatherScreen:setHomeLocation(wantedLocation)
          favouriteScreen ->> App: animateScrollToPage(WeatherScreen)
          WeatherScreen ->> App: loadingicon
          App ->> U: Display weatherscreen loading
          WeatherScreen->> Met: fetchWeather(wantedtLocation)
          Met -->> WeatherScreen: weatherdata
          WeatherScreen ->> App:  Show weatherdata
          App ->>U: Display weatherscreen with data
          U ->> App: Clicks on bookmarkicon
          App ->> WeatherScreen: Handle click
          WeatherScreen->> WeatherScreen: data.toggleInFavourites()
          WeatherScreen->> WeatherScreen: Update bookmark
          WeatherScreen->> WeatherScreen: Make toast meassge
          WeatherScreen ->> App: Display updated bookmark and toast-message
          App ->> U: Display updated bookmark and toast-message on WeatherScreen
      end

      alt Adds to favourties in favouriteScreen
          U ->> App: Clicks on bookmarkicon on wanted location
          App ->> favouriteScreen: Handle click
          favouriteScreen->> favouriteScreen: Data.toggleInFavourites()
          favouriteScreen->> favouriteScreen: Update bookmark
          favouriteScreen->> favouriteScreen: make toast-message
          favouriteScreen ->> App: Display updated bookmark and toast-message on searchDialogue
          App ->> U: Display updated bookmark and toast-message on searchDialogue

      end
    
