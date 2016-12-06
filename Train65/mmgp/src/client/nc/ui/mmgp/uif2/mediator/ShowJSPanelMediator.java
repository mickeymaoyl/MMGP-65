package nc.ui.mmgp.uif2.mediator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.script.ScriptException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import nc.desktop.ui.WorkbenchEnvironment;
import nc.ui.uif2.ScriptingRuntime;
import nc.vo.mmgp.util.MMStringUtil;
import nc.vo.uif2.LoginContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

public class ShowJSPanelMediator implements BeanFactoryAware {
	private LoginContext context;

	public LoginContext getContext() {
		return context;
	}

	public void setContext(LoginContext context) {
		this.context = context;

	}

	private BeanFactory beanFactory;

	@Override
	public void setBeanFactory(BeanFactory bf) throws BeansException {
		beanFactory = bf;
		if (beanFactory == null) {
			throw new RuntimeException();
		}
		ActionMap actionMap = context.getEntranceUI().getActionMap();
		actionMap.put("showMMGPScript", new ShowScriptPanelAction());
		InputMap inputMap = context.getEntranceUI().getInputMap(
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_J,
				InputEvent.CTRL_DOWN_MASK + InputEvent.ALT_DOWN_MASK
						+ InputEvent.SHIFT_DOWN_MASK), "showMMGPScript");
	}

	public class ShowScriptPanelAction extends AbstractAction {

		private static final long serialVersionUID = 1L;
		ScriptingRuntime rt;
		private JFrame scriptFrame;

		@Override
		public void actionPerformed(ActionEvent e) {
			// ToftPanelAdaptor tpa = (ToftPanelAdaptor) getContext()
			// .getEntranceUI();
			if (rt == null) {
				rt = new ScriptingRuntime();
				rt.put("env", WorkbenchEnvironment.getInstance());
				rt.put("tpa", getContext().getEntranceUI());
				rt.put("bf", beanFactory);
			}

			if (scriptFrame == null) {
				scriptFrame = new JFrame(nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0022")/*@res "JavaScript运行面板"*/);
				ScriptPanel sp = new ScriptPanel(rt);
				scriptFrame.setLayout(new BorderLayout());
				scriptFrame.getContentPane().add(sp, BorderLayout.CENTER);
				sp.setBorder(BorderFactory.createLineBorder(Color.red));
				scriptFrame.setSize(new Dimension(600, 480));
			}
			if (!scriptFrame.isVisible()) {
				scriptFrame.setVisible(true);
			}
		}
	}

	private class ScriptPanel extends JPanel {

		private static final long serialVersionUID = 9111361853158116597L;

		private JTextArea result = new JTextArea();
		JTextArea editor = new JTextArea();
		ScriptingRuntime rt;

		public ScriptPanel(ScriptingRuntime srt) {
			rt = srt;
			setLayout(new BorderLayout());
			result.setEditable(false);
			add(result, BorderLayout.NORTH);
			add(editor);
			add(new JButton(new ExecAction()), BorderLayout.SOUTH);

		}

		class ExecAction extends AbstractAction {
			private static final long serialVersionUID = -5820540787547499369L;

			ExecAction() {
				putValue(Action.NAME, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("common","UC001-0000026")/*@res "执行"*/);
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				String script = editor.getText();
				if (editor.getSelectedText() != null) {
					script = editor.getSelectedText();
				}
				try {
					Object ret = rt.eval(script);
					result.setText(MMStringUtil.objectToString(ret));
				} catch (ScriptException e1) {
					JOptionPane.showConfirmDialog(ScriptPanel.this, nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID("mmgp001_0","0mmgp001-0023")/*@res "脚本执行出错"*/,
							"", JOptionPane.YES_OPTION,
							JOptionPane.ERROR_MESSAGE);
					nc.bs.logging.Logger.error(e1.getMessage(), e1);
				}

			}

		}

	}

}