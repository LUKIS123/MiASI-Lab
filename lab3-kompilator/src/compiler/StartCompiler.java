package compiler;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import grammar.firstLexer;
import grammar.firstParser;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class StartCompiler {
    public static void main(String[] args) {
        CharStream inp = null;

        try {
            inp = CharStreams.fromFileName("we.first");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        CharStream inp = CharStreams.fromString("1\n2+3+4","wejście");
        firstLexer lex = new firstLexer(inp);
        CommonTokenStream tokens = new CommonTokenStream(lex);
        firstParser par = new firstParser(tokens);

        ParseTree tree = par.prog();

        //st group
//        STGroup.trackCreationEvents = true;
        STGroup group = new STGroupFile("src/compiler/stack.stg");

        EmitVisitor em = new EmitVisitor(group);
        ST res = em.visit(tree);

        System.out.println("===================================\n");
        System.out.println(res.render());

        try {
            var wr = new FileWriter("wy.asm");
            wr.write(res.render());
            wr.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        res.inspect();
    }
}


/*
Luzniejsze zajecia, mamy zrozumiec idee używania szablonow - stosunkowo proste działania z szablonami...
- arytmetyka obowiązkowo, za logike Pan nie radzi się brać (dużo trudniejsze) - 4 działania na liczbach calkowitych.
- kalkulator (kompilator) ma działąć na liczbach podanych bezposredniow instrukcji
- deklaracja zmiennej z inicjalizacja, odczyt zmiennej i zapis zmiennej.
Sprawdzanie poprawnosci na wyrażeniach jak poniżej:
1-2+x*3

Cały czas pracujemy lokalnie, mamy na poczatku wyrazenie, dostaje je parser i on w kazdym momencie widzi tylko dane wyrazenie..
NIE OCHODZI NAS CZYM JEST EXPRESSION.. dzialamy lokalnie , podobnie jak ostatniooo.
expr * expr
przy visitorze robilismy visity dla obu ctx.expr i monozylismy wyniki
tutaj tak samo, pobieram tylko szablon dodawania i do jego parametrow dodaje expr pod zadana nazwa..
...
koncowy wynik jest na stosie, albo w Akumulatorze A. po takim wywolaniu mamy ciag instrukcji w czego efekcie sa 2 liczby na stosie, niewazne czy zmienne czy bezposrednie zmienne.
jak mamy inta to tylko w tym jednym template go obslugujemy, int(i)
NIE ROBIMY OPEACJI KTORE DLA DANEGO MIEJSCA NIE PRZYNALEZA.


Następne zajęcia:
Pozostajemy przy kompilatorze, ale bardziej skomplikowane rzeczy
dostaniemy funkcje z parametrami (jako ze ostatnio byly waunkowe i petle)
instrukcje logiczne - uważać na nie ponieważ sporo pracy, raczej przestrzega Pan
 */

/*
Symulatory - napsisane w PERL, trzeba pobrać interpreter perla, nie ma go domyslnie w windows.
SYmulatory dla instrukcji stosowych praz rejrestrowych - chyba zeby przetesotwać wynik kompilatora.
2 tryby - bez parametrów i wklejamy kod wygenerowany.
    -

Jesli zakonczymy znakiem konca pliku ctr+d, ctr+c - przerwanie wtedy dopiero wykonuje działanie - dziwnie na winsows
- drugi sposob z nazwa pliku, kod wczytywany ale nie jest uruchamiany.
S albo N sluzy do przechodzenia linijka po linice.
R bez sledzenie
T do breaka
ETYKIETA START - zaczynamy od tej etykiety wtedy lepiej dziala na symulatorze...


Zadanie domowe dla chetnych -
szablone moga byc w 3 rodzajach ogranicznikow <<, "", <% - wtedy trzeba zmienic w pliku stg
róznia sie sposobem traktowania spacji, nowych lini i tablulacji

"" - nie pomija nic...
<< - jak jest, pomija pierwsza i ostatnia nowa linie. wciecia sie sumuja i wtedy dziwne formatowanie
<%
%> - ignoruje wszczelkie wciecia i nowe linie
 */

// 30 czerwca , 7 lipca. - egzamin

// R - run , T - do breaka...