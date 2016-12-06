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
 * <b> 报表数据精度处理公共类 </b>
 * <p>
 * 这里只实现了主数量和合计行数量精度处理， 没有处理辅数量，金额，单价等等的精度处理，如需要处理请扩展该类
 * </p>
 * 
 * @since 创建日期 Aug 13, 2013
 * @author liwsh
 */
public class MMGPReportScaleStrategy {

	// 精度处理工具
	private ReportScaleProcess process;

	private String measdoccode; // 主计量单位

	private String[] numScaleFiledCode; // 进行数量精度控制字段

	private String[] totalFields; // 合计字段精度控制
	
	private String currency; //币种

	private String[] moneyScaleFiledCode; // 进行金额精度空值的字段
	
	private String[] percentScaleFiledCode; // 百分比精度的字段

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
	 * 百分比精度（根据参数YG6010控制），默认为2
	 * @param process
	 * @param percentScaleFiledCode 百分比字段
	 */
	public MMGPReportScaleStrategy(ReportScaleProcess process,
			String[] appointScaleFiledCode) {
		this.percentScaleFiledCode= appointScaleFiledCode;
		this.process = process;
		this.initialization();
	}
	
	/**
	 * 百分比精度（根据参数YG6010控制），默认为2
	 * @param percentScaleFiledCode  百分比字段
	 */
	public MMGPReportScaleStrategy(String[] appointScaleFiledCode) {
		this.percentScaleFiledCode= appointScaleFiledCode;
		this.process = new ReportScaleProcess();
		this.initialization();
	}

	/**
	 * 初始化精度工具
	 */
	private void initialization() {

		// 设置主单位 ---主数量
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
			//获取参数YG6010值
			int lotSize = 2;
			try {
				lotSize = SysInitQuery.getParaInt(AppContext.getInstance().getPkGroup(), "YG6010");
			} catch (BusinessException e) {
				 ExceptionUtils.wrappBusinessException(e.getMessage());
			}
			this.process.setConstantDigits(this.getPercentScaleFiledCode(),lotSize);
		}
	}

	// 主单位
	protected String getUnitKey() {
		//
		return this.measdoccode;

	}

	// 主数量字段
	protected String[] getNumFields() {
		return this.numScaleFiledCode;
	}

	// 合计行精度控制
	protected String[] getTotalFields() {
		return this.totalFields;
	}
	//币种
	public String getCurrency() {
		return this.currency;
	}
	//金额字段
	public String[] getMoneyScaleFiledCode() {
		return this.moneyScaleFiledCode;
	}
	//百分比字段
	public String[] getPercentScaleFiledCode() {
		return this.percentScaleFiledCode;
	}
	
	
	
	//================以下方法为新增加，原来的获取方式太麻烦了其实就是获取process=========================
	//begin 2014-12-02  zhangyhk add 
	
	/**
	 * 币种与金额精度控制
	 * @param currency 币种字段名 不可为空，否则精度失效
	 * @param moneyScaleFiledCode 需要控制精度的字段名 不可为空，否则精度失效
	 * @param process 报表精度处理的process
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
	 * 单位与数量精度
	 * @param measdocField 单位字段名称，不可为空，否则精度失效
	 * @param numScaleFileds 需要控制精度的数量字段数组 ，不可为空，否则精度失效
	 * @param process 报表精度处理的process
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
	 * 合计行精度控制
	 * @param totalFields 需要控制合计行的字段名称数组
	 * @param process 报表精度处理的process
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
	 * 百分比精度（根据参数YG6010控制），默认为2
	 * @param percentScaleFiledCode 需要控制百分比精度的字段名称数组,不可为空，否则精度失效
	 * @param process 报表精度处理的process
	 * @return
	 */
	public static ReportScaleProcess create4percentScale(String[] percentScaleFiledCode,ReportScaleProcess process){
		if(null == process){
			process = new ReportScaleProcess();
		}
		if(!MMArrayUtil.isEmpty(percentScaleFiledCode)){
			//获取参数YG6010值
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
