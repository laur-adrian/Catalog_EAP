package catalog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCData {
    private static JDBCData instanta;
    private Connection conexiune;
    private String URL;
    private String utilizator;
    private String parola;
    private List<Utilizator> utilizatori;
    private List<Grupa> listaGrupe;

    private JDBCData(String URL, String utilizator, String parola) {
        this.URL = URL;
        this.utilizator = utilizator;
        this.parola = parola;
        try {
            conexiune = DriverManager.getConnection(URL,utilizator,parola);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static JDBCData getInstance(String URL, String utilizator, String parola){
        if(instanta == null) {
            instanta = new JDBCData(URL, utilizator, parola);
        }
        return instanta;
    }

    public static JDBCData getInstance(){
        if(instanta == null) {
            throw new RuntimeException("Conexiunea nu a fost configurata inca. Apelati metoda getInstance cu parametri");
        }
        return instanta;
    }

    public List<Grupa> citesteGrupe(){
        try {
            List<Grupa> grupe = new ArrayList<>();
            Statement stmt = conexiune.createStatement();
            ResultSet rsGrupe = stmt.executeQuery("select * from grupe");
            while(rsGrupe.next()){
                Grupa g = new Grupa(rsGrupe.getString("numeGrupa"));
                grupe.add(g);
            }
            this.listaGrupe = grupe;
            return grupe;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public List<Utilizator> citesteUtilizatori() {
        try{
            utilizatori = new ArrayList<>();
            Statement stmt = conexiune.createStatement();
            ResultSet rsAdministratori = stmt.executeQuery("select * from administratori");
            while (rsAdministratori.next()){
                Administrator a = new Administrator(rsAdministratori.getString("nume"),
                                                    rsAdministratori.getString("numeUtilizator"),
                                                    rsAdministratori.getString("parola"));
                utilizatori.add(a);
            }
            ResultSet rsProfesori = stmt.executeQuery("select * from profesori");
            while (rsProfesori.next()){
                Profesor p = new Profesor(rsProfesori.getString("nume"),
                                          rsProfesori.getString("numeUtilizator"),
                                          rsProfesori.getString("parola"));
                utilizatori.add(p);
            }
            ResultSet rsMaterii = stmt.executeQuery("select * from materii");
            while(rsMaterii.next()){
                String numeMaterie = rsMaterii.getString("numeMaterie");
                String numeGrupa = rsMaterii.getString("grupa");
                String profesor = rsMaterii.getString("profesor");
                Grupa g = preiaGrupaDupaNume(numeGrupa);
                Profesor p = preiaProfesorDupaNumeUtilizator(profesor);
                g.adaugareMaterie(numeMaterie,p);
            }
            ResultSet rsStudenti = stmt.executeQuery("select * from studenti");
            while (rsStudenti.next()){
                Student s = new Student(rsStudenti.getString("nume"),
                                        rsStudenti.getString("numeUtilizator"),
                                        rsStudenti.getString("parola"));
                utilizatori.add(s);
                Grupa g = preiaGrupaDupaNume(rsStudenti.getString("grupa"));
                g.adaugareStudent(s);
            }
            ResultSet rsNote = stmt.executeQuery("select * from note");
            while (rsNote.next()){
                String numeUtilizatorS = rsNote.getString("numeUtilizator");
                String numeMaterie = rsNote.getString("materie");
                int nota = rsNote.getInt("nota");
                Student s = preiaStudentDupaNumeUtilizator(numeUtilizatorS);
                s.adaugareNota(numeMaterie, nota);
            }
            return utilizatori;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public Grupa preiaGrupaDupaNume (String numeGrupa){
        for (Grupa g : listaGrupe)
        {
            if (numeGrupa.equals(g.getNume()))
                return g;
        }
        return null;
    }

    public Profesor preiaProfesorDupaNumeUtilizator (String numeUtilizator){
        for (Utilizator p : utilizatori)
        {
            if (numeUtilizator.equals(p.getNumeUtilizator()))
                return (Profesor)p;
        }
        return null;
    }

    public Student preiaStudentDupaNumeUtilizator (String numeUtilizator){
        for (Utilizator s : utilizatori)
        {
            if (numeUtilizator.equals(s.getNumeUtilizator()))
                return (Student)s;
        }
        return null;
    }

    private void clearDB(){
        try {
            Statement stmt = conexiune.createStatement();
            stmt.executeUpdate("delete from note");
            stmt.executeUpdate("delete from studenti");
            stmt.executeUpdate("delete from materii");
            stmt.executeUpdate("delete from profesori");
            stmt.executeUpdate("delete from grupe");
            stmt.executeUpdate("delete from administratori");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void scrieGrupeInDB(List<Grupa> grupe){
        clearDB();
        listaGrupe = grupe;
        try {
            Statement stmt = conexiune.createStatement();
            StringBuilder sql = new StringBuilder("insert into grupe values ");
            for (Grupa g: grupe)
            {
                sql.append("('").append(g.getNume()).append("'),");
            }
            sql.deleteCharAt(sql.length()-1);
            stmt.executeUpdate(sql.toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void scrieUtilizatoriInDB(List<Utilizator> utilizatori){
        try {
            Statement stmt = conexiune.createStatement();
            StringBuilder sql = new StringBuilder("insert into administratori values ");
            for (Utilizator a : utilizatori)
            {
                if (a.getDrepturi().equals(Utilizator.Drepturi.ADMIN))
                {
                    sql.append("('").append(a.getNumeUtilizator()).append("', '").append(a.getNume()).append("', '")
                                    .append(a.getParola()).append("'),");
                }
            }
            sql.deleteCharAt(sql.length()-1);
            stmt.executeUpdate(sql.toString());

            sql = new StringBuilder("insert into profesori values ");
            for (Utilizator p : utilizatori)
            {
                if (p.getDrepturi().equals(Utilizator.Drepturi.PROFESOR))
                {
                    sql.append("('").append(p.getNumeUtilizator()).append("', '").append(p.getNume()).append("', '")
                            .append(p.getParola()).append("'),");
                }
            }
            sql.deleteCharAt(sql.length()-1);
            stmt.executeUpdate(sql.toString());

            sql = new StringBuilder("insert into materii values ");
            for (Grupa g: listaGrupe)
            {
                for (String materie : g.getMaterii().keySet()) {
                    sql.append("('").append(materie).append("', '").append(g.getNume())
                                    .append("', '").append(g.getMaterii().get(materie).getNumeUtilizator()).append("'),");
                }
            }
            sql.deleteCharAt(sql.length()-1);
            stmt.executeUpdate(sql.toString());

            sql = new StringBuilder("insert into studenti values ");
            for (Utilizator s: utilizatori) {
                if (s.getDrepturi().equals(Utilizator.Drepturi.STUDENT)){
                    sql.append("('").append(s.getNumeUtilizator()).append("', '").append(s.getNume()).append("', '")
                            .append(s.getParola()).append("', '").append(preiaNumeGrupaDupaStudentNumeUtilizator(s.getNumeUtilizator())).append("'),");
                }
            }
            sql.deleteCharAt(sql.length()-1);
            stmt.executeUpdate(sql.toString());

            sql = new StringBuilder("insert into note values ");
            List<Student> listaStudenti = new ArrayList<>();
            for (Grupa g: listaGrupe) {
                for (Student s:g.getStudenti()) {
                    listaStudenti.add(s);
                }
            }
            for (Student s: listaStudenti) {
                for (String materie : s.getNote().keySet()) {
                    sql.append("('").append(s.getNumeUtilizator()).append("', '").append(materie)
                            .append("', '").append(s.getNote().get(materie)).append("'),");
                }
            }
            sql.deleteCharAt(sql.length()-1);
            stmt.executeUpdate(sql.toString());

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public String preiaNumeGrupaDupaStudentNumeUtilizator (String numeUtilizatorStudent) {

        for (Grupa g: listaGrupe) {
            for (Student s: g.getStudenti()) {
                if (s.getNumeUtilizator().equals(numeUtilizatorStudent))
                {
                    return g.getNume();
                }
            }

        }
        return null;
    }


}
