# MatchMakingServer
An application written in Java that hosts a matchmaking server for a tic-tac-toe game.

The first (unmatched) player waits in the server lobby. When a second player connects a new thread is created to host the game, which gets passed socket references to the players as well as their respective Data Input/Output streams.

Basic QoL Features:

- Built to clear system.in buffers before players take their turns so inputs read during the opposite player's turn do not affect the game itself.
- System exits gracefully if win/loss/tie conditions are met.

- Console screen clears on update so that client is not overwhelmed by text during play.
