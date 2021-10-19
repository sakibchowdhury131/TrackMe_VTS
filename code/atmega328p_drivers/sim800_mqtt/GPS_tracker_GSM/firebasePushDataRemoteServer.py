from paho.mqtt import client as mqtt_client
import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
import datetime



broker = 'test.mosquitto.org'
port = 1883
topic = "devices"
client_id = '#test_ID_remote_admin_2'
username = 'sakib'
password = 'enteryourpass'
deviceID = 0
latitude = 0
longitude = 0

# Fetch the service account key JSON file contents
cred = credentials.Certificate('/home/ubuntu/fir-rtd-test-firebase-adminsdk-w2zku-37f5238819.json')


# Initialize the app with a service account, granting admin privileges
firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://fir-rtd-test-default-rtdb.firebaseio.com/'
})

# As an admin, the app has access to read and write all data, regradless of Security Rules
ref = db.reference('devices')
print(ref.get())

def connect_mqtt() -> mqtt_client:
    def on_connect(client, userdata, flags, rc):
        if rc == 0:
            print("Connected to MQTT Broker!")
        else:
            print("Failed to connect, return code %d\n", rc)

    client = mqtt_client.Client(client_id)
    client.username_pw_set(username, password)
    client.on_connect = on_connect
    client.connect(broker, port)
    return client




def subscribe(client: mqtt_client):
    def on_message(client, userdata, msg):
        message = msg.payload.decode()

        x = message.split("/")
        deviceID = x[0]
        latitude = x[1]
        longitude = x[2]
        print(f"Received `{message}` from topic")
        e = datetime.datetime.now()
        users_ref = ref.child(deviceID+'/'+str(e.day)+'_'+str(e.month)+'_'+str(e.year)+'_'+str(e.hour)+'_'+str(e.minute)+'_'+str(e.second))
        users_ref.set({
            'latitude': latitude,
            'longitude': longitude
        })
    client.subscribe(topic)
    client.on_message = on_message



def run():
    client = connect_mqtt()
    subscribe(client)
    client.loop_forever()

if __name__ == '__main__':
    run()