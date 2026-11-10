package com.metaspersonales.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/** Cierra Hibernate cuando Tomcat descarga la aplicación. */
@WebListener
public class HibernateShutdownListener implements ServletContextListener {
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        HibernateUtil.shutdown();
    }
}
