package main;

import entity.Department;
import entity.Lector;
import service.DBException;
import service.DBService;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class UI {
    private DBService dbService = null;

    public UI(DBService dbService) {
        this.dbService = dbService;
    }

    public void menu() throws IOException {
        System.out.println("----------User Inerface----------");
        System.out.println("1.Show table lectors");
        System.out.println("2.Show table departments:");
        System.out.println("3.Who is head of department");
        System.out.println("4.Show {department_name} statistic.");
        System.out.println("5.Show the average salary for department.");
        System.out.println("6.Show count of employee for department.");
        System.out.println("7.Global search in table Lectors.");
        System.out.println("0.Exit.");
        System.out.print("Your choice: ");

    }

    public boolean function() throws IOException {
        Scanner scanner = new Scanner(System.in);

        try {
            switch (scanner.nextInt()) {

                case 1: {

                    showLectors();
                    System.out.println("\n" + "Press Enter to continue");
                    scanner.next();
                    scanner.nextLine();
                    return true;
                }
                case 2: {

                    showDepartmets();
                    System.out.println("\n" +
                            "Press Enter to continue");
                    scanner.next();
                    scanner.nextLine();
                    return true;
                }

                case 3: {

                    System.out.print("Who is head of department ");
                    scanner = new Scanner(System.in);
                    String name = scanner.nextLine();
                    if (name.trim() == "") {
                        System.out.println("\n" +
                                "Not correct input!");
                        return true;
                    }
                    findHeadOfDepartments(name);
                    System.out.println("\n" +
                            "Press Enter to continue");
                    scanner.next();
                    scanner.nextLine();
                    return true;
                }
                case 4: {

                    System.out.println("Show {department_name} statistic. ");
                    System.out.print("department name: ");
                    scanner = new Scanner(System.in);
                    String name = scanner.nextLine();
                    if (name.trim() == "") {
                        System.out.println("\n" +
                                "Not correct input!");
                    } else showStatistic(name);
                    System.out.println("\n" +
                            "Press Enter to continue");
                    scanner.nextLine();
                    return true;
                }
                case 5: {
                    System.out.flush();
                    System.out.print("Show the average salary for department ");
                    scanner = new Scanner(System.in);
                    String name = scanner.nextLine();
                    if (name.trim() == "") {
                        System.out.println("\n" +
                                "Not correct input!");
                    } else showAVG(name);
                    System.out.println("\n" +
                            "Press Enter to continue");
                    scanner.next();
                    scanner.nextLine();
                    return true;
                }
                case 6: {
                    System.out.print("Show count of employee for department  ");
                    scanner = new Scanner(System.in);
                    String name = scanner.nextLine();
                    if (name.trim() == "") {
                        System.out.println("\n" +
                                "Not correct input!");
                    } else getCountEmployee(name);
                    System.out.println("\n" +
                            "Press Enter to continue");
                    scanner.next();
                    scanner.nextLine();
                    return true;
                }
                case 7: {

                    System.out.print("Global search in table Lectors : ");
                    scanner = new Scanner(System.in);
                    String text = scanner.nextLine();
                    if (text.trim() == "") {
                        System.out.println("\n" +
                                "Not correct input!");
                    } else globalSearch(text);
                    System.out.println("\n" +
                            "Press Enter to continue");
                    scanner.next();
                    scanner.nextLine();
                    return true;
                }
                case 0: {
                    System.out.println("Bye!");
                    return false;
                }
                default: {
                    System.out.println("Not correct input!");
                    return true;
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Not correct inputQ");
            return true;
        }
    }

    private void globalSearch(String text) {
        StringBuilder str = new StringBuilder("");
        try {
            List<Lector> list = dbService.globalSerch(text);
            if (list.isEmpty()) {
                System.out.println("\n" + "Nothing found according to templateQ");
            }
            int i = 0;
            for (Lector lector : list) {
                str.append(lector.getFirstName() + " ");
                str.append(lector.getLastName());
                i++;
                if (i < list.size()) str.append(", ");
            }
            System.out.println(str.toString());
        } catch (DBException e) {
            e.printStackTrace();
        }

    }

    private void getCountEmployee(String name) {
        try {
            System.out.println(dbService.countLectorsInDepartments(name));
        } catch (DBException e) {
            System.out.println("Don't find a department " + name + "!");
        } catch (NullPointerException e) {
            System.out.println("Don't find department " + name + "!");
        }
    }

    private void showAVG(String name) {

        try {
            System.out.println("The average salary of " + name + "is " + dbService.getAVGSalaryInDepartements(name));
        } catch (DBException e) {
            System.out.println("Don't find a department " + name + "!");
        } catch (NullPointerException e) {
            System.out.println("Don't find department " + name + "!");
        }

    }

    private void showStatistic(String name) {
        String[] degrees = new String[]{"assistans", "associate professors", "professors"};
        StringBuilder text = new StringBuilder("");
        try {
            List<Long> statistic = dbService.showStatisticByDegree(name, degrees);
            int i = 0;
            for (long count : statistic) {
                text.append(degrees[i] + " - ");
                text.append(count);
            }
            System.out.println(text.toString());
        } catch (DBException e) {
            System.out.println("Don't find a department " + name + "!");
        } catch (NullPointerException e) {
            System.out.println("Don't find department  " + name + "!");
        }

    }

    private void findHeadOfDepartments(String name) {
        try {
            Lector lector = dbService.getHeadOfDepartment(name);
            if (lector == null) {
                System.out.println("Don't find head of department or department" + name);
            }
            System.out.println("Head of " + name + " department is" + lector.getFirstName() + " " + lector.getLastName());
        } catch (DBException e) {
            System.out.println("Don't find a department " + name + "!");
        } catch (NullPointerException e) {
            System.out.println("Don't find head of department or department " + name + "!");
        }
    }

    private void showDepartmets() {
        try {
            List<Department> list = dbService.getAllDepatments();
            StringBuilder text = new StringBuilder("");
            text.append("Name\t" + "Head" + "\n");
            for (Department department : list) {
                text.append(department.getName() + "\t");
                text.append(department.getHead().getFirstName() + " ");
                text.append(department.getHead().getLastName() + "\n");
            }

            System.out.println(text.toString());
        } catch (DBException e) {
            e.printStackTrace();
        }
    }

    private void showLectors() {
        try {
            List<Lector> list = dbService.getAllLectors();
            StringBuilder text = new StringBuilder("");
            text.append("Firstname\t" + "Lastname\t" + "Degree\t" + "Salary\n");
            for (Lector lector : list) {
                text.append(lector.getFirstName() + "\t");
                text.append(lector.getLastName() + "\t");
                text.append(lector.getDegree() + "\t");
                text.append(lector.getSalary() + "\n");
            }
            System.out.println(text.toString());

        } catch (DBException e) {
            e.printStackTrace();
        }
    }

}
