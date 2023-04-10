# UKA WebSim

This project contains the simulator for testing out patterns and sequences in the browser.

## Requirements

To view the simulator a browser is required. The only supported browser is Google Chrome, but others might work as well.

## Development

To contribute to the project the recommended setup is a Linux based distro running `docker` and `docker-compose`.

To start developing within a container run the command:

```bash
./docker/start.sh
```

The container will automatically detect any file changes made locally and restart the service.
The service itself can be reached in the browser at [http://localhost:8080](http://localhost:8080)