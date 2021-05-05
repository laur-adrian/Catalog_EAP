package catalog;

public class Utilizator {

    public enum Drepturi {
        ADMIN, PROFESOR, STUDENT
    }

    private String nume;
    private String numeUtilizator;
    private String parola;
    private Drepturi drepturi;

    public Utilizator(String nume, String numeUtilizator, String parola, Drepturi drepturi) {
        this.nume = nume;
        this.numeUtilizator = numeUtilizator;
        this.parola = parola;
        this.drepturi = drepturi;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getNumeUtilizator() {
        return numeUtilizator;
    }

    public void setNumeUtilizator(String numeUtilizator) {
        this.numeUtilizator = numeUtilizator;
    }

    public String getParola() {
        return parola;
    }

    public Drepturi getDrepturi() {
        return drepturi;
    }

    @Override
    public String toString() {
        return "Utilizator{" +
                "nume='" + nume + '\'' +
                ", numeUtilizator='" + numeUtilizator + '\'' +
                ", parola='" + parola + '\'' +
                ", drepturi=" + drepturi +
                '}';
    }
}
