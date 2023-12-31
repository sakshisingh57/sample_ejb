/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import EJB.EmployeesFacadeLocal;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import modelo.Employees;
import utils.Utils;

/**
 *
 * @author diego
 */
@Named //Ambitos de clase: Vista, aplicacion, sesion, peticion 
@ViewScoped
public class ProfileController implements Serializable{
    
    private Employees employees;
    
    private Employees employeeToUpdate;
    
    private Date date;
    
    @EJB
    private EmployeesFacadeLocal employeesEJB;
    
    @PostConstruct
    public void init(){
        employees = showInfo();
        date = employees.getBirthdayDate();
        employeeToUpdate = new Employees();
    }
    
    public Employees showInfo() {
        try {
            Employees pr = (Employees) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("empleadoLogged");
            System.out.println(pr.getIdEmployee());
            List result = employeesEJB.showEmployeeInfo(pr.getIdEmployee());
            if(!result.isEmpty()) {
                return (Employees) result.get(0);
            } else {
                FacesContext.getCurrentInstance().getExternalContext().redirect("public/error404.xhtml");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Oh no! Algo ha ido mal");
            return null;
        }
    }
    
    public void updateInfoEmployee() {
        try {
            String user = "", name = "";
            boolean todoOk = false, todoOk2 = false;
            String returnUsername = employeeToUpdate.getUsername().trim();
            if(Utils.validUsername(returnUsername)) {
                user = employeeToUpdate.getUsername();
                todoOk=true;
            }else{
                if(returnUsername.length()==0){
                     user = employees.getUsername();
                     todoOk=true;
                }else{
                    FacesContext.getCurrentInstance().addMessage("MessageId", new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Username introducido erróneo, "
                            + "se procede a dejar el almacenado en base de datos"));
                    user = employees.getUsername();
                }
            }
            String returnName = employeeToUpdate.getNameEmp().trim();
            if(Utils.validName(returnName)) {
                name = employeeToUpdate.getNameEmp();
                todoOk2=true;
            }else{
                if(returnName.length()==0){
                    name = employees.getNameEmp();
                    todoOk2=true;
                }else{
                    FacesContext.getCurrentInstance().addMessage("MessageId", new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Nombre introducido erróneo, "
                            + "se procede a dejar el almacenado en base de datos"));
                    name = employees.getNameEmp();
                }
                
            }
            try {
                employeesEJB.updateEmployee(user, name, employees.getDni(), date);
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage("MessageId", new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Datos introducidos duplicados"));
            }
            if(todoOk && todoOk2){
                FacesContext.getCurrentInstance().getExternalContext().redirect("profile.xhtml");
            }
            
        } catch (Exception e) {
            System.out.println("Oh no! Algo ha ido mal");
        }
    }
    
    public void updateInfoSensibilityEmployee() {
        try {
            String ssn = "", phoneNumber = "", dni = "";
            boolean todoOk = false, todoOk2 = false, todoOk3 = false;
            String returnSSN = employeeToUpdate.getSsn().trim();
            if(Utils.validSSN(returnSSN)) {
                ssn = employeeToUpdate.getSsn().trim();
                todoOk=true;
            }else{
                if(returnSSN.length()==0) {
                    ssn = employees.getSsn();
                    todoOk=true;
                }else{
                    FacesContext.getCurrentInstance().addMessage("MessageId2", new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "SSN introducido erróneo, "
                            + "se procede a dejar el almacenado en base de datos"));
                    ssn = employees.getSsn();
                }
            }
            
            String returnPhoneNumber = employeeToUpdate.getPhoneNum().trim();
            if(Utils.validPhoneNumber(returnPhoneNumber)) {
                phoneNumber = employeeToUpdate.getPhoneNum().trim();
                todoOk2 = true;
            }else{
                if (returnPhoneNumber.length() == 0) {
                    phoneNumber = employees.getPhoneNum();
                    todoOk2 = true;
                }else{
                    FacesContext.getCurrentInstance().addMessage("MessageId2", new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Número de teléfono introducido erróneo, "
                            + "se procede a dejar el almacenado en base de datos"));
                     phoneNumber = employees.getPhoneNum();
                }
            }
            
            String returnDNI = employeeToUpdate.getDni().trim();
            if(Utils.validDNI(returnDNI)) {
                dni = employeeToUpdate.getDni().trim();
                todoOk3 = true;
            }else{
                if (returnDNI.length()==0) {
                    dni = employees.getDni();
                    todoOk3 = true;
                }else{
                    FacesContext.getCurrentInstance().addMessage("MessageId2", new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "DNI introducido erróneo, "
                            + "se procede a dejar el almacenado en base de datos"));
                    dni = employees.getDni();
                } 
            }
            if(todoOk && todoOk2 && todoOk3){
                if(employeesEJB.updateEmployeeSensibiliti(ssn, phoneNumber, dni, employees.getIdEmployee())) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("profile.xhtml");
                }else {
                    FacesContext.getCurrentInstance().addMessage("MessageId", new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Datos introducidos duplicados"));
                }
                //FacesContext.getCurrentInstance().getExternalContext().redirect("profile.xhtml");
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage("MessageId", new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning", "Datos introducidos duplicados"));
            System.out.println("Oh no! Algo ha ido mal 10");
        }
    }
    
    public Employees getEmployees() {
        return employees;
    }

    public void setEmployees(Employees employees) {
        this.employees = employees;
    }
    
    public Employees getEmployeesToUpdate() {
        return employeeToUpdate;
    }

    public void setEmployeesToUpdate(Employees employees) {
        this.employeeToUpdate = employees;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    
}
