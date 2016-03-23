  
## JMS demo [![Build Status](https://semaphoreci.com/api/v1/dgroup/jspc-test/branches/master/shields_badge.svg)](https://semaphoreci.com/dgroup/jspc-test) [![Java version](https://img.shields.io/badge/java-8+-brightgreen.svg)](http://www.oracle.com/technetwork/java/javase/downloads/index.html)

This is simple example of JMS technology on Oracle Weblogic server.
See the [original article](https://blogs.oracle.com/soaproactive/entry/how_to_create_a_simple).


If you want to run this example, please perform steps below:

1. Download [GitHub project](https://github.com/dgroup/Servlets_demo/archive/master.zip);
2. Install [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html);
3. Install [Oracle WebLogic](http://www.oracle.com/technetwork/middleware/weblogic/downloads/wls-main-097127.html)
4. [Create domain, jms server, etc on WebLogic](https://blogs.oracle.com/soaproactive/entry/how_to_create_a_simple).
5. Start domain (`cd weblogic/wls12210/user_projects/domains/<your_domain>/bin/startWebLogic.cmd`)
6. Build project. Call script below in `cmd`
```cmd
    gradle build
```
7. Run the `examples.jms.queue.QueueSend` and `examples.jms.queue.QueueReceive` as general java classes;

## Quick tech overview
- JMS 1.2;
- Oracle WebLogic server;
- Gradle (build system); 
- PMD (code rules validation);

Have a fun.
