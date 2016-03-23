## JMS demo [![Build Status](https://semaphoreci.com/api/v1/dgroup/jms-test/branches/master/shields_badge.svg)](https://semaphoreci.com/dgroup/jms-test) [![Java version](https://img.shields.io/badge/java-8+-brightgreen.svg)](http://www.oracle.com/technetwork/java/javase/downloads/index.html)

This is simple example of JMS technology on Oracle Weblogic server. See the [original article](https://blogs.oracle.com/soaproactive/entry/how_to_create_a_simple).
Also jms-related materials:
- [What's New in JMS 2.0, Part One: Ease of Use](http://www.oracle.com/technetwork/articles/java/jms20-1947669.html)
- [Н.Алименков](https://www.youtube.com/watch?v=ExjPxDxkmFo) [vs](http://www.slideshare.net/alimenkou/do-we-need-jms-in-21st-century) [JMS](https://www.youtube.com/watch?v=RVwXdCfzJZA) (о граблях JMS)
 

If you want to run this example, please perform steps below:

1. Download [GitHub project](https://github.com/dgroup/Servlets_demo/archive/master.zip);
2. Install [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html);
3. Install [Oracle WebLogic](http://www.oracle.com/technetwork/middleware/weblogic/downloads/wls-main-097127.html)
4. [Create domain, jms server, etc on WebLogic](https://blogs.oracle.com/soaproactive/entry/how_to_create_a_simple).
5. Start domain (`cd weblogic/wls12210/user_projects/domains/<your_domain>/bin/startWebLogic.cmd`)
6. Build project. Call `gradle build` in `cmd`
7. Run the `examples.jms.queue.QueueSend` and `examples.jms.queue.QueueReceive` as general java classes;

## Quick tech overview
- JMS 1.2;
- Oracle WebLogic server;
- Gradle (build system); 
- PMD (code rules validation);

## jclub 
New **POST** request to url `https://hooks.slack.com/services/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` 
with text  
```json
{
        "text": "<...>"
}
```

Have a fun.