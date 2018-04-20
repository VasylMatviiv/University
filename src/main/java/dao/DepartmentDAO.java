package dao;

import entity.Department;
import entity.Lector;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;


import java.util.List;
import java.util.Set;

public class DepartmentDAO {
    private Session session;

    public DepartmentDAO(Session session) {
        this.session = session;
    }

    public long insert(String name) {
        return (Long) session.save(new Department(name));
    }

    public Department get(long id) {
        return (Department) session.get(Department.class, id);
    }

    public Department get(String name) {
        Criteria criteria = session.createCriteria(Department.class);

        return ((Department) criteria.add(Restrictions.eq("name", name)).uniqueResult());
    }

    public void remove(Department department) {
        session.delete(department);
    }
    public Lector getDepartmentHead(String name){
        Department department=get(name);
        Query query = session.createQuery("select l  from Lector l  " +
                " l.departments department" +
                " where lector.id = :departmentHeadID").
                setLong("departmentHeadID", department.getHead().getId());
        Lector lector = (Lector) query.uniqueResult();
        return lector;
    }

    public void setHead ( String name,Lector lector) {
        Department department = get(name);
        if(department==null) return;
        department.setHead(lector);
        session.update(department);
    }
    public void setLectorToDepartment(String name , Lector lector){
        Department department= get(name);
        if(department==null) return;

        Set< Lector>set= department.getLectors();
        set.add(lector);
        department.setLectors(set);
        session.update(department);
    }
    public List<Department> getAll(){
        Criteria criteria = session.createCriteria(Department.class);
        return criteria.list();
    }

    }



