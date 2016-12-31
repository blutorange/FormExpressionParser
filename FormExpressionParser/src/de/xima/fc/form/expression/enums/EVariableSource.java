package de.xima.fc.form.expression.enums;

import de.xima.fc.form.expression.iface.evaluate.IExternalContext;
import de.xima.fc.form.expression.iface.evaluate.ILibrary;
import de.xima.fc.form.expression.iface.parse.ISourceResolvable;

public enum EVariableSource {
	/**Either global or local variables of which no copies need to be made. */
	ENVIRONMENTAL {
		@Override
		public boolean isResolved() {
			return true;
		}

		@Override
		public boolean isAssignable() {
			return true;
		}

		@Override
		public boolean isExternal() {
			return false;
		}

		@Override
		public boolean isDefinitelyAssigned() {
			return false;
		}
	},
	/**
	 * <p>
	 * Variables that may be used by anonymous functions, declared outside their own context.
	 * </p><p>
	 * Each function stores its own closure variables, as well as a reference to its
	 * parent. To look up a closure variable, start at the current function and traverse
	 * the graph upwards until a function contains the closure variable.
	 * </p><p>
	 * When the <code>source</code> is the value of {@link ISourceResolvable#getSource()},
	 * <code>(source>>16)&0xFFFF0000</code> is the number of parents we need to go up, and
	 * <code>source&0x0000FFFF</code> is the position in the symbol table of that parent
	 * containing the value of the variable.
	 * </p>
	 */
	CLOSURE {
		@Override
		public boolean isResolved() {
			return true;
		}

		@Override
		public boolean isAssignable() {
			return true;
		}

		@Override
		public boolean isExternal() {
			return false;
		}

		@Override
		public boolean isDefinitelyAssigned() {
			return false;
		}
	},
	/** Variables provided by the {@link IExternalContext} */
	EXTERNAL_CONTEXT {
		@Override
		public boolean isResolved() {
			return true;
		}

		@Override
		public boolean isAssignable() {
			return false;
		}

		@Override
		public boolean isExternal() {
			return true;
		}

		@Override
		public boolean isDefinitelyAssigned() {
			return true;
		}
	},
	/** Variables provided by some system library {@link ILibrary}. */
	LIBRARY {
		@Override
		public boolean isResolved() {
			return true;
		}

		@Override
		public boolean isAssignable() {
			return false;
		}

		@Override
		public boolean isExternal() {
			return false;
		}

		@Override
		public boolean isDefinitelyAssigned() {
			return true;
		}
	},
	/** Variables not resolved yet. */
	UNRESOLVED {
		@Override
		public boolean isResolved() {
			return false;
		}

		@Override
		public boolean isAssignable() {
			return false;
		}

		@Override
		public boolean isExternal() {
			return false;
		}

		@Override
		public boolean isDefinitelyAssigned() {
			return false;
		}
	};

	public abstract boolean isResolved();

	public abstract boolean isAssignable();

	public abstract boolean isExternal();

	public abstract boolean isDefinitelyAssigned();
}