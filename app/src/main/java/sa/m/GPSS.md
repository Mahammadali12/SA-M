
GENERATE (Exponential (1,0,0.71));

ASSIGN proc_time,(Normal(1,2, 0.3))

LBL QUEUE BUF; 

SEIZE SERVER;occupying server

DEPART BUF;

ADVANCE P$proc_time; delay is read from attribute

RELEASE SERVER
TERMINATE 0; delete processed job, update counter

GENERATE (Exponential(1,0,0.25));
ASSIGN proc_time,(Normal(1,2.5,0.5)) 
TRANSFER ,LBL;

GENERATE 500; stop timer
TERMINATE 1

START 1; automatic start (optional)
