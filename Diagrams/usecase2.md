```mermaid
    sequenceDiagram
    actor U as User
    U ->>App: User clicks on MrPraktisk
    App ->>OpenAi: getGPTResponse(prompt)
    OpenAi-->>App: message
    App->>U: display message on screen
```