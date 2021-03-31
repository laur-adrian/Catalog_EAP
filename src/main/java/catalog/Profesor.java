package catalog;


public class Profesor extends Utilizator{

    public Profesor(String nume, String numeUtilizator, String parola) {
        super(nume, numeUtilizator, parola, Drepturi.PROFESOR);
    }


}
