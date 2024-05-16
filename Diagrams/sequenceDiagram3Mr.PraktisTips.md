```mermaid
  sequenceDiagram
    actor U as User
    Participant App as MasterUi
    Participant WeatherScreen as WeatherScreen
    Participant OpenAi as OpenAi
  

    App->>U: Display weatherScreen
    U->> App: Clicks on Mr Praktisk
    App->> WeatherScreen: Handle click
    WeatherScreen ->> App: Message loading icon
    App ->> U: Display message loading
    WeatherScreen ->> OpenAi: getGPTrespons(prompt)
    OpenAi -->> WeatherScreen: Message
    WeatherScreen ->> App: Mr Praktisk speak animation with message
    App ->> U: Display message with Mr. Praktisk speaking
