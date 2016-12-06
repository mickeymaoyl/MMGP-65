package nc.report.mmgp;

import nc.pubitf.para.SysInitQuery;
import nc.report.mmgp.ReportScaleProcess;
import nc.vo.mmgp.util.MMArrayUtil;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

/**
 * 
 * <b> �������ݾ��ȴ������� </b>
 * <p>
 * ����ֻʵ�����������ͺϼ����������ȴ��� û�д��������������۵ȵȵľ��ȴ�������Ҫ��������չ����
 * </p>
 * 
 * @since �������� Aug 13, 2013
 * @author liwsh
 */
public class MMGPReportScaleStrategy {

	// ���ȴ�����
	private ReportScaleProcess process;

	private String measdoccode; // ��������λ

	private String[] numScaleFiledCode; // �����������ȿ����ֶ�

	private String[] totalFields; // �ϼ��ֶξ��ȿ���
	
	private String currency; //����

	private String[] moneyScaleFiledCode; // ���н��ȿ�ֵ���ֶ�
	
	private String[] percentScaleFiledCode; // �ٷֱȾ��ȵ��ֶ�

	public ReportScaleProcess getReportScaleProcess() {
		return this.process;
	}

	public MMGPReportScaleStrategy(String measdocField,
			String[] numScaleFileds, String[] totalFields) {
		this.measdoccode = measdocField;
		this.numScaleFiledCode = numScaleFileds;
		this.totalFields = totalFields;
		this.process = new ReportScaleProcess();
		this.initialization();
	}
	public MMGPReportScaleStrategy(String currency,
			String[] moneyScaleFiledCode) {
		this.currency = currency;
		this.moneyScaleFiledCode = moneyScaleFiledCode;
		this.process = new ReportScaleProcess();
		this.initialization();
	}
	
	public MMGPReportScaleStrategy(String currency,
			String[] moneyScaleFiledCode,
			ReportScaleProcess process) {
		this.currency = currency;
		this.moneyScaleFiledCode = moneyScaleFiledCode;
		this.process = process;
		this.initialization();
	}

	public MMGPReportScaleStrategy(String measdocField,
			String[] numScaleFileds, String[] totalFields,
			ReportScaleProcess process) {
		this.measdoccode = measdocField;
		this.numScaleFiledCode = numScaleFileds;
		this.totalFields = totalFields;
		this.process = process;
		this.initialization();
	}
	/**
	 * �ٷֱȾ��ȣ����ݲ���YG6010���ƣ���Ĭ��Ϊ2
	 * @param process
	 * @param percentScaleFiledCode �ٷֱ��ֶ�
	 */
	public MMGPReportScaleStrategy(ReportScaleProcess process,
			String[] appointScaleFiledCode) {
		this.percentScaleFiledCode= appointScaleFiledCode;
		this.process = process;
		this.initialization();
	}
	
	/**
	 * �ٷֱȾ��ȣ����ݲ���YG6010���ƣ���Ĭ��Ϊ2
	 * @param percentScaleFiledCode  �ٷֱ��ֶ�
	 */
	public MMGPReportScaleStrategy(String[] appointScaleFiledCode) {
		this.percentScaleFiledCode= appointScaleFiledCode;
		this.process = new ReportScaleProcess();
		this.initialization();
	}

	/**
	 * ��ʼ�����ȹ���
	 */
	private void initialization() {

		// ��������λ ---������
		if (!MMStringUtil.isEmpty(this.getUnitKey())
				&& !MMArrayUtil.isEmpty(this.getNumFields())) {
			this.process.setNumDigits(this.getUnitKey(), this.getNumFields());
		}

		if (!MMArrayUtil.isEmpty(this.totalFields)) {
			this.process.setTotalFields(this.totalFields);
		}
		if(!MMStringUtil.isEmpty(this.getCurrency())){
			this.process.setMnyDigits(this.getCurrency(),this.getMoneyScaleFiledCode());
		}
		if(!MMArrayUtil.isEmpty(this.percentScaleFiledCode)){
			//��ȡ����YG6010ֵ
			int lotSize = 2;
			try {
				lotSize = SysInitQuery.getParaInt(AppContext.getInstance().getPkGroup(), "YG6010");
			} catch (BusinessException e) {
				 ExceptionUtils.wrappBusinessException(e.getMessage());
			}
			this.process.setConstantDigits(this.getPercentScaleFiledCode(),lotSize);
		}
	}

	// ����λ
	protected String getUnitKey() {
		//
		return this.measdoccode;

	}

	// �������ֶ�
	protected String[] getNumFields() {
		return this.numScaleFiledCode;
	}

	// �ϼ��о��ȿ���
	protected String[] getTotalFields() {
		return this.totalFields;
	}
	//����
	public String getCurrency() {
		return this.currency;
	}
	//����ֶ�
	public String[] getMoneyScaleFiledCode() {
		return this.moneyScaleFiledCode;
	}
	//�ٷֱ��ֶ�
	public String[] getPercentScaleFiledCode() {
		return this.percentScaleFiledCode;
	}
	
	
	
	//================���·���Ϊ�����ӣ�ԭ���Ļ�ȡ��ʽ̫�鷳����ʵ���ǻ�ȡprocess=========================
	//begin 2014-12-02  zhangyhk add 
	
	/**
	 * ��������ȿ���
	 * @param currency �����ֶ��� ����Ϊ�գ����򾫶�ʧЧ
	 * @param moneyScaleFiledCode ��Ҫ���ƾ��ȵ��ֶ��� ����Ϊ�գ����򾫶�ʧЧ
	 * @param process �����ȴ����process
	 * @return
	 */
	public static ReportScaleProcess creat4moneyScale(String currency,String[] moneyScaleFiledCode,ReportScaleProcess process){
		
		if(null == process){
			process=new ReportScaleProcess();
		}
		if(!MMStringUtil.isEmpty(currency) && !MMArrayUtil.isEmpty(moneyScaleFiledCode)){
			process.setMnyDigits(currency,moneyScaleFiledCode);
		}
		return process;
	}
	
	/**
	 * ��λ����������
	 * @param measdocField ��λ�ֶ����ƣ�����Ϊ�գ����򾫶�ʧЧ
	 * @param numScaleFileds ��Ҫ���ƾ��ȵ������ֶ����� ������Ϊ�գ����򾫶�ʧЧ
	 * @param process �����ȴ����process
	 * @return
	 */
	public static ReportScaleProcess create4numScale(String measdocField,String[] numScaleFileds,ReportScaleProcess process){
		if(null == process){
			process = new ReportScaleProcess();
		}
		if (!MMStringUtil.isEmpty(measdocField)
				&& !MMArrayUtil.isEmpty(numScaleFileds)) {
			process.setNumDigits(measdocField, numScaleFileds);
		}
		return process;
	}
	
	/**
	 * �ϼ��о��ȿ���
	 * @param totalFields ��Ҫ���ƺϼ��е��ֶ���������
	 * @param process �����ȴ����process
	 * @return
	 */
	public static ReportScaleProcess create4totalScale(String[] totalFields,ReportScaleProcess process){
		if(null == process){
			process = new ReportScaleProcess();
		}
		process.setTotalFields(totalFields);
		return process;
	}
	
	/**
	 * �ٷֱȾ��ȣ����ݲ���YG6010���ƣ���Ĭ��Ϊ2
	 * @param percentScaleFiledCode ��Ҫ���ưٷֱȾ��ȵ��ֶ���������,����Ϊ�գ����򾫶�ʧЧ
	 * @param process �����ȴ����process
	 * @return
	 */
	public static ReportScaleProcess create4percentScale(String[] percentScaleFiledCode,ReportScaleProcess process){
		if(null == process){
			process = new ReportScaleProcess();
		}
		if(!MMArrayUtil.isEmpty(percentScaleFiledCode)){
			//��ȡ����YG6010ֵ
			int lotSize = 2;
			try {
				lotSize = SysInitQuery.getParaInt(AppContext.getInstance().getPkGroup(), "YG6010");
			} catch (BusinessException e) {
				 ExceptionUtils.wrappBusinessException(e.getMessage());
			}
			process.setConstantDigits(percentScaleFiledCode,lotSize);
		}
		return process;
	}
	//=====end=======================================================================================
	
	
}
