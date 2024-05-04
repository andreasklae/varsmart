```mermaid
    sequenceDiagram
        actor U as User
        U->>App: Opens app
        App->>Met: fetchAllWarnings()
        Met-->>App: list of alerts
        App->>App: calculate distance to each alert
    
        App->>U: Display alerts within 40km
       
    
        alt checks alerts in warning screen instead of home 
        U ->> App: Navigate to warning screen
        App->>Met: fetchAllWarnings()
        Met-->>App: list of alerts
        App ->> U: Display list of all alerts
        end
```
