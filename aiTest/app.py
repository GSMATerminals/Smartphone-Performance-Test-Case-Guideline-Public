from flask import Flask
from src.views_benchi import app

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8082, threaded=True)
