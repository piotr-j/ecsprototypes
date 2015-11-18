package io.piotrjastrzebski.ecsclones.restrainingofbob.processors.logic;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.utils.*;

import static com.artemis.Aspect.all;

/**
 * Created by EvilEntity on 18/11/2015.
 */
public class Groups extends BaseSystem {
	private static final Bag<String> EMPTY_BAG = new Bag<>();

	/** All entities and groups mapped with group names as key. */
	private final ObjectMap<String, IntBag> entitiesByGroup;
	/** All entities and groups mapped with entities as key. */
	private final IntMap<Bag<String>> groupsByEntity;

	/**
	 * Creates a new GroupManager instance.
	 */
	public Groups() {
		entitiesByGroup = new ObjectMap<>();
		groupsByEntity = new IntMap<>();
	}

	@Override
	protected void processSystem() {}

	@Override
	protected void initialize() {
		world.getAspectSubscriptionManager()
			.get(all())
			.addSubscriptionListener(new EntitySubscription.SubscriptionListener() {
				@Override
				public void inserted(IntBag entities) {}

				@Override
				public void removed(IntBag entities) {
					deleted(entities);
				}
			});
	}

	public void add(Entity e, String group) {
		add(e.getId(), group);
	}

	public void add(int eid, String group) {
		IntBag entities = entitiesByGroup.get(group);
		if(entities == null) {
			entities = new IntBag();
			entitiesByGroup.put(group, entities);
		}
		if (!entities.contains(eid)) entities.add(eid);

		Bag<String> groups = groupsByEntity.get(eid);
		if(groups == null) {
			groups = new Bag<>();
			groupsByEntity.put(eid, groups);
		}
		if (!groups.contains(group)) groups.add(group);
	}

	public void remove(Entity e, String group) {
		remove(e.getId(), group);
	}

	public void remove(int eid, String group) {
		IntBag entities = entitiesByGroup.get(group);
		if(entities != null) {
			entities.removeValue(eid);
		}

		Bag<String> groups = groupsByEntity.get(eid);
		if(groups != null) {
			groups.remove(group);
			if (groups.size() == 0) groupsByEntity.remove(eid);
		}
	}

	public void removeFromAllGroups(Entity e) {
		removeFromAllGroups(e.getId());
	}

	public void removeFromAllGroups(int eid) {
		Bag<String> groups = groupsByEntity.get(eid);
		if(groups == null) return;
		for(int i = 0, s = groups.size(); s > i; i++) {
			IntBag entities = entitiesByGroup.get(groups.get(i));
			if(entities != null) {
				entities.removeValue(eid);
			}
		}
		groupsByEntity.remove(eid);
	}

	public IntBag getEntities(String group) {
		IntBag entities = entitiesByGroup.get(group);
		if(entities == null) {
			entities = new IntBag();
			entitiesByGroup.put(group, entities);
		}
		return entities;
	}

	public ImmutableBag<String> getGroups(Entity e) {
		return getGroups(e.getId());
	}
	public ImmutableBag<String> getGroups(int eid) {
		Bag<String> groups = groupsByEntity.get(eid, null);
		return groups != null ? groups : EMPTY_BAG;
	}

	public boolean isInAnyGroup(Entity e) {
		return isInAnyGroup(e.getId());
	}

	public boolean isInAnyGroup(int e) {
		return getGroups(e).size() > 0;
	}

	public boolean isInGroup(Entity e, String group) {
		return isInGroup(e.getId(), group);
	}

	public boolean isInGroup(int e, String group) {
		if(group != null) {
			Bag<String> bag = groupsByEntity.get(e);
			if (bag != null) {
				Object[] groups = bag.getData();
				for(int i = 0, s = bag.size(); s > i; i++) {
					String g = (String)groups[i];
					if(group.equals(g)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	void deleted(IntBag entities) {
		int[] ids = entities.getData();
		for (int i = 0, s = entities.size(); s > i ; i++) {
			removeFromAllGroups(ids[i]);
		}
	}
}
