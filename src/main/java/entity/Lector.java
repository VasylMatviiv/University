package entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "lectors")
public class Lector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;
    @Column(name = "degree")
    private String degree;
    @Column(name = "salary")
    private double salary;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "lector_department", joinColumns = {
            @JoinColumn(name = "lector_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "department_id",
                    nullable = false)
            })
    private Set<Department> departments;

    public Lector() {
    }



    public Lector(String firstname, String lastname, String degree, double salary) {
        this.firstName = firstname;
        this.lastName = lastname;
        this.degree = degree;
        this.salary = salary;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public Set<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(Set<Department> departments) {
        this.departments = departments;
    }


}
