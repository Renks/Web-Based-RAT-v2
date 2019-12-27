# Web-Based-RAT-v2
This is a web based rat I created a while ago using Java, It was initially created to bypass ISP firewall and portforwarding.

# Why?
I used to use client server applications for basic communication which worked fine as long as you're on local network — But If you go WAN/Public/Internet mode you'd have to setup portforwarding(which is easy enough to configure) However, In some areas ISP's filter/block all the ports unless you pay certain amount of money to activate or buy a premium plan. So I figured I'd have to find a workaround, and This is the result.
FYI, It isn't actually a RAT(I don't know why I named it "RAT") but an app which enables communication between two machines on the Internet.

# How it works?
Let's suppose you have a machine #1 and it needs to communicate with machine #2 on the internet. But as soon as machine #1 sends packets to machine #2; The ISP of machine #2 recieves the packet and drops it, since machine #2 never really requested for those. So to avoid blocking you make the machine #2 request for those packets by making it connect to a webserver. Then, machine #1 also connects to the same webserver and both machines communicate with each other through the webserver. In this case the machines are the "Clients" and webserver is the "Server" they connect to.

# Taking it even further
You might have seen those services such as ngrock,servo.net,VPNs etc. which allow you to establish connection temporarily(usually once). They can be pretty useful in our case since this web based connection isn't realtime. There is an intentional delay between request and response to limit the resource use. But we can use ngrock(or any other one-time services listed above) to establish connection by sending its address through the webserver to the machine #2 which then automatically connects to machine #1 with the new ngrock address. Then the connection is Realtime! and even if you exit or lose the session you can send the new address through the webserver provided by the ngrock to establish the connection again.
