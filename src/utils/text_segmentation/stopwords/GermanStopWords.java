package utils.text_segmentation.stopwords;


/*  Please see the license information at the end of this file. */

import Step1_SourceRetrieval.P1_SharedClass.S6_Segmentation.Utils.SetFactory;

import java.util.Set;


/** Freddy Choi's stop word list.
 */

public class GermanStopWords
extends BaseStopWords
implements StopWords
{
	/** Stop words. */

	protected static Set<String> GermanStopWordsSet   =
			SetFactory.createNewSet();

	/** Create the stop word filter.
	 */

	public GermanStopWords()
	{
		stopWordsSet.addAll( GermanStopWordsSet );
	}

	/** Static initializer. */

	static
	{
		GermanStopWordsSet.add( "aber");
		GermanStopWordsSet.add( "alle");
		GermanStopWordsSet.add( "allem");
		GermanStopWordsSet.add( "allen");
		GermanStopWordsSet.add( "aller");
		GermanStopWordsSet.add( "alles");
		GermanStopWordsSet.add( "als");
		GermanStopWordsSet.add( "also");
		GermanStopWordsSet.add( "am");
		GermanStopWordsSet.add( "an");
		GermanStopWordsSet.add( "ander");
		GermanStopWordsSet.add( "andere");
		GermanStopWordsSet.add( "anderem");
		GermanStopWordsSet.add( "anderen");
		GermanStopWordsSet.add( "anderer");
		GermanStopWordsSet.add( "anderes");
		GermanStopWordsSet.add( "anderm");
		GermanStopWordsSet.add( "andern");
		GermanStopWordsSet.add( "anderr");
		GermanStopWordsSet.add( "anders");
		GermanStopWordsSet.add( "auch");
		GermanStopWordsSet.add( "auf");
		GermanStopWordsSet.add( "aus");
		GermanStopWordsSet.add( "bei");
		GermanStopWordsSet.add( "bin");
		GermanStopWordsSet.add( "bis");
		GermanStopWordsSet.add( "bist");
		GermanStopWordsSet.add( "da");
		GermanStopWordsSet.add( "damit");
		GermanStopWordsSet.add( "dann");
		GermanStopWordsSet.add( "der");
		GermanStopWordsSet.add( "den");
		GermanStopWordsSet.add( "des");
		GermanStopWordsSet.add( "dem");
		GermanStopWordsSet.add( "die");
		GermanStopWordsSet.add( "das");
		GermanStopWordsSet.add( "da�");
		GermanStopWordsSet.add( "derselbe");
		GermanStopWordsSet.add( "derselben");
		GermanStopWordsSet.add( "denselben");
		GermanStopWordsSet.add( "desselben");
		GermanStopWordsSet.add( "demselben");
		GermanStopWordsSet.add( "dieselbe");
		GermanStopWordsSet.add( "dieselben");
		GermanStopWordsSet.add( "dasselbe");
		GermanStopWordsSet.add( "dazu");
		GermanStopWordsSet.add( "dein");
		GermanStopWordsSet.add( "deine");
		GermanStopWordsSet.add( "deinem");
		GermanStopWordsSet.add( "deinen");
		GermanStopWordsSet.add( "deiner");
		GermanStopWordsSet.add( "deines");
		GermanStopWordsSet.add( "denn");
		GermanStopWordsSet.add( "derer");
		GermanStopWordsSet.add( "dessen");
		GermanStopWordsSet.add( "dich");
		GermanStopWordsSet.add( "dir");
		GermanStopWordsSet.add( "du");
		GermanStopWordsSet.add( "dies");
		GermanStopWordsSet.add( "diese");
		GermanStopWordsSet.add( "diesem");
		GermanStopWordsSet.add( "diesen");
		GermanStopWordsSet.add( "dieser");
		GermanStopWordsSet.add( "dieses");
		GermanStopWordsSet.add( "doch");
		GermanStopWordsSet.add( "dort");
		GermanStopWordsSet.add( "durch");
		GermanStopWordsSet.add( "ein");
		GermanStopWordsSet.add( "eine");
		GermanStopWordsSet.add( "einem");
		GermanStopWordsSet.add( "einen");
		GermanStopWordsSet.add( "einer");
		GermanStopWordsSet.add( "eines");
		GermanStopWordsSet.add( "einig");
		GermanStopWordsSet.add( "einige");
		GermanStopWordsSet.add( "einigem");
		GermanStopWordsSet.add( "einigen");
		GermanStopWordsSet.add( "einiger");
		GermanStopWordsSet.add( "einiges");
		GermanStopWordsSet.add( "einmal");
		GermanStopWordsSet.add( "er");
		GermanStopWordsSet.add( "ihn");
		GermanStopWordsSet.add( "ihm");
		GermanStopWordsSet.add( "es");
		GermanStopWordsSet.add( "etwas");
		GermanStopWordsSet.add( "euer");
		GermanStopWordsSet.add( "eure");
		GermanStopWordsSet.add( "eurem");
		GermanStopWordsSet.add( "euren");
		GermanStopWordsSet.add( "eurer");
		GermanStopWordsSet.add( "eures");
		GermanStopWordsSet.add( "f�r");
		GermanStopWordsSet.add( "gegen");
		GermanStopWordsSet.add( "gewesen");
		GermanStopWordsSet.add( "hab");
		GermanStopWordsSet.add( "habe");
		GermanStopWordsSet.add( "haben");
		GermanStopWordsSet.add( "hat");
		GermanStopWordsSet.add( "hatte");
		GermanStopWordsSet.add( "hatten");
		GermanStopWordsSet.add( "hier");
		GermanStopWordsSet.add( "hin");
		GermanStopWordsSet.add( "hinter");
		GermanStopWordsSet.add( "ich");
		GermanStopWordsSet.add( "mich");
		GermanStopWordsSet.add( "mir");
		GermanStopWordsSet.add( "ihr");
		GermanStopWordsSet.add( "ihre");
		GermanStopWordsSet.add( "ihrem");
		GermanStopWordsSet.add( "ihren");
		GermanStopWordsSet.add( "ihrer");
		GermanStopWordsSet.add( "ihres");
		GermanStopWordsSet.add( "euch");
		GermanStopWordsSet.add( "im");
		GermanStopWordsSet.add( "in");
		GermanStopWordsSet.add( "indem");
		GermanStopWordsSet.add( "ins");
		GermanStopWordsSet.add( "ist");
		GermanStopWordsSet.add( "jede");
		GermanStopWordsSet.add( "jedem");
		GermanStopWordsSet.add( "jeden");
		GermanStopWordsSet.add( "jeder");
		GermanStopWordsSet.add( "jedes");
		GermanStopWordsSet.add( "jene");
		GermanStopWordsSet.add( "jenem");
		GermanStopWordsSet.add( "jenen");
		GermanStopWordsSet.add( "jener");
		GermanStopWordsSet.add( "jenes");
		GermanStopWordsSet.add( "jetzt");
		GermanStopWordsSet.add( "kann");
		GermanStopWordsSet.add( "kein");
		GermanStopWordsSet.add( "keine");
		GermanStopWordsSet.add( "keinem");
		GermanStopWordsSet.add( "keinen");
		GermanStopWordsSet.add( "keiner");
		GermanStopWordsSet.add( "keines");
		GermanStopWordsSet.add( "k�nnen");
		GermanStopWordsSet.add( "k�nnte");
		GermanStopWordsSet.add( "machen");
		GermanStopWordsSet.add( "man");
		GermanStopWordsSet.add( "manche");
		GermanStopWordsSet.add( "manchem");
		GermanStopWordsSet.add( "manchen");
		GermanStopWordsSet.add( "mancher");
		GermanStopWordsSet.add( "manches");
		GermanStopWordsSet.add( "mein");
		GermanStopWordsSet.add( "meine");
		GermanStopWordsSet.add( "meinem");
		GermanStopWordsSet.add( "meinen");
		GermanStopWordsSet.add( "meiner");
		GermanStopWordsSet.add( "meines");
		GermanStopWordsSet.add( "mit");
		GermanStopWordsSet.add( "muss");
		GermanStopWordsSet.add( "musste");
		GermanStopWordsSet.add( "nach");
		GermanStopWordsSet.add( "nicht");
		GermanStopWordsSet.add( "nichts");
		GermanStopWordsSet.add( "noch");
		GermanStopWordsSet.add( "nun");
		GermanStopWordsSet.add( "nur");
		GermanStopWordsSet.add( "ob");
		GermanStopWordsSet.add( "oder");
		GermanStopWordsSet.add( "ohne");
		GermanStopWordsSet.add( "sehr");
		GermanStopWordsSet.add( "sein");
		GermanStopWordsSet.add( "seine");
		GermanStopWordsSet.add( "seinem");
		GermanStopWordsSet.add( "seinen");
		GermanStopWordsSet.add( "seiner");
		GermanStopWordsSet.add( "seines");
		GermanStopWordsSet.add( "selbst");
		GermanStopWordsSet.add( "sich");
		GermanStopWordsSet.add( "sie");
		GermanStopWordsSet.add( "ihnen");
		GermanStopWordsSet.add( "sind");
		GermanStopWordsSet.add( "so");
		GermanStopWordsSet.add( "solche");
		GermanStopWordsSet.add( "solchem");
		GermanStopWordsSet.add( "solchen");
		GermanStopWordsSet.add( "solcher");
		GermanStopWordsSet.add( "solches");
		GermanStopWordsSet.add( "soll");
		GermanStopWordsSet.add( "sollte");
		GermanStopWordsSet.add( "sondern");
		GermanStopWordsSet.add( "sonst");
		GermanStopWordsSet.add( "�ber");
		GermanStopWordsSet.add( "um");
		GermanStopWordsSet.add( "und");
		GermanStopWordsSet.add( "uns");
		GermanStopWordsSet.add( "unse");
		GermanStopWordsSet.add( "unsem");
		GermanStopWordsSet.add( "unsen");
		GermanStopWordsSet.add( "unser");
		GermanStopWordsSet.add( "unses");
		GermanStopWordsSet.add( "unter");
		GermanStopWordsSet.add( "viel");
		GermanStopWordsSet.add( "vom");
		GermanStopWordsSet.add( "von");
		GermanStopWordsSet.add( "vor");
		GermanStopWordsSet.add( "w�hrend");
		GermanStopWordsSet.add( "war");
		GermanStopWordsSet.add( "waren");
		GermanStopWordsSet.add( "warst");
		GermanStopWordsSet.add( "was");
		GermanStopWordsSet.add( "weg");
		GermanStopWordsSet.add( "weil");
		GermanStopWordsSet.add( "weiter");
		GermanStopWordsSet.add( "welche");
		GermanStopWordsSet.add( "welchem");
		GermanStopWordsSet.add( "welchen");
		GermanStopWordsSet.add( "welcher");
		GermanStopWordsSet.add( "welches");
		GermanStopWordsSet.add( "wenn");
		GermanStopWordsSet.add( "werde");
		GermanStopWordsSet.add( "werden");
		GermanStopWordsSet.add( "wie");
		GermanStopWordsSet.add( "wieder");
		GermanStopWordsSet.add( "will");
		GermanStopWordsSet.add( "wir");
		GermanStopWordsSet.add( "wird");
		GermanStopWordsSet.add( "wirst");
		GermanStopWordsSet.add( "wo");
		GermanStopWordsSet.add( "wollen");
		GermanStopWordsSet.add( "wollte");
		GermanStopWordsSet.add( "w�rde");
		GermanStopWordsSet.add( "w�rden");
		GermanStopWordsSet.add( "zu");
		GermanStopWordsSet.add( "zum");
		GermanStopWordsSet.add( "zur");
		GermanStopWordsSet.add( "zwar");
		GermanStopWordsSet.add( "zwischen");
	}
}


