package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks.base;

/**
 * Created by EvilEntity on 22/11/2015.
 */
public interface Compare {
	boolean compare (float a, float b);
	boolean compare (int a, int b);
	boolean compare (String a, String b);
}
