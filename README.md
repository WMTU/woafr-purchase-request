woafr-purchase-request
======================

Current version: 1.0

Java widget for requesting music purchases in WO Automation for Radio

Required Java libraries: Apache Commons Lang, MySQL Connector/J.

Widget package structure: (.zip file)

```
+---classes
|   +---edu
|       +---mtu
|           +---wmtu
|               +---resources
|               |   \---purchase.png
|               |   \---messages.properties
|               \---PurchaseRequestWidget.class
+---lib
|   /---commons-lang3-3.3.2.jar
|   /---mysql-connector-java-5.1.34-bin.jar
+---resources
|   /---config.properties
/---plugin.xml
```

===
#### PurchaseRequestWidget.java

The java source of the widget, with the database credentials removed.

===
#### plugin.xml

An xml file that describes the packaged widget.

===
#### resources/config.properties.example

Example database configuration.

===
#### classes/edu/mtu/wmtu/resources/purchase.png

An icon for the widget in the WO Automation for Radio UI.
