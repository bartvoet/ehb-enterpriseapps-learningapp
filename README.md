
# Learning-project

## Concept

The **basic concept** is an build an **application** you can use to **prepare and test** yourself
for an exam or other stuff you want know by heart...

For this the core of the application exists out of 3 concepts:

* **Question**  
  A class that asks you a question and validates the response
* **QuestionSeries**  
  A series or group questions related to one each other
* **QuestionSession**  
  Represents the actual questioning.   
  It contains state (which quetions are finished and which not)
  and picks the questions randomly
  

~~~
  +-------------+               +-------------+
  |             |               |             |
  |   SERIES    +-->CONTAINS<-->+  QUESTION   |
  |             |               |             |
  +------+------+               +---+---------+
         ^                          ^
         |                          |
      <LOADS>                   <CONTAINS>
         |                          |
  +------+-------+                  |
  |              |                  |
  |   SESSION    +------------------+
  |              |
  +--------------+
~~~

> At the time of writing only one type of question is supported
> but the aim is to support multiple types (like questions based
> on image, sound, ...)

### User-representation

Within the context of a webapplication these objects are **linked**
to a **user**.  

A user can **create** as **series** (or import through a CSV).  
If he want to use/run these questions this is done through a session.  

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

The **session** **loads** (when asked) the **questions** coming from a series 
and start asking this questions until the user is capable of answering all the 
questions.

The **session**-object picks randomly questions and keeps in the STARTED-state
until the user has answerd all the question correctly.

In the context of the application both the series and the session belong
to a user.

### QuestionInterface

The core of the application is decoupled from the operating environment
(webapp, desktop, command line, mobile, ...) and can be used

The algorithm can be executed in both a Spring Boot-application
as in command line.

Following snippet of code demonstrates this within a command line-app

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

### RandomPicker for testability

To make the core of the system testable you can stub/manipulate the random
behavior of a QuestionSession through an interface called QuestionPicker
shown below.

~~~java
package be.ehb.bvo.leanring.algo;

public interface QuestionPicker {
    int pickQuestion(int fromTotal);
}
~~~

In order to make a session predictable (and testable) you can thus inject through the
constructor the RandomPicker

~~~java
...
new QuestionSession(myMockedPicker);
...
~~~

By default a RandomQuestionPicker is used

~~~java
public QuestionSession() {
    this(new RandomQuestionPicker());
}
~~~

## Spring Boot & Configuration

### Security

The security-code is isolated in the security-package.  

#### Binding to JPA

A binding is foreseen ot to the User-entity in our database (JPA).  
This is done through

* The registration of **UserDetailsServiceImpl** (implementing UserDetailService)

~~~java
@Bean
public UserDetailsService userDetailsService() {
    return new UserDetailsServiceImpl();
}
~~~

* This is exposed as a Bean from the WebSecurityConfig-class

~~~java
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
...
~~~

* This UserDetailsServiceImpl will load a user throug JPA 
* And wrap it in a custom UserDetails-implementation

~~~java
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepository.findByName(username);

        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        return new LearningUserDetails(user);
    }
}
~~~

* The **binding** to **Spring Security** is then done throug the exposure of 
  a DaoAuthenticationProvider-bean as an AuthProvider

~~~java
@Bean
public DaoAuthenticationProvider authProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService());
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
}
~~~

#### Not storing raw passwords with BCrypt

* In order to avoid clear passwords **BCrypt** is used
* And exposed as a **bean** from the WebSecurityConfig

~~~java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
~~~

* Reused in the controller

~~~java
@Autowired
private PasswordEncoder encoder;

...

@PostMapping("/create")
public String greetingSubmit(@ModelAttribute User newuser, Model model) {
    newuser.setPassword((encoder.encode(newuser.getPassword())));
    userRepository.save(newuser);
    model.addAttribute("newuser", newuser);
    return "confirm";
}
~~~

> Note:  
> Designwise I'm going to move this out of the controller and isolate this 
> in a dedicated Repository or Service as I'm also exposing Rest-services 
> through different controllers.

#### HTTPS

HTTPS is enforced through a selfsigned PKS12-file

~~~properties
server.ssl.key-store-type=PKCS12
server.ssl.key-store=${SSL_KEYFILE}
server.ssl.key-store-password=${SSL_PASSWORD}
server.ssl.key-alias=${SSL_ALIAS}
server.ssl.enabled=true
~~~

This is only activated in case of the production-profile

### Config

#### Profiles

Extra prod/see config

#### Docker and k8s

#### Env-variables

### Message-properties for i118n

### CSV-upload and -download

### Both rest and ui-controllers

### Commandline-version


(will send another document)
