package com.vlad.dino3d.min3d.vos;


import com.vlad.dino3d.min3d.interfaces.IDirtyParent;

public class BooleanManaged extends AbstractDirtyManaged
{
	private boolean _b;

	public BooleanManaged(boolean $value, IDirtyParent $parent)
	{
		super($parent);
		set($value);
	}
	
	public boolean get()
	{
		return _b;
	}
	public void set(boolean $b)
	{
		_b = $b;
		setDirtyFlag();
	}
}