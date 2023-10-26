# WebSocket

## Communication Protocol

1. Client send standard HTTP request to server with `Upgrade: websocket` header to establish a
   WebSocket connection

2. Server responds with status `101 Switching Protocols` in response with `Upgrade: websocket`
   header

3. Handshake is complete and the connection is established, the client and server can now send
   messages to each other

## ChatRoom Application

- [x] Client can send messages to the server
- [x] Server can broadcast user joins and user leaves to all connected clients