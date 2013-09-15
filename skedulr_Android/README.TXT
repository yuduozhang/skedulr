======================================================================
UserID:	    y367zhang
StudentID:  20358891
Name:	    Yuduo Zhang 	
======================================================================

To get best experience from this app, please test on the phone.
The filter function of the course picker screen cannot work on emulator 
properly due to the slow computing speed. I tested my app on a HTC 
reserved from DC, everything including the filter just works greatly!

======================================================================
Design Philosophy: Simplicity
I don't like to add unnecessary features. I believe simplicity is just
beautiful.
======================================================================

======================================================================
Note
======================================================================
1. Please set "Compiler compliance level" to 1.6. (1.5 will cause
errors)

2. All going back behavior is achieved by pressing "go back" button.

3. Loading course list while logging in will take some time, please be
patient. (If Android alert dialog pops up, please press "Wait" button)

My tester:  Hao Chen, 20329692, h84chen@uwaterloo.ca

======================================================================
Most important extension:
======================================================================
Allow users to swap a scheduled course with a time conflicting course.
Please see Course Picker Screen description for more details.

This feature is strongly required by my tester since repeating
removing-and-adding process is annoying and should be done with a swapping
feature.

Also, my network error dialog allows users to go to wifi setting screen
easily.

======================================================================
Login Screen
======================================================================

Improvement
----------------------------------------------------------------------
1. If users returns to this screen after logging in, there are 1 more 
change to the screen: the "New Account" button will be removed. 
After users logout, "New Account" button will be readded.

Design reason: 
Users are not supposed to create new accounts while logging in.

2. If users press logout button while logging in, two different alert
dialogs will show:
    a. If the schedule has been changed since last save, the dialog will
       ask users whether they want to save before logout.
    b. If the schedule has not been changed since last save, the dialog
       ask users to confirm logout.

3. If users press "Go back" button while logging in, two different alert 
dialogs will show, which is similar to the two above but ask users to
confirm to exit.  

Design reason: 
Loggint out or exiting the app is the best time to trigger a save if 
necessary. Before logging out or exiting, a confirmation message can 
help users avoid logging out or exiting by pressing wrong buttons
accidently. I overwrite the "go back" button of android here.


======================================================================
Create Account Screen
======================================================================

Improvement
----------------------------------------------------------------------
1. Different design after the account is created successfully.
    a. An alert dialog will show to ask users whether they want to go
       back to login screen or not.
    b. If users click on "Yes", they will be brought back to login
       screen to login with the new registered account.
    c. If users click on "No", all the fields on this screen will be
       cleared so that users can create another account.

Design reason:
1. My tester reports to me that sometimes he would like to create an
account for his friend or he wants to create several accounts. It is not
always true that users want to login after creating an account
successfully. Hence it's better to let users decide what to do after
creating an account.

2. My tester reports that forcing users to enter login information again
can avoid the case that users type in wrong user name accidently.
(Oops, I cannot login, maybe I had a typo in the username. Nice
confirmation!)

3. Forcing users to enter login information again is also widely
distributed for security reason.

(I believe this is a design decision, it should not affect the marks
allocated to "Create Account Works")

======================================================================
Schedule Screen
======================================================================

Design
----------------------------------------------------------------------
1. Using drawable icon for the checkbox button. (The android default
ones are too big, and ugly)

2. To remove courses, users simply tab the checkbox and press "Remove"
button. Pressing one more button (remove) is asking users to confirm,
hence to avoid removing courses accidently.

3. Users are able to save schedule by pressing "Save" button on this
screen. (Users don't need to check the courses they want to save, the
schedule they see will be saved. Checkboxs are only for removing.)
If saved successfully, a confirmation dialog will appear. Users
will like to see this confirmation message since it will let them feel
more secure. (Ah, I know my schedule is saved now. i.e. without
encountering a network error or something)

4. By pressing the "Add" button, users will be brought to the course
picker screen.


======================================================================
Course Picker Screen
======================================================================

Improvement
----------------------------------------------------------------------
1. Swapping a scheduled course with a time conflicting course:
If users want to add a course that has time conflict with a
scheduled course, an alert dialog will show to ask whether users want to
swap them or not.

2. Used drawable icon to polish the UI. (If courses are added to
schedule, a '-' icon will be showed, if not, '+' will be showed. Users
can remove a course by tabbing on '-' or add by tabbing '+')

3. Courses in current schedule will be expanded so that users know what
courses they have already added, and they can decide whether to remove
them or not by tabbing on '-' icon.

Design
----------------------------------------------------------------------
1. Filter at the top. Users can type in any keyword of the course they
want to choose to narrow down the list of courses to consider.
(i.e. 
a. typing in "engl" will show all the English courses
b. typing in "operating" will show the courses of witch title include 
"operating".
Note: typing in only "cs" will not filter cs courses properly since too
many courses title include "cs". You need to type more information. e.g.
cs 350, etc
)

Note: Filter cannot work properly in emulator due to slow computing speed.
Please test this feature on a phone.

2. Courses are displayed in a expandable listview. Course title is
displayed so users are not required to know subject codes or course
numbers. By tabbing any item in the listview, the full course
information will be showed.

3. Courses that have time conflicts with current schedule will be
displayed in red text. Conflicting information is showed properly.

4. If users want to add a 6th course, an alert dialog will show. Users
can choose to go back to schedule screen to remove some courses, or stay
on current screen.

======================================================================
Handling Network Errors
======================================================================

Whenever an operation detects network errors, an alert dialog will show
and allow users to jump to wifi setting screen easily.
