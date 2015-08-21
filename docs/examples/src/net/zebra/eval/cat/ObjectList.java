/**
 * 
 */
package net.zebra.eval.cat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 
 * @author Loc Nguyen
 *
 * @param <E>
 */
public abstract class ObjectList<E> {

	
	/**
	 * List of objects
	 */
	protected List<E> objects = null;
	
	
	/**
	 * Constructor
	 */
	public ObjectList() {
		objects = new ArrayList<E>();
	}
	
	
	/**
	 * 
	 * @param object
	 * @return identity of given object
	 */
	public abstract int getId(E object);
	
	
	/**
	 * 
	 * @param object
	 */
	public void add(E object) {
		int id = getId(object); 
		if (id == -1)
			objects.add(object);
		else {
			int found = find(id);
			if (found == -1)
				objects.add(object);
			else
				objects.set(found, object);
		}
	}
	
	
	/**
	 * 
	 * @param objects
	 */
	public void addAll(Collection<E> objects) {
		for (E object : objects) {
			add(object);
		}
	}
	
	
	/**
	 * 
	 * @param index
	 * @return object by index
	 */
	public E get(int index) {
		return objects.get(index);
	}
	
	
	/**
	 * 
	 * @param objectId
	 * @return object by identity
	 */
	public E getById(int objectId) {
		int index = find(objectId);
		if (index == -1)
			return null;
		else
			return get(index);
	}
	
	
	/**
	 * 
	 * @return size of objects list
	 */
	public int size() {
		return objects.size();
	}
	
	
	/**
	 * 
	 * @param objectId item identity
	 * @return object index
	 */
	public int find(int objectId) {
		for (int i = 0; i < objects.size(); i++) {
			E object = objects.get(i);
			if (getId(object) == objectId) {
				return i;
			}
		}
		return -1;
	}

	
	/**
	 * 
	 * @param object
	 * @return object index
	 */
	public int find(E object) {
		for (int i = 0; i < objects.size(); i++) {
			E e = objects.get(i);
			if (e == object)
				return i;
		}
		return find(getId(object));
	}

	
	/**
	 * 
	 * @param index
	 * @return removed object
	 */
	public E remove(int index) {
		return objects.remove(index);
	}
	
	
	/**
	 * 
	 * @param objectId
	 * @return removed object
	 */
	public E removeById(int objectId) {
		int index = find(objectId);
		if (index == -1)
			return null;
		else
			return remove(index);
	}

	
	/**
	 * 
	 */
	public void clear() {
		objects.clear();
	}
	
	
	/**
	 * 
	 * @return list of objects
	 */
	List<E> getList() {
		return objects;
	}
}
