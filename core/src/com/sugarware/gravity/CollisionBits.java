package com.sugarware.gravity;

public class CollisionBits {
	public static final short CATEGORY_ENTITY = 0x0001;
	public static final short CATEGORY_WORLD = 0x0002;
	public static final short CATEGORY_LIGHT = 0x0004;
	
	public static final short MASK_ENTITY = CATEGORY_ENTITY | CATEGORY_WORLD;
	public static final short MASK_WORLD = -1;
	public static final short MASK_LIGHT = CATEGORY_ENTITY;
}
