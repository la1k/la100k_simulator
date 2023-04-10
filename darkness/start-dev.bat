docker run --rm -it --name uka_devserver -v %CD%:/app -e GRADLE_USER_HOME=/home/user/.gradle -p 127.0.0.1:5678:5678 ghcr.io/lysreklamen/publicpkg/devserver:latest
