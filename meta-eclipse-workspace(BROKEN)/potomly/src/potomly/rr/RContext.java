package potomly.rr;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;

import potomly.rr.*;



/**
 * @author rawley
 * 
 * The container for all the information that RR requires.  This includes source code,
 * and precaclulated preconditions (TODO).  Much of the initial set up, such as parsing
 * the code, occurs in the RInstance constructors.
 *
 */
public class RContext {
	
	/**
	 * Stores the source "document" mapping to the AST.  
	 */
	Map<ICompilationUnit, CompilationUnit> compiled;
	
	Cursor cursor;
	
	/**
	 * A constructor for RInstance that accepts an {@link ITextSelection}.
	 * <p>
	 * The source code is parsed from this.  No changes to the source code are performed here,
	 * only information gathering.  
	 * 
	 * @param ts The text selection that information is gathered from
	 * @throws RInstanceCreationException Thrown upon internal errors
	 */
	public RContext(ITextSelection ts) throws RInstanceCreationException {
		//	TODO:	this needs to be stored, so we can determine what the cursor is on.
		//			That will likely require an ASTVisitor looking for a method call
		//			encompassing the same offset in the same document as ts.  
		IDocument doc;
		try {
			//	This relies on reflection, and is probably overcomplicated.
			//	Trying to cast to a Document would likely be as effective, 
			//	and might be necissary in the future anyways.  
			doc = (IDocument)ts.getClass().getMethod("getDocument").invoke(ts);
		} catch (Exception e) {
			//	TODO:	replace this with an alternate way to get the document.
			//			Maybe have it be passed as a parameter?
			throw new RInstanceCreationException("Could not get document", e);
		}
		//	The latest version of the Java standard, because Eclipse very much wants
		//	me to use it (all the others are deprecated).  
		ASTParser parser = ASTParser.newParser(AST.JLS10);
		parser.setResolveBindings(true);
		
		//	Here we loop through all of the compilation units (source code files),
		//	and store them, along with their AST, in compiled.  
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		for (IProject proj : workspace.getRoot().getProjects()) {
			IPackageFragment[] fragments = new IPackageFragment[] {};
			try {
				fragments = JavaCore.create(proj).getPackageFragments();
			} catch (JavaModelException jme) {
				throw new RInstanceCreationException("Could not get package fragments.", jme);
			}
			for (IPackageFragment pkgf : fragments) {
				ICompilationUnit[] cus;
				try {
					cus = pkgf.getCompilationUnits();
				} catch (JavaModelException jme) {
					throw new RInstanceCreationException("Could not get compilation units.", jme);
				}
				for (ICompilationUnit cu : cus) {
					//	TODO: generating and storing ASTs, and collect other data, such as
					//	
					parser.setSource(cu);
					compiled.put(cu, (CompilationUnit)parser.createAST(null));
				}
			}
		}
	}
}
