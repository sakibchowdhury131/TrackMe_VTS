
from PyQt5.uic import loadUi
from PyQt5 import QtWidgets, QtGui
from PyQt5.QtWidgets import QDialog
from PyQt5.QtCore import QDate, QDateTime
import csv
import pyrebase
import os


firebaseConfig = {
  "apiKey": "AIzaSyD1GlIv47UcrH7h_oSkC7AzzjkXsXuUWB4",
  "authDomain": "thdimensionprojectdb.firebaseapp.com",
  "databaseURL": "https://thdimensionprojectdb-default-rtdb.firebaseio.com",
  "projectId": "thdimensionprojectdb",
  "storageBucket": "thdimensionprojectdb.appspot.com",
  "messagingSenderId": "627411714147",
  "appId": "1:627411714147:web:0434348a0082254e94f856",
  "measurementId": "G-Z4ME2XMXB7"
}
'''
firebase = pyrebase.initialize_app(firebaseConfig)
db = firebase.database().child("storeDB")
print(db.child("dev_test"+"/year_"+str(2022)+"/month_"+str(1)+"/date_"+str(28)).get().val().keys())
'''



class MainWindow(QDialog):
    
    def __init__(self):
        super(MainWindow, self).__init__()
        loadUi("src/ui/interface.ui", self)
        self.setWindowIcon(QtGui.QIcon("icon.png"))
        self.setWindowIconText("Vehicle Power Monitor")
        self.setWindowTitle("Vehicle Power Monitor")
        self.tableWidget.setColumnWidth(0, 200) #0th column size
        self.tableWidget.setColumnWidth(1, 278) #1th column size
        self.tableWidget.setColumnWidth(2, 200) #0th column size
        self.tableWidget.setColumnWidth(3, 200) #0th column size
        self.tableWidget.setColumnWidth(4, 200) #0th column size
        self.loadDataButton.clicked.connect(self.intermediate_loading)
        self.loginButton.clicked.connect(self.intermediate_login)
        self.genCSVButton.clicked.connect(self.generateCSV)
        self.presetDate = QDate.currentDate()
        self.date.setDate(self.presetDate)
        self.update(self.titleLabel)
        self.userIsAuthenticated = False
        
        

    def login(self):

        if self.userIsAuthenticated:
            self.loginLabel.setStyleSheet("color: red")
            self.loginLabel.setText("Already Logged In")
            self.update(self.loginLabel)
            return
        email = self.email.text()
        password = self.password.text()
        
        

        if len(email) == 0 or len(password) == 0:
            self.loginLabel.setStyleSheet("color: red")
            self.loginLabel.setText("Please Enter your email and password")
            self.update(self.loginLabel)
            

        else: 
            try:
                firebase = pyrebase.initialize_app(firebaseConfig)
                auth = firebase.auth()
                user = auth.sign_in_with_email_and_password(email, password)
                self.loginLabel.setStyleSheet("color: green")
                self.loginLabel.setText("Authenticated")
                self.update(self.loginLabel)
                self.userIsAuthenticated = True
                #print(user)
            except:
                self.loginLabel.setStyleSheet("color: red")
                self.loginLabel.setText("Invalid Email or Password")
                self.update(self.loginLabel)

    def intermediate_login(self):
        self.loginLabel.setStyleSheet("color: green")
        self.loginLabel.setText("Authenticating...")
        self.update(self.loginLabel)
        self.login()

    def intermediate_loading(self):
        self.logLabel.setStyleSheet("color: green")
        self.logLabel.setText("Log: "+ "Loading...")
        self.update(self.logLabel)
        self.loadData()
            

    def loadData(self):
        if self.userIsAuthenticated:
            devID = self.deviceID.text()
            if devID == "":
                self.logLabel.setStyleSheet("color: red")
                self.logLabel.setText("Log: "+ "Please enter device ID")
                self.update(self.logLabel)
                return
            else:
                try: 
                    
                    day = self.date.date().day()
                    month = self.date.date().month()
                    year = self.date.date().year()

                    self.day = day
                    self.month = month
                    self.year = year
                    #print(day)
                    #print(month)
                    #print(year)
                    
                    firebase = pyrebase.initialize_app(firebaseConfig)
                    db = firebase.database().child("storeDB")
                    self.data = db.child(devID+"/year_"+str(year)+"/month_"+str(month)+"/date_"+str(day)).get().val()
                    #print(self.data)
                    
                    
                    row = 0
                    self.tableWidget.setRowCount(len(self.data.keys()))
                    for key in self.data.keys():
                        self.tableWidget.setItem(row, 0, QtWidgets.QTableWidgetItem(str(day)+"/"+str(month)+"/"+str(year)))
                        self.tableWidget.setItem(row, 1, QtWidgets.QTableWidgetItem(str(key)))
                        self.tableWidget.setItem(row, 2, QtWidgets.QTableWidgetItem(str(self.data[key]["voltage"])))
                        self.tableWidget.setItem(row, 3, QtWidgets.QTableWidgetItem(str(self.data[key]["current"])))
                        self.tableWidget.setItem(row, 4, QtWidgets.QTableWidgetItem(str(self.data[key]["power"])))
                        row+=1
                    self.logLabel.setStyleSheet("color: green")
                    self.logLabel.setText("Log: " + "Data Loaded")
                    self.update(self.logLabel)
                except:
                    #print("an error occurred")
                    self.logLabel.setStyleSheet("color: red")
                    self.logLabel.setText("Log: " + "No Data Available for this device or the provided date")
                    self.update(self.logLabel)
        else: 
            self.logLabel.setStyleSheet("color: red")
            self.logLabel.setText("Log: " + "Please Login First")
            self.update(self.logLabel)



    def generateCSV(self):
        if self.userIsAuthenticated:
            try:
                currentPath = os.getcwd()
                outPath = os.path.join(currentPath, 'outputs')
                if not os.path.exists(outPath):
                    os.mkdir(outPath)

                filePath = os.path.join(outPath, QDateTime.currentDateTime().toString("yyyy_MM_dd_HH_mm_ss") + '.csv')
                

                
                with open(filePath, 'w') as f:
                    csv.writer(f).writerow(["Date", "Time", "Voltage (Volts)", "Current (Amps)", "Power (Watt)"])
                    for key in self.data.keys():
                        writer = csv.writer(f)
                        writer.writerow([str(self.day)+"/"+str(self.month)+"/"+str(self.year), str(key), self.data[key]["voltage"], self.data[key]["current"], self.data[key]["power"]])
                self.logLabel.setStyleSheet("color: green")
                self.logLabel.setText("Log: " + "CSV file saved in: " + filePath)
                self.update(self.logLabel)
            except:
                self.logLabel.setStyleSheet("color: red")
                self.logLabel.setText("Log: " + "An Error Occurred")
                self.update(self.logLabel)
        else:
            self.logLabel.setStyleSheet("color: red")
            self.logLabel.setText("Log: " + "Please Login First")
            self.update(self.logLabel)

    def update(self, label):
        label.adjustSize()

