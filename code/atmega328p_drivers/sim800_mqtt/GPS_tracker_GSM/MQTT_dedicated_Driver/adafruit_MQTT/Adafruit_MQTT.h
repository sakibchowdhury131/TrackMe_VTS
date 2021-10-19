#ifndef _ADAFRUIT_MQTT_H_
#define _ADAFRUIT_MQTT_H_

#include "Arduino.h"

#if defined(ARDUINO_SAMD_ZERO) || defined(ARDUINO_STM32_FEATHER)
#define strncpy_P(dest, src, len) strncpy((dest), (src), (len))
#define strncasecmp_P(f1, f2, len) strncasecmp((f1), (f2), (len))
#endif

#define ADAFRUIT_MQTT_VERSION_MAJOR 0
#define ADAFRUIT_MQTT_VERSION_MINOR 17
#define ADAFRUIT_MQTT_VERSION_PATCH 0

// Uncomment/comment to turn on/off debug output messages.
//#define MQTT_DEBUG
// Uncomment/comment to turn on/off error output messages.
#define MQTT_ERROR

// Set where debug messages will be printed.
#define DEBUG_PRINTER Serial
// If using something like Zero or Due, change the above to SerialUSB

// Define actual debug output functions when necessary.
#ifdef MQTT_DEBUG
#define DEBUG_PRINT(...)                                                       \
  { DEBUG_PRINTER.print(__VA_ARGS__); }
#define DEBUG_PRINTLN(...)                                                     \
  { DEBUG_PRINTER.println(__VA_ARGS__); }
#define DEBUG_PRINTBUFFER(buffer, len)                                         \
  { printBuffer(buffer, len); }
#else
#define DEBUG_PRINT(...)                                                       \
  {}
#define DEBUG_PRINTLN(...)                                                     \
  {}
#define DEBUG_PRINTBUFFER(buffer, len)                                         \
  {}
#endif

#ifdef MQTT_ERROR
#define ERROR_PRINT(...)                                                       \
  { DEBUG_PRINTER.print(__VA_ARGS__); }
#define ERROR_PRINTLN(...)                                                     \
  { DEBUG_PRINTER.println(__VA_ARGS__); }
#define ERROR_PRINTBUFFER(buffer, len)                                         \
  { printBuffer(buffer, len); }
#else
#define ERROR_PRINT(...)                                                       \
  {}
#define ERROR_PRINTLN(...)                                                     \
  {}
#define ERROR_PRINTBUFFER(buffer, len)                                         \
  {}
#endif




// Use 3 (MQTT 3.0) or 4 (MQTT 3.1.1)
#define MQTT_PROTOCOL_LEVEL 4

#define MQTT_CTRL_CONNECT 0x1
#define MQTT_CTRL_CONNECTACK 0x2
#define MQTT_CTRL_PUBLISH 0x3
#define MQTT_CTRL_PUBACK 0x4
#define MQTT_CTRL_PUBREC 0x5
#define MQTT_CTRL_PUBREL 0x6
#define MQTT_CTRL_PUBCOMP 0x7
#define MQTT_CTRL_SUBSCRIBE 0x8
#define MQTT_CTRL_SUBACK 0x9
#define MQTT_CTRL_UNSUBSCRIBE 0xA
#define MQTT_CTRL_UNSUBACK 0xB
#define MQTT_CTRL_PINGREQ 0xC
#define MQTT_CTRL_PINGRESP 0xD
#define MQTT_CTRL_DISCONNECT 0xE

#define MQTT_QOS_1 0x1
#define MQTT_QOS_0 0x0

#define CONNECT_TIMEOUT_MS 6000
#define PUBLISH_TIMEOUT_MS 500
#define PING_TIMEOUT_MS 500
#define SUBACK_TIMEOUT_MS 500

// Adjust as necessary, in seconds.  Default to 5 minutes.
#define MQTT_CONN_KEEPALIVE 300

// Largest full packet we're able to send.
// Need to be able to store at least ~90 chars for a connect packet with full
// 23 char client ID.
#define MAXBUFFERSIZE (150)

#define MQTT_CONN_USERNAMEFLAG 0x80
#define MQTT_CONN_PASSWORDFLAG 0x40
#define MQTT_CONN_WILLRETAIN 0x20
#define MQTT_CONN_WILLQOS_1 0x08
#define MQTT_CONN_WILLQOS_2 0x18
#define MQTT_CONN_WILLFLAG 0x04
#define MQTT_CONN_CLEANSESSION 0x02

// how much data we save in a subscription object
// and how many subscriptions we want to be able to track.
#if defined(__AVR_ATmega32U4__) || defined(__AVR_ATmega328P__)
#define MAXSUBSCRIPTIONS 5
#define SUBSCRIPTIONDATALEN 20
#else
#define MAXSUBSCRIPTIONS 15
#define SUBSCRIPTIONDATALEN 100
#endif
