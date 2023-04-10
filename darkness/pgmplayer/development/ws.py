import argparse
import asyncio
import websockets
import threading
import time

from ..pgm import PGMReader

# Contain all the active WebSocket clients in a set
CLIENTS = set()

# Store the event loop here
LOOP = asyncio.get_event_loop()

async def _ws_handler(websocket, path):
    print("WS Connection opened!")
    CLIENTS.add(websocket)
    try:
        async for msg in websocket:
            pass # Ignore all incoming messages
    except websockets.ConnectionClosedError:
        print("Closing connection")
    finally:
        CLIENTS.remove(websocket)

    
    await websocket.close()

async def _broadcast(data):
    tasks = [ws.send(data) for ws in CLIENTS]
    await asyncio.gather(
        *tasks,
        return_exceptions=False,
    )


def broadcast(data):
    asyncio.run_coroutine_threadsafe(_broadcast(data), LOOP)

def _server_thread(loop):
    asyncio.set_event_loop(loop)

    start_server = websockets.serve(_ws_handler, "0.0.0.0", 5678)

    loop.run_until_complete(start_server)
    loop.run_forever()

def start():
    print("Starting WebSocket server")

    t = threading.Thread(target=_server_thread, args=(LOOP, ))
    t.setDaemon(True)
    t.start()

    
