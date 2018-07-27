package klorles;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.e4.core.di.annotations.Execute;

import klorles.rr.IRefactoring;


public class EvaluateContributingHandler {
	
	private static final String IREFACTORING_ID =
            "klorles.refactoring";

	public EvaluateContributingHandler() {
		// TODO Auto-generated constructor stub
	}
    @Execute
    public void execute(IExtensionRegistry registry) {
        IConfigurationElement[] config = registry.getConfigurationElementsFor(IREFACTORING_ID);
        try {
            for (IConfigurationElement e : config) {
                System.out.println("Evaluating klorles extension");
                final Object o =
                        e.createExecutableExtension("class");
                if (o instanceof IRefactoring) {
                    executeExtension(o);
                } else {
                	System.err.println(o.getClass().getName() 
                			+ " does not implement klorles.rr.IRefactoring");
                }
            }
        } catch (CoreException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void executeExtension(final Object o) {
        ISafeRunnable runnable = new ISafeRunnable() {
            @Override
            public void handleException(Throwable e) {
                System.err.println("Exception in client:");
                System.err.println(e.getMessage());
            }

            @Override
            public void run() throws Exception {
                //((IRefactoring) o).refactor(context);
            }
        };
        SafeRunner.run(runnable);
    }
	
}
