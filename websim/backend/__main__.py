from .server import app

app.config['SEND_FILE_MAX_AGE_DEFAULT'] = 0

# This app should be launched using uwsgi for prod.
# This entrypoint is just for development use
app.run(debug=True)
