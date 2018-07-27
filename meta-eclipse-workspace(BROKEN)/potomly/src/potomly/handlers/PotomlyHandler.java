package potomly.handlers;


import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;

import potomly.rr.*;


public class PotomlyHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		ISelection sel = HandlerUtil.getCurrentSelection(event);
		
		RContext instance;
		if (sel instanceof ITextSelection) {
			ITextSelection ts = (ITextSelection) sel;
			try {
				instance = new RContext(ts);
			} catch (RInstanceCreationException rice) {
				System.err.println(rice.getStackTrace());
				MessageDialog.openError(
						window.getShell(),
						"Error creating RR instance:", 
						rice.getMessage() + "\n"
						+ rice.getCause() == null ? "" : rice.getCause().getMessage());
				//	As of Eclipse Photon, we always need to return null.
				return null;
			}
		} else {
			//	TODO: check other selection types, and make constructors for them?
		}
		
		//	TODO:	allow other plugins to hook into this as a framework or library
		//			or something.  For now, I'll just have a IRefactorable interface
		//			that takes in the context.  
		
		MessageDialog.openInformation(
				window.getShell(),
				"Potomly",
				"ok");
		return null;
	}
}
