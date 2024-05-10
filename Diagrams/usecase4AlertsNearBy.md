```mermaid
  sequenceDiagram
    actor U as User
    Participant App as MasterUi
    Participant WeatherScreen as WeatherScreen
    Participant  Dataholder as Dataholder
    Participant WarningScreen as WarningScreen
    Participant Met as Met
    

  
    U ->>App: User opens app
    WeatherScreen ->> App: Display loading screen
    App->> U: Display loading screen 
    WeatherScreen->>Dataholder: data.updateAll()
    Dataholder->>Met: fetchAllWarnings()

    Met -->> Dataholder: list of alerts
    Dataholder->>Dataholder: Calculate distance to each alert
    Dataholder->>Dataholder: Sort list by distance from chosen location
    Dataholder-->>WeatherScreen: list of alerts nearby location
    WeatherScreen-->>App: list of alerts nearby location
    App->>U:Display list of alerts nearby
