package utils.text_segmentation.stopwords;


/*  Please see the license information at the end of this file. */

import utils.text_segmentation.utils.SetFactory;

import java.util.Set;


/** Freddy Choi's stop word list.
 */

public class SpanishStopWords
    extends BaseStopWords
    implements StopWords
{
    /** Stop words. */

    protected static Set<String> spanishStopWordsSet   =
        SetFactory.createNewSet();

    /** Create the stop word filter.
     */

    public SpanishStopWords()
    {
        stopWordsSet.addAll( spanishStopWordsSet );
    }

    /** Static initializer. */

    static
    {
    	spanishStopWordsSet.add( "alg�n");
    	spanishStopWordsSet.add( "alguna");
    	spanishStopWordsSet.add( "algunas");
    	spanishStopWordsSet.add( "alguno");
    	spanishStopWordsSet.add( "algunos");
    	spanishStopWordsSet.add( "ambos");
    	spanishStopWordsSet.add( "ampleamos");
    	spanishStopWordsSet.add( "ante");
    	spanishStopWordsSet.add( "antes");
    	spanishStopWordsSet.add( "aquel");
    	spanishStopWordsSet.add( "aquellas");
    	spanishStopWordsSet.add( "aquellos");
    	spanishStopWordsSet.add( "aqui");
    	spanishStopWordsSet.add( "arriba");
    	spanishStopWordsSet.add( "atras");
    	spanishStopWordsSet.add( "bajo");
    	spanishStopWordsSet.add( "bastante");
    	spanishStopWordsSet.add( "bien");
    	spanishStopWordsSet.add( "cada");
    	spanishStopWordsSet.add( "cierta");
    	spanishStopWordsSet.add( "ciertas");
    	spanishStopWordsSet.add( "cierto");
    	spanishStopWordsSet.add( "ciertos");
    	spanishStopWordsSet.add( "como");
    	spanishStopWordsSet.add( "con");
    	spanishStopWordsSet.add( "conseguimos");
    	spanishStopWordsSet.add( "conseguir");
    	spanishStopWordsSet.add( "consigo");
    	spanishStopWordsSet.add( "consigue");
    	spanishStopWordsSet.add( "consiguen");
    	spanishStopWordsSet.add( "consigues");
    	spanishStopWordsSet.add( "cual");
    	spanishStopWordsSet.add( "cuando");
    	spanishStopWordsSet.add( "dentro");
    	spanishStopWordsSet.add( "desde");
    	spanishStopWordsSet.add( "donde");
    	spanishStopWordsSet.add( "dos");
    	spanishStopWordsSet.add( "el");
    	spanishStopWordsSet.add( "ellas");
    	spanishStopWordsSet.add( "ellos");
    	spanishStopWordsSet.add( "empleais");
    	spanishStopWordsSet.add( "emplean");
    	spanishStopWordsSet.add( "emplear");
    	spanishStopWordsSet.add( "empleas");
    	spanishStopWordsSet.add( "empleo");
    	spanishStopWordsSet.add( "en");
    	spanishStopWordsSet.add( "encima");
    	spanishStopWordsSet.add( "entonces");
    	spanishStopWordsSet.add( "entre");
    	spanishStopWordsSet.add( "era");
    	spanishStopWordsSet.add( "eramos");
    	spanishStopWordsSet.add( "eran");
    	spanishStopWordsSet.add( "eras");
    	spanishStopWordsSet.add( "eres");
    	spanishStopWordsSet.add( "es");
    	spanishStopWordsSet.add( "esta");
    	spanishStopWordsSet.add( "estaba");
    	spanishStopWordsSet.add( "estado");
    	spanishStopWordsSet.add( "estais");
    	spanishStopWordsSet.add( "estamos");
    	spanishStopWordsSet.add( "estan");
    	spanishStopWordsSet.add( "estoy");
    	spanishStopWordsSet.add( "fin");
    	spanishStopWordsSet.add( "fue");
    	spanishStopWordsSet.add( "fueron");
    	spanishStopWordsSet.add( "fui");
    	spanishStopWordsSet.add( "fuimos");
    	spanishStopWordsSet.add( "gueno");
    	spanishStopWordsSet.add( "ha");
    	spanishStopWordsSet.add( "hace");
    	spanishStopWordsSet.add( "haceis");
    	spanishStopWordsSet.add( "hacemos");
    	spanishStopWordsSet.add( "hacen");
    	spanishStopWordsSet.add( "hacer");
    	spanishStopWordsSet.add( "haces");
    	spanishStopWordsSet.add( "hago");
    	spanishStopWordsSet.add( "incluso");
    	spanishStopWordsSet.add( "intenta");
    	spanishStopWordsSet.add( "intentais");
    	spanishStopWordsSet.add( "intentamos");
    	spanishStopWordsSet.add( "intentan");
    	spanishStopWordsSet.add( "intentar");
    	spanishStopWordsSet.add( "intentas");
    	spanishStopWordsSet.add( "intento");
    	spanishStopWordsSet.add( "ir");
    	spanishStopWordsSet.add( "la");
    	spanishStopWordsSet.add( "largo");
    	spanishStopWordsSet.add( "las");
    	spanishStopWordsSet.add( "lo");
    	spanishStopWordsSet.add( "los");
    	spanishStopWordsSet.add( "mientras");
    	spanishStopWordsSet.add( "mio");
    	spanishStopWordsSet.add( "modo");
    	spanishStopWordsSet.add( "muchos");
    	spanishStopWordsSet.add( "muy");
    	spanishStopWordsSet.add( "nos");
    	spanishStopWordsSet.add( "nosotros");
    	spanishStopWordsSet.add( "otro");
    	spanishStopWordsSet.add( "para");
    	spanishStopWordsSet.add( "pero");
    	spanishStopWordsSet.add( "podeis");
    	spanishStopWordsSet.add( "podemos");
    	spanishStopWordsSet.add( "poder");
    	spanishStopWordsSet.add( "podria");
    	spanishStopWordsSet.add( "podriais");
    	spanishStopWordsSet.add( "podriamos");
    	spanishStopWordsSet.add( "podrian");
    	spanishStopWordsSet.add( "podrias");
    	spanishStopWordsSet.add( "por");
    	spanishStopWordsSet.add( "por qu�");
    	spanishStopWordsSet.add( "porque");
    	spanishStopWordsSet.add( "primero");
    	spanishStopWordsSet.add( "puede");
    	spanishStopWordsSet.add( "pueden");
    	spanishStopWordsSet.add( "puedo");
    	spanishStopWordsSet.add( "quien");
    	spanishStopWordsSet.add( "sabe");
    	spanishStopWordsSet.add( "sabeis");
    	spanishStopWordsSet.add( "sabemos");
    	spanishStopWordsSet.add( "saben");
    	spanishStopWordsSet.add( "saber");
    	spanishStopWordsSet.add( "sabes");
    	spanishStopWordsSet.add( "ser");
    	spanishStopWordsSet.add( "si");
    	spanishStopWordsSet.add( "siendo");
    	spanishStopWordsSet.add( "sin");
    	spanishStopWordsSet.add( "sobre");
    	spanishStopWordsSet.add( "sois");
    	spanishStopWordsSet.add( "solamente");
    	spanishStopWordsSet.add( "solo");
    	spanishStopWordsSet.add( "somos");
    	spanishStopWordsSet.add( "soy");
    	spanishStopWordsSet.add( "su");
    	spanishStopWordsSet.add( "sus");
    	spanishStopWordsSet.add( "tambi�n");
    	spanishStopWordsSet.add( "teneis");
    	spanishStopWordsSet.add( "tenemos");
    	spanishStopWordsSet.add( "tener");
    	spanishStopWordsSet.add( "tengo");
    	spanishStopWordsSet.add( "tiempo");
    	spanishStopWordsSet.add( "tiene");
    	spanishStopWordsSet.add( "tienen");
    	spanishStopWordsSet.add( "todo");
    	spanishStopWordsSet.add( "trabaja");
    	spanishStopWordsSet.add( "trabajais");
    	spanishStopWordsSet.add( "trabajamos");
    	spanishStopWordsSet.add( "trabajan");
    	spanishStopWordsSet.add( "trabajar");
    	spanishStopWordsSet.add( "trabajas");
    	spanishStopWordsSet.add( "trabajo");
    	spanishStopWordsSet.add( "tras");
    	spanishStopWordsSet.add( "tuyo");
    	spanishStopWordsSet.add( "ultimo");
    	spanishStopWordsSet.add( "un");
    	spanishStopWordsSet.add( "una");
    	spanishStopWordsSet.add( "unas");
    	spanishStopWordsSet.add( "uno");
    	spanishStopWordsSet.add( "unos");
    	spanishStopWordsSet.add( "usa");
    	spanishStopWordsSet.add( "usais");
    	spanishStopWordsSet.add( "usamos");
    	spanishStopWordsSet.add( "usan");
    	spanishStopWordsSet.add( "usar");
    	spanishStopWordsSet.add( "usas");
    	spanishStopWordsSet.add( "uso");
    	spanishStopWordsSet.add( "va");
    	spanishStopWordsSet.add( "vais");
    	spanishStopWordsSet.add( "valor");
    	spanishStopWordsSet.add( "vamos");
    	spanishStopWordsSet.add( "van");
    	spanishStopWordsSet.add( "vaya");
    	spanishStopWordsSet.add( "verdad");
    	spanishStopWordsSet.add( "verdadera");
    	spanishStopWordsSet.add( "verdadero");
    	spanishStopWordsSet.add( "vosotras");
    	spanishStopWordsSet.add( "vosotros");
    	spanishStopWordsSet.add( "voy");
    	spanishStopWordsSet.add( "yo"); 	
       }
}


