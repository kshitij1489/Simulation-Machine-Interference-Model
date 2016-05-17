# Simulation-Machine-Interference-Model

Problem Statement
---
There are M machines in a system. Each machine is operational for a period of time and then it breaks down.
Once broken, it remains non-operational until it has been fixed by a repairman. Repairman takes a certain period of time to fix a machine. Broken machines are put in a queue and are served in a FIFO manner. The current implementation has one repairman, i.e M/M/1 queueing model. This model could be used to
answer questions like Mean operational Time, Mean waiting time of the server, Mean length of the queue. 

This problem has numerous applications in computer networks, supply chain, manufacturing, call centers etc.

*Implementation*

* In our implementation, the mean operational time and mean service time follows  exponential distribution. It can be easily extended to handle any distribution.
* Currently this simulation M/M/1 queueing model.
* The default output prints the status of each machine(i.e next breakdown time), queue length, next service time.

