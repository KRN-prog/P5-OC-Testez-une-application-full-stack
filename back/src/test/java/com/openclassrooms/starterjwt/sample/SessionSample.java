package com.openclassrooms.starterjwt.sample;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class SessionSample {
    public static Session oneSessionSample() throws ParseException {

        String datestring = "2024-02-06 16:30:00";
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newDate = formatDate.parse(datestring);

        Clock customClock = Clock.fixed(
                Instant.parse("2024-02-06T16:30:00Z"),
                ZoneId.of("UTC"));
        LocalDateTime now = LocalDateTime.ofInstant(customClock.instant(), ZoneId.systemDefault());

        Session session = new Session();
        session.setId(1L);
        session.setName("jean");
        session.setDate(newDate);
        session.setDescription("This is a description");
        session.setTeacher(new Teacher(1L,"Dupont","Jordan", now, now));
        session.setUsers(List.of(new User(1L, "jean@gmail.com","Doe","jean","test123",false, now, now),
                new User(2L, "jeanne@gmail.com","Beanns","jeanne","password123",true, now, now)));
        session.setCreatedAt(now);
        session.setUpdatedAt(now);

        return session;
    }


    public static Session oneOtherSessionSample() throws ParseException {

        String datestring = "2023-02-06 16:30:00";
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newDate = formatDate.parse(datestring);

        Clock customClock = Clock.fixed(
                Instant.parse("2023-02-06T16:30:00Z"),
                ZoneId.of("UTC"));
        LocalDateTime now = LocalDateTime.ofInstant(customClock.instant(), ZoneId.systemDefault());

        Session session = new Session();
        session.setId(2L);
        session.setName("jeanne");
        session.setDate(newDate);
        session.setDescription("jeanne description");
        session.setTeacher(new Teacher(1L,"Kinda","Adrien", now, now));
        session.setUsers(List.of(new User(4L, "mireille@gmail.com","Wee","Mireille","mireillePass1",true, now, now),
                new User(6L, "bernard@gmail.com","Beald","Bernard","BernardBernard123",true, now, now)));
        session.setCreatedAt(now);
        session.setUpdatedAt(now);

        return session;
    }


    public static Session oneModifierSessionSample() throws ParseException {

        String datestring = "2024-02-06 16:30:00";
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newDate = formatDate.parse(datestring);

        Clock customClock = Clock.fixed(
                Instant.parse("2024-02-06T16:30:00Z"),
                ZoneId.of("UTC"));
        LocalDateTime now = LocalDateTime.ofInstant(customClock.instant(), ZoneId.systemDefault());

        Session session = new Session();
        session.setId(1L);
        session.setName("David");
        session.setDate(newDate);
        session.setDescription("This is a description");
        session.setTeacher(new Teacher(1L,"Dupont","Jordan", now, now));
        session.setUsers(List.of(new User(1L, "jean@gmail.com","Doe","jean","test123",false, now, now),
                new User(2L, "jeanne@gmail.com","Beanns","jeanne","password123",true, now, now)));
        session.setCreatedAt(now);
        session.setUpdatedAt(now);

        return session;
    }
}
