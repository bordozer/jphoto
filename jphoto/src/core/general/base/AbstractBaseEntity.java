package core.general.base;

import core.interfaces.BaseEntity;

public abstract class AbstractBaseEntity implements BaseEntity {

	private int id;

	protected AbstractBaseEntity() {
	}

	protected AbstractBaseEntity( int id ) {
		this.id = id;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId( int id ) {
		this.id = id;
	}

	public boolean isNew (){
		return id == 0;
	}
}
