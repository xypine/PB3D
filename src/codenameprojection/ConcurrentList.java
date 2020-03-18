/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package codenameprojection;

import java.util.ArrayList;
import java.util.List;

public class ConcurrentList<T> {

	private List<T> list;
	
	private List<T> alternativeList;
	
	private boolean locked = false;
	
	public ConcurrentList(int initialSize) {
		super();
		
		list = new ArrayList<T>(initialSize);
		alternativeList = new ArrayList<T>(initialSize);
	}
	
	public ConcurrentList() {
		super();
		
		list = new ArrayList<T>();
		alternativeList = new ArrayList<T>();
	}
	
	public void add(T t) {
		if(!locked) {
			list.add(t);
		} else {
			alternativeList.add(t);
		}
	}
	
	public List<T> lock() {
		alternativeList.clear();
		locked = true;
		return list;
	}
	
	public void unlock() {
		list.clear();
				
		if (!alternativeList.isEmpty()) {
			list.addAll(alternativeList);
		}

		locked = false;
	}

	public List<T> getList() {
		return list;
	}

	public void clear() {
		list.clear();
	}

	public boolean isEmpty() {
		return list.isEmpty() && alternativeList.isEmpty();
	}

	public void removeAll(List<T> removeList) {
		if(!locked) {
			for(T t: removeList) {
				list.remove(t);
			}
		}		
	}

    @Override
    protected Object clone() {
        //return super.clone(); //To change body of generated methods, choose Tools | Templates.
        return new ConcurrentList<T>();
    }
        
}