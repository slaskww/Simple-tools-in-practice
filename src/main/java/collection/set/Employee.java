package collection.set;

public class Employee implements Comparable<Employee> {

    private String firstName;
    private String lastName;
    private int age;

    public Employee(String firstName, String lastName, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Employee employee = (Employee) o;

        if (age != employee.age) return false;
        if (firstName != null ? !firstName.equals(employee.firstName) : employee.firstName != null) return false;
        return lastName != null ? lastName.equals(employee.lastName) : employee.lastName == null;
    }

    @Override
    public int hashCode() {
        int result = firstName != null ? firstName.hashCode() : 0;
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + age;
        return result;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                "}\n";
    }

    /**
     * metoda compareTo() z interfejsu Comparable, jest niezbędna jeśli chcemy, by instancje klasy Employee byly umieszczane w zbiorze TreeSet.
     * TreeSet inaczej niż HashSet, nie opiera swoich operacji na metodach hashCode() i equals(), lecz na metodzie compareTo() lub implementacji metody compare() dostarczanej
     * w postaci lambda-wyrażenia do konstruktora TreeSet
     */

    @Override
    public int compareTo(Employee employee) {
        int idx = lastName.compareTo(employee.lastName);
        if (idx == 0){
            idx = firstName.compareTo(employee.firstName);
        }
        if (idx == 0){
            idx = age - employee.getAge();
        }
        return idx;
    }
}
