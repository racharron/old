package context_test.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.*;


public class TestHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println(event.toString());
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		ITextEditor editor = (ITextEditor)HandlerUtil.getActiveEditor(event);
		IFile file = (IFile)editor.getAdapter(IFile.class);
		assert file != null;
		Document doc = (Document)((AbstractDecoratedTextEditor)HandlerUtil
				.getActiveEditor(event))
				.getDocumentProvider()
				.getDocument(editor.getEditorInput());
		assert doc != null;
		System.out.println(doc.getClass().getName());
		ASTParser parser = ASTParser.newParser(AST.JLS10);
		parser.setResolveBindings(false);
		parser.setBindingsRecovery(false);
		parser.setSource(doc.get().toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		CompilationUnit activeCu = (CompilationUnit)parser.createAST(null);
		PackageDeclaration p = activeCu.getPackage();
		
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		ITextSelection selection = (ITextSelection)HandlerUtil.getCurrentSelection(event);
		MessageDialog.openInformation(
				window.getShell(),
				"Context_Test",
				"ok");
		return null;
	}
}

