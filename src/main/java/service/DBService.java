package service;

import dao.DepartmentDAO;
import dao.LectorsDAO;
import entity.Department;
import entity.Lector;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class DBService {
    private final SessionFactory sessionFactory;

    public DBService() {
        Configuration configuration = new Configuration().configure().
                addResource("hibernate.cfg.xml").
                addAnnotatedClass(Lector.class).
                addAnnotatedClass(Department.class);

        sessionFactory = createSessionFactory(configuration);
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    public void insertLector(String firstName, String lastName, String degree, double salary) throws DBException {
        Session session = sessionFactory.openSession();
        try {
            LectorsDAO dao = new LectorsDAO(session);
            dao.insert(firstName, lastName, degree, salary);
        } catch (HibernateException e) {
            throw new DBException(e);
        } finally {
            session.close();
        }
    }

    public Lector getLector(String firstName, String lastName) throws DBException {
        Session session = sessionFactory.openSession();
        try {
            LectorsDAO dao = new LectorsDAO(session);
            Lector lector = dao.get(firstName, lastName);
            return lector;
        } catch (HibernateException e) {
            throw new DBException(e);
        } finally {
            session.close();
        }

    }

    public Department getDepartment(String name) throws DBException {
        Session session = sessionFactory.openSession();
        try {
            DepartmentDAO dao = new DepartmentDAO(session);
            Department department = dao.get(name);
            return department;
        } catch (HibernateException e) {
            throw new DBException(e);
        } finally {
            session.close();
        }
    }

    public Set<Lector> getLectorsFromDepartments(String name) throws DBException {
        return getDepartment(name).getLectors();
    }

    public void setLectorsToDepartments(String name, String firstName, String lastName) throws DBException {
        Session session = sessionFactory.openSession();
        Lector lector = getLector(firstName, lastName);
        if (lector == null) return;
        try {
            DepartmentDAO dao = new DepartmentDAO(session);
            dao.setLectorToDepartment(name, lector);
        } catch (HibernateException e) {
            throw new DBException(e);
        } finally {
            session.close();
        }
    }

//    public Lector getHeadOfDepartment(String name) throws DBException {
//        Session session = null;
//        try {
//            session = sessionFactory.openSession();
//            DepartmentDAO dao=new DepartmentDAO(session);
//            return dao.getDepartmentHead(name);
//        } catch (HibernateException e) {
//            throw new DBException(e);
//        } finally {
//            session.close();}
//    }
public Lector getHeadOfDepartment(String name) throws DBException {
        return getDepartment(name).getHead();
}

    public void setHeadOfDepartment(String name, String firstName, String lastNAme) throws DBException {
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();
            Lector lector = getLector(firstName, lastNAme);
            DepartmentDAO dao = new DepartmentDAO(session);
            dao.setHead(name, lector);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            throw new DBException(e);
        } finally {
            session.close();
        }
    }

    public List<Department> getAllDepatments() throws DBException {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            DepartmentDAO dao = new DepartmentDAO(session);
            return dao.getAll();
        } catch (HibernateException e) {
            throw new DBException(e);
        } finally {
            session.close();
        }
    }

    public List<Lector> getAllLectors() throws DBException {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            LectorsDAO dao = new LectorsDAO(session);
            return dao.getAll();
        } catch (HibernateException e) {
            throw new DBException(e);
        } finally {
            session.close();
        }
    }


    public void insertDepartment(String name) throws DBException {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            DepartmentDAO dao = new DepartmentDAO(session);
            dao.insert(name);
        } catch (HibernateException e) {
            throw new DBException(e);
        } finally {
            session.close();
        }
    }

    public double getAVGSalaryInDepartements(String name) throws DBException {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            DepartmentDAO dao = new DepartmentDAO(session);
            Department department = dao.get(name);
            LectorsDAO lectorsDAO = new LectorsDAO(session);
            return lectorsDAO.avgSalaryInDepartment(department);
        } catch (HibernateException e) {
            throw new DBException(e);
        } finally {
            session.close();
        }

    }

    public List<Lector> globalSerch(String text) throws DBException {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            LectorsDAO dao = new LectorsDAO(session);
            return dao.globalSearch(text);
        } catch (HibernateException e) {
            throw new DBException(e);
        } finally {
            session.close();
        }
    }


    public long countLectorsInDepartments(String name) throws DBException {

        Session session = null;
        try {
            session = sessionFactory.openSession();
            DepartmentDAO dao = new DepartmentDAO(session);
            Department department = dao.get(name);

            LectorsDAO dao1 = new LectorsDAO(session);

            return dao1.countLectorsInDepartments(department);
        } catch (HibernateException e) {
            throw new DBException(e);
        } finally {
            session.close();
        }
    }

    public List showStatisticByDegree(String name, String[] degrees) throws DBException {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            DepartmentDAO dao = new DepartmentDAO(session);
            Department department = dao.get(name);
            LectorsDAO lectorsDAO = new LectorsDAO(session);
            List<Long> statistic = new ArrayList<>();
            for (String str : degrees) {
                statistic.add(lectorsDAO.statisticInDepartments(department, str));
            }
            return statistic;
        } catch (HibernateException e) {
            throw new DBException(e);
        } finally {
            session.close();
        }
    }

}
