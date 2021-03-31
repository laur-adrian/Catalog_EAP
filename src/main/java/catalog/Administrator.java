package catalog;

public class Administrator extends Utilizator{

    public Administrator(String nume, String numeUtilizator, String parola) {
        super(nume, numeUtilizator, parola, Drepturi.ADMIN);
    }
}
