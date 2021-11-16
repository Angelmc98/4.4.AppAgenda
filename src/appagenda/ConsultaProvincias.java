/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appagenda;

import entidades.Provincia;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author amedi
 */
public class ConsultaProvincias 
{

    public static void main(String[] args) 
    {
        EntityManagerFactory emf=
           Persistence.createEntityManagerFactory("AppAgendaPU");
        EntityManager em=emf.createEntityManager();
        
        // Obtenemos una lista completa de las provincias que se han almacenado en la tabla
        Query queryProvincias = em.createNamedQuery("Provincia.findAll");
        // Se ejecuta la consulta
        List<Provincia> listProvincias = queryProvincias.getResultList();
       // Recorremos el contenido de una lista
       for(int i=0;i<listProvincias.size();i++)
       {
           Provincia provincia=listProvincias.get(i);
           System.out.println(provincia.getNombre());
       }
       
       Query queryProvinciaCadiz = em.createNamedQuery("Provincia.findByNombre");
    queryProvinciaCadiz.setParameter("nombre", "Cadiz");
    List<Provincia> listProvinciasCadiz =queryProvinciaCadiz.getResultList();
    for(Provincia provinciaCadiz:listProvinciasCadiz)
    {
        System.out.println(provinciaCadiz.getId()+":");
        System.out.println(provinciaCadiz.getNombre());
    }
        
    
    Provincia provinciaId2=em.find(Provincia.class,2);
    if (provinciaId2 != null)
    {
        System.out.print(provinciaId2.getId() + ":");
        System.out.println(provinciaId2.getNombre());
    } 
    else 
    {
       System.out.println("No hay ninguna provincia con ID=2");
    }
    
   
    queryProvinciaCadiz.setParameter("nombre", "Cadiz");
    em.getTransaction().begin();
    for(Provincia provinciaCadiz : listProvinciasCadiz){
    provinciaCadiz.setCodigo("CA");
    em.merge(provinciaCadiz);  
    }
    em.getTransaction().commit();
    
    //elimiar provincia cuyo id 
    Provincia provinciaId15 = em.find(Provincia.class, 15);
    em.getTransaction().begin();
    if(provinciaId15 != null)
    {
        em.remove(provinciaId15);
    }
    else
    {
        System.out.println("No hay ninguna provincia con ID = 15");
    }
    
   }
    
}
