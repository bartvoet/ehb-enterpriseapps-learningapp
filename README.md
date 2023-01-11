
# Learning-project

## Concept

The basic concept is an application you can use to prepare and test yourself
for an exam or other stuff you want know by heart...

For this the core of the application exists out of 3 concepts:

* **Question**  
  A class that asks you a question and validates the response
* **QuestionSeries**  
  A series or group questions related to one each other
* **QuestionSession**  
  

~~~
  +-------------+               +-------------+
  |             |               |             |
  |   SERIES    +-->CONTAINS<-->+  QUESTION   |
  |             |               |             |
  +------+------+               +---+---------+
        ^                          ^
        +                          +
      <LOADS>                 <CONTAINS>
        +                          +
  +------+-------+                  |
  |              |                  |
  |   SESSION    +------------------+
  |              |
  +--------------+
~~~

> At the time of writing only one type of question is supported
> but the aim is to support multiple types (like questions based
> on image, sound, ...)

## User-representation

Within the context of a webapplication these objects are **linked**
to a **user**.  

A user can **create** as **series** (or import through a CSV).  
If he want to use/run these questions this is done through a session.  

This **session** will **load** the **questions** coming from a series and start
asking this questions until the user is capable of answering all the 
questions.

The **session**-object picks randomly questions and keeps in the STARTED-state
until the user has answerd all the question correctly.

In the context of the application both the series and the session belong
to a user.

~~~
   +----------+           +-------------+               +-------------+
   |          |           |             |               |             |
   |   USER   +---------->+   SERIES    +-->CONTAINS<-->+  QUESTION   |
   |          |           |             |               |             |
   +-----+----+           +------+------+               +---+---------+
         |                       ^                          ^     
         |                       |                          |   
         |                     <LOADS>                 <CONTAINS> 
         |                       |                          |  
         |                +------+-------+                  |  
         |                |              |                  |  
         +--------------->+   SESSION    +------------------+  
                          |              |                     
                          +--------------+                     
~~~

### QuestionInterface

The algorithm can be executed in both a Spring Boot-application
as in command line.

Following code demonstrates this with a command line-app

~~~java
    public static void main(String[] args) {
        QuestionSession session = new QuestionSession()
                .addQuestion(new ListQuestion("Naam?").withAnswers("Bart", "Voet"))
                .addQuestion(new ListQuestion("Leeftijd").withAnswers("48"))
                .startSession();

        ScannerQuestionInterface qi = new ScannerQuestionInterface();

        while(!session.hasEnded()) {
            QuestionFeedback feecback = session.askAndValidate(qi);
            if(feecback.isValid()) {
                System.out.println("OK");
            } else {
                System.out.println("NOK");
            }
        }
    }
~~~

To be able to adapt the algorithm to your system you need to implement
you're **own version** of the **QuestionInterface**

~~~java
package be.ehb.bvo.leanring.algo;

import java.util.List;

public interface QuestionInterface {
    List<String> askList(String question, int size);

    List getListOfAnswers();

}
~~~

As shown in the underlying diagram...

~~~
   +----------+           +-------------+               +-------------+
   |          |           |             |               |             |
   |   USER   +---------->+   SERIES    +-->CONTAINS<-->+  QUESTION   |
   |          |           |             |               |             |
   +----------+           +------+------+               +---+-----+---+
                                 ^                          ^     |    
                                 |                          |     |    
                               <LOADS>               <CONTAINS>   |    
                                 |                          |     |    
                          +------+-------+                  |     |    
                          |              |                  |     |
                          |   SESSION    +------------------+     |
                          |              |                        |
                          +--------------+                        |
                                                        <USES FOR INTERACTION>
                                                                  |
                                                                  |
\\|//                  +--------------------+                     |
+'.'+                  |                    |                     |
/[ ]\  +-------------->+  QUESTIONINTERFACE +---------------------+
_| \_                  |                    |
student                +--------------------+
                          SYSTEMDEPENDENT
~~~


### 