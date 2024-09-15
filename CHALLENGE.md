# Doctor’s appointment
We want to write a very simple appointment application for one doctor. Patients will view all the available time slots and will take on of them by entering their info.

### Story: Doctor adds open times.
As a doctor I would like to add a start and end time for each day, so that this time is broken down into 30 minutes periods. If one of the periods it becomes less than 30 minutes during breakdown, then it should be ignored.

#### Test cases:
1. If doctor enters an end date that is sooner than start date, appropriate error should be shown
2. If doctor enters start and end date so that the period is less than 30 minutes then no time should be added.

### Story: Doctor can view 30 minutes appointments
As a doctor I would like to see my open (not taken by patients) and taken 30 minutes appointment.

#### Test cases:
1. If there is no appointment set, empty list should be shown.
2. If there are some taken appointment, then phone number and name of the patient should also be shown.

### Story: Doctor can delete open appointment
As a doctor I would like to be able to delete some of my open appointments.

#### Test cases:
1. If there is no open appointment then 404 error is shown.
2. If the appointment is taken by a patient, then a 406 error is shown
3. **Concurrency check**; if doctor is deleting the same appointment that a patient is taking at the same time.

### Story: Patients can view a doctor open appointment
As a patient I like to be able to see all the open appointments for the given day. So, I can take one of these appointments.

#### Test cases:
1. If the doctor doesn’t have any open appointment that day, then, an empty list should be shown.

### Story: Patients can take an open appointment
As a patient I like to be able to take an open appointment by giving my name and phone number.

#### Test cases:
1. If either phone number or name is not given, then an appropriate error message should be given.
2. If the appointment is already **taken** or **deleted**, then an appropriate error message should be given.
3. **Concurrency check**; patient is taking an appointment that is in the process of deletion or being taken by another patient.

### Story: Patients can view their own appointments
As a patient I like to be able to view my own appointments, providing only my phone number.

#### Test cases:
1. If there is no appointment with this phone number, then an empty list should be shown.
2. If there are more than one appointment taken by this user, then all should be shown.

