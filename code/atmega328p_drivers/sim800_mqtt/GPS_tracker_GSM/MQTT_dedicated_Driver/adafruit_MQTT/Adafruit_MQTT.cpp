#include "Adafruit_MQTT.h"

#if defined(ARDUINO_SAMD_ZERO) || defined(ARDUINO_SAMD_MKR1000) ||             \
    defined(ARDUINO_SAMD_MKR1010) || defined(ARDUINO_ARCH_SAMD)
static char *dtostrf(double val, signed char width, unsigned char prec,
                     char *sout) {
  char fmt[20];
  sprintf(fmt, "%%%d.%df", width, prec);
  sprintf(sout, fmt, val);
  return sout;
}
#endif

#if defined(ESP8266)
int strncasecmp(const char *str1, const char *str2, int len) {
  int d = 0;
  while (len--) {
    int c1 = tolower(*str1++);
    int c2 = tolower(*str2++);
    if (((d = c1 - c2) != 0) || (c2 == '\0')) {
      return d;
    }
  }
  return 0;
}
#endif




void printBuffer(uint8_t *buffer, uint16_t len) {
  DEBUG_PRINTER.print('\t');
  for (uint16_t i = 0; i < len; i++) {
    if (isprint(buffer[i]))
      DEBUG_PRINTER.write(buffer[i]);
    else
      DEBUG_PRINTER.print(" ");
    DEBUG_PRINTER.print(F(" [0x"));
    if (buffer[i] < 0x10)
      DEBUG_PRINTER.print("0");
    DEBUG_PRINTER.print(buffer[i], HEX);
    DEBUG_PRINTER.print("], ");
    if (i % 8 == 7) {
      DEBUG_PRINTER.print("\n\t");
    }
  }
  DEBUG_PRINTER.println();
}

/* Not used now, but might be useful in the future
static uint8_t *stringprint(uint8_t *p, char *s) {
  uint16_t len = strlen(s);
  p[0] = len >> 8; p++;
  p[0] = len & 0xFF; p++;
  memmove(p, s, len);
  return p+len;
}
*/





static uint8_t *stringprint(uint8_t *p, const char *s, uint16_t maxlen = 0) {
  // If maxlen is specified (has a non-zero value) then use it as the maximum
  // length of the source string to write to the buffer.  Otherwise write
  // the entire source string.
  uint16_t len = strlen(s);
  if (maxlen > 0 && len > maxlen) {
    len = maxlen;
  }
  /*
  for (uint8_t i=0; i<len; i++) {
    Serial.write(pgm_read_byte(s+i));
  }
  */
  p[0] = len >> 8;
  p++;
  p[0] = len & 0xFF;
  p++;
  strncpy((char *)p, s, len);
  return p + len;
}





// Adafruit_MQTT Definition ////////////////////////////////////////////////////

Adafruit_MQTT::Adafruit_MQTT(const char *server, uint16_t port, const char *cid,
                             const char *user, const char *pass) {
  servername = server;
  portnum = port;
  clientid = cid;
  username = user;
  password = pass;

  // reset subscriptions
  for (uint8_t i = 0; i < MAXSUBSCRIPTIONS; i++) {
    subscriptions[i] = 0;
  }

  will_topic = 0;
  will_payload = 0;
  will_qos = 0;
  will_retain = 0;

  packet_id_counter = 0;
}



Adafruit_MQTT::Adafruit_MQTT(const char *server, uint16_t port,
                             const char *user, const char *pass) {
  servername = server;
  portnum = port;
  clientid = "";
  username = user;
  password = pass;

  // reset subscriptions
  for (uint8_t i = 0; i < MAXSUBSCRIPTIONS; i++) {
    subscriptions[i] = 0;
  }

  will_topic = 0;
  will_payload = 0;
  will_qos = 0;
  will_retain = 0;

  packet_id_counter = 0;
}
