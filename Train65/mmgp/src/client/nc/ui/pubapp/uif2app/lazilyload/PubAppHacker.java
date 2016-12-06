package nc.ui.pubapp.uif2app.lazilyload;

public class PubAppHacker {
	public static void setActionLazilyLoadListener(ActionLazilyLoad lazilyload,IChildrenChangeListener l){
		lazilyload.listener = l;
	}
}
