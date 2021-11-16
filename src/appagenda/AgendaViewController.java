/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package appagenda;

import entidades.Persona;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * FXML Controller class
 *
 * @author amedi
 */
public class AgendaViewController implements Initializable {

    private EntityManager entityManager;
    @FXML
    private TableView<Persona> tableViewAgenda;
    @FXML
    private TableColumn<Persona,String> columnNombre;
    @FXML
    private TableColumn<Persona,String> columnApellidos;
    @FXML
    private TableColumn<Persona,String> columnEmail;
    @FXML
    private TableColumn<Persona,String> columnProvincia;
    @FXML
    private TextField textFieldNombre;
    @FXML
    private TextField textFieldApellidos;
    
    private Persona personaSeleccionada;
    @FXML
    private AnchorPane rootAgendaView;
    
    
    public void setEntityManager(EntityManager entityManager)
    {
        this.entityManager=entityManager;
    }   
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        
        columnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        
        columnApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        columnProvincia.setCellValueFactory(
                cellData->{
                    SimpleStringProperty property=new SimpleStringProperty();
                    if(cellData.getValue().getProvincia()!=null){
                        property.setValue(cellData.getValue().getProvincia().getNombre());
                    }
                    return property;
                });
        
        tableViewAgenda.getSelectionModel().selectedItemProperty().addListener(
                (observable,oldValue,newValue)->{
                    personaSeleccionada=newValue;
                    //Mostramos los valores del nombre y apellidos
                    if(personaSeleccionada != null)
                    {
                        textFieldNombre.setText(personaSeleccionada.getNombre());
                        textFieldApellidos.setText(personaSeleccionada.getApellidos());
                    }
                    else
                    {
                        textFieldNombre.setText("");
                        textFieldApellidos.setText("");
                    }
                });
        
    }    
    
    // Mostramos todos los registros de la tabla Persona
    public void cargarTodasPersonas()
    {
        
        Query queryPersonaFindAll = entityManager.createNamedQuery("Persona.findAll");
        List<Persona> listPersona=queryPersonaFindAll.getResultList();
        tableViewAgenda.setItems(FXCollections.observableArrayList(listPersona));
        
    }

    @FXML
    private void onActionButtonGuardar(ActionEvent event)
    {
        if(personaSeleccionada != null)
        {
            personaSeleccionada.setNombre(textFieldNombre.getText());
            personaSeleccionada.setApellidos(textFieldApellidos.getText());
            
            //Actualizamos el objeto
            entityManager.getTransaction().begin();
            entityManager.merge(personaSeleccionada);
            entityManager.getTransaction().commit();
            
            // actualizamos en el TableView los valores del obejto
            int numFilaSeleccionada = tableViewAgenda.getSelectionModel().getSelectedIndex();
            tableViewAgenda.getItems().set(numFilaSeleccionada, personaSeleccionada);
                    
            // El foco vuelve a TableView
            TablePosition pos = new TablePosition(tableViewAgenda, numFilaSeleccionada, null);
            tableViewAgenda.getFocusModel().focus(pos);
            tableViewAgenda.requestFocus();
            
        }
        
        
    }

    @FXML
    private void onActionButtonNuevo(ActionEvent event) 
    {
        try
        {
            //Cargar la vista detalle
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PersonaDetalleView.fxml"));
            Parent rootDetalleView=fxmlLoader.load();
            
             // Ocultar la vista de la lista
            rootAgendaView.setVisible(false);
            //Añadir la vista detalle al StackPane principal para que se muestre
            StackPane rootMain =
                (StackPane) rootAgendaView.getScene().getRoot();
            rootMain.getChildren().add(rootDetalleView);
            
            PersonaDetalleViewController personaDetalleViewController = (PersonaDetalleViewController) fxmlLoader.getController();
            personaDetalleViewController.setRootAgendaView(rootAgendaView);
           
          
            personaDetalleViewController.setTableViewPrevio(tableViewAgenda);
            personaSeleccionada = new Persona();
            personaDetalleViewController.setPersona(entityManager, personaSeleccionada, true);
            //Mostrar datos actuales del objeto
            personaDetalleViewController.mostrarDatos();
        }
        catch(IOException ex)
        {
            Logger.getLogger(AgendaViewController.class.getName()).log(Level.SEVERE, null, ex);
            
        }
  
        
    }
    

    @FXML
    private void onActionButtonEditar(ActionEvent event) throws IOException 
    {
        try
        {
            //Cargar la vista de detalle
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PersonaDetalleView.fxml"));
           Parent rootDetalleView = fxmlLoader.load();
           
            // Ocultar la vista de la lista
            rootAgendaView.setVisible(false);
            
            // Añadir la vista detalle al StackPane principal para que se muestre
            StackPane rootMain = (StackPane) rootAgendaView.getScene().getRoot();
            rootMain.getChildren().add(rootDetalleView);
           
            PersonaDetalleViewController personaDetalleViewController = (PersonaDetalleViewController) fxmlLoader.getController();
            personaDetalleViewController.setRootAgendaView(rootAgendaView);

            personaDetalleViewController.setTableViewPrevio(tableViewAgenda);
            personaDetalleViewController.setPersona(entityManager, personaSeleccionada,false);
            //Mostrar datos actuales del objeto
            personaDetalleViewController.mostrarDatos();
        }
        catch (IOException ex)
        {
            Logger.getLogger(AgendaViewController.class.getName()).log(Level.SEVERE,null,ex);
        }
    
    }

    @FXML
    private void onActionButtonSuprimir(ActionEvent event) 
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar");
        alert.setHeaderText("¿Desea suprimir el siguiente registro?");
        alert.setContentText(personaSeleccionada.getNombre() + " " + personaSeleccionada.getApellidos());
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            // Acciones a realizar si el usuario acepta
            entityManager.getTransaction().begin();
            entityManager.merge(personaSeleccionada);
            entityManager.remove(personaSeleccionada);
            entityManager.getTransaction().commit();
            
            tableViewAgenda.getItems().remove(personaSeleccionada);
            tableViewAgenda.getFocusModel().focus(null);
            tableViewAgenda.requestFocus();
        } 
        else 
        {
            // Acciones a realizar si el usuario cancela
            int numFilaSeleccionada = tableViewAgenda.getSelectionModel().getSelectedIndex();
            tableViewAgenda.getItems().set(numFilaSeleccionada,personaSeleccionada);
            TablePosition pos = new TablePosition(tableViewAgenda, numFilaSeleccionada,null);
            tableViewAgenda.getFocusModel().focus(pos);
            tableViewAgenda.requestFocus();
        }
        
        
    }
    
    
    
    
    
    
}
