package net.gvcc.goffice.common.reporting.exports;

/**
 *
 * <p>
 * The <code>Flattener</code> interface
 * </p>
 * <p>
 * Interfaccia che espone i metodi necessari alla trasformazione di qualsiasi oggetto in una <code>List&lt;Map&lt;String,Object&gt;&gt;</code> fino ad un livello di annidamento specifico
 * 
 * @author marco mancuso
 * @version 1.0
 * 
 * @see "Disegno Architetturale Moduli Core: Sistema di esposizione dati a software di aziende terze"
 * @see "Disegno Architetturale Moduli Core: Sistema di esposizione dati a software di aziende terze - Implementazione prototipale - Strategie di flattening"
 */
public interface Flattener {
	public FlatObject flatten(Object o, int level);

	public FlatObject flatten(Object o);

	public int getLevel();
}
