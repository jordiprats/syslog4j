# syslog4j

syslog 4 tomcat

This valve will send tomcat logs to syslog

```
\<Valve className="es.systemadmin.syslog4j.Syslog4Tomcat" pattern="%v %{X-Forwarded-For}i %l %u %t "%r" %s %b"/\>
```

Create a syslog configuration file in tomcat's conf directory:

```
\# echo "1.2.3.4:514" > conf/syslog
```

For futher details, please see: http://systemadmin.es/2013/02/mandar-logs-de-tomcat-a-syslog
