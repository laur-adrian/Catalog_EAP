package catalog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Student extends Utilizator{

    private Map<String, Integer> note; // cheia este numele materiei si valoarea este nota finala la materia respectiva

    public Student(String nume, String numeUtilizator, String parola) {
        super(nume, numeUtilizator, parola, Drepturi.STUDENT);
        note = new HashMap<>();
    }

    public Map<String, Integer> getNote() {
        return note;
    }

    public void adaugareNota(String materie, int nota){
        note.put(materie, nota);
    }

    @Override
    public String toString() {
        return "Student{"
                + super.toString() + ", " +
                "note=" + note +
                '}';
    }
}
