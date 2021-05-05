package catalog;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Catalog {

    private List<Grupa> grupe;
    private List<Utilizator> utilizatori;
    private Utilizator utilizatorLogat;

    private CSVData csvData = CSVData.getInstance();

    private final static String numeFisierLog = "src/main/csv/syslog.csv";
    private BufferedWriter log;

    public Catalog() {
//        grupe = citesteGrupe();
//        utilizatori = citesteUtilizatori();
        utilizatori = csvData.citesteUtilizatori();
        grupe = csvData.citesteGrupe(utilizatori);

//        Utilizator admin = new Utilizator("Laur", "laurica", "farafrica", Utilizator.Drepturi.ADMIN);
//        utilizatori.add(admin);

        try {
            FileWriter fw = new FileWriter(numeFisierLog);
            log = new BufferedWriter(fw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Grupa> citesteGrupe(){
        List<Grupa> grupe;
        grupe = new ArrayList<>();
        return grupe;
    }

    public static List<Utilizator> citesteUtilizatori(){
        List<Utilizator> utilizatori;
        utilizatori = new ArrayList<>();
        return utilizatori;
    }

    public boolean logare(String user, String parola){
        for (Utilizator u : utilizatori){
            if (u.getNumeUtilizator().equals(user)){
                if (u.getParola().equals(parola)) {
                    utilizatorLogat=u;
                    System.out.println("Logare reusita!");
                    scrieInLog("Login reusit cu user " + user);
                    return true;
                }
            }
        }
        return false;
    }

    public void delogare(){
        scrieInLog("Delogare reusita pentru user " + utilizatorLogat.getNumeUtilizator());
        utilizatorLogat=null;
        System.out.println("Delogare reusita!");
    }

    public void adaugareUtilizator(String nume, String numeUtilizator, String parola, Utilizator.Drepturi drept){
        if (utilizatorLogat != null) {
            if (utilizatorLogat.getDrepturi().equals(Utilizator.Drepturi.ADMIN)) {
                if(drept == Utilizator.Drepturi.ADMIN)
                {
                    Administrator admin = new Administrator(nume, numeUtilizator, parola);
                    utilizatori.add(admin);
                    System.out.println("Adaugarea utilizatorului "+numeUtilizator+" cu drepturi de "+Utilizator.Drepturi.ADMIN+" s-a incheiat cu succes");
                    scrieInLog("Adaugare reusita a utilizatorului " + numeUtilizator + " cu drepturi de " + Utilizator.Drepturi.ADMIN);
                }
                else if(drept == Utilizator.Drepturi.PROFESOR)
                {
                    Profesor prof = new Profesor(nume, numeUtilizator, parola);
                    utilizatori.add(prof);
                    System.out.println("Adaugarea utilizatorului "+numeUtilizator+" cu drepturi de "+Utilizator.Drepturi.PROFESOR+" s-a incheiat cu succes");
                    scrieInLog("Adaugare reusita a utilizatorului " + numeUtilizator + " cu drepturi de " + Utilizator.Drepturi.PROFESOR);
                }
                else if(drept == Utilizator.Drepturi.STUDENT)
                {
                    Student stud = new Student(nume, numeUtilizator, parola);
                    utilizatori.add(stud);
                    System.out.println("Adaugarea utilizatorului "+numeUtilizator+" cu drepturi de "+Utilizator.Drepturi.STUDENT+" s-a incheiat cu succes");
                    scrieInLog("Adaugare reusita a utilizatorului " + numeUtilizator + " cu drepturi de " + Utilizator.Drepturi.STUDENT);
                }
            }
            else{
                System.err.println("Nu aveti drepturile necesare pentru a adauga un utilizator nou!");
            }
        }
        else{
            System.err.println("Niciun utilizator nu este logat! Pentru efectuarea acestei actiuni va rugam sa va logati!");
        }
    }

    public void adaudareGrupa(Grupa grupa){
        if (utilizatorLogat != null) {
            if (utilizatorLogat.getDrepturi().equals(Utilizator.Drepturi.ADMIN)) {
                grupe.add(grupa);
                System.out.println("Grupa "+grupa.getNume()+" a fost adaugată cu succes!");
                scrieInLog("Adaugare reusita a grupei" + grupa.getNume());
            }
            else{
                System.err.println("Nu aveti drepturile necesare pentru a adauga o grupa noua");
            }
        }
        else{
            System.err.println("Niciun utilizator nu este logat! Pentru efectuarea acestei actiuni va rugam sa va logati!");
        }
    }



    public Grupa preiaGrupaDupaNume (String numeGrupa){
        for (Grupa g : grupe)
        {
            if (numeGrupa.equals(g.getNume()))
                return g;
        }
        return null;
    }

    public Student preiaStudentDupaNume (String numeStudent)
    {
        for (Utilizator s : utilizatori)
        {
            if (numeStudent.equals(s.getNume()) && Utilizator.Drepturi.STUDENT == s.getDrepturi())
            {
                return (Student)s;
            }
        }
        return null;
    }

    public void adaugareStudentGrupa(String numeGrupa, String numeStudent){
        if (utilizatorLogat != null) {
            if (utilizatorLogat.getDrepturi().equals(Utilizator.Drepturi.ADMIN)) {
                Grupa g = preiaGrupaDupaNume(numeGrupa);
                Student s = preiaStudentDupaNume(numeStudent);
                g.adaugareStudent(s);
                System.out.println("Studentul "+numeStudent+" a fost adaugat cu succes in grupa "+numeGrupa);
                scrieInLog("Adaugare reusita a studentului" + numeStudent + "in grupa " + numeGrupa);
            }
            else{
                System.err.println("Nu aveti drepturile necesare pentru a adauga un student nou unei grupe!");
            }
        }
        else{
            System.err.println("Niciun utilizator nu este logat! Pentru efectuarea acestei actiuni va rugam sa va logati!");
        }
    }

    public Profesor preiaProfDupaNume(String numeProf)
    {
        for (Utilizator p : utilizatori)
        {
            if (numeProf.equals(p.getNume()) && Utilizator.Drepturi.PROFESOR == p.getDrepturi())
            {
                return (Profesor) p;
            }
        }
        return null;
    }

    public void adugareMaterieGrupa(String materie, String numeProfesor, String numeGrupa){
        if (utilizatorLogat != null) {
            if (utilizatorLogat.getDrepturi().equals(Utilizator.Drepturi.ADMIN)) {
                Profesor profesor = preiaProfDupaNume(numeProfesor);
                Grupa grupa = preiaGrupaDupaNume(numeGrupa);
                grupa.adaugareMaterie(materie, profesor);
                System.out.println("Materia "+materie+" predata de "+numeProfesor+" a fost adaugata cu succes in lista materiilor grupei "+numeGrupa);
                scrieInLog("Materia "+materie+" predata de "+numeProfesor+" a fost adaugata cu succes in lista materiilor grupei "+numeGrupa);
            }
            else{
                System.err.println("Nu aveti drepturile necesare pentru a adauga o materie si un profesor unei grupe!");
            }
        }
        else{
            System.err.println("Niciun utilizator nu este logat! Pentru efectuarea acestei actiuni va rugam sa va logati!");
        }
    }

    public boolean verificareNumeMaterie (String materie, Grupa grupa){
        for (String m : grupa.getMaterii().keySet())
        {
            if (m.equals(materie))
            {
                return true;
            }
        }
        return false;
    }

    public void adaugareNota(String numeGrupa, String materie, int nota, String numeStudent){
        if (utilizatorLogat != null){
            if(utilizatorLogat.getDrepturi().equals(Utilizator.Drepturi.ADMIN) || utilizatorLogat.getDrepturi().equals(Utilizator.Drepturi.PROFESOR)){
                Grupa g = preiaGrupaDupaNume(numeGrupa);
                Student student = preiaStudentDupaNume(numeStudent);
                if(verificareNumeMaterie(materie, g))
                {
                    student.adaugareNota(materie, nota);
                    System.out.println("Nota obtinută de studentul "+numeStudent+"la materia "+materie+"a fost agaugata cu succes în catalog");
                    scrieInLog("Nota obtinută de studentul "+numeStudent+"la materia "+materie+"a fost agaugata cu succes în catalog");
                }
                else
                    System.err.println("Materia " + materie + " nu exista in cadrul grupei "+ numeGrupa + "!");
            }
            else{
                System.err.println("Nu aveti drepturile necesare pentru a adauga o nota unui student!");
            }
        }
        else{
            System.err.println("Niciun utilizator nu este logat! Pentru efectuarea acestei actiuni va rugam sa va logati!");
        }
    }

    public void afisareNoteStudent(String numeStudent){
        Student student = preiaStudentDupaNume(numeStudent);
        System.out.println("Notele studentului "+numeStudent+" sunt:");
        for (String materie : student.getNote().keySet())
        {
            System.out.println(materie + " " + student.getNote().get(materie));
        }
        scrieInLog("Afisarea notelor studentului " + numeStudent + " a fost incheiata cu succes");
    }

    public void afisareNoteMaterieGrupa(String materie, String numeGrupa){ // Afiseaza notelor tuturor studentilor dintr-o grupa la o anumita materie
        System.out.println("Notele studentilor din grupa "+numeGrupa+" la materia "+materie+" sunt:");
        Grupa grupa = preiaGrupaDupaNume(numeGrupa);
        for (Student s : grupa.getStudenti())
        {
            for (String m : s.getNote().keySet())
            {
                if (m.equals(materie))
                    System.out.println(s.getNume() + " " + s.getNote().get(materie));
            }
        }
        scrieInLog("Afisarea notelor studentilor grupei " + numeGrupa + " la materia " + materie + " a fost incheiata cu succes");
    }

    public void afisareStudentiGrupa(String numeGrupa){
        System.out.println("Lista studentilor din grupa "+numeGrupa+":");
        Grupa grupa = preiaGrupaDupaNume(numeGrupa);
        for (Student s : grupa.getStudenti())
        {
            System.out.println(s.getNume());
        }
        scrieInLog("Afisarea studentilor din grupa " + numeGrupa + " a fost incheiata cu succes");
    }

    public void afisareMateriiGrupa(String numeGrupa){
        System.out.println("Lista materiilor studiate la grupa "+numeGrupa+":");
        Grupa grupa = preiaGrupaDupaNume(numeGrupa);
        for (String materie : grupa.getMaterii().keySet())
        {
            System.out.println(materie);
        }
        scrieInLog("Afisarea materiilor grupei " + numeGrupa + " a fost incheiata cu succes");
    }

    public void salveazaInCSV() {
        csvData.scrieUtilizatoriInCSV(utilizatori);
        csvData.scrieGrupeInCSV(grupe);

        try {
            log.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void scrieInLog(String ceScrie) {
        try {
            log.write(ceScrie + "," + System.currentTimeMillis() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
