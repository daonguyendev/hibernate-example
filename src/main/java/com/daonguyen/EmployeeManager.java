package com.daonguyen;

import com.daonguyen.entity.Employee;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Iterator;
import java.util.List;

public class EmployeeManager {

    private static SessionFactory factory;

    public static void main(String[] args) {
        try {
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        EmployeeManager employeeManager = new EmployeeManager();

        // Add few employees into database
        Integer empID1 = employeeManager.addEmployee("Ty", "Le", 1000);
        Integer empID2 = employeeManager.addEmployee("Teo", "Tran", 3000);
        Integer empID3 = employeeManager.addEmployee("Tin", "Nguyen", 2000);

        // List of all employees
        employeeManager.listEmployees();

        // Update employee's salary
        employeeManager.updateEmployee(empID1, 1500);

        // Delete an employee from the database
        employeeManager.deleteEmployee(empID2);

        // List of all employees after update
        employeeManager.listEmployees();
    }

    public Integer addEmployee(String fname, String lname, int salary) {
        Session session = factory.openSession();
        Transaction trans = null;
        Integer empID = null;

        try {
            trans = session.beginTransaction();
            Employee employee = new Employee(fname, lname, salary);
            empID = (Integer) session.save(employee);
            trans.commit();
        } catch (HibernateException e) {
            if (trans != null)
                trans.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return empID;
    }

    public void listEmployees() {
        Session session = factory.openSession();
        Transaction trans = null;

        try {
            trans = session.beginTransaction();
            List employees = session.createQuery("FROM Employee").list();

            for (Iterator iterator = employees.iterator(); iterator.hasNext();) {
                Employee employee = (Employee) iterator.next();
                System.out.print("First name: " + employee.getFirstName());
                System.out.print(" | Last name: " + employee.getLastName());
                System.out.println(" | Salary: " + employee.getSalary());
            }

            trans.commit();
        } catch (HibernateException e) {
            if (trans != null)
                trans.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void updateEmployee(Integer empID, int salary) {
        Session session = factory.openSession();
        Transaction trans = null;

        try {
            trans = session.beginTransaction();
            Employee employee = (Employee) session.get(Employee.class, empID);
            employee.setSalary(salary);
            session.update(employee);
            trans.commit();
        } catch (HibernateException e) {
            if (trans != null)
                trans.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void deleteEmployee(Integer empID) {
        Session session = factory.openSession();
        Transaction trans = null;

        try {
            trans = session.beginTransaction();
            Employee employee = (Employee) session.get(Employee.class, empID);
            session.delete(employee);
            trans.commit();
        } catch (HibernateException e) {
            if (trans != null)
                trans.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
