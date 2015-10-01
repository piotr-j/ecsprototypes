package io.piotrjastrzebski.ecsclones.restrainingofbob.tasks;

import com.artemis.MundaneWireException;
import com.artemis.World;
import com.artemis.annotations.Wire;

/**
 * Base task for all tasks we will use
 * Created by PiotrJ on 19/08/15.
 */
@Wire
public interface Injectable {
	public void initialize (World world) throws MundaneWireException;
}
