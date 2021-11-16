/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appagenda;

import entidades.Provincia;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author amedi
 */
public class AppAgenda {

    
    public static void main(String[] args) 
    {
       
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("AppAgendaPU");
        EntityManager em=emf.createEntityManager();
        
        // Creamos las provincias
        Provincia provinciaCadiz = new Provincia();
        provinciaCadiz.setNombre("Cadiz");
        Provincia provinciaSevilla = new Provincia();
        provinciaSevilla.setNombre("Sevilla");
        
        // Se inicia la transacion
        em.getTransaction().begin();
        em.persist(provinciaSevilla);
        em.persist(provinciaCadiz);
        em.getTransaction().commit();
        
        
        // Se cierra la conexion
        em.close();
        emf.close();
        try
        {
            DriverManager.getConnection("jdbc:derby:BDAgenda;shutdown=true");
        } 
        catch (SQLException ex){}
    }
    
}


