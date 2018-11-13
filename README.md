PhoneAutoTest-system-stability-testing Introduction
---
The System stability testing test script can generate an APP that can be installed on android smartphone.
The APP can drive the smartphone to automatically start up and exit specified applications and loop execution for 10 times.
The APP can also record the times that defects happens during applications start up and exit execution.
Email, Browser, Map, Phone are chosen as the application examples. Tester could also self-define the applications to be tested and the number of automatic test loops.
The operation guide is in the file BaseAccessibilityService.java. The number of loops can be modified in the file MainActivity.java.

