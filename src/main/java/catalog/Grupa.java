package catalog;

import java.util.*;

public class Grupa {
    private String nume;
    private List<Student> studenti;
    private Map<String, Profesor> materii;

    public Grupa(String nume) {
        this.nume = nume;
        studenti = new ArrayList<>();
        materii = new HashMap<>();
    }

    public Grupa(String nume, List<Student> studenti) {
        this.nume = nume;
        this.studenti = studenti;
        this.materii = new HashMap<>();
    }

    public String getNume() {
        return nume;
    }

    public List<Student> getStudenti() {
        return studenti;
    }

    public Map<String, Profesor> getMaterii() {
        return materii;
    }

    public void adaugareMaterie(String materie, Profesor profesor){
        materii.put(materie, profesor);
    }
    public void adaugareStudent(Student student) {studenti.add(student);}

    // TODO metode de listare al mepului (doar profii, doar materiile, sau ambele)

}
