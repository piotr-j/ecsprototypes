package io.piotrjastrzebski.ecsclones.restrainingofbob.btedit;

/**
 * Created by PiotrJ on 16/10/15.
 */
public interface IPersist<E> {
	public void onSave (String tree);
	public void onSaveAs (String tree);
	public void onLoad ();
}
