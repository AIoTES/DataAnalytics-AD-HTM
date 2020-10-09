FROM airhacks/glassfish:5.1.0

ADD https://repo1.maven.org/maven2/joda-time/joda-time/2.5/joda-time-2.5.jar /opt/glassfish5/glassfish/modules

COPY /target/WEB-INF /opt/glassfish5/glassfish/domains/domain1/autodeploy/ClickDigitalBackend_war_exploded/WEB-INF


