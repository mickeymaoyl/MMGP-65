package nc.bs.mmgp.process;

import nc.impl.pubapp.pattern.rule.processer.AroundProcesser;

@SuppressWarnings("rawtypes")
public class NoProcess extends AroundProcesser {

	public NoProcess() {
		super(null);
	}

	@Override
	public Object[] after(Object[] vos) {
		return vos;
	}

	@Override
	public Object[] before(Object[] vos) {
		return vos;
	}
	

}
