package io.piotrjastrzebski.ecsclones.restrainingofbob.components.logic.ai;

import com.artemis.PooledComponent;
import com.artemis.annotations.EntityId;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.fsm.StackStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.utils.ObjectMap;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.BTreeLoader;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.storage.FloatStorage;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.storage.IntStorage;
import io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic.ai.storage.StringStorage;

/**
 * Created by PiotrJ on 31/08/15.
 */
public class EnemyBrain extends PooledComponent {
	public final static String SELF_ID = "self";
	// current hp of entity
	public float hp;
	public float maxHP;
	// min dst2 to pursue target
	public float minDst2;
//	@EntityID
	public int target;
	@EntityId
	public int id;
	public boolean inRange;
	protected IntStorage intStorage;

	protected FloatStorage floatStorage;
	protected StringStorage stringStorage;
	public BehaviorTree<EnemyBrain> tree;

	public String path;
	public String name;
	public String treePath;
	public ObjectMap<String, BehaviorTree> stateToTree = new ObjectMap<>();
	public StackStateMachine<EnemyBrain, State<EnemyBrain>> fsm;
	public ObjectMap<String, State<EnemyBrain>> nameToState = new ObjectMap<>();
	public ObjectMap<State<EnemyBrain>, String> stateToName = new ObjectMap<>();

//	public EnemyBrain set (String path, BehaviorTree<EnemyBrain> tree) {
//		this.path = path;
//		this.tree = tree;
//		return this;
//	}

	public EnemyBrain () {
		reset();
	}

	@Override protected void reset () {
		hp = 0;
		maxHP = 0;
		minDst2 = 0;
		target = -1;
		id = -1;
		inRange = false;
		if (intStorage != null) intStorage.clear();
		if (floatStorage != null) floatStorage.clear();
		if (stringStorage != null) stringStorage.clear();
		tree = null;
		name = null;
		treePath = null;
		fsm = null;
		nameToState.clear();
		stateToName.clear();
//		path = null;
		stateToTree.clear();
	}

	public void setOwnerId(int id) {
		this.id = id;
		getIntStorage().putValue(SELF_ID, id);
	}

	public IntStorage getIntStorage () {
		if (intStorage == null) intStorage = new IntStorage();
		return intStorage;
	}

	public FloatStorage getFloatStorage () {
		if (floatStorage == null) floatStorage = new FloatStorage();
		return floatStorage;
	}

	public StringStorage getStringStorage () {
		if (stringStorage == null) stringStorage = new StringStorage();
		return stringStorage;
	}
}
