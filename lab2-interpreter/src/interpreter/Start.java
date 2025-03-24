package interpreter;

import grammar.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;

public class Start {
    public static void main(String[] args) {
        CharStream inp;
        try {
            inp = CharStreams.fromFileName("we.first");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        CharStream inp = CharStreams.fromString("1+2*3-(4+5)","wejście");
//        CharStream inp = CharStreams.fromStream(System.in);

        firstLexer lex = new firstLexer(inp);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        firstParser par = new firstParser(tokens);

        ParseTree tree = par.prog();

        CalculateVisitor v = new CalculateVisitor(inp, tokens);
        Integer res = v.visit(tree);
//        System.out.printf("Wynik: %d\n", res);
    }
}

/*
    11.03 - lab2

    // zalozenie poejktu od zera...
    // wzorzec odwiedzajacy...
    // zadanie na dzis to kalkulator - 1+2, mnozenie dzielenie, nawiasy. narazie na liczbach...
    //

    visitor - wzorzec odwiedzajacy
    mamy sobie drzewo (drzewo programu - kazdy element to inna klasa czy cos
    dla kazdego z wezlow nalezy cos zrobic - np. wypisac, obliczyc, zrobic cos innego
    w kazdego rodzaju wezle (klasie) musimy zrobic cos innego

    // dzisiaj zrobimy kalkulator, ale nastepny lab to moze kompilator i wtedy lipa, trzeba zmieniac caly kod
    // wiec robimy wzorzec odwiedzajacy - dla kazdego rodzaju wezla mamy osobna klase, ktora robi cos innego

    visitor . visit() przyjmuje ParseTree
 */


// na zaliczenie tylko kompilacja i uruchomienie
// ew liczy jakies podstawowe rzeczy
// na lepsza ocene caly 4 dzialaniowy kalk
// nawiasy wystarczy zw gramatyce dobrze zdefiniowac
// w kodzie nic za bardzo nie trzeba zmieniac
// na 5 trzeba bedzie dodac cos od siebie oraz wytlumaczyc...

// zajecia nr 3 - interpreter
// notatki:
/*
uwaga na hierarchie dzialan - porownania, nietrywialne, w jakiej kolejnosci sie te dzialania wykonywaly,. im wczesniej w regule definicja tym wyzszy priorytet
zmienne - 3 rzeczy sa potrzebne - deklaracja zmiennej, odczyt zmiennej i zapis zmiennej. wystarczy zmienne globalne. deklaracja zmiennej - wstawiamy do
expr nie jest dobrym miejscem na deklaracje, int wyrzucic do stata,
wyrazenie warunkowe jest, dodac petle (przynajmniej jednak)
na ocene dostateczna, przynajmniej grametyke zrobic - semantyka - to co sie da. bez zmiennych to 4 np. na 5 wszystko





 */
// zadania do wykonania za tydzien:
/*
za tydzien compiler, kazdy pakiet ma swoje miejsce uruchomienia.
stg to string template pliki zawieraja szablony, nalezy sie zapoznac, za tydzien bedzie trzeba to wykorzystac
za tydzien i za 2 bedziemy gorzystac z pakietu grammar (first g4) - w 11 linii znajduje sie print. mozna zastapic kropka albo dolare,
w 12 linii ? zrobic deklaracje zmiennej?
global symbols zawiera funkcje - klasy pomocnicze

-> gramatyka javy gdzies tam sie znajduje, tylko wyklad, chyba ze refaktor javy bedziemy robic na projekcie to bedziemy korzystac z tego.
jesli bedziemy mieli problem z kompilowaniem, wylaczyc rewriter -> mark as excluded - wtedy wylaczamyz budowania projektu..
 */