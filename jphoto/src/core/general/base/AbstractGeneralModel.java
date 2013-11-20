package core.general.base;

import core.interfaces.GeneralModel;
import org.springframework.validation.BindingResult;

public abstract class AbstractGeneralModel extends AbstractGeneralPageModel implements GeneralModel {

	protected BindingResult bindingResult;
	private boolean isNew = false;

	@Override
	public BindingResult getBindingResult() {
		return bindingResult;
	}

	@Override
	public void setBindingResult( final BindingResult bindingResult ) {
		this.bindingResult = bindingResult;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew( boolean aNew ) {
		isNew = aNew;
	}

	@Override
	public void clear() {
		bindingResult = null;
		isNew = false;
	}
}
