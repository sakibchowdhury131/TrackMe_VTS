#include "mqtt.h"
#include <SoftwareSerial.h>

SoftwareSerial SIM868(8, 7); //RX, TX
String broker = "test.mosquitto.org";
String port = "1883";
char ClientID[] = "dummyClient";
char Topic[] = "data";
char mqttMessage[127];
char Message[127];
bool setupDone = false; 
int8_t msgID = 0;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  SIM868.begin(9600);
  initializationProc();
  
}

void loop() {
  // put your main code here, to run repeatedly:
  if (setupDone == true){
      sendMQTTMessage(msgID);
      msgID++;
      //updateSerial();
    
  }


}


///////////////////////////////////////////////////////// Use this function for a two-way communication between master CPU and SIM868 //////////////////////////////////////////////////////


void updateSerial()
{
  delay(200);
  while (Serial.available()) 
  {
    SIM868.write(Serial.read());//Forward what Serial received to Software Serial Port
  }
  while(SIM868.available()) 
  {
    Serial.write(SIM868.read());//Forward what Software Serial received to Serial Port
  }
}


///////////////////////////////////////////////////////// read response of a command //////////////////////////////////////////////////////


String readResp(String cmd, int8_t responseLen){  // response length is needed to erase unwanted parts of the string
  String x = "";
  if (send_command(cmd)){
     x = SIM868.readString();
  }
  int8_t cmdLen = cmd.length();
  x.remove(0,cmdLen+1);
  x.trim();
  x.remove(responseLen, int8_t(x.length()-responseLen));
  return x;
}

/////////////////////////////////////////////////////////////////////////Send AT command to SIM868 /////////////////////////////////////////////////////////////////////////////////////////////////////////
bool send_command(String cmd)     // will be used to send AT command to the SIM chip
{
  SIM868.println(cmd);
  return true;
}

/////////////////////////////////////////////////////////////// check Handshake //////////////////////////////////////////////////////////

bool handshake(){
  String temp = readResp("AT", 2);
  if (temp == "OK"){
    return true;
  } else {
    return false;
  }
}

//////////////////////////////////check signal quality: will return integer  in range (0, 31)//////////////////////////////////////////
int8_t gsmSigQuality(){
  String temp = readResp("AT+CSQ", 10);
  int8_t indexOfColon = temp.indexOf(":");
  temp.remove(0, indexOfColon+2);
  int8_t indexOfComma = temp.indexOf(",");
  temp.remove(indexOfComma, temp.length()+1);
  return temp.toInt();
}

/////////////////////////////////////////////////////////////////////// Check GSM Signal Status /////////////////////////////////////////////////////////////////////////////////////////////////////////////

bool GSMSigCheck(){
  int8_t sigQuality = gsmSigQuality();
  if (sigQuality <=1){
    Serial.print("No Signal "+String(sigQuality)+ " "); 
    return false;
  } else if (sigQuality>1 && sigQuality <=9) {
    Serial.print("Extreme Weak Signal "+String(sigQuality)+ " ");
    return true; 
  } else if(sigQuality > 9 && sigQuality <= 14) {
    Serial.print("Weak Signal "+String(sigQuality)+ " ");
    return true;
  } else if(sigQuality > 14 && sigQuality <= 19) {
    Serial.print("Good Signal "+String(sigQuality)+ " ");
    return true;
  } else {
    Serial.print("Excellent Signal "+String(sigQuality) + " ");
    return true;
  }
}

///////////////////////////////////////////////////////////////Check network registration /////////////////////////////////////////////////////
int8_t checkNetReg(){
  String temp = readResp("AT+CREG?", 10);
  temp.remove(0,temp.indexOf(",")+1);
  temp.remove(1, temp.length()+1);
  return temp.toInt();
}

//////////////////////////////////////////////////////// Check GPRS Connection ///////////////////////////////////////////////////////
bool checkGPRS(){
  String temp = readResp("AT+CGATT?", 9);
  temp.remove(0, temp.indexOf(":")+2);
  temp.remove(1, temp.length()+1);
  if (temp.toInt() == 1){
    return true;
  } else {
    return false;
  }
}

///////////////////////////////////////////////////////// enable GPRS connection /////////////////////////////////////////////////////////

void connectToGPRS(){
  readResp("AT+CGATT=1", 2);
}

/////////////////////////////////////////////////////// setup APN ////////////////////////////////////////////////////////////////////////

bool setAPN(String APN){  
  String check = readResp("AT+CSTT?", 13+APN.length());
  check.remove(0, check.indexOf(":")+2);
  check.remove(check.indexOf(","), check.length()+1);

  if (check == APN){
    return true;
  }
  String temp = readResp("AT+CSTT="+APN, 2);
  if (temp == "OK"){
    return true;
  } else {
    return false;
  }
}

//////////////////////////////////////////////////// Bring up wireless connection (GPRS or CSD) ////////////////////////////////////////////

bool selectGPRS(){
  String temp = readResp("AT+CIICR", 2);
  if (temp == "OK"){
    return true;
  } else {
    return false;
  }
}

/////////////////////////////////////////////// get local IP ////////////////////////////////////////////////////////////////////

String getIP(){
  String temp = readResp("AT+CIFSR", 15);
  return temp;
}

//////////////////////////////////////////////////////////////Connect to Internet //////////////////////////////////////////////////////////

bool connectInternet(){
  String APN = "\"wap\"";
  Serial.print("Setting APN ("+APN+") ----------------> ");
  if (setAPN(APN)){
    Serial.println("OK");
    Serial.print("selecting GPRS ----------------------------> ");
    if (selectGPRS()){
      Serial.println("OK");     
      Serial.println(getIP()); 
      return true;
    } else {
      Serial.println("Failed");
      return false;
    }
  } else {
    Serial.println("Failed");
    return false; 
  }
}

//////////////// establish TCP Connection ///////////////////////////////

  
bool connectTCP(String brokerUrl, String brokerPort){

  String connectionCMD = "AT+CIPSTART=\"TCP\",\""+brokerUrl+"\",\""+brokerPort+"\"";
  String response = readResp(connectionCMD, 2);
  Serial.println(response);
  if (response == "OK"){
    String secondresponse = "";
    while (secondresponse.length() <2){
      secondresponse = "";
      secondresponse = SIM868.readString();
    }
    secondresponse.remove(0,secondresponse.indexOf("CONNECT"));
    secondresponse.remove(10,secondresponse.length());

    if (secondresponse == "CONNECT OK"){
      //Serial.println("Second Response received: "+secondresponse);
      return true;
    } else {
      //Serial.println("Second Response received: "+secondresponse);
      return false;
    }
    
  } else {
    return false; 
  }
}

//////////////////////////////////////////////////////////////// connect MQTT ////////////////////////////////////////////////////////////////////////////////

void connectMQTT(char* clientID){
  int8_t mqttMessageLength = 16 + strlen(clientID);
  mqtt_connect_message(mqttMessage, clientID);
  readResp("AT+CIPSEND", 1);
  for (int j = 0; j < mqttMessageLength; j++) {
    SIM868.write(mqttMessage[j]);
  }
  SIM868.write(byte(26)); 
  delay(2000);
  Serial.println("MQTT Connected");
}

//////////////////////////////////////////////////////////////// build json data ///////////////////////////////////////////////////////////////////////////////////////

String buildJson(int8_t id) {
  String data = "{";
  data+="\n";
  data+= "\"dummyData\": {";
  data+="\n";
  data+="\"ID\": \"#testID\"";
  data+=id;
  data+="\n";
  data+="\"latitude\": ";
  data+="91.28";
  data+="\n";
  data+="\"longitude\": ";
  data+="24.86";
  data+="\n";
  data+="}";
  data+="\n";
  data+="}";
  return data;
}

///////////////////////////////////////////////////////////////// MQTT publish ///////////////////////////////////////////////////////////////////////////////////

void sendMQTTMessage(int8_t id){
  String temp = buildJson(id);
  temp.toCharArray(Message, temp.length());
  
  

  send_command("AT+CIPSEND");
  delay(500);
  int8_t mqttMessageLength = 4 + strlen(Topic) + strlen(Message);
  //Serial.println(mqttMessageLength);
  mqtt_publish_message(mqttMessage, Topic, Message);
  for (int k = 0; k < mqttMessageLength; k++) {
    SIM868.write(mqttMessage[k]);
  }
  SIM868.write(byte(26)); // (signals end of message)
  Serial.println("-------------Sent-------------"); // Message contents
  String response = "";
  while (response.length() <=0){
    response = SIM868.readString();
  }

  Serial.println(response);
  delay(500);
  //SIM868.println("AT+CIPSHUT");
}

///////////////////////////////////////////////////////////////// this function will handle initial checking procedures ////////////////////////////////////////

void initializationProc(){
  Serial.println("**************************initializing basic test connection procedure *********************");
  //////////// checking AT command /////////////////////////////
  Serial.print("Matching Baud-rate ------------------------> ");
  if (handshake()){
    Serial.println("OK");
  } else {
    Serial.println("Failed");
  }

  /////////// Network Registration ////////////////////////////

  while (checkNetReg() != 1){
    Serial.println("waiting for successful network registration...");
    delay(10000);
  }
  Serial.println("Network Registeration ---------------------> OK");


  ////////////checking GSM Signal Status /////////////////////////

  Serial.print("Checking GSM signal -----------------------> ");
  if (GSMSigCheck()){
    Serial.println("(OK)");




    ///////////////check GPRS //////////////////////////////////////
    Serial.print("Checking GPRS signal ----------------------> ");
    if (checkGPRS()){
      Serial.println("OK");


      ///////////////check GPRS //////////////////////////////////////
      Serial.print("Checking GPRS signal ----------------------> ");
      if (checkGPRS()){
        Serial.println("OK");

        //////////////// connect internet //////////////////////////////
        //SIM868.println("AT+CIPSHUT");
        //delay(1000);
        Serial.println("**************************initializing internet connection procedure ***********************");
        if (connectInternet()){
          Serial.println("Internet Connection Successfull");
          Serial.print("Establishing TCP/IP connection -------------> ");
          if(connectTCP(broker, port)){
            Serial.println("OK");
          } else {
            Serial.println("Failed");
          }
          connectMQTT(ClientID);



          setupDone = true; 
        } else {
          Serial.println("Internet Connection failed");
        }

      } else {
        Serial.println("Failed"); 
        connectToGPRS();
      }



      
    } else {
      Serial.println("Failed"); 
      connectToGPRS();
    }
  } else {
    Serial.println("(Failed)");
  }

  



  

}
  
