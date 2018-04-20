package DAO;

import entity.Department;
import entity.Lector;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class LectorsDAO {
    private Session session;

    public LectorsDAO(Session session) {
        this.session = session;
    }

    public long insert(String firstName, String lastName,
                       String degree, double sallary) {
        return (Long) session.save(new Lector(firstName, lastName, degree, sallary));
    }

    public Lector get(long id) {
        return (Lector) session.get(Lector.class, id);
    }

    public Lector get(String firstName, String lastName) {
        Criteria criteria = session.createCriteria(Lector.class);

        return (Lector) criteria.add(Restrictions.and
                (
                        Restrictions.eq("firstName", firstName),
                        Restrictions.eq("lastName", lastName)
                )).uniqueResult();
    }

    public List<Lector> getAll() {
        Criteria criteria = session.createCriteria(Lector.class);
        return criteria.list();
    }

    public List<Lector> globalSearch(String text) {
        Criteria criteria = session.createCriteria(Lector.class);
        criteria.add(Restrictions.or(
                Restrictions.like("firstName", "%" + text + "%"),
                Restrictions.like("lastName", "%" + text + "%")
        ));

        return criteria.list();
    }

    public Long statisticInDepartments(Department department, String degree) {

        Query query = session.createQuery("select count(l)  from Lector l INNER JOIN " +
                "l.departments department" +
                " where department.id = :departmentID" +
                " AND LOWER( l.degree)=:degreeName").
                setLong("departmentID", department.getId()).
                setString("degreeName", degree);
        Long count = (Long) query.uniqueResult();


        return count;
    }

    public Long countLectorsInDepartments(Department department) {
        Query query = session.createQuery("select count(l) from Lector l INNER JOIN " +
                "l.departments department" +
                " where department.id = :departmentID ").setLong("departmentID", department.getId());
        return (Long) query.uniqueResult();
    }

    public Double avgSalaryInDepartment(Department department) {
        Query query = session.createQuery("select avg(l.salary) from Lector l INNER JOIN " +
                "l.departments department" +
                " where department.id = :departmentID ").setLong("departmentID", department.getId());
        return (Double) query.uniqueResult();
    }

    public void remove(Lector lector) {
        session.delete(lector);
    }
}
