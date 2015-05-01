Commentary
--------------------
Each available resource runs in its own thread. All resources take the next message from the shared queue. 
The Queue is ordered by the message's group and by the time the message was received. It's assumed that the message
group is represented by a long number. If it isn't a number than the message group has to be mapped to a number
depending on when the message was received. The queue is a default implementation from java platform concurrent package.  
