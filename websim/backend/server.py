from flask import Flask

app = Flask(__name__)

# Trigger import of
from . import views

