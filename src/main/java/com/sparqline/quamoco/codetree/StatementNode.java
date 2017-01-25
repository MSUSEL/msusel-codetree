package com.sparqline.quamoco.codetree;

public class StatementNode extends CodeNode {

	private StatementType type;

	public StatementNode(StatementType type, int start, int end) {
		super("Statement", "Statment", start, end);
		this.type = type;
	}

	public StatementType getStatementType() {
		return type;
	}

	@Override
	public String getType() {
		return CodeNodeType.STATEMENT;
	}

	@Override
	public void update(CodeNode c) {
		if (c == null)
			return;

		updateLocation(c.getStart(), c.getEnd());
	}

	@Override
	public StatementNode cloneNoChildren() {
		StatementNode fnode = new StatementNode(this.type, this.getStart(), this.getEnd());

		copyMetrics(fnode);

		return fnode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected StatementNode clone() throws CloneNotSupportedException {
		return cloneNoChildren();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}
}
