package com.space.testingentity;


import com.space.model.Ship;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.Query;

public class App {
    public static void main(String[] args) {
        SessionFactory sessionFactory = new Configuration()
                .configure()
                .buildSessionFactory();

        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from Ship where id = 1");
        Ship ship = (Ship) query.getSingleResult();
        System.out.println(ship.toString());

        session.close();
        sessionFactory.close();

    }
}
